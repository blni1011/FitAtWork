package eu.iums.fitwork;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private boolean leaderBoard;

    public User(String username, String name, String lastName, String email, boolean leaderBoard) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.leaderBoard = leaderBoard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(boolean leaderBoard) {
        this.leaderBoard = leaderBoard;
    }
}
