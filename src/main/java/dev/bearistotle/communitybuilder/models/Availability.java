package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Transactional
@Table(name = "Availability")
public class Availability {

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int availabilityId;

    @NotNull
    @ManyToMany(mappedBy = "availabilities", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Activity> activities;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String recurrencePattern;

    @ManyToOne
    private Location location;

    public Availability(ArrayList<Activity> activities,
                        User user,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern,
                        Location location){
        this.activities = activities;
        this.user = user;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(ArrayList<Activity> activities,
                        User user,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        this.activities = activities;
        this.user = user;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
    }

    public Availability(){
        this.activities = new ArrayList<>();
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return availabilityId == that.availabilityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availabilityId);
    }

    @Override
    public String toString() {
        return "Availability{" +
                "activities=" + activities +
                ", user=" + user +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", location=" + location +
                '}';
    }
}
