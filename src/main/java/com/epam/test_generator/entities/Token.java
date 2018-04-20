package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.TokenTrait;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * This class represents token entity.
 * Token is a special key used to confirm user identity while performing password reset.
 *
 */
@Entity
public class Token implements TokenTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String tokenUuid;

    @OneToOne
    private User user;

    @NotNull
    private Date expiryDate;

    public static Token withExpiryDuration(Integer minutes) {
        Token token = new Token();
        token.tokenUuid = UUID.randomUUID().toString();
        token.expiryDate = Date.from(Instant.now().plus(minutes, ChronoUnit.MINUTES));
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getTokenUuid() {
        return tokenUuid;
    }

    public void setTokenUiid(String tokenUiid) {
        this.tokenUuid = tokenUiid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(int minutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.expiryDate = now.getTime();
    }

}
