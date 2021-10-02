package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import com.s1dmlgus.instagram02.web.dto.JoinDto;
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
    public void join(JoinDto joinDto){

        User user = joinDto.toEntity();

        // 1. 중복확인
        duplicateUser(user);
        // 2. 암호화
        bcryptPw(user);
        // 3. 권한 설정
        user.setRole(Role.USER);

        // 4. 영속화
        userRepository.save(user);
    }


    protected void bcryptPw(User user) {

        String encode = bCryptPasswordEncoder.encode(user.getUsername());
        user.bcryptPw(encode);
    }

    protected void duplicateUser(User user){

        boolean existsUsername = userRepository.existsByUsername(user.getUsername());
        if(existsUsername){
            throw new RuntimeException("현재 사용중인 닉네임입니다.");
        }
    }
}
