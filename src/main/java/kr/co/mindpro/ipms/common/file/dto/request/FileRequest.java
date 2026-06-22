package kr.co.mindpro.ipms.common.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 파일 업로드 요청 데이터
 */
@Builder
@Schema(description = "파일 업로드 요청", type = "object")
public record FileRequest(
		

    @Schema(description = "서류 일련번호 (PK)", example = "1")
    @NotBlank(message = "서류 일련번호는 필수입니다.")
    Long docSeq, // 데이터베이스와 연계될 서류 키값
		

    @Schema(description = "파일 비고 및 설명", example = "서류구분")
    String note
) {}
