package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.SubscribeService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class SubscribeApiController {

    private final SubscribeService subscribeService;

    // 구독하기
    @PostMapping("/api/subscribe/{toUserId}")
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long toUserId) {

        ResponseDto<?> subscribe = subscribeService.subscribe(principalDetails.getUser().getId(), toUserId);

        return new ResponseEntity<>(subscribe, HttpStatus.OK);
    }

    // 구독 취소하기
    @DeleteMapping("/api/subscribe/{toUserId}")
    public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long toUserId){

        ResponseDto<?> unSubscribe = subscribeService.unSubscribe(principalDetails.getUser().getId(), toUserId);

        return new ResponseEntity<>(unSubscribe, HttpStatus.OK);
    }

    // 구독 유저 조회하기
    @GetMapping("/api/subscribe/{pageId}")
    public ResponseEntity<?> subscribe(@PathVariable Long pageId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        List<SubscribeDto> subscribeDtos = subscribeService.subscribeList(principalDetails.getUser().getId(), pageId);

        return new ResponseEntity<>(subscribeDtos, HttpStatus.OK);

    }


}
