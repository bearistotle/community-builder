package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
// TODO: Consider adding a numParticipants field
@Entity
@Transactional
@Table(name = "Activity")
public class Activity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int activityId;

    @NotNull
    @Size(min=3,max=30,message="Activity name must be between 3 and 30 characters.")
    private String name;


    @NotNull
    @Size(min=10,max=500,message="Activity description must be between 10 and 500 characters.")
    private String description;

    @ManyToMany(mappedBy = "activities", cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<User> users;


    @OneToMany(cascade = CascadeType.ALL)
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

    public int getActivityId() {
        return activityId;
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

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user){ this.users.remove(user); }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeEvent(Event event){ this.events.remove(event); }
}
