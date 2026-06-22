package kr.co.mindpro.ipms.domain.preference.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PreferenceResponse {

    public record PreferenceDetail(
            String appSeq,
            String officeSeq,
            String preferenceSeq,
            String priorCountryCode,
            String preferenceNo,
            String wipoCategoryCode,
            String preferenceSearch,
            String fullContentUrl,

            /* --- 날짜 정보 추가 (DueDate 연동용) --- */
            @Schema(description = "우선권 주장일", example = "20260115")
            String preferenceAssertDate,

            @Schema(description = "제출 마감일", example = "20260210")
            String submitDeadLineDate,

            @Schema(description = "제출 완료일", example = "20260205")
            String submitClosingDate,
            @Schema(description = "접수일", example = "20260205")
            String preferenceRegDate,

            @Schema(description = "비고", example = "일본 우선권 주장 및 관련 서류 확인 필요")
            String note
    ) {}
}
