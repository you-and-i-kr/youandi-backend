package com.example.coupleapp.repository;


import com.example.coupleapp.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    List<PhotoEntity> findByPhotoId(Long photoId);

    boolean existsByPhotoId(Long photoId);

    boolean existsByMyPhoneNumber(String my_phone_number);

    boolean existsByYourPhoneNumber(String your_phone_number);
    PhotoEntity findUserByPhotoId(Long photoId);
}