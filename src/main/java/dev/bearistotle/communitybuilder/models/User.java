package dev.bearistotle.communitybuilder.models;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: Add password confirm field (and password?) as @Transient fields
@Entity
@Transactional
@Table(name = "User")
public class User {

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

    // TODO: Determine if any other fields are needed (for instance a calendar field)
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

    @ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_availability",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "availability_id") }
    )
    private List<Availability> availabilities;

    public User(String username, String email, String pwHash){
        this.username = username;
        this.email = email;
        this.pwHash = pwHash;
        this.events = new ArrayList<>();
        this.activities = new ArrayList<>();
        this.availabilities = new ArrayList<>();

    }

    public User(){
        this.events = new ArrayList<>();
        this.activities = new ArrayList<>();
        this.availabilities = new ArrayList<>();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public int getUserId() {
        return userId;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(@NotNull Event event){
        this.events.add(event);
    }

    public void removeEvent(@NotNull Event event){
        this.events.remove(event);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(@NotNull String pwHash) {
        this.pwHash = pwHash;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(@NotNull Activity activity) { this.activities.add(activity); }

    public void removeActivity(@NotNull Activity activity){ this.activities.remove(activity); }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public void addAvailability(Availability availability){
        this.availabilities.add(availability);
    }

    public void removeAvailability(Availability availability){
        this.availabilities.remove(availability);
    }
}
