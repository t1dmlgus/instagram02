package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public ResponseDto<?> Update(long id, UserUpdateRequestDto userUpdateRequestDto) {

        // 1. 영속화
        User user = userRepository.findById(id).get();

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
            throw new CustomException("현재 사용중인 닉네임입니다.");
        }
    }
}
