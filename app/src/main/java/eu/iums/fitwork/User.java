package eu.iums.fitwork;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private int fitPoints;


    public User(String username, String name, String lastName, String email) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.fitPoints = 0;
    }
    public User() {

    }

    public int getFitPoints() {
        return fitPoints;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}