package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.Versionable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;


/**
 * This class represents example of possible steps for user.
 */
@Entity
public class StepSuggestion implements Versionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private StepType type;

    @Version
    @NotNull
    private Long version;

    @ManyToOne
    @JoinTable(
        name = "PROJECT_STEPSUGGESTION",
        joinColumns = @JoinColumn(name = "STEPSUGGESTION_ID"),
        inverseJoinColumns = @JoinColumn(name = "PROJECT_ID")
    )
    private Project project;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "STEPSUGGESTION_STEP")
    private List<Step> steps = new ArrayList<>();

    private ZonedDateTime lastUsedDate = ZonedDateTime.now();

    public StepSuggestion() {
    }

    public StepSuggestion(String content, StepType type) {
        this.content = content;
        this.type = type;
    }

    public StepSuggestion(DefaultStepSuggestion defaultStepSuggestion){
        this.content = defaultStepSuggestion.getContent();
        this.type = defaultStepSuggestion.getType();
    }

    public StepSuggestion(String content, StepType type, List<Step> steps) {
        this.content = content;
        this.type = type;
        this.steps = steps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void add(Step step) {
        steps.add(step);
        this.lastUsedDate = ZonedDateTime.now();
    }

    public void remove(Step step) {
        steps.remove(step);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ZonedDateTime getLastUsedDate() {
        return lastUsedDate;
    }

    public void setLastUsedDate(ZonedDateTime lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepSuggestion that = (StepSuggestion) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            Objects.equals(type,that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, type);
    }

    @Override
    public String toString() {
        return "StepSuggestion{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", type=" + type +
            ", version=" + version +
            ", steps=" + steps +
            ", lastUsedDate" + lastUsedDate +
            '}';
    }
}
