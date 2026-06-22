package kr.co.mindpro.ipms.domain.duedate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

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
public class DueDateManageVO extends BaseVO {

    // --- [1. 사건 및 권리 정보] ---
    private String caseCategoryCode;    // 사건구분 코드
    private String caseCategoryName;    // 사건구분 명칭 (국내/해외 등)
    private String dueDateCode;         // 구분 코드
    private String dueDateName;         // 구분 명칭 (출원/심판 등)
    private String rightTypeCode;       // 권리구분 코드
    private String rightTypeName;       // 권리구분 명칭 (특허/상표 등)
    private String countryCode;         // 국가 코드
    private String countryName;         // 국가 명칭
    private String titleKo;             // 국문 명칭
    private String titleEn;             // 영문 명칭

    // --- [2. 날짜 및 마감 정보] ---
    private String deadline;             // 마감일 (YYYYMMDD)
    private String dueTypeCode;         // 마감종류 코드
    private String dueTypeName;         // 마감종류 명칭

    private String appSeq;
    private String appNo;               // 출원번호
    private String appDate;             // 출원일 (YYYYMMDD)
    private String regNo;               // 등록번호
    private String regDate;             // 등록일 (YYYYMMDD)

    // --- [3. 관리 번호 정보] ---
    private String ourRef;              // 자사관리번호
    private String yourRef;             // 고객관리번호
    private String applicantRefNo;      // 출원인관리번호
    private String appRouteCode;            // 출원루트
    private String appRouteName;            // 출원루트

    // --- [4. 사람 및 조직 정보 (SEQ & Name)] ---
    private String applicantSeq;        // 출원인 SEQ
    private String applicantName;       // 출원인 명칭
    private String clientSeq;           // 의뢰인 SEQ
    private String clientName;          // 의뢰인 명칭
    private String inventorName;        // 발명자 명칭
    private String inventor;        // 발명자
    private String deptName;            // 부서명

    private String adminMgrSeq;         // 관리담당자 SEQ
    private String adminMgrName;        // 관리담당자 명칭
    private String caseMgrSeq;          // 사건담당자 SEQ
    private String caseMgrName;         // 사건담당자 명칭
    private String attorneySeq;         // 담당변호인 SEQ
    private String attorneyName;        // 담당변호인 명칭

    // --- [5. 기타 내부 관리 필드] ---
    private String tblSeq;              // 원천 테이블 PK
    private String officeSeq;           // 사무소 PK
    private String duedateSeq;          // 기일 마스터 PK
    private String duedateCompleteYn;   // 처리완료 여부 (Y/N)
}