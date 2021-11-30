package com.s1dmlgus.instagram02.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Modifying
    @Query(value = "INSERT INTO subscribe(from_user_id, to_user_id, created_date) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
    void subscribe(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    @Modifying
    @Query(value = "DELETE FROM subscribe WHERE from_user_id = :fromUserId AND to_user_id = :toUserId", nativeQuery = true)
    void unSubscribe(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);


    // 구독 상태
    @Query(value = "SELECT COUNT(*) FROM subscribe where from_user_id = :principalId and to_user_id = :pageId", nativeQuery = true)
    int mSubscribeState(@Param("pageId") Long pageId, @Param("principalId") Long principalId);

    // 구독자 수
    @Query(value = "SELECT COUNT(*) FROM subscribe where to_user_id = :pageId", nativeQuery = true)
    int mSubscribeCount(@Param("pageId") Long pageId);


}
