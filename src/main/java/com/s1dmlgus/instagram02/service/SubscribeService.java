package com.s1dmlgus.instagram02.service;


import com.s1dmlgus.instagram02.domain.subscribe.SubscribeRepository;
import com.s1dmlgus.instagram02.handler.exception.CustomApiException;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager em;


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

    // 구독 유저 조회
    @Transactional(readOnly = true)
    public List<SubscribeDto> subscribeList(Long principalId, Long pageId) {

        // 쿼리 준비
        String sb = "SELECT u.id, u.username, u.profile_Image_Url, " +
                "if((SELECT true FROM subscribe WHERE from_User_id = ? AND to_User_id = u.id), 1, 0) follow, " +
                "if((?=u.id), 1, 0) iam " +
                "FROM user u INNER JOIN subscribe s " +
                "ON u.id = s.from_User_id " +
                "WHERE s.to_User_id = ? ";

        // 1. 물음표 principalId
        // 2. 물음표 principalId
        // 3. 물음표 pageUserId

        // 쿼리 완성
        Query nativeQuery = em.createNativeQuery(sb)
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, pageId);


        // qlrm 라이브러리 -> dto에 DB결과를 매핑하기 위해
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDto> list =result.list(nativeQuery, SubscribeDto.class);

        return list;
    }


    // 구독 상태 확인
    @Transactional(readOnly = true)
    public boolean getSubscribeState(Long pageId, Long sessionId) {

        return  subscribeRepository.mSubscribeState(pageId, sessionId) == 1;
    }

    // 구독자 수 카운트
    @Transactional(readOnly = true)
    public int getSubscribeCount(Long pageId) {

        return subscribeRepository.mSubscribeCount(pageId);
    }


}
