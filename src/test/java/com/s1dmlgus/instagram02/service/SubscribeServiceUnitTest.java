package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.domain.subscribe.SubscribeRepository;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.subscribe.SubscribeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@Transactional
@ExtendWith(MockitoExtension.class)
class SubscribeServiceUnitTest {


    @InjectMocks
    private SubscribeService subscribeService;

    @Mock
    private SubscribeRepository subscribeRepository;
    @Mock
    private EntityManager em;

    private Long sessionUserId;
    private Long pageUserId;

    @BeforeEach
    public void setUp(){
        sessionUserId = 1L;
        pageUserId = 2L;
    }




    @DisplayName("구독하기 테스트")
    @Test
    public void subscribeTest() throws Exception{
        //given


        doNothing().when(subscribeRepository).subscribe(anyLong(), anyLong());
        when(subscribeRepository.mSubscribeCount(pageUserId)).thenReturn(1);


        //when
        ResponseDto<?> subscribe = subscribeService.subscribe(sessionUserId, pageUserId);

        //then
        assertThat(subscribe.getMessage()).isEqualTo("구독하였습니다.");
    }

    
    @DisplayName("구독취소 테스트")
    @Test
    public void unSuscribeTest() throws Exception{
        //given

        doNothing().when(subscribeRepository).unSubscribe(anyLong(), anyLong());
        when(subscribeRepository.mSubscribeCount(pageUserId)).thenReturn(1);


        //when
        ResponseDto<?> unSubscribe = subscribeService.unSubscribe(sessionUserId, pageUserId);


        //then
        assertThat(unSubscribe.getMessage()).isEqualTo("구독 취소하였습니다.");


    }

    @DisplayName("구독자 리스트 가져오기 테스트")
    @Test
    public void subscribeList() throws Exception{
        //given

        SubscribeDto subscribeDto = new SubscribeDto(BigInteger.valueOf(1), "t1dmlgus", null, 1, 0);
        List<Object> ar = new ArrayList<>();
        Object[] result_object = {BigInteger.valueOf(1), "t1dmlgus", null, 1, 0};
        ar.add(result_object);


        String sb = "SELECT u.id, u.username, u.profile_Image_Url, " +
                "if((SELECT true FROM subscribe WHERE from_User_id = ? AND to_User_id = u.id), 1, 0) follow, " +
                "if((?=u.id), 1, 0) iam " +
                "FROM user u INNER JOIN subscribe s " +
                "ON u.id = s.from_User_id " +
                "WHERE s.to_User_id = ? ";

        Query mockedQuery = mock(Query.class);


        when(em.createNativeQuery(sb)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyInt(), anyLong())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(ar);

        //when
        List<SubscribeDto> subscribeDtos = subscribeService.subscribeList(sessionUserId, pageUserId);

        //then
        assertThat(subscribeDtos.get(0).getUsername()).isEqualTo("t1dmlgus");



    }

    @DisplayName("구독 상태 확인 테스트")
    @Test
    public void getSubscribeStateTest() throws Exception{
        //given
        when(subscribeRepository.mSubscribeState(pageUserId, sessionUserId)).thenReturn(1);


        //when
        boolean subscribeState = subscribeService.getSubscribeState(pageUserId, sessionUserId);

        //then
        assertThat(subscribeState).isTrue();
    }

    @DisplayName("구독자 수 카운트 테스트")
    @Test
    public void getSubscribeCountTest() throws Exception{
        //given

        when(subscribeRepository.mSubscribeCount(sessionUserId)).thenReturn(2);

        //when
        int subscribeCount = subscribeService.getSubscribeCount(sessionUserId);

        //then
        assertThat(subscribeCount).isEqualTo(2);
    }


}