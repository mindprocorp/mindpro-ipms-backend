package kr.co.mindpro.ipms.domain.code.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시스템 등록 정보 공통 응답")
public record DocumentRequest(
    @Schema(description = "접수제출 구분 ", example = "10") String entry_type,
    @Schema(description = "권리 구분", example = "10") String pat_type,
    @Schema(description = "서류구분", example = "10") String doc_div
) {}