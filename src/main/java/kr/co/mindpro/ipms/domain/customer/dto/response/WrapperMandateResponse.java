package kr.co.mindpro.ipms.domain.customer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.customer.vo.WrapperMandateVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WrapperMandateResponse {

    @Builder
    @Schema(description = "포괄위임 정보 응답 객체")
    public record WrapperMandateDetail(
            @Schema(description = "포괄위임 일련번호", example = "WMAN20260000001", format = "SEQ")
            String wrappermandateSeq,

            @Schema(description = "고객 일련번호", example = "CUST20260000001", format = "SEQ")
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
    ) {
        /**
         * VO -> Response DTO 변환 정적 메서드
         */
        public static WrapperMandateDetail from(WrapperMandateVO vo) {
            if (vo == null) return null;

            return WrapperMandateDetail.builder()
                    .wrappermandateSeq(vo.getWrappermandateSeq())
                    .customerSeq(vo.getCustomerSeq())
                    .attorneyName(vo.getAttorneyName())
                    .designatedAttorney(vo.getDesignatedAttorney())
                    .agentNo(vo.getAgentNo())
                    .mandateDate(vo.getMandateDate())
                    .mandateWrapperNo(vo.getMandateWrapperNo())
                    .patentCustomerNo(vo.getPatentCustomerNo())
                    .mandateRange(vo.getMandateRange())
                    .note(vo.getNote())
                    .sort(vo.getSortOrder()) // VO는 sortOrder, DTO는 sort로 매핑
                    .build();
        }
    }
}
