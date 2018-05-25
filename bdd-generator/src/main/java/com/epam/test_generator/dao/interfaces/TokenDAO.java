package com.epam.test_generator.dao.interfaces;


import com.epam.test_generator.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDAO extends JpaRepository<Token, Long> {

    Token findByTokenUuid(String tokenUuid);

}