package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.image.Image;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.UserProfileResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@Transactional
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    Logger logger = LoggerFactory.getLogger(UserServiceUnitTest.class);

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
                .isInstanceOf(CustomApiException.class)
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

    @DisplayName("회원가입 테스트")
    @Test
    public void joinTest() throws Exception {
        //given
        JoinRequestDto joindto = createJoinDto();
        User user = joindto.toEntity();

        doReturn(user).when(userRepository).save(any(User.class));

        //when
        ResponseDto<?> join = userService.join(joindto);
        logger.info("join {} :", join);

        //then
        assertThat(join.getMessage()).isEqualTo("회원가입이 정상적으로 되었습니다.");

    }

    @DisplayName("회원정보 수정 테스트")
    @Test
    public void userUpdateTest() throws Exception{
        //given
        User user = createJoinDto().toEntity();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        //when
        ResponseDto<?> update = userService.update(1L, createUpdateRequestDto());
        
        //then
        assertThat(update.getMessage()).isEqualTo("회원 수정이 완료되었습니다.");
        
    }

    
    @DisplayName("프로필정보 가져오기 테스트")
    @Test
    public void getProfileTest() throws Exception{
        //given
        Long userId = 1L;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(createProfile()));

        //when
        UserProfileResponseDto profile = userService.getProfile(userId);
        logger.info("profile : {}", profile);

        //then
        Assertions.assertThat(profile.getImages().get(0).getCaption()).isEqualTo("테스트이미지");

    }
    
    
    // 회원정보수정 DTO
    private UserUpdateRequestDto createUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .name("이의현")
                .website("github/t1dmlgus")
                .bio("안녕하세요")
                .phone("01022")
                .gender("남")
                .build();
    }


    // 회원가입 DTO
    private JoinRequestDto createJoinDto() {

        JoinRequestDto joinRequestDto1 = new JoinRequestDto();
        joinRequestDto1.setUsername("s1dmlgus");
        joinRequestDto1.setPassword("1234");
        joinRequestDto1.setEmail("dmlgusgngl@gmail.com");
        joinRequestDto1.setName("dmlgus");

        return joinRequestDto1;
    }


    // 유저생성 Entity
    private User createUser() {
        return User.builder()
                .username("test1Dmlgus")
                .password("1234")
                .email("dmlgus@gmail.com")
                .name("테스트의현")
                .build();
    }


    // 프로필dto 주입
    private User createProfile(){

        User user = createUser();
        Image image = Image.builder()
                .caption("테스트이미지")
                .postImageUrl("테스트명.jpg")
                .user(user)
                .build();
        user.getImages().add(image);

        return user;

    }

}