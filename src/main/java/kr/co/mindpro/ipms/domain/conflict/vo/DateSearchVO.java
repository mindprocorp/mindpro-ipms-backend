package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 분쟁/심판(Conflict) 통합 매핑 VO
 */
@Data
@Builder
@Schema(description = "분쟁/심판 통합 등록 및 수정 데이터 객체")
public class DateSearchVO {

    private String filterCode; // SearchFieldVO의 filterCode (예: D001, D002)
    private String startDate;  // 시작일 (YYYYMMDD)
    private String endDate;    // 종료일 (YYYYMMDD)
}