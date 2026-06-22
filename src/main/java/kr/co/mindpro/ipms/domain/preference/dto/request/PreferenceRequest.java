package kr.co.mindpro.ipms.domain.preference.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PreferenceRequest {
    @Schema(description = "우선권 상세 정보")
    public record PreferenceDetail(
            @Schema(description = "출원 일련번호", example = "APP202600001")
            String appSeq,

            @Schema(description = "우선권 일련번호 (수정 시 필수)", example = "PREF202600001")
            String preferenceSeq,

            @Schema(description = "선출원 국가 코드", example = "JP")
            String priorCountryCode,

            @Schema(description = "우선권 번호", example = "JP2025-123456")
            String preferenceNo,

            @Schema(description = "WIPO 분류 코드", example = "A01B")
            String wipoCategoryCode,

            @Schema(description = "우선권 조회", example = "Y")
            String preferenceSearch,

            @Schema(description = "원문 URL", example = "https://patentscope.wipo.int/search/en/detail.jsf?docId=JP123456")
            String fullContentUrl,

            @Schema(description = "비고", example = "일본 우선권 주장 및 관련 서류 확인 필요")
            String note,

            /* --- 날짜 정보 추가 (DueDate 연동용) --- */
            @Schema(description = "우선권 주장일", example = "20260115")
            String preferenceAssertDate,

            @Schema(description = "제출 마감일", example = "20260210")
            String submitDeadLineDate,

            @Schema(description = "제출 완료일", example = "20260205")
            String submitClosingDate,

            @Schema(description = "접수일", example = "20260205")
            String preferenceRegDate


    ) {}
}
