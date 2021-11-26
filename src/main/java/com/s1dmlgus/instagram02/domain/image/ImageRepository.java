package com.s1dmlgus.instagram02.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByUserId(Long userId);

}
