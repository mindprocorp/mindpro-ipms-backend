package kr.co.mindpro.ipms.domain.system.vo;

import lombok.Data;

@Data
public class RoleMenuMapVO {
    private String mapSeq;
    private String roleSeq;
    private String menuSeq;
    private String canRead;
    private String canWrite;
    private String canDelete;
    private String canExcel;
    
    // 메뉴 정보 (findAuthorizedMenuCodes JOIN 결과)
    private String menuCd;
    private String menuNm;
    private String parentMenuSeq;
    private String menuUrl;
    private String menuIcon;
    private Integer dispOrd;
    private String dispType;   // GNB | ICON_SIDEBAR | HIDDEN
    private String menuType;   // FOLDER | PAGE
    private String sidebarYn;  // 좌측 사이드바 표시 여부
}
