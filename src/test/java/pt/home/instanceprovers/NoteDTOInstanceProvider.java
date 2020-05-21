package pt.home.instanceprovers;

import pt.home.api.v1.model.NoteDTO;

import java.time.LocalDateTime;

//NoteDTO instance provider for testing, with default values
public class NoteDTOInstanceProvider {

	public final static Long BASE_ID = 1L;
	public final static String CONTENT = "Some content";
	public final static String IMAGE_NAME = "imageName.jpeg";
	public final static String IMAGE_64 = "image 64 encoded";

	private Long id = BASE_ID;
	private String content = CONTENT;
	private Boolean finished = Boolean.FALSE;
	private String imageName = IMAGE_NAME;
	private LocalDateTime dueDate = LocalDateTime.now();
	private String image64 = IMAGE_64;

	public NoteDTOInstanceProvider withId(final Long id) {
		this.id = id;
		return this;
	}

	public NoteDTOInstanceProvider withContent(final String content) {
		this.content = content;
		return this;
	}

	public NoteDTOInstanceProvider withIsFinished(final Boolean isFinished) {
		this.finished = isFinished;
		return this;
	}

	public NoteDTOInstanceProvider withImageName(final String imageName) {
		this.imageName = imageName;
		return this;
	}

	public NoteDTOInstanceProvider withDueDate(final LocalDateTime dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public NoteDTOInstanceProvider withImage64(final String image64) {
		this.image64 = image64;
		return this;
	}

	public NoteDTO getInstance() {
		return new NoteDTO(
				this.id,
				this.content,
				this.finished,
				this.imageName,
				this.dueDate,
				this.image64
		);
	}
}
