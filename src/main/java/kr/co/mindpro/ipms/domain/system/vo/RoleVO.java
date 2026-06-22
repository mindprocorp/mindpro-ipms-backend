package kr.co.mindpro.ipms.domain.system.vo;

import lombok.Data;

@Data
public class RoleVO {
    private String roleSeq;
    private String roleCd;
    private String roleNm;
    private String note;
    private String officeSeq;
    private String delYn;
    private String useYn;
    private String roleType; // SYSTEM_ADMIN, SYSTEM_USER, CUSTOM(기본)
}
