package dev.bearistotle.communitybuilder.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Events are an actual calendar item, where users will participate in an activity. Relate a datetime, location,
 * activity, and set of users.
 * Minimal constructor: Event(String name,
 *                      String description,
 *                      LocalDateTime startTime,
 *                      LocalDateTime end,
 *                      Location location,
 *                      Activity activity)
 * Maximal constructor: Event(String name,
 *                            String description,
 *                            LocalDateTime startTime,
 *                            LocalDateTime endTime,
 *                            Location location,
 *                            Activity activity,
 *                            int minParticipants,
 *                            int maxParticipants,
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
    @Size(min = 3, max = 25, message = "Event names must be between 3 and 25 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 500, message = "Descriptions must be between 10 and 500 characters.")
    private String description;

    @NotNull
    @DateTimeFormat
    private LocalDate date;

    @NotNull
    private String recurrencePattern;

    @NotNull
    @DateTimeFormat
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @NotNull
    private Location location;

    @NotNull
    @ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_activity",
            joinColumns = { @JoinColumn(name = "event_id") },
            inverseJoinColumns = { @JoinColumn(name = "activity_id") }
    )
    private List<Activity> activities;

    private int minParticipants;

    private int maxParticipants;

    @ManyToMany(mappedBy = "events", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<User> users;

    // TODO: Add 'creator' field. Will have editing privileges, etc. that participants ("users") won't.
    public Event(String name,
                 String description,
                 LocalDate date,
                 String recurrencePattern,
                 LocalTime startTime,
                 LocalTime endTime,
                 Location location,
                 ArrayList<Activity> activities,
                 int minParticipants,
                 int maxParticipants,
                 ArrayList<User> users) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.recurrencePattern = recurrencePattern;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.activities = activities;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.users = users;
    }

    public Event(String name,
                 String description,
                 LocalDate date,
                 String recurrencePattern,
                 LocalTime startTime,
                 LocalTime endTime,
                 Location location,
                 ArrayList<Activity> activities) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.recurrencePattern = recurrencePattern;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.activities = activities;
        this.users = new ArrayList<>();
    }

    public Event() {
        this.activities = new ArrayList<>();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity){
        this.activities.remove(activity);
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return eventId == event.eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location=" + location +
                ", activities=" + activities +
                '}';
    }
}
