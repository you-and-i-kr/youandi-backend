package com.example.coupleapp.service;

import com.example.coupleapp.dto.PhotoDTO;
import com.example.coupleapp.entity.MemberEntity;
import com.example.coupleapp.entity.PhotoEntity;
import com.example.coupleapp.exception.domian.PhotoErrorCode;
import com.example.coupleapp.exception.domian.PhotoException;
import com.example.coupleapp.repository.MemberRepository;
import com.example.coupleapp.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final S3ImageService s3ImageService;
    private final MemberRepository memberRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, S3ImageService s3ImageService, MemberRepository memberRepository) {
        this.photoRepository = photoRepository;
        this.s3ImageService = s3ImageService;
        this.memberRepository = memberRepository;
    }

    @Override
    public PhotoDTO uploadPhoto(MultipartFile file,Long memberId) {
        // Amazon S3에 이미지 업로드 및 URL 받아오는 로직 (s3ImageService를 사용)
        String imageUrl = s3ImageService.uploadImageFile(file);
        // 데이터베이스에 사진 메타데이터 저장
        MemberEntity member = memberRepository.findMemberByMemberId(memberId);

        PhotoEntity photo = new PhotoEntity();
        photo.setImgUrl(imageUrl);
        photo.setMyPhoneNumber(member.getMy_phone_number());
        photo.setYourPhoneNumber(member.getYour_phone_number());
        photo.setMember(member);
        // 다른 필드 설정
        PhotoEntity savedPhoto = photoRepository.save(photo);

        return convertToDTO(savedPhoto);
    }
//
//    @Override
//    public List<PhotoDTO> getAllPhotos() {
//        // 데이터베이스에서 모든 사진 목록 가져오는 로직 (photoRepository 사용)
//        List<PhotoEntity> photos = photoRepository.findAll();
//        return photos.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

    @Override
    public PhotoDTO getPhotoById(Long photoId) {
        // 특정 ID에 해당하는 사진을 데이터베이스에서 가져오는 로직 (photoRepository 사용)
        Optional<PhotoEntity> optionalPhoto = photoRepository.findById(photoId);
        if (optionalPhoto.isPresent()) {
            PhotoEntity photo = optionalPhoto.get();
            return convertToDTO(photo);
        } else {
            // 적절한 예외 처리
            return null;
        }
    }

    @Override
    public PhotoDTO updatePhoto(Long photoId, PhotoDTO updatedPhotoDTO) {
        return null;
    }

    @Override
    public PhotoEntity updatePhoto(Long photoId, MultipartFile file) {
        PhotoEntity existingPhoto = photoRepository.findById(photoId)
                .orElseThrow(()-> new PhotoException(PhotoErrorCode.FAIL_UPDATE));
        existingPhoto.setImgUrl(existingPhoto.getImgUrl());
        // Save the updated memoEntity in the repository
        return photoRepository.save(existingPhoto);
    }

    @Override
    public void deletePhoto(Long photoId) {
        // 특정 ID에 해당하는 사진 삭제하는 로직 (photoRepository 사용)
        photoRepository.deleteById(photoId);
    }

    private PhotoDTO convertToDTO(PhotoEntity photo) {
        // Photo 엔티티를 PhotoDTO로 변환하는 로직
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhotoId(photo.getPhotoId());
        photoDTO.setImgUrl(photo.getImgUrl());
        photoDTO.setMyPhoneNumber(photo.getMyPhoneNumber());
        photoDTO.setYourPhoneNumber(photo.getYourPhoneNumber());
        // 다른 필드 설정
        return photoDTO;
    }
}
