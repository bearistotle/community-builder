package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private ArrayList<User> users;

    @ManyToMany(mappedBy = "activities", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private ArrayList<Event> events;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "activity_availability",
            joinColumns = { @JoinColumn(name = "activity_id") },
            inverseJoinColumns = { @JoinColumn(name = "availability_id") }
    )
    private ArrayList<Availability> availabilities;

    public Activity(String name,
                    String description,
                    ArrayList<User> users,
                    ArrayList<Event> events,
                    ArrayList<Availability> availabilities) {
        this.name = name;
        this.description = description;
        this.users = users;
        this.events = events;
        this.availabilities = availabilities;
    }

    public Activity(String name,
                    String description){
        this.name = name;
        this.description = description;
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
        this.availabilities = new ArrayList<>();
    }

    public Activity(){
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
        this.availabilities = new ArrayList<>();
    }

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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user){ this.users.remove(user); }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeEvent(Event event){ this.events.remove(event); }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(ArrayList<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public void addAvailability(Availability availability){
        this.availabilities.add(availability);
    }

    public void removeAvailability(Availability availability){
        this.availabilities.remove(availability);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return activityId == activity.activityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + activityId + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
