package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
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
    private int availibiliyId;

    @NotNull
    @ManyToOne
    private Activity activity;

    @NotNull
    @ManyToOne
    private Resident resident;

    private LocalDate date;

    private ArrayList<DayOfWeek> days;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String recurrencePattern;

    private Location location;

    public Availability(Activity activity,
                        Resident resident,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern,
                        Location location){
        this.activity = activity;
        this.resident = resident;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(Activity activity,
                        Resident resident,
                        ArrayList<DayOfWeek> days,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern,
                        Location location){
        this.activity = activity;
        this.resident = resident;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(Activity activity,
                        Resident resident,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        this.activity = activity;
        this.resident = resident;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(Activity activity,
                        Resident resident,
                        ArrayList<DayOfWeek> days,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        this.activity = activity;
        this.resident = resident;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.location = location;
    }

    public Availability(){}

    public int getAvailibiliyId() {
        return availibiliyId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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

    public ArrayList<DayOfWeek> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayOfWeek> days) {
        this.days = days;
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
        return availibiliyId == that.availibiliyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availibiliyId);
    }

    @Override
    public String toString() {
        return "Availability{" +
                "activity=" + activity +
                ", resident=" + resident +
                ", date=" + date +
                ", days=" + days +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", location=" + location +
                '}';
    }
}
