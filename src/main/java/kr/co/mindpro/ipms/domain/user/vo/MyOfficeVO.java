package kr.co.mindpro.ipms.domain.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 멀티 사무소 드롭다운 조회용 VO.
 * findMyOffices 결과 매핑 전용 (사무소 정보 + 해당 유저의 membership 상태).
 */
@Getter
@Setter
@ToString
public class MyOfficeVO {
    private String officeSeq;
    private String officeShortName;
    private String officeAuthYn;     // Y=사업자, N=개인
    private String adminAuth;         // Y/N
    private String roleNm;            // utb_role_mst.role_nm
    private String roleType;          // utb_role_mst.role_type (SUPER_ADMIN/SYSTEM_ADMIN/SYSTEM_USER/CUSTOM/null)
    private String acctStatusCode;    // ACTIVE/PENDING/LOCKED/null (utb_user_info.acct_status_code)
}
