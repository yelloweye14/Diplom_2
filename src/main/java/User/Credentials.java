package User;


public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Credentials from(User user) {
        return new Credentials(user.getEmail(), user.getPassword());
    }

    public static Credentials getWrongLoginPassword() {
        return new Credentials("email", "p@ssword");
    }

    public static Credentials getWrongLogin(User user) {
        return new Credentials("email", user.getPassword());
    }

    public static Credentials getEmptyLogin(User user) {
        return new Credentials("", user.getPassword());
    }

    public static Credentials getWrongPassword(User user) {
        return new Credentials(user.getEmail(), "p@ssword");
    }

    public static Credentials getEmptyPassword(User user) {
        return new Credentials(user.getEmail(), "");
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
