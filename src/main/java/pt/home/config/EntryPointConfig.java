package pt.home.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//add the logger filter
public class EntryPointConfig {

    @Bean
    public FilterRegistrationBean<CommonHttpLogFilter> logFilter() {
        final FilterRegistrationBean<CommonHttpLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CommonHttpLogFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
