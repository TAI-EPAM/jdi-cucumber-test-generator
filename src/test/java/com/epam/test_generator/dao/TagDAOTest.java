package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.entities.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class TagDAOTest {

    @Autowired
    TagDAO tagDAO;

    @Test
    public void testCreateAndRetrieve() {
        Tag originalTag = new Tag("tag1");

        long id = tagDAO.save(originalTag).getId();

        Tag newTag = new Tag("tag1");
        newTag.setId(id);

        Assert.assertEquals(newTag, tagDAO.findOne(id));
    }

    @Test
    public void testRemove() {
        Tag originalTag = new Tag("tag1");

        long id = tagDAO.save(originalTag).getId();
        tagDAO.delete(originalTag);

        Assert.assertTrue(!tagDAO.exists(id));
    }

    @Test
    public void testRemoveById() {
        Tag originalTag = new Tag("tag1");

        long id = tagDAO.save(originalTag).getId();
        tagDAO.delete(id);

        Assert.assertTrue(!tagDAO.exists(id));
    }

    @Test
    public void testAddList() {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

        List<Long> ids = tagDAO.save(tags).stream().map(Tag::getId).collect(Collectors.toList());

        Tag tag1 = new Tag("tag1");
        tag1.setId(ids.get(0));
        Tag tag2 = new Tag("tag2");
        tag2.setId(ids.get(1));
        Tag tag3 = new Tag("tag3");
        tag3.setId(ids.get(2));

        ArrayList<Tag> newTags = new ArrayList<>();
        newTags.add(tag1);
        newTags.add(tag2);
        newTags.add(tag3);

        Assert.assertTrue(newTags.equals(tagDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

        tagDAO.save(tags);

        tagDAO.delete(tags);

        Assert.assertTrue(tagDAO.findAll().isEmpty());
    }
}
