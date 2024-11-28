package com.groom.orbit.member.app;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.groom.orbit.S3.S3UploadService;
import com.groom.orbit.common.dto.CommonSuccessDto;
import com.groom.orbit.config.openai.AiFeedbackRequestDto;
import com.groom.orbit.config.openai.AiFeedbackResponseDto;
import com.groom.orbit.config.openai.OpenAiClient;
import com.groom.orbit.job.app.InterestJobService;
import com.groom.orbit.job.app.dto.JobDetailResponseDto;
import com.groom.orbit.member.app.dto.request.UpdateMemberRequestDto;
import com.groom.orbit.member.dao.jpa.MemberRepository;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.ResumeQueryService;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {

  private final MemberRepository memberRepository;
  private final InterestJobService interestJobService;
  private final ResumeQueryService resumeQueryService;
  private final MemberQueryService memberQueryService;
  private final S3UploadService s3UploadService;
  private final OpenAiClient openAiClient;

  private AiFeedbackRequestDto createAiFeedbackRequest(Long memberId) {

    List<String> jobName =
        interestJobService.findJobsByMemberId(memberId).stream()
            .map(JobDetailResponseDto::name)
            .toList();
    String job = String.join(",", jobName);

    GetResumeResponseDto getResumeResponseDto = resumeQueryService.getResume(memberId);

    List<String> categoryStringList =
        resumeQueryService.convertToResumeStrings(getResumeResponseDto);

    String academy = categoryStringList.get(0);
    String career = categoryStringList.get(1);
    String qualification = categoryStringList.get(2);
    String experience = categoryStringList.get(3);
    String etc = categoryStringList.get(4);

    return AiFeedbackRequestDto.from(job, academy, career, qualification, experience, etc);
  }

  public String createAiFeedbackResponse(Long memberId) {
    AiFeedbackResponseDto responseDto =
        openAiClient.createAiFeedback(createAiFeedbackRequest(memberId));

    Member member = memberQueryService.findMember(memberId);

    member.setAiFeedback(responseDto.getAnswer());

    memberRepository.save(member);

    return responseDto.getAnswer();
  }

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
