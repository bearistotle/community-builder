package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Transactional
public class Activity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3,max=30,message="Activity name must be between 3 and 30 characters.")
    private String name;


    @NotNull
    @Size(min=10,max=500,message="Activity description must be between 10 and 500 characters.")
    private String description;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<User> users = new ArrayList<>();

    @OneToMany
    @JoinColumn(name="activity_id")
    private List<Event> events = new ArrayList<>();

    public Activity(String name,
                    String description,
                    List<User> users,
                    List<Event> events) {
        this.name = name;
        this.description = description;
        this.users = users;
        this.events = events;
    }

    public Activity(String name,
                    String description){
        this.name = name;
        this.description = description;
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public Activity(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
