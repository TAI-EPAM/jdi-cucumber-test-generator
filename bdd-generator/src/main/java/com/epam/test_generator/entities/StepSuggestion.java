package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.Versionable;
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

    public StepSuggestion() {
    }

    public StepSuggestion(String content, StepType type) {
        this.content = content;
        this.type = type;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
