package com.s1dmlgus.instagram02.web.controller.api;

import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class AuthApiControllerIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원가입 통합 테스트")
    @Test
    public void joinTest() throws Exception{
        //given
        JoinDto joinDto = createJoinDto();

        //when
        ResponseDto<?> join = userService.join(joinDto);
        String message = join.getMessage();

        //then
        assertThat(message).isEqualTo("회원가입이 정상적으로 되었습니다.");

    }




    private JoinDto createJoinDto(){

        JoinDto joinDto = new JoinDto();
        joinDto.setUsername("t1dmlgus");
        joinDto.setPassword("1234");
        joinDto.setEmail("dmlgusgngl@gmail.com");
        joinDto.setName("이의현");

        return joinDto;
    }


}
