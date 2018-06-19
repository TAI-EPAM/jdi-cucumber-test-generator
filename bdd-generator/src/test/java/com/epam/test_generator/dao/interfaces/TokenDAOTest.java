package com.epam.test_generator.dao.interfaces;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import com.epam.test_generator.entities.Token;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
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