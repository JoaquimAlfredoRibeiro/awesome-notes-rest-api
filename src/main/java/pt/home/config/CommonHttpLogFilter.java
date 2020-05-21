package pt.home.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CommonHttpLogFilter extends OncePerRequestFilter {

	private static final int MAX_PAYLOAD_CHAR_LENGTH = 10000;
	private static final int BUFFER_OFFSET_FROM_START = 0;

	@Override
	protected void doFilterInternal(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain filterChain
	) throws ServletException, IOException {

		//Random UUID to identify request
		final String httpRequestId = UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
		final long requestStartTime = System.currentTimeMillis();

		//Wrap request and response
		final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

		//Implement Filter in chain
		filterChain.doFilter(wrappedRequest, wrappedResponse);

		logIncomingHttpRequest(wrappedRequest, request.getMethod(), request.getRequestURL().toString(), httpRequestId);

		final long requestDuration = System.currentTimeMillis() - requestStartTime;

		logOutgoingHttpRequest(wrappedResponse, httpRequestId, response.getStatus(), requestDuration);

		wrappedResponse.copyBodyToResponse();
	}

	//Get content as String from buffer, with max length
	private static String getContentAsString(final byte[] buffer, final String charset) {

		if (buffer == null || buffer.length == 0) {
			return StringUtils.EMPTY;
		}

		final int length = Math.min(buffer.length, MAX_PAYLOAD_CHAR_LENGTH);

		try {

			return new String(buffer, BUFFER_OFFSET_FROM_START, length, charset);
		} catch (final UnsupportedEncodingException ex) {

			log.warn("Unsupported encode detected while logging request / response");
			return "Unsupported Encoding";
		}
	}

	private static void logIncomingHttpRequest(
			final ContentCachingRequestWrapper request,
			final String httpMethod,
			final String requestUrl,
			final String httpRequestId) {

		final String requestBody = getContentAsString(
				request.getContentAsByteArray(),
				request.getCharacterEncoding())
				.replaceAll("\\n+", StringUtils.EMPTY)
				.replace(" ", StringUtils.EMPTY);

		if (requestBody.length() > 0) {
			log.info("Http Request Id: {}, with method: {}, for URL: {} and RequestBody {}",
					httpRequestId,
					httpMethod,
					requestUrl,
					requestBody
			);
		}
	}

	private static void logOutgoingHttpRequest(
			final ContentCachingResponseWrapper response,
			final String httpRequestId,
			final int httResponseStatusCode,
			final long duration
	) {

		final byte[] buffer = response.getContentAsByteArray();

		log.info("Http Response Id: {} replied with StatusCode: {} and ResponseBody: {} in {}ms ",
				httpRequestId,
				HttpStatus.valueOf(httResponseStatusCode),
				getContentAsString(buffer, response.getCharacterEncoding()),
				duration);
	}
}
