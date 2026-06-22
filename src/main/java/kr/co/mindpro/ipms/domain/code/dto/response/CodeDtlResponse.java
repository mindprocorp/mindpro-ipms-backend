package kr.co.mindpro.ipms.domain.code.dto.response;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * [DTO] 공통 코드 상세 정보 응답 객체
 * DB 테이블 utb_code_dtl의 정보를 안전하게 전달합니다.
 *
 * @author	 : intst
 * @fileName : CodeDtlResponse.java
 * @since	 : 2026. 01. 19.
 */
@Builder
@Schema(description = "공통 코드 상세 정보 응답 데이터")
public record CodeDtlResponse(
    @Schema(description = "코드 일련번호") String codeSeq,
    @Schema(description = "그룹 코드") String grpCd,
    @Schema(description = "상세 코드") String dtlCd,
    @Schema(description = "코드 명") String cdNm,
    @Schema(description = "KIPO 코드") String kipoCd,
    @Schema(description = "참조 값 1") String refVal1,
    @Schema(description = "참조 값 2") String refVal2,
    @Schema(description = "표시 순서") Integer dispOrd,
    @Schema(description = "사용 여부") String useYn,
    @Schema(description = "비고") String note,
    @Schema(description = "등록자") String createUser,
    @Schema(description = "등록 일시") LocalDateTime createAt,
    @Schema(description = "수정자") String updateUser,
    @Schema(description = "수정 일시") LocalDateTime updateAt
) {

    /**
     * CodeDtlVO를 CodeDtlResponse로 변환하는 정적 팩토리 메서드
     */
    public static CodeDtlResponse of(CodeDtlVO vo) {
        if (vo == null) return null;

        return CodeDtlResponse.builder()
                .codeSeq(vo.getCodeSeq())
                .grpCd(vo.getGrpCd())
                .dtlCd(vo.getDtlCd())
                .cdNm(vo.getCdNm())
                .kipoCd(vo.getKipoCd())
                .refVal1(vo.getRefVal1())
                .refVal2(vo.getRefVal2())
                .dispOrd(vo.getDispOrd())
                // 별도 use_yn 컬럼 사용 (null이면 기본 'Y'로 노출)
                .useYn(vo.getUseYn() != null ? vo.getUseYn() : "Y")
                .note(vo.getNote())
                .createUser(vo.getCreateUser())
                .createAt(convertDateTime(vo.getCreateAt()))
                .updateUser(vo.getUpdateUser())
                .updateAt(convertDateTime(vo.getUpdateAt()))
                .build();
    }

    /**
     * OffsetDateTime을 LocalDateTime으로 안전하게 변환하는 내부 헬퍼 메서드
     */
    private static LocalDateTime convertDateTime(Object dateTime) {
        if (dateTime == null) return null;
        if (dateTime instanceof OffsetDateTime odt) return odt.toLocalDateTime();
        if (dateTime instanceof LocalDateTime ldt) return ldt;
        return null;
    }
}