package kr.co.mindpro.ipms.common.nts.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 국세청 사업자 통신용 DTO 모음
 */
public class NtsDto {

    /**
     * API 요청 객체
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StatusRequest {
        private List<String> b_no; // 사업자번호 리스트
    }

    /**
     * API 응답 객체
     */
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusResponse {
        private String b_no;            // 사업자등록번호
        private String b_stts;          // 상태 (계속사업자, 휴업, 폐업)
        private String b_stts_cd;       // 상태코드 (01, 02, 03)
        private String tax_type;        // 과세유형
        private String tax_type_cd;     // 과세유형코드
        private String end_dt;          // 폐업일
        private String utcc_yn;         // 결과유무
        private String rbf_tax_type;    // 직전과세유형
        private String rbf_tax_type_cd; // 직전과세유형코드
    }
}