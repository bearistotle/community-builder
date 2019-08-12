package dev.bearistotle.communitybuilder.models;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Check all relationships (both sides) for correct type and correct set up
// TODO: Add password confirm field (and password?) as @Transient fields
@Entity
@Transactional
@Table(name = "User")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int userId;

    @NotNull
    @Size(min=5,max=25,message="Username must be 5-25 characters long.")
    private String username;

    @Email
    private String email;

    private String pwHash;
    //Room Number?
    //Calendar?
    @ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_event",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") }
    )
    private List<Event> events;

    @ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_activity",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "activity_id") }
    )
    private List<Activity> activities;


    public User(String username, String email, String pwHash){
        this.username = username;
        this.email = email;
        this.pwHash = pwHash;
        this.events = new ArrayList<>();
        this.activities = new ArrayList<>();
    }

    public User(){}

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (o == null){
            return false;
        }
        if (!(o instanceof User)){
            return false;
        }
        User other = (User) o;
        return this.getUserId() == other.getUserId() &&
                this.getUsername().equals(other.getUsername()) &&
                this.getEmail().equals(other.getEmail());
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + userId;
        result = prime * result + (email == null ? 0 : email.hashCode());
        result = prime * result + (username == null ? 0 : username.hashCode());
        return result;
    }

    public int getUserId() {
        return userId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public void removeEvent(Event event){
        this.events.remove(event);
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

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity){ this.activities.remove(activity); }
}
