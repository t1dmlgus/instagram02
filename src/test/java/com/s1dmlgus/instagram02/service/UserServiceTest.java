package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.web.dto.JoinDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @DisplayName("닉네임 중복검사 테스트")
    @Test
    public void duplicateUsernameTest() throws Exception {
        //given

        User user1 = createUser();
        User user2 = createUser();
        //when
        userRepository.save(user1);


        //then
        assertThatThrownBy(() -> userService.duplicateUser(user2))
                .isInstanceOf(Exception.class)
                .hasMessage("현재 사용중인 닉네임입니다.");
    }

    private User createUser() {
        return User.builder()
                .username("t1dmlgus")
                .password("1234")
                .email("dmlgus@gmail.com")
                .name("이의현")
                .build();
    }

    @DisplayName("비밀번호 암호화 테슽")
    @Test
    public void bcryptPwTest() throws Exception {
        //given
        User user = createUser();

        //when
        userService.bcryptPw(user);

        //then
        assertThat(user.getPassword()).isNotEqualTo("1234");
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void joinTest() throws Exception {
        //given
        JoinDto joinDto = new JoinDto("s1dmlgus", "1234", "dmlgus@gamil.com", "이의현");

        //when
        userService.join(joinDto);
        User user = userRepository.findById(1L).get();

        //then
        assertThat(user.getUsername()).isEqualTo("s1dmlgus");

    }





}