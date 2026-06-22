package kr.co.mindpro.ipms.domain.code.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import lombok.Builder;

/**
 * [DTO] 공통 코드 관리(CUD) 요청 객체
 * 관리 화면에서의 마스터/상세 코드 저장 및 일괄 처리를 담당합니다.
 *
 * @author   : intst
 * @fileName : CodeManageRequest.java
 * @since    : 2026. 01. 19.
 */
@Schema(description = "공통 코드 관리 요청 데이터")
public class CodeManageRequest {

    /**
     * 공통 코드 마스터 저장 요청
     */
    @Builder
    @Schema(description = "마스터 코드 저장 정보")
    public record Master(
        @NotBlank(message = "그룹 코드는 필수입니다.")
        @Schema(description = "그룹 코드", example = "USE_YN") String grpCd,
        
        @NotBlank(message = "코드 명은 필수입니다.")
        @Schema(description = "코드 명", example = "사용여부") String cdNm,
        
        @Schema(description = "표시 순서") Integer dispOrd,
        @Schema(description = "비고") String note,
        @Schema(description = "행 상태 (I: 추가, U: 수정, D: 삭제)") String rowStatus
    ) {
        public CodeMstVO toVO() {
            CodeMstVO vo = new CodeMstVO();
            vo.setGrpCd(this.grpCd());
            vo.setCdNm(this.cdNm());
            vo.setDispOrd(this.dispOrd() != null ? this.dispOrd() : 0);
            vo.setNote(this.note());
            // 필요한 경우 BaseVO 필드나 RowStatus 등 추가 세팅
            return vo;
        }
    }

    /**
     * 공통 코드 상세 저장 요청
     */
    @Builder
    @Schema(description = "상세 코드 저장 정보")
    public record Detail(
        @Schema(description = "코드 일련번호 (수정/삭제 시 필수)") String codeSeq,
        @NotBlank(message = "그룹 코드는 필수입니다.") @Schema(description = "그룹 코드") String grpCd,
        @NotBlank(message = "상세 코드는 필수입니다.") @Schema(description = "상세 코드") String dtlCd,
        @NotBlank(message = "코드 명은 필수입니다.") @Schema(description = "코드 명") String cdNm,
        @Schema(description = "KIPO 코드") String kipoCd,
        @Schema(description = "참조 값 1") String refVal1,
        @Schema(description = "참조 값 2") String refVal2,
        @Schema(description = "표시 순서") Integer dispOrd,
        @Schema(description = "비고") String note,
        
        @NotBlank(message = "행 상태값(I/U/D)은 필수입니다.")
        @Schema(description = "행 상태 (I: 추가, U: 수정, D: 삭제)") String rowStatus
    ) {
        public CodeDtlVO toVO() {
            CodeDtlVO vo = new CodeDtlVO();
            vo.setCodeSeq(this.codeSeq());
            vo.setGrpCd(this.grpCd());
            vo.setDtlCd(this.dtlCd());
            vo.setCdNm(this.cdNm());
            vo.setKipoCd(this.kipoCd());
            vo.setRefVal1(this.refVal1());
            vo.setRefVal2(this.refVal2());
            vo.setDispOrd(this.dispOrd() != null ? this.dispOrd() : 0);
            vo.setNote(this.note());
            vo.setRowStatus(this.rowStatus());
            return vo;
        }
    }
}
