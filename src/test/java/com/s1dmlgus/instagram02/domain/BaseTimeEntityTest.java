package com.s1dmlgus.instagram02.domain;

import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BaseTimeEntityTest {


    @Autowired
    public UserRepository userRepository;

    @DisplayName("BaseTimeEntity 등록 테스트")
    @Test
    public void createBaseTimeEntityTest() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        User save = userRepository.save(User.builder()
                .username("t1dmlgus")
                .password("1234")
                .email("dmlgus@gmail.com")
                .name("의현")
                .build());


        //then
        Assertions.assertThat(save.getCreatedDate()).isAfter(now);
    }

}