package dev.bearistotle.communitybuilder.models;


public class PropertyManager extends User {

    private boolean isAdmin;

    public PropertyManager(String username, String email, String pwHash){
        super(username, email, pwHash);
        this.isAdmin = true;
    }

    public PropertyManager(){
        super();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "isAdmin='" + isAdmin + '\'' +
                "username='" + this.getUsername() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                '}';
    }
}
