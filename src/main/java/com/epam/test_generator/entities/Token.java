package com.epam.test_generator.entities;

import com.epam.test_generator.entities.api.TokenTrait;

import java.time.ZonedDateTime;
import java.util.Objects;
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
    private ZonedDateTime expiryDate;

    public static Token withExpiryDuration(Integer minutes) {
        Token token = new Token();
        token.tokenUuid = UUID.randomUUID().toString();
        token.expiryDate = ZonedDateTime.now().plusMinutes(minutes);
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

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(int minutes) {
        this.expiryDate = ZonedDateTime.now().plusMinutes(minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return Objects.equals(id, token.id) &&
            Objects.equals(tokenUuid, token.tokenUuid) &&
            Objects.equals(user, token.user) &&
            Objects.equals(expiryDate, token.expiryDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tokenUuid, user, expiryDate);
    }
}
