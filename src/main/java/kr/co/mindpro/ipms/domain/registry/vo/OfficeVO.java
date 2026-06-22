package kr.co.mindpro.ipms.domain.registry.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 사무소 마스터 정보 Value Object
 *
 * @author	 : intst
 * @fileName : OfficeVO.java
 * @since	 : 2026. 1. 7.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OfficeVO extends BaseVO {
    private String officeSeq;
    private String officeShortName;
    private String officeAddr;
    private String officeTel;
    private String officeAuthYn;
    private String officeStateCode;
    private String officeInviteCode;
    private String planSeq;          // 현재 배정된 플랜 seq (조회용)
    private String currentPlanNm;    // 현재 배정된 플랜명 (재배정 모달에서 표시용)
}
