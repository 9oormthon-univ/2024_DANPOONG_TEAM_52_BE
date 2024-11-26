package com.groom.orbit.resume.app;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.common.exception.CommonException;
import com.groom.orbit.common.exception.ErrorCode;
import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;
import com.groom.orbit.resume.app.dto.ResumeResponseDto;
import com.groom.orbit.resume.dao.ResumeRepository;
import com.groom.orbit.resume.dao.entity.ResumeCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeQueryService {

  private final ResumeRepository resumeRepository;
  private final MemberQueryService memberQueryService;

  public GetResumeResponseDto getResume(Long memberId) {

    Map<ResumeCategory, List<ResumeResponseDto>> categorizedResumes =
        resumeRepository.findAllByMemberId(memberId).stream()
            .map(ResumeResponseDto::toResumeResponseDto)
            .collect(Collectors.groupingBy(ResumeResponseDto::resumeCategory));

    return new GetResumeResponseDto(
        categorizedResumes.getOrDefault(ResumeCategory.ACADEMY, List.of()),
        categorizedResumes.getOrDefault(ResumeCategory.CAREER, List.of()),
        categorizedResumes.getOrDefault(ResumeCategory.QUALIFICATION, List.of()),
        categorizedResumes.getOrDefault(ResumeCategory.EXPERIENCE, List.of()),
        categorizedResumes.getOrDefault(ResumeCategory.ETC, List.of()));
  }

  public GetResumeResponseDto checkIsResume(Long memberId, Long otherId) {

    Member member = memberQueryService.findMember(otherId);

    if (member.getIsProfile().equals(true)) {
      return getResume(otherId);
    } else {
      if (memberId.equals(otherId)) {
        return getResume(memberId);
      } else {
        throw new CommonException(ErrorCode.NOT_MATCH_AUTH_CODE);
      }
    }
  }

  public List<String> convertToResumeStrings(GetResumeResponseDto responseDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Map<ResumeCategory, List<ResumeResponseDto>> categorizedResumes =
        Map.of(
            ResumeCategory.ACADEMY, responseDto.academyList(),
            ResumeCategory.CAREER, responseDto.careerList(),
            ResumeCategory.QUALIFICATION, responseDto.qualificationList(),
            ResumeCategory.EXPERIENCE, responseDto.experienceList(),
            ResumeCategory.ETC, responseDto.etcList());

    return Arrays.stream(ResumeCategory.values())
        .map(
            category -> {
              List<ResumeResponseDto> resumeList =
                  categorizedResumes.getOrDefault(category, List.of());

              if (resumeList.isEmpty()) {
                return String.format("Category: %s\n없음.\n", category);
              }

              String details =
                  resumeList.stream()
                      .map(
                          dto -> {
                            String startDate =
                                dto.startDate() != null ? sdf.format(dto.startDate()) : "N/A";
                            String endDate =
                                dto.endDate() != null ? sdf.format(dto.endDate()) : "N/A";
                            return String.format(
                                "제목: %s\n내용: %s\n시작일: %s\n마감일: %s",
                                dto.title(), dto.content(), startDate, endDate);
                          })
                      .collect(Collectors.joining("\n\n"));

              return String.format("Category: %s\n%s", category, details);
            })
        .collect(Collectors.toList());
  }
}
