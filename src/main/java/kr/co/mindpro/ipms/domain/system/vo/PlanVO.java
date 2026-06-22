package kr.co.mindpro.ipms.domain.system.vo;

import lombok.Data;

@Data
public class PlanVO {
    private String planSeq;
    private String planCd;
    private String planNm;
    private String note;
    private Integer sortOrd;
    private String useYn;
    private String delYn;
    private String createUser;    // insertPlan XML에서 참조
    private String updateUser;    // updatePlan XML에서 참조
    private Integer officeCount;  // 조회 시 매핑 집계
    private Integer userCount;    // 플랜에 속한 사무소들의 사용자 수 합계
    private Integer menuCount;    // 플랜에 허용된 메뉴 수
}
