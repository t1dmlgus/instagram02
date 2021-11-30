package com.s1dmlgus.instagram02.service;

import com.s1dmlgus.instagram02.web.dto.subscribe.SubscribeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SubscribeServiceUnitTest {

    @Autowired
    private SubscribeService subscribeService;

//    @Spy
//    private EntityManager em;
//    @Mock
//    private JpaResultMapper jpaResultMapper;
//
    @DisplayName("구독자 리스트 테스트")
    @Test
    public void subscribeList() throws Exception{
        //given

        //when
        List<SubscribeDto> subscribeDtos = subscribeService.subscribeList(2L, 3L);
        for (SubscribeDto subscribeDto : subscribeDtos) {
            System.out.println("subscribeDto = " + subscribeDto);
        }

        //then
        //Assertions.assertThat(subscribeDtos.get(0).getUsername()).isEqualTo("t1dmlgus");


    }


}