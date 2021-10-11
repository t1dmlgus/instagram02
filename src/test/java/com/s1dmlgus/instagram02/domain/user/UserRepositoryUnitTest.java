package com.s1dmlgus.instagram02.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;


@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) // ANY: 가짜 DB로 테스트, NONE: 실제 DB로 테스트
@DataJpaTest    // Repository 들을 다 IoC 등록
class UserRepositoryUnitTest {


    @Autowired
    private UserRepository userRepository;
    
    @DisplayName("유저 저장 단위 테스트")
    @Test
    public void save_User() throws Exception{
        //given
        createUser();

        //when
        User saveUser = userRepository.save(createUser());

        //then
        assertThat(saveUser.getUsername()).isEqualTo("t1dmlgus");
    }


    private User createUser() {
        return User.builder()
                .username("t1dmlgus")
                .password("1234")
                .email("dmlgus@gmail.com")
                .name("이의현")
                .build();
    }
}