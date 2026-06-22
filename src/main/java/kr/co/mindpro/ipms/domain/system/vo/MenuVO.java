package kr.co.mindpro.ipms.domain.system.vo;

import lombok.Data;

@Data
public class MenuVO {
    private String menuSeq;
    private String menuCd;
    private String menuNm;
    private String parentMenuSeq;
    private String menuUrl;
    private String menuIcon;
    private Integer dispOrd;
    private String useYn;
    private String delYn;
    private String dispType;   // GNB | ICON_SIDEBAR | HIDDEN
    private String menuType;   // FOLDER | PAGE
    private String sidebarYn;  // 좌측 사이드바 표시 여부
    private String superAdminOnly;  // Y: 슈퍼어드민 전용 (루트 체크 시 자식 자동 전파)
}
