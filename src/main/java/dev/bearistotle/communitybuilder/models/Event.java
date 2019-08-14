package dev.bearistotle.communitybuilder.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Events are an actual calendar item, where users will participate in an activity. Relate a datetime, location,
 * activity, and set of users.
 * Minimal constructor: Event(String name,
 *                      String description,
 *                      LocalDateTime beginning,
 *                      LocalDateTime end,
 *                      Location location,
 *                      Activity activity)
 * Maximal constructor: Event(String name,
 *                            String description,
 *                            LocalDateTime beginning,
 *                            LocalDateTime end,
 *                            Location location,
 *                            Activity activity,
 *                            HashMap<String, Integer> numParticipants,
 *                            List<User> participants)
 *
 */
@Entity
@Transactional
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int eventId;

    @NotNull
    @Size(min=3, max=25, message="Event names must be between 3 and 25 characters.")
    private String name;

    @NotNull
    @Size(min=10,max=500,message="Descriptions must be between 10 and 500 characters.")
    private String description;

    @NotNull
    @DateTimeFormat
    private LocalDateTime beginning;

    @NotNull
    @DateTimeFormat
    private LocalDateTime end;

    @ManyToOne
    @NotNull
    private Location location;

    @NotNull
    @ManyToOne
    private Activity activity;

    /** numParticipants should be a HashMap with two entries, "min=x" and "max=y", for some Integers x and y. */
    private HashMap<String, Integer> numParticipants;

    @ManyToMany(mappedBy = "events", cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<User> users;

    public Event(String name,
                 String description,
                 LocalDateTime beginning,
                 LocalDateTime end,
                 Location location,
                 Activity activity,
                 HashMap<String, Integer> numParticipants,
                 List<User> users){
        this.name = name;
        this.description = description;
        this.beginning = beginning;
        this.end = end;
        this.location = location;
        this.activity = activity;
        this.numParticipants = numParticipants;
        this.users = users;
    }

    public Event(String name,
                 String description,
                 LocalDateTime beginning,
                 LocalDateTime end,
                 Location location,
                 Activity activity){
        this.name = name;
        this.description = description;
        this.beginning = beginning;
        this.end = end;
        this.location = location;
        this.activity = activity;
        this.numParticipants = new HashMap<>();
        this.users = new ArrayList<>();
    }

    public Event(){
        this.numParticipants = new HashMap<>();
        this.users = new ArrayList<>();
    }

    public int getEventId() {
        return eventId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<User> getUsers(){
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user){ this.users.remove(user); }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public HashMap<String, Integer> getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(HashMap<String, Integer> numParticipants) {
        this.numParticipants = numParticipants;
    }
}
