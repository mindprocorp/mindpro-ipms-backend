package kr.co.mindpro.ipms.domain.code.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "시스템 등록 정보 공통 응답")
public record DocumentResponse(
        @Schema(description = "문서 시퀀스", example = "1")
        String docSeq,

        @Schema(description = "문서 구분 코드", example = "DOC")
        String docDiv,

        @Schema(description = "등록 구분", example = "MANUAL")
        String entryType,

        @Schema(description = "특허 유형", example = "PATENT")
        String patType,

        @Schema(description = "문서명", example = "출원서")
        String docNm,

        @Schema(description = "자동 생성 여부", example = "Y")
        String autoYn,

        @Schema(description = "기준일 유형", example = "APP_DATE")
        String baseDateType,

        @Schema(description = "기한 단위", example = "DAY")
        String deadlineUnit,

        @Schema(description = "기한 값", example = "30")
        String deadlineVal,

        @Schema(description = "참조 값", example = "REF01")
        String refVal,

        @Schema(description = "정렬 순서", example = "1")
        String sortOrd,

        @Schema(description = "생성자")
        String createUser,

        @Schema(description = "생성일시")
        OffsetDateTime createAt,

        @Schema(description = "수정자")
        String updateUser,

        @Schema(description = "수정일시")
        OffsetDateTime updateAt,

        @Schema(description = "삭제 여부", example = "N")
        String delYn,

        @Schema(description = "비고")
        String note
) {}