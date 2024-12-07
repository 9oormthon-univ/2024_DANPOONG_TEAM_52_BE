package com.groom.orbit.ai.app.openai;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.groom.orbit.ai.app.AiService;
import com.groom.orbit.ai.app.VectorService;
import com.groom.orbit.ai.dao.vector.Vector;
import com.groom.orbit.goal.app.dto.response.RecommendGoalListResponseDto;
import com.groom.orbit.member.app.dto.response.GetFeedbackResponseDto;
import com.groom.orbit.quest.app.dto.response.RecommendQuestListResponseDto;
import com.groom.orbit.resume.app.dto.GetResumeResponseDto;
import com.groom.orbit.resume.app.dto.ResumeResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService implements AiService {

  private final ChatModel chatModel;
  private final VectorService vectorService;

  @Value("classpath:/templates/ai-feedback-prompt.txt")
  private Resource aiFeedbackPrompt;

  @Value("classpath:/templates/goal-recommend-prompt.txt")
  private Resource goalRecommendPrompt;

  @Value("classpath:/templates/quest-recommend-prompt.txt")
  private Resource questRecommendPrompt;

  private static final String PARAMETER_NEW_LINE_LIST_DELIMITER = "\n  -";
  private static final String PARAMETER_LIST_DELIMITER = ",";

  public GetFeedbackResponseDto getMemberFeedback(String interestJobs, GetResumeResponseDto dto) {
    BeanOutputConverter<GetFeedbackResponseDto> converter =
        getConverter(GetFeedbackResponseDto.class);
    String format = converter.getFormat();

    PromptTemplate promptTemplate = createPromptTemplate(aiFeedbackPrompt);
    String response =
        callChatModel(
            promptTemplate,
            Map.of(
                "job", interestJobs,
                "academy", convertResumeDtoToString(dto.academyList()),
                "career", convertResumeDtoToString(dto.careerList()),
                "qualification", convertResumeDtoToString(dto.qualificationList()),
                "experience", convertResumeDtoToString(dto.experienceList()),
                "etc", convertResumeDtoToString(dto.etcList()),
                "format", format));

    return converter.convert(response);
  }

  @Override
  public RecommendGoalListResponseDto recommendGoal(Long memberId) {
    BeanOutputConverter<RecommendGoalListResponseDto> converter =
        getConverter(RecommendGoalListResponseDto.class);
    String format = converter.getFormat();
    List<Vector> similarVectors = vectorService.findSimilarVector(memberId);

    Vector myVector = similarVectors.getFirst();
    List<Vector> othersVector = similarVectors.subList(1, similarVectors.size());

    PromptTemplate promptTemplate = createPromptTemplate(goalRecommendPrompt);
    String response =
        callChatModel(
            promptTemplate,
            Map.of(
                "job",
                String.join(PARAMETER_LIST_DELIMITER, myVector.interestJobs()),
                "myGoal",
                String.join(PARAMETER_LIST_DELIMITER, myVector.goals()),
                "goalList",
                String.join(
                    PARAMETER_LIST_DELIMITER,
                    othersVector.stream().flatMap(vector -> vector.goals().stream()).toList()),
                "format",
                format));
    return converter.convert(response);
  }

  @Override
  public RecommendQuestListResponseDto recommendQuest(Long memberId) {
    BeanOutputConverter<RecommendQuestListResponseDto> converter =
        getConverter(RecommendQuestListResponseDto.class);
    String format = converter.getFormat();
    List<Vector> similarVectors = vectorService.findSimilarVector(memberId);

    Vector myVector = similarVectors.getFirst();
    List<Vector> othersVector = similarVectors.subList(1, similarVectors.size());

    PromptTemplate promptTemplate = createPromptTemplate(questRecommendPrompt);
    String response =
        callChatModel(
            promptTemplate,
            Map.of(
                "job",
                convertListToString(myVector.interestJobs()),
                "goal",
                String.join(PARAMETER_LIST_DELIMITER, myVector.goals()),
                "myQuest",
                convertListToString(myVector.quests()),
                "questList",
                convertListToString(
                    othersVector.stream().flatMap(vector -> vector.goals().stream()).toList()),
                "format",
                format));
    return converter.convert(response);
  }

  private PromptTemplate createPromptTemplate(Resource resource) {
    return new PromptTemplate(resource);
  }

  private <T> BeanOutputConverter<T> getConverter(Class<T> converterClass) {
    return new BeanOutputConverter<>(converterClass);
  }

  private String convertListToString(List<String> data) {
    return String.join(PARAMETER_LIST_DELIMITER, data);
  }

  private String convertResumeDtoToString(List<ResumeResponseDto> data) {
    return String.join(
        PARAMETER_NEW_LINE_LIST_DELIMITER, data.stream().map(ResumeResponseDto::title).toList());
  }

  private String callChatModel(PromptTemplate promptTemplate, Map<String, Object> variables) {
    Prompt prompt = promptTemplate.create(variables);
    ChatResponse response = chatModel.call(prompt);

    log.info("prompt is {}", prompt.getContents());

    return response.getResult().getOutput().getContent();
  }
}
