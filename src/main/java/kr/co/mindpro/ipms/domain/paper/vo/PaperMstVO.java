package kr.co.mindpro.ipms.domain.paper.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : CostVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperMstVO extends BaseVO {
    @Schema(description = "파일매핑 일련번호", example = "FLM20260000001")
    private String fileMappSeq;

    @Schema(description = "사무소 시퀀스", example = "PGOKR20260000002")
    private String officeSeq;

    @Schema(description = "업무(테이블) 시퀀스 (CFL, APP, BILL 등의 PK)", example = "CFT20260000022")
    private String tblSeq;

    @Schema(description = "업무코드 ", example = "CFTMST")
    private String tblCode;

    @Schema(description = "문서Seq", example = "CFT20260000022")
    private String docSeq;
    @Schema(description = "문서명", example = "특허출원서")
    private String docName;

    @Schema(description = "파일 유형 코드 (10:접수, 20:제출, 90:기타)", example = "10")
    private String fileTypeCode;

    @Schema(description = "파일 종류 코드 (10:문서, 20:이미지, 90:기타)", example = "10")
    private String fileKindCode;
    private String fileKindName;

    @Schema(description = "파일 분류 코드 (10:국내, 20:개국, 30:PCT 등)", example = "10")
    private String fileCategoryCode;

    @Schema(description = "물리 파일 일련번호 (utb_file_mst 연동)", example = "F2026_0ET6YJ9G6S1QE")
    private String fileSeq;

    @Schema(description = "입력받은 등록일", example = "20260120")
    private OffsetDateTime inputCreateAt;
}