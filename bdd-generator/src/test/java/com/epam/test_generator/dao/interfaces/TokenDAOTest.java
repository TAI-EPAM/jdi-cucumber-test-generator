package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Token;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class TokenDAOTest {

    @Autowired
    private TokenDAO sut;

    @Test
    public void findByToken_SimpleToke_Ok() {
        Token token = Token.withExpiryDuration(15);
        token.setTokenUiid("token");
        sut.save(token);
        Token byToken = sut.findByTokenUuid("token");
        Assert.assertThat(byToken, is(equalTo(token)));
    }
}