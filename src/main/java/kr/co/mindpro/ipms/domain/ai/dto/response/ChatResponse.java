package kr.co.mindpro.ipms.domain.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "AI 결과요청")
public record ChatResponse (
    @Schema(description = "답변메세지")
    String resultMessage
){}
