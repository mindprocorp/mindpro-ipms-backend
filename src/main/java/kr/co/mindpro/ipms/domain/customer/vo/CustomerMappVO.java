package kr.co.mindpro.ipms.domain.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 고객 매핑 정보 객체
 * 각 업무(사건)와 고객(출원인/의뢰인 등)의 관계를 정의합니다.
 *
 * @author	 : min
 * @fileName	 : CustomerMappVO.java
 * @since	 : 2026. 01. 07.
 */

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public class CustomerMappVO extends BaseVO {
    @Schema(description = "고객 매핑 일련번호")
    private String customerMappSeq;

    @Schema(description = "사무소 일련번호")
    private String officeSeq;

    @Schema(description = "고객 일련번호")
    private String customerSeq;

    @Schema(description = "업무 식별자 (APPMST 등)")
    private String tblSeq;

    @Schema(description = "관계 코드 (CURTMR 등)")
    private String relationCode;

    @Schema(description = "지분율")
    private String shareRatio;

    @Schema(description = "고객 카테고리 코드 (client, applicant 등)")
    private String customerCategoryCode;

    @Schema(description = "정렬 순서")
    private String orderNo;

}