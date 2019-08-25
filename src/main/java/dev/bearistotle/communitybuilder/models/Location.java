package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

// TODO: Add reserved dates (and other important fields) to this class

@Entity
@Transactional
@Table(name = "Location")
public class Location {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int locationId;

    @NotNull
    @Size(min=3,max=30,message="Location names must be between 3 and 30 characters.")
    private String name;

    private int maxCapacity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="location_id")
    private List<Event> events;

    public Location(String name, int maxCapacity){
        this.name = name;
        this.maxCapacity = maxCapacity;
    }
    public Location(){}

    public int getLocationId(){
        return locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public void removeEvent(Event event){
        this.events.remove(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return locationId == location.locationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
