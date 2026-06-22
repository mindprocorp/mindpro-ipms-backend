package kr.co.mindpro.ipms.domain.jobprogress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import kr.co.mindpro.ipms.domain.jobprogress.dto.request.JobProgressRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : ParticipantVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor  // MyBatis 객체 생성을 위해 필수
@AllArgsConstructor // SuperBuilder와 함께 쓰기 위해 필요
@EqualsAndHashCode(callSuper = true)
public class JobProgressMergeVO extends BaseVO {
    @Schema(description = "진행사항 일련번호")
    private String progressSeq;

    @Schema(description = "사무소 일련번호")
    private String officeSeq;

    @Schema(description = "업무 테이블 코드", example = "PROGRS")
    private String tblCode;

    @Schema(description = "업무 테이블 시퀀스")
    private String tblSeq;

    @Schema(description = "대상코드")
    private String targetCode;

    @Schema(description = "대상코드")
    private String targetCodeName;

    @Schema(description = "부서명")
    private String deptName;

    @Schema(description = "진행 상태")
    private String progressState;

    // --- 1. 통지 / 접수 ---
    @CommonMapping(type="DATE", group="PRG", code="", description="통지일")
    private String noticeDate;

    @CommonMapping(type="DATE", group="PRG", code="", description="대리인 접수일")
    private String agentReceiptDate;

    @Schema(description = "접수서류명")
    private String receiptDocName;

    @Schema(description = "접수서류 시퀀스")
    private String receiptDocSeq;

    @Schema(description = "접수서류 내용")
    private String receiptDocContent;

    @CommonMapping(type="PERSON", group="PRG", code="", description="심사관")
    private String examiner;

    // --- 2. 접수 보고 / 검토 ---
    @CommonMapping(type="DATE", group="PRG", code="", description="접수보고기한")
    private String receiptReportLimitDate;

    @CommonMapping(type="DATE", group="PRG", code="", description="접수보고일")
    private String receiptReportDate;

    @CommonMapping(type="PERSON", group="PRG", code="", description="접수보고자")
    private String receiptReportManager;

    @CommonMapping(type="DATE", group="PRG", code="", description="검토의견기한")
    private String reviewOpinionLimitDate;

    @CommonMapping(type="DATE", group="PRG", code="", description="검토보고일")
    private String reviewReportDate;

    @CommonMapping(type="PERSON", group="PRG", code="", description="검토보고자")
    private String reviewReportManager;

    // --- 3. 지시 / 기연 / 제출 ---
    @CommonMapping(type="DATE", group="PRG", code="", description="지시일")
    private String instructionDate;

    @Schema(description = "지시내용")
    private String instructionContent;

    @Schema(description = "기연 여부/내용")
    private String extensionCount;

    @CommonMapping(type="DATE", group="PRG", code="", description="문서제출기한")
    private String documentLimitDate;

    @CommonMapping(type="DATE", group="PRG", code="", description="문서제출일")
    private String documentSubmitDate;

    @Schema(description = "제출서류명")
    private String submitDocName;

    @Schema(description = "제출서류명")
    private String submitDocSeq;

    @CommonMapping(type="PERSON", group="PRG", code="", description="제출담당자")
    private String submitManager;

    @Schema(description = "비고")
    private String note;

    // --- 4. 제출 보고 ---
    @CommonMapping(type="DATE", group="PRG", code="", description="제출보고기한")
    private String submitReportLimitDate;

    @CommonMapping(type="DATE", group="PRG", code="", description="제출보고일")
    private String submitReportDate;

    @CommonMapping(type="PERSON", group="PRG", code="", description="제출보고자")
    private String submitReportManager;

    // @CommonMapping에서 code가 비어있으면 기본적으로 필드명에 Name이 붙은 곳에 이름을 주입하는 구조라면 아래와 같이 추가합니다.
    private String examinerName;
    private String receiptReportManagerName;
    private String reviewReportManagerName;
    private String submitManagerName;

    private String submitReportManagerName;




    public void fillFromRequest(JobProgressRequest.JobProgressDetail request) {
        this.targetCode = request.target().code();
        this.deptName = request.deptName();
        this.progressState = request.progressState();

        this.noticeDate = request.noticeDate();
        this.agentReceiptDate = request.agentReceiptDate();
        //this.receiptDocName = request.receiptDocName();
        this.receiptDocContent = request.receiptDocContent();
        this.examiner = request.examiner().userSeq();

        this.receiptReportLimitDate = request.receiptReportLimitDate();
        this.receiptReportDate = request.receiptReportDate();
        this.receiptReportManager = request.receiptReportManager().userSeq();

        this.reviewOpinionLimitDate = request.reviewOpinionLimitDate();
        this.reviewReportDate = request.reviewReportDate();
        this.reviewReportManager = request.reviewReportManager().userSeq();

        this.instructionDate = request.instructionDate();
        this.instructionContent = request.instructionContent();

        this.extensionCount = request.extensionCount();
        this.documentLimitDate = request.documentLimitDate();
        this.documentSubmitDate = request.documentSubmitDate();
        //this.submitDocName = request.submitDocName();
        this.submitManager = request.submitManager().userSeq();
        this.note = request.note();

        this.submitReportLimitDate = request.submitReportLimitDate();
        this.submitReportDate = request.submitReportDate();
        this.submitReportManager = request.submitReportManager().userSeq();
    }

}