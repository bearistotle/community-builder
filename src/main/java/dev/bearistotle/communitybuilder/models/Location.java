package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Transactional
public class Location {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3,max=30,message="Location names must be between 3 and 30 characters.")
    private String name;

    private int maxCapacity;

    @OneToMany
    @JoinColumn(name="event_id")
    private List<Event> events;

    public Location(String name, int maxCapacity){
        this.name = name;
        this.maxCapacity = maxCapacity;
    }
    public Location(){}

    public int getId(){
        return id;
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
}
