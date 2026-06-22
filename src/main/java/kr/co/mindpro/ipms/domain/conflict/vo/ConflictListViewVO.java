package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 분쟁/심판(Conflict) 통합 매핑 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "분쟁/심판 통합 등록 및 수정 데이터 객체")
public class ConflictListViewVO {


    @Schema(description = "분쟁 일련번호", example = "1001")
    private String cftSeq;  //

    @Schema(description = "OurRef (출원관리번호)", example = "OUR-2026-001")
    private String ourRef; // J

    @Schema(description = "YourRef (고객관리번호)", example = "YOUR-999")
    private String yourRef; //

    @Schema(description = "현재상태", example = "진행중")
    private String currentStatus; //

    @Schema(description = "포기건 여부", example = "Y")
    private String isAbandoned; //

    @Schema(description = "사건구분(구분)", example = "심판")
    private String caseCategory;

    @CommonMapping(type="DATE", group="CFT", code="", description="접수일")
    @Schema(description = "접수일", example = "20260116")
    private String receiptDate;

    @Schema(description = "계류법정", example = "특허법원")
    private String pendingCourt;

    @Schema(description = "사건번호", example = "2026허1234")
    private String caseNo;

    @Schema(description = "사건종류", example = "무효심판")
    private String caseType;

    @Schema(description = "대리인구분", example = "내자")
    private String agentCategory;

    @CommonMapping(type="PERSON", group="CFT", code="", description="의뢰인")
    @Schema(description = "의뢰인")
    private String clientName;

    @CommonMapping(type="DATE", group="APP", code="", description="청구마감일(사건마감일)")
    @Schema(description = "사건마감일", example = "20261231")
    private String dueLimitDate;

    /**
     * ConflictMstVO(DB 데이터)를 ConflictListViewVO(화면 데이터)로 변환
     */
//    public static ConflictListViewVO from(ConflictMstVO db) {
//        if (db == null) return null;
//
//        return ConflictListViewVO.builder()
//                .cftSeq(db.getConflictSeq())
//                .caseCategory(db.getCaseCategoryCode())
//                .pendingCourt(db.getLitigationOffice())
//                .caseNo(db.getLitigationCaseNo())
//                .cas(db.getCaseTypeyCode())
//                .agentCategory(db.getAgentCategoryCode())
//                .currentStatus(db.getState())
//                // 포기 여부 로직 포함
//                .isAbandoned(org.springframework.util.StringUtils.hasText(db.getFinalResult())
//                        && db.getFinalResult().contains("포기") ? "Y" : "N")
//                .build();
//    }
    public void fillFromMaster(AppBasicInfoVO appVo) {
        if (appVo != null) {
            this.ourRef = appVo.getAssetNo();
            this.yourRef = appVo.getAgentRef();
            this.clientName = appVo.getAppCategory();
        }
    }
}