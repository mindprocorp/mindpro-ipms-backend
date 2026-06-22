package kr.co.mindpro.ipms.domain.memo.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 분쟁/심판 상세 응답 통합 DTO
 */
@Data
@Schema(description = "분쟁/심판 응답 결과 ")
public class MemoResponse {

    @Builder(toBuilder = true)
    @Schema(description = "메모 상세 결과응답")
    public record MemoDetail(
            @Schema(description = "업무 일련번호 (특허/상표/진행사항 등)", example = "PRG20260000001")
            String tblSeq,

            @Schema(description = "메모 시퀀스 (특허/상표/진행사항 등)", example = "MEMMST20260000005")
            String memoSeq,

            @Schema(description = "필독 여부 (Y: 필독, N: 일반)", allowableValues = {"Y", "N"}, example = "N")
            String mustReadYn,

            @Schema(description = "메모 제목", example = "심사관 전화 응대 결과")
            String memoTitle,

            @Schema(description = "메모 작성자 이름", example = "홍길동")
            String memoUserName,

            @Schema(description = "작성일", example = "20261211")
            String memoRegDate,

            @Schema(description = "고객사명 / 거래처명", example = "(주)마인드프로")
            String customerName,

            @Schema(description = "메모 첨부파일")
            List<CommonRecordResponse.FileInfo> fileInfo,

            @Schema(description = "메모 본문 내용", example = "보정서 제출 기한 연장에 대해 협의 완료함.")
            String note

    ) {
        public MemoDetail(String tblSeq, String memoSeq, String mustReadYn, String memoTitle,
                          String memoUserName, String memoRegDate, String customerName, String note) {
            this(
                    tblSeq, memoSeq, mustReadYn, memoTitle,
                    memoUserName, memoRegDate, customerName, new ArrayList<>(), note);
        }
    }

}