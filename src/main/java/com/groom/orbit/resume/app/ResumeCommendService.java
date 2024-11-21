package com.groom.orbit.resume.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.dto.ResumeRequestDto;
import com.groom.orbit.resume.dao.ResumeRepository;
import com.groom.orbit.resume.dao.entity.Resume;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeCommendService {

  private final ResumeRepository resumeRepository;
  private final MemberQueryService memberQueryService;

  public CommonSuccessDto createResume(Long memberId, ResumeRequestDto request) {

    Member member = memberQueryService.findMember(memberId);

    resumeRepository.save(request.toResume(member));

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateResume(Long resumeId, ResumeRequestDto requestDto) {

    Resume resume =
        resumeRepository
            .findById(resumeId)
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESUME));

    resume.updateResume(requestDto);

    resumeRepository.save(resume);

    return CommonSuccessDto.fromEntity(true);
  }
}
