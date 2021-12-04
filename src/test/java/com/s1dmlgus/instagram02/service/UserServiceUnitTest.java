package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @Mock
    private SubscribeService subscribeService;

    private User user;

    @BeforeEach
    public void setUp(){

        user = User.builder()
                .id(1L)
                .username("testID")
                .password("1234")
                .email("test@gmail.com")
                .name("이의현")
                .build();
    }

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("닉네임 중복검사 테스트")
    @Test
    public void duplicateUsernameTest() throws Exception {
        //given
        User user1 = User.builder()
                .username("테스트1")
                .build();
        User user2 = User.builder()
                .username("테스트1")
                .build();


        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);

        //when


        //then
        assertThatThrownBy(() -> userService.duplicateUser(user2))
                .isInstanceOf(CustomApiException.class)
                .hasMessage("현재 사용중인 닉네임입니다.");
    }


    @DisplayName("비밀번호 암호화 테스트")
    @Test
    public void bcryptPwTest() throws Exception {
        //given

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
        ResponseDto<?> update = userService.update(createUpdateRequestDto(), new PrincipalDetails(user));

        //then
        assertThat(update.getMessage()).isEqualTo("회원 수정이 완료되었습니다.");

    }

    
    @DisplayName("회원정보 가져오기 테스트")
    @Test
    public void getProfileTest() throws Exception{
        //given
        Long sessionId = user.getId();
        Long pageId = 2L;
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(pageId);
        when(subscribeService.getSubscribeState(pageId, sessionId)).thenReturn(true);
        when(subscribeService.getSubscribeCount(pageId)).thenReturn(1);

        
        //when
        ResponseDto<?> profile = userService.profile(pageId, new PrincipalDetails(user));
        logger.info("profile : {}", profile);

        //then
        assertThat(profile.getMessage()).isEqualTo("회원 정보를 조회합니다.");
  
    }
    
    @DisplayName("로그인 유저가 해당 페이지 주인인지 확인 테스트")
    @Test
    public void getPageOwnerStateTest() throws Exception{
        //given
        Long pageId = 1L;

        //when
        boolean pageOwnerState = userService.getPageOwnerState(pageId, user.getId());

        //then
        assertThat(pageOwnerState).isTrue();
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


}