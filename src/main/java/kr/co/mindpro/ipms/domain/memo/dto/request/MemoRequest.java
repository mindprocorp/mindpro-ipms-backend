package kr.co.mindpro.ipms.domain.memo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class MemoRequest {
    @Schema(description = "메모 일괄 저장 요청 객체")
    public record MemoListRequest(

            @Schema(description = "저장 대상 메모 리스트")
            List<MemoDetail> memoList
    ) {}

    @Schema(description = "메모 상세 정보 데이터")
    public record MemoDetail(
            @Schema(description = "업무 일련번호 (특허/상표/진행사항 등)", example = "PRG20260000001", format = "SEQ")
            String tblSeq,

            @Schema(description = "메모 시퀀스 (특허/상표/진행사항 등)", example = "MEMMST20260000022")
            String memoSeq,

            @Schema(description = "필독 여부 (Y: 필독, N: 일반)", allowableValues = {"Y", "N"}, example = "N", format = "YN")
            String mustReadYn,

            @Schema(description = "메모 제목", example = "심사관 전화 응대 결과")
            String memoTitle,

            @Schema(description = "메모 작성자 이름", example = "홍길동")
            String memoUserName,

            @Schema(description = "작성일", example = "20261211", format = "YYYYMMDD")
            String memoRegDate,

            @Schema(description = "고객사명 / 거래처명", example = "(주)마인드프로")
            String customerName,

            @Schema(description = "메모 본문 내용", example = "보정서 제출 기한 연장에 대해 협의 완료함.")
            String note,

            @Schema(description = "수정 시 삭제할 첨부파일 fileSeq 목록")
            List<String> deletedFileSeqList
    ) {}

}
