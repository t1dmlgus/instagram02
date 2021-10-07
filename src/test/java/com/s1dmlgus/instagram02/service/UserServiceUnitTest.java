package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomException;
import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
import com.s1dmlgus.instagram02.web.dto.auth.JoinDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@Transactional
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("닉네임 중복검사 테스트")
    @Test
    public void duplicateUsernameTest() throws Exception {
        //given
        User user1 = createUser();
        User user2 = createUser();
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);

        //when


        //then
        assertThatThrownBy(() -> userService.duplicateUser(user2))
                .isInstanceOf(CustomException.class)
                .hasMessage("현재 사용중인 닉네임입니다.");
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

    @DisplayName("회원가입 단위 테스트")
    @Test
    public void joinTest() throws Exception {
        //given
        JoinDto joindto = createJoinDto();
        User user = joindto.toEntity();

        doReturn(user).when(userRepository).save(any(User.class));

        //when
        userService.join(joindto);

        //then
        assertThat(user.getUsername()).isEqualTo("s1dmlgus");

    }

    private JoinDto createJoinDto() {

        JoinDto joinDto1 = new JoinDto();
        joinDto1.setUsername("s1dmlgus");
        joinDto1.setPassword("1234");
        joinDto1.setEmail("dmlgusgngl@gmail.com");
        joinDto1.setName("dmlgus");

        return joinDto1;
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