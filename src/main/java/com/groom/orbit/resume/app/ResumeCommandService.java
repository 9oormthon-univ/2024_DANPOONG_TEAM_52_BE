package com.groom.orbit.resume.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.goal.app.MemberGoalService;
import com.groom.orbit.goal.dao.entity.MemberGoal;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.dto.ResumeRequestDto;
import com.groom.orbit.resume.app.dto.ResumeResponseDto;
import com.groom.orbit.resume.dao.ResumeRepository;
import com.groom.orbit.resume.dao.entity.Resume;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeCommandService {

  private final ResumeRepository resumeRepository;
  private final MemberQueryService memberQueryService;
  private final MemberGoalService memberGoalService;

  public Resume findResume(Long resumeId) {
    return resumeRepository
        .findById(resumeId)
        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESUME));
  }

  public CommonSuccessDto createResume(Long memberId, ResumeRequestDto request) {
    Member member = memberQueryService.findMember(memberId);

    resumeRepository.save(request.toResume(member));

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto updateResume(Long memberId, Long resumeId, ResumeRequestDto requestDto) {
    Resume resume = findResume(resumeId);
    resume.validate(memberId);

    resume.update(requestDto);

    return CommonSuccessDto.fromEntity(true);
  }

  public CommonSuccessDto deleteResume(Long memberId, Long resumeId) {
    Resume resume = findResume(resumeId);
    resume.validate(memberId);

    resume.delete();
    resumeRepository.deleteById(resumeId);

    return CommonSuccessDto.fromEntity(true);
  }

  public ResumeResponseDto createResumeFromMemberGoal(
      Long memberId, Long memberGoalId, ResumeRequestDto requestDto) {
    MemberGoal memberGoal = memberGoalService.findByMemberIdAndId(memberId, memberGoalId);
    Member member = memberGoal.getMember();
    Resume resume = requestDto.toResume(member);

    resume.createFromMemberGoal(memberGoal);
    resumeRepository.save(resume);

    return ResumeResponseDto.fromResume(resume);
  }
}
