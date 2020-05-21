package pt.home.instanceprovers;

import pt.home.domain.Note;

import java.time.LocalDateTime;

//Note instance provider for testing, with default values
public class NoteInstanceProvider {

	public final static Long BASE_ID = 1L;
	public final static String CONTENT = "Some content";
	public final static String IMAGE_NAME = "imageName.jpeg";

	private Long id = BASE_ID;

	private String content = CONTENT;

	private Boolean finished = Boolean.FALSE;

	private String imageName = IMAGE_NAME;

	private LocalDateTime dueDate = LocalDateTime.now();

	private Boolean active = true;

	private Long createdBy = 1L;

	private Long updatedBy = 1L;

	public NoteInstanceProvider withId(final long id) {
		this.id = id;
		return this;
	}

	public NoteInstanceProvider withContent(final String content) {
		this.content = content;
		return this;
	}

	public NoteInstanceProvider withIsFinished(final Boolean finished) {
		this.finished = finished;
		return this;
	}

	public NoteInstanceProvider withImageName(final String imageName) {
		this.imageName = imageName;
		return this;
	}

	public NoteInstanceProvider withDueDate(final LocalDateTime dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public NoteInstanceProvider withIsActive(final Boolean active) {
		this.active = active;
		return this;
	}

	public NoteInstanceProvider withCreatedBy(final Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Note getInstance() {
		return new Note(
				this.id,
				this.content,
				this.finished,
				this.imageName,
				this.dueDate,
				this.active
		);
	}

	//specific getInstance method for specific userId testing
	public Note getInstanceWithCreatedBy() {
		Note note = new Note(
				this.id,
				this.content,
				this.finished,
				this.imageName,
				this.dueDate,
				this.active
		);

		note.setCreatedBy(this.createdBy);
		return note;
	}
}
