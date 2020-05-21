package pt.home.instanceprovers;

import pt.home.domain.auth.User;

//Note instance provider for testing, with default values
public class UserInstanceProvider {


    public final static Long ID = 1L;
    public final static String FIRST_NAME = "Some content";
    public final static String LAST_NAME = "imageName.jpeg";
    public final static String USERNAME = "imageName.jpeg";
    public final static String EMAIL = "imageName.jpeg";
    public final static String PASSWORD = "imageName.jpeg";

    private Long id = ID;
    private String firstName = FIRST_NAME;
    private String lastName = LAST_NAME;
    private String username = USERNAME;
    private String email = EMAIL;
    private String password = PASSWORD;

    public UserInstanceProvider withId(final Long id) {
        this.id = id;
        return this;
    }

    public UserInstanceProvider withFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserInstanceProvider withLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserInstanceProvider withUserName(final String userName) {
        this.username = userName;
        return this;
    }

    public UserInstanceProvider withEmail(final String email) {
        this.email = email;
        return this;
    }

    public UserInstanceProvider withPassword(final String password) {
        this.password = password;
        return this;
    }

    public User getInstance() {
        return new User(
                this.firstName,
                this.lastName,
                this.username,
                this.email,
                this.password
        );
    }

    //specific getInstance method for specific userId testing
    public User getInstanceWithId() {
        User user = new User(
                this.firstName,
                this.lastName,
                this.username,
                this.email,
                this.password
        );

        user.setId(this.id);
        return user;
    }
}
