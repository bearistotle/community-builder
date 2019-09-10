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

@Entity
@Transactional
@Table(name = "Availability")
public class Availability {

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(min = 3, max = 200, message = "Name must be 3--200 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 500, message = "Description must be 10--500 characters.")
    private String description;

    @NotNull
    @ManyToMany(mappedBy = "availabilities", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Activity> activities;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String recurrencePattern;

    public Availability(String name,
                        String description,
                        ArrayList<Activity> activities,
                        User creator,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        this.name = name;
        this.description = description;
        this.activities = activities;
        this.creator = creator;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;}

    public Availability(){
        this.activities = new ArrayList<>();
    }

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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void removeActivity(Activity activity){ this.activities.remove(activity); }

    public void addActivity(Activity activity){ this.activities.add(activity); }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Availability{" +
                "id= " + id +
                "activities= " + activities +
                ", creator= " + creator +
                ", date= " + date +
                ", startTime= " + startTime +
                ", endTime= " + endTime +
                ", recurrencePattern= " + recurrencePattern + '\'' + '}';
    }
}
