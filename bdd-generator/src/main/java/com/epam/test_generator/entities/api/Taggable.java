package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Tag;

import java.util.HashSet;
import java.util.Set;

/**
 * Interface for classes which have set of tags.
 * Provides some methods for manipulating this tags.
 */
public interface Taggable {

    Set<Tag> getTags();

    void setTags(Set<Tag> tags);

    default void removeTags() {
        setTags(new HashSet<>());
    }

    default void updateTags (Set<Tag> newTags) {
        if (newTags.isEmpty()) {
            removeTags();
        } else {
            setTags(newTags);
        }
    }

}
