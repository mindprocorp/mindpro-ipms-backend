package kr.co.mindpro.ipms.domain.jobprogress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 관계자 테이블 매핑 VO
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "관계자 상세 정보")
public class JobProgressVO extends BaseVO {
    private String officeSeq;
    private String progressSeq;
    private String tblCode;
    private String tblSeq;

    private String deptName;           // 부서명
    private String targetCode;         // 대상코드
    private String targetCodeName;         // 대상코드
    private String instructionContent; // 지시내용
    private String progressState;      // 상태
    private String extensionCount;     //기연횟수

    private String receiptDocName;     // 접수서류명
    private String receiptDocSeq;     // 접수서류명
    private String receiptDocContent;  // 접수서류내용
    private String submitDocName;      // 제출서류명
    private String submitDocSeq;      // 제출서류명



}