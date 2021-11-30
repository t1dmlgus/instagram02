package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.domain.subscribe.SubscribeRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    // 구독하기
    @Transactional
    public ResponseDto<?> subscribe(Long fromUserId, Long toUserId){

        int subscribeCount = 0;

        try {
            subscribeRepository.subscribe(fromUserId, toUserId);
            subscribeCount= subscribeRepository.mSubscribeCount(toUserId);
        } catch (Exception e) {
            throw new CustomApiException("이미 구독한 상태입니다.");
        }

        return new ResponseDto<>("구독하였습니다.", subscribeCount);
    }

    // 구독 취소하기
    @Transactional
    public ResponseDto<?> unSubscribe(Long fromUserId, Long toUserId){

        subscribeRepository.unSubscribe(fromUserId, toUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(toUserId);

        return new ResponseDto<>("구독 취소하였습니다.", subscribeCount);
    }



}
