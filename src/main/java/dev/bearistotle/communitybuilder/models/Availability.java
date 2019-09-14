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
@DiscriminatorValue("availability")
@Transactional
public class Availability extends CalendarItem {

    @NotNull
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "availability_activity",
            joinColumns = { @JoinColumn(name = "availability_id") },
            inverseJoinColumns = { @JoinColumn(name = "activity_id") }
    )
    private List<Activity> activities;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    public Availability(String name,
                        String description,
                        User creator,
                        ArrayList<Activity> activities,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        super(name,
                description,
                date,
                startTime,
                endTime,
                recurrencePattern);
        this.creator = creator;
        this.activities = activities;
    }

    public Availability(){
        this.activities = new ArrayList<>();
    }

    public String getItemType(){
        return "availability";
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Availability{" +
                "availabilityId= " + this.getId() +
                "activities= " + activities +
                ", creator= " + creator +
                ", date= " + this.getDate() +
                ", startTime= " + this.getStartTime() +
                ", endTime= " + this.getEndTime() +
                ", recurrencePattern= " + this.getRecurrencePattern() + '\'' + '}';
    }
}
