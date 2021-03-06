package com.s1dmlgus.instagram02.config.oauth;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class Oauth2DetailsService extends DefaultOAuth2UserService {

    Logger logger = LoggerFactory.getLogger(Oauth2DetailsService.class);

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        logger.info(oAuth2User.getAttributes().toString());

        Map<String, Object> userInfo = oAuth2User.getAttributes();

        String username = "facebook_" + (String) userInfo.get("id");
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .name(name)
                    .build();

            user.setRole(Role.ROLE_USER);

            return new PrincipalDetails(userRepository.save(user), oAuth2User.getAttributes());           // ?????? -> ????????? ??????
        } else {                             // ?????????????????? ?????? ??????????????? ????????????
            return new PrincipalDetails(userEntity, oAuth2User.getAttributes());

        }
    }
}
