package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Transactional
@Table(name = "Availability")
public class Availability {

    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private int availabilityId;

    @NotNull
    @ManyToMany(mappedBy = "availabilities", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private ArrayList<Activity> activities;

    @NotNull
    @ManyToOne
    private Resident resident;

    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String recurrencePattern;

    private Location location;

    public Availability(ArrayList<Activity> activities,
                        Resident resident,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern,
                        Location location){
        this.activities = activities;
        this.resident = resident;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(ArrayList<Activity> activities,
                        Resident resident,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        this.activities = activities;
        this.resident = resident;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
    }

    public Availability(){}

    public int getAvailibiliyId() {
        return availabilityId;
    }

    public ArrayList<Activity> getActivity() {
        return activities;
    }

    public void setActivity(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
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
                ", resident=" + resident +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", location=" + location +
                '}';
    }
}
