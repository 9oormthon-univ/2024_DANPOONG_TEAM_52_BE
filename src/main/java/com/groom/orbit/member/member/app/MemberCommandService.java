package com.groom.orbit.member.member.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.groom.orbit.S3.S3UploadService;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.member.member.app.dto.request.UpdateMemberRequestDto;
import com.groom.orbit.member.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.member.dao.jpa.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {

  private final MemberRepository memberRepository;
  private final MemberQueryService memberQueryService;
  private final S3UploadService s3UploadService;

  public CommonSuccessDto updateMember(Long memberId, UpdateMemberRequestDto requestDto) {
    Member member = memberQueryService.findMember(memberId);

    member.updateMember(requestDto);

    memberRepository.save(member);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateMemberProfileImage(Long memberId, MultipartFile multipartFile) {

    Member member = memberQueryService.findMember(memberId);

    String newProfileUrl = s3UploadService.uploadFile(multipartFile);

    member.setImageUrl(newProfileUrl);

    memberRepository.save(member);

    return CommonSuccessDto.fromEntity(true);
  }
}
