package kr.co.mindpro.ipms.domain.duedate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 관계자 상세 정보 응답 DTO
 */
@Data
@SuperBuilder
@Schema(description = "기일 정보 요약 응답")
public class DueDateResponse {
    @Builder
    @Schema(description = "기일 상세 내역 (목록용)")
    public record DueDateDetail(
            // --- [1. 사건 기본 정보] ---
            @Schema(description = "사건구분 (국내/해외/외국)")
            CommonRecordResponse.CodeInfo caseCategory,
            CommonRecordResponse.CodeInfo appRoute,

            @Schema(description = "구분 (출원/심판/분쟁 등)")
            CommonRecordResponse.CodeInfo dueDate,

            @Schema(description = "국가 정보")
            CommonRecordResponse.CodeInfo country,

            @Schema(description = "권리구분 (특허/실용/디자인/상표)")
            CommonRecordResponse.CodeInfo rightType,

            @Schema(description = "국문 명칭", example = "차세대 반도체 제조 장치")
            String titleKo,

            @Schema(description = "영문 명칭", example = "Next-gen Semiconductor Device")
            String titleEn,

            // --- [2. 날짜 및 마감 정보] ---
            @Schema(description = "마감일 (YYYYMMDD)", example = "20260520")
            String deadline,

            @Schema(description = "마감종류 (OA/등록료/심사청구 등)")
            CommonRecordResponse.CodeInfo dueType,
            @Schema(description = "출원키", example = "10-2026-1234567", type ="SEQ")
            String appSeq,
            @Schema(description = "출원번호", example = "10-2026-1234567")
            String appNo,

            @Schema(description = "출원일 (YYYYMMDD)", example = "20260130")
            String appDate,

            @Schema(description = "등록번호", example = "10-1234567-0000")
            String regNo,

            @Schema(description = "등록일 (YYYYMMDD)", example = "20260312")
            String regDate,

            // --- [3. 관리 번호 정보] ---
            @Schema(description = "OurRef (자사관리번호)", example = "REF-2026-001")
            String ourRef,

            @Schema(description = "YourRef (고객관리번호)", example = "ABC-US-26-01")
            String yourRef,

            @Schema(description = "출원인관리번호", example = "CLIENT-NO-99")
            String applicantRefNo,

            // --- [4. 관계자 및 조직 정보] ---
            @Schema(description = "출원인 정보")
            CommonRecordResponse.PersonInfo applicant,

            @Schema(description = "의뢰인 정보")
            CommonRecordResponse.PersonInfo client,

            @Schema(description = "발명자 명칭 (다수일 수 있어 성명만 처리)", example = "홍길동 외 2명")
            String inventor,

            @Schema(description = "부서", example = "IP전략팀")
            String deptName,

            @Schema(description = "관리담당자 정보")
            CommonRecordResponse.PersonInfo adminMgr,

            @Schema(description = "사건담당자 정보")
            CommonRecordResponse.PersonInfo caseMgr,

            @Schema(description = "담당변호인(변리사) 정보")
            CommonRecordResponse.PersonInfo attorney,

            @Schema(description = "기일 마스터 PK (완료 처리용)")
            String duedateSeq,

            @Schema(description = "원천 테이블 PK")
            String tblSeq,

            @Schema(description = "처리완료 여부", example = "N")
            String duedateCompleteYn
    ) {}

    @Builder
    @Schema(description = "접발송내역")
    public record ProgressHistoryDetail(
            String appSeq,           // 출원 키 (상세 이동용)
            String conflictSeq,      //심판키
            String caseCategoryName, // 사건구분 (내국/외국)
            String progressTypeName, // 구분 (접수/발송)
            CommonRecordResponse.CodeInfo rightType,
            String ourRef,           // OurRef
            String appDate,          // 출원일
            String appNo,            // 출원번호
            String regDate,          // 등록일
            String regNo,            // 등록번호
            String productClass,     // 분류 (분류/분류코드)
            String eventDate,        // 일자 (접수/발송일)
            String content,          // 내용 (서류명)
            String applicantName,    // 출원인
            String titleKo           // 국문명칭
    ) {}




}
