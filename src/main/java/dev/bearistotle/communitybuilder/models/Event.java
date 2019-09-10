package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Events are an actual calendar item, where participants will participate in an activity. Relate a datetime, location,
 * activity, and set of participants.
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
public class Event extends Availability {

    @ManyToOne
    private Location location;

    private int minParticipants;

    private int maxParticipants;

    @ManyToMany(mappedBy = "events", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<User> participants;

    public Event(String name,
                 String description,
                 User creator,
                 ArrayList<Activity> activities,
                 LocalDate date,
                 LocalTime startTime,
                 LocalTime endTime,
                 String recurrencePattern,
                 Location location,
                 int minParticipants,
                 int maxParticipants,
                 ArrayList<User> participants) {
        super(name,
                description,
                activities,
                creator,
                date,
                startTime,
                endTime,
                recurrencePattern);
        this.location = location;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
    }

    public Event(String name,
                 String description,
                 User creator,
                 ArrayList<Activity> activities,
                 LocalDate date,
                 LocalTime startTime,
                 LocalTime endTime,
                 String recurrencePattern,
                 Location location,
                 int minParticipants,
                 int maxParticipants) {
        super(name,
                description,
                activities,
                creator,
                date,
                startTime,
                endTime,
                recurrencePattern);
        this.location = location;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.participants = new ArrayList<>();
    }

    public Event(String name,
                 String description,
                 User creator,
                 ArrayList<Activity> activities,
                 LocalDate date,
                 LocalTime startTime,
                 LocalTime endTime,
                 String recurrencePattern,
                 Location location) {
        super(name,
                description,
                activities,
                creator,
                date,
                startTime,
                endTime,
                recurrencePattern);
        this.location = location;
        this.minParticipants = 0;
        this.maxParticipants = 0;
        this.participants = new ArrayList<>();
    }

    public Event() {
        this.participants = new ArrayList<>();
    }


    public List<User> getParticipants() {
        return participants;
    }

    public void addUser(User user) {
        this.participants.add(user);
    }

    public void removeUser(User user) {
        this.participants.remove(user);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return this.getId() == event.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", date=" + this.getDate() +
                ", recurrencePattern='" + this.getRecurrencePattern() + '\'' +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.getEndTime() +
                ", location=" + this.getLocation() +
                ", activities=" + this.getActivities() +
                '}';
    }
}
