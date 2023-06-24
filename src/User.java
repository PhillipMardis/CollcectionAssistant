public class User {
    private int userId;
    private String username;
    private boolean isDealer;

    public User(int userId, String username, boolean isDealer) {
        this.userId = userId;
        this.username = username;
        this.isDealer = isDealer;
    }

    // getters
    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    public boolean isDealer() {
        return isDealer;
    }
    // setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }
}
