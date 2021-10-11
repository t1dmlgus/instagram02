package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
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

        user.setRole(Role.USER);

        // 4. 영속화
        User saveUser = userRepository.save(user);


        // user 엔티티를 dto 내에 넣어도 되는지, 아니면 ResponseUserDto를 만드는게 좋을지.,.
        return new ResponseDto<>("회원가입이 정상적으로 되었습니다.", saveUser.toDto());
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
