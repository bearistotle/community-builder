package dev.bearistotle.communitybuilder.models;


import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Transactional
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=5,max=25,message="Username must be 5-25 characters long.")
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String passwordHash;
    //Room Number?
    //Calendar?
    @ManyToMany
    private List<Activity> activities;

    public User(String username, String email, String passwordHash){
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User(){}

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity){
        this.activities.remove(activity);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
