package kr.co.mindpro.ipms.domain.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

    @Builder
    @Schema(description = "AI 요청")
    public record ChatRequest(
            @NotBlank(message = "메세지는 필수입니다.")
            @Schema(description = "메세지", example = "당신에 대해서 알려주세요")
            String message,
            
            @Schema(description = "AI 모델 연결 식별자", example = "100")
            String aiCode,
            
            @Schema(description = "이전 대화 기록(선택)", example = "사용자: 안녕\\nAI: 안녕하세요")
            String history
    ) {}



