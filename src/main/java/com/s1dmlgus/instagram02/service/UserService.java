package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.handler.exception.CustomException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserProfileResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeService subscribeService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    // 회원가입
    @Transactional
    public ResponseDto<?> join(JoinRequestDto joinRequestDto){

        User user = joinRequestDto.toEntity();

        // 1. 중복확인
        duplicateUser(user);
        // 2. 암호화
        bcryptPw(user);
        // 3. 권한 설정
        user.setRole(Role.ROLE_USER);

        // 4. 영속화
        User saveUser = userRepository.save(user);

        return new ResponseDto<>("회원가입이 정상적으로 되었습니다.", new JoinResponseDto(saveUser));
    }

    // 프로필 업데이트
    @Transactional
    public ResponseDto<?> update(long id, UserUpdateRequestDto userUpdateRequestDto) {

        // 1. 영속화
        User user = userRepository.findById(id).orElseThrow(
                ()-> {return new CustomApiException("찾을 수 없는 id입니다.");});

        // 2. 더티체킹
        user.updateUserProfile(userUpdateRequestDto);


        return new ResponseDto<>("회원 수정이 완료되었습니다.",user);
    }




    protected void bcryptPw(User user) {

        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.bcryptPw(encode);
    }

    protected void duplicateUser(User user){

        boolean existsUsername = userRepository.existsByUsername(user.getUsername());
        if(existsUsername){
            throw new CustomApiException("현재 사용중인 닉네임입니다.");
        }
    }

    // 프로필 가져오기
    @Transactional
    public UserProfileResponseDto getProfile(Long pageId, Long sessionId) {

        User user = userRepository.findById(pageId).orElseThrow(() -> {
            throw new CustomException("해당 유저가 없습니다");
        });
        logger.info("user : {}", user);

        boolean pageOwnerState = getPageOwnerState(pageId, sessionId);
        boolean subscribeState = subscribeService.getSubscribeState(pageId, sessionId);
        int subscribeCount = subscribeService.getSubscribeCount(pageId);


        return new UserProfileResponseDto(user, pageOwnerState, subscribeState, subscribeCount);
    }

    // 페이지 주인 확인
    protected boolean getPageOwnerState(Long pageid, Long sessionId) {

        return pageid.equals(sessionId);
    }



}
