package dev.bearistotle.communitybuilder.models;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import static javax.persistence.FetchType.LAZY;

@Entity
@Inheritance
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "item_type")
@Table(name = "calendar_item")
@Transactional
public abstract class CalendarItem {

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private int id;

    @NotNull
    @Size(min = 3, max = 200, message = "Name must be 3--200 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 500, message = "Description must be 10--500 characters.")
    private String description;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String recurrencePattern;

    /**
     * Each calendarItem will have a probability of being in each Topic and then in each subtopic that falls beneath
     * each Topic. These will be stored in a HashMap with the classifier name (Topics, Subtopic 1, Subtopic 2, etc.) as
     * the key and a HashMap with the classes in the classifier as the key and the probability of the calendarItem being
     * in the class as the value. One major choice point in the logic of my app is whether I get and store the classifi-
     * cation data for every Topic and SubTopic for every calItem or whether I only get and store SubTopic data for the
     * most probable Topics or Topics with greater than a certain degree of probability.
     */
    @Lob
    @Basic(fetch = LAZY)
    private HashMap<String, HashMap<String, Double>> classification;

    /**
     * Storing matchLevels as a field in each object duplicates 50% of matchLevel data, as the match between C1 and C2
     * is the same as the match between C2 and C1, but C1 stores the former and C2 the latter. This is another choice
     * point in my app. The problem with creating a new class that would store each matchLevel relation just once is
     * that it will be harder to generate and to know how to access the specific relation. If each calItem stores its
     * matchLevel to each other, I can get each by using the id of the calItem in question as the key and the level as
     * the value. If the data is stored independently, it's harder for me to see what the key would be and while I could
     * set up some policy and follow it, doing so would make my code less intuitive/harder to read and it would make the
     * relationships harder to generate. As is, I can easily loop through all the other calItems and generate and store
     * a match level.
     */
    private HashMap<Integer, Double> matchLevels;

    public CalendarItem(String name,
                        String description,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrencePattern = recurrencePattern;
        this.classification = new HashMap<>();
        this.matchLevels = new HashMap<>();
    }

    public CalendarItem() {
        this.classification = new HashMap<>();
        this.matchLevels = new HashMap<>();
    }

    abstract void classify();

    abstract void calculateMatchLevels();

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

    public HashMap<String, HashMap<String, Double>> getClassification() {
        return classification;
    }

    public void setClassification(HashMap<String, HashMap<String, Double>> classification) {
        this.classification = classification;
    }

    public HashMap<Integer, Double> getMatchLevels() {
        return matchLevels;
    }

    public void setMatchLevels(HashMap<Integer, Double> matchLevels) {
        this.matchLevels = matchLevels;
    }

    public void addMatchLevel(Integer calItemId, Double matchLevel) {
        this.matchLevels.put(calItemId, matchLevel);
    }
}