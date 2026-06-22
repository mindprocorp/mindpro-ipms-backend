package kr.co.mindpro.ipms.domain.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class WrapperMandateRequest {

    @Builder
    @Schema(description = "포괄위임 정보 저장 요청 객체")
    public record WrapperMandateDetail(
            @Schema(description = "포괄위임 일련번호 (수정 시 필수)", example = "WMAN20260000001", format = "SEQ")
            String wrappermandateSeq,

            @Schema(description = "고객 일련번호 (등록 시 필수)", example = "CUST20260000001", format = "SEQ")
            String customerSeq,

            @Schema(description = "변리사명", example = "강감찬")
            String attorneyName,

            @Schema(description = "지정변리사", example = "강감찬, 을지문덕")
            String designatedAttorney,

            @Schema(description = "대리인번호", example = "9-2026-000001-0")
            String agentNo,

            @Schema(description = "위임일", example = "20260210", format = "YYYYMMDD")
            String mandateDate,

            @Schema(description = "포괄위임 등록번호", example = "202612345678")
            String mandateWrapperNo,

            @Schema(description = "특허고객번호", example = "1-2026-000001-1")
            String patentCustomerNo,

            @Schema(description = "위임범위", example = "특허, 실용신안, 디자인에 관한 모든 절차")
            String mandateRange,

            @Schema(description = "정렬 순서", example = "1")
            Integer sort,

            @Schema(description = "비고", example = "특허고객번호 부여 전 임시 등록")
            String note
    ) {}
}
