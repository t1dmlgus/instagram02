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
    @Query(value = "DELETE FROM subscribe WHERE from_user_id = :fromUserId AND to_user_id = :toUserId)", nativeQuery = true)
    void unSubscribe(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);



}
