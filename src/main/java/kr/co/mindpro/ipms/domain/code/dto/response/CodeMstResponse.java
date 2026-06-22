package kr.co.mindpro.ipms.domain.code.dto.response;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import lombok.Builder;

/**
 * [DTO] 공통 코드 마스터 응답 객체
 * * @author	 : intst
 * @fileName : CodeMstResponse.java
 * @since	 : 2026. 01. 19.
 */
@Builder
@Schema(description = "공통 코드 마스터 응답 데이터")
public record CodeMstResponse(
    @Schema(description = "코드 일련번호") String codeSeq,
    @Schema(description = "그룹 코드") String grpCd,
    @Schema(description = "코드 명") String cdNm,
    @Schema(description = "표시 순서") Integer dispOrd,
    @Schema(description = "사용 여부") String useYn,
    @Schema(description = "삭제 여부") String delYn,
    @Schema(description = "비고") String note,
    @Schema(description = "등록자") String createUser,
    @Schema(description = "등록 일시") LocalDateTime createAt,
    @Schema(description = "수정자") String updateUser,
    @Schema(description = "수정 일시") LocalDateTime updateAt
) {

    /**
     * CodeMstVO를 CodeMstResponse로 변환하는 정적 팩토리 메서드
     */
    public static CodeMstResponse of(CodeMstVO vo) {
        if (vo == null) return null;

        return CodeMstResponse.builder()
                .codeSeq(vo.getCodeSeq())
                .grpCd(vo.getGrpCd())
                .cdNm(vo.getCdNm())
                .dispOrd(vo.getDispOrd())
                .useYn(vo.getUseYn() != null ? vo.getUseYn() : "Y")
                .delYn(vo.getDelYn())
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
