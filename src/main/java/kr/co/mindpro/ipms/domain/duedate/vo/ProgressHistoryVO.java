package kr.co.mindpro.ipms.domain.duedate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 기일 테이블 매핑 객체
 * DB 테이블 Duedate_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : DuedateVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "기일 통합 관리 객체 (Master + Mapping 통합)")
public class ProgressHistoryVO extends BaseVO {

    private String appSeq;           // 출원 키
    private String conflictSeq;           // 심판 키
    private String caseCategoryName; // 사건구분 명칭 (내국/외국)
    private String progressTypeName; // 구분 명칭 (접수/발송)
    private String rightTypeName;    // 권리 명칭 (특허/상표 등)
    private String rightTypeCode;    // 권리 명칭 (특허/상표 등)
    private String ourRef;           // OurRef (관리번호)
    private String appDate;          // 출원일 (YYYYMMDD)
    private String appNo;            // 출원번호
    private String regDate;          // 등록일 (YYYYMMDD)
    private String regNo;            // 등록번호
    private String productClass;     // 분류 (NICE 분류 등)
    private String eventDate;        // 실제 발생일 (접수/발송일)
    private String content;          // 내용 (서류명 등)
    private String applicantName;    // 출원인 명칭
    private String titleKo;          // 국문 명칭
}