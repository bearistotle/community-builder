package dev.bearistotle.communitybuilder.models;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

//TODO: Check all relationships (both sides) for correct type and correct set up

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
    //Activities?
    //TODO: fix column naming problem
    @ManyToMany
    @JoinTable(
            name="user_event",
            joinColumns = @JoinColumn(name="event_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<Event> events;

    public User(String username, String email, String passwordHash){
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.events = new ArrayList<>();
    }

    public User(){}

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
