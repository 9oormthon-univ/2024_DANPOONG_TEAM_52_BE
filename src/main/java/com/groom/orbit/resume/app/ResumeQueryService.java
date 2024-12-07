package com.groom.orbit.resume.app;

import static com.groom.orbit.resume.dao.entity.ResumeCategory.*;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.orbit.member.app.MemberQueryService;
import com.groom.orbit.member.dao.jpa.entity.Member;
import com.groom.orbit.resume.app.dto.GetOtherResumeResponseDto;
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

  public GetResumeResponseDto getMyResume(Long memberId) {
    Map<ResumeCategory, List<ResumeResponseDto>> categorizedResumes =
        getCategorizedResume(memberId);

    return new GetResumeResponseDto(
        categorizedResumes.getOrDefault(ACADEMY, List.of()),
        categorizedResumes.getOrDefault(CAREER, List.of()),
        categorizedResumes.getOrDefault(QUALIFICATION, List.of()),
        categorizedResumes.getOrDefault(EXPERIENCE, List.of()),
        categorizedResumes.getOrDefault(ETC, List.of()));
  }

  public GetOtherResumeResponseDto getOtherResume(Long otherId) {
    Member member = memberQueryService.findMember(otherId);

    Map<ResumeCategory, List<ResumeResponseDto>> categorizedResumes = getCategorizedResume(otherId);

    return new GetOtherResumeResponseDto(
        categorizedResumes.getOrDefault(ACADEMY, List.of()),
        categorizedResumes.getOrDefault(CAREER, List.of()),
        categorizedResumes.getOrDefault(QUALIFICATION, List.of()),
        categorizedResumes.getOrDefault(EXPERIENCE, List.of()),
        categorizedResumes.getOrDefault(ETC, List.of()),
        member.getImageUrl(),
        member.getNickname());
  }

  private Map<ResumeCategory, List<ResumeResponseDto>> getCategorizedResume(Long memberId) {
    return resumeRepository.findAllByMemberId(memberId).stream()
        .map(ResumeResponseDto::fromResume)
        .collect(Collectors.groupingBy(ResumeResponseDto::resumeCategory));
  }

  /** TODO Resume가 없는 사용자 조회시 빈 화면이 나와야할 것 같은데 ?? */
  public Object checkIsResume(Long memberId, Long otherId) {
    if (memberId.equals(otherId)) {
      return getMyResume(memberId);
    }
    return getOtherResume(otherId);
  }

  @Deprecated
  public List<String> convertToResumeStrings(GetResumeResponseDto responseDto) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Map<ResumeCategory, List<ResumeResponseDto>> categorizedResumes =
        Map.of(
            ACADEMY, responseDto.academyList(),
            CAREER, responseDto.careerList(),
            QUALIFICATION, responseDto.qualificationList(),
            EXPERIENCE, responseDto.experienceList(),
            ETC, responseDto.etcList());

    return Arrays.stream(values())
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
                                dto.startDate() != null ? formatter.format(dto.startDate()) : "N/A";
                            String endDate =
                                dto.endDate() != null ? formatter.format(dto.endDate()) : "N/A";
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
