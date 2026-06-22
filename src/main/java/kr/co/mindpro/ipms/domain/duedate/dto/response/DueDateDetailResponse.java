package kr.co.mindpro.ipms.domain.duedate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관계자 상세 정보 응답 DTO
 */
@Data
@SuperBuilder
@Schema(description = "기일 정보 요약 응답")
public class DueDateDetailResponse {

    @Schema(description = "업무 일련번호", example = "CFTINF20260000005")
    private String tblSeq;

    @Schema(description = "기일 요약 맵 (Key: 기일코드, Value: 날짜)",
            example = "{ \"REQ\": \"2026-01-13\", \"DUE\": \"2026-12-31\" }")
    private List<DueDateVO> dueDateList; // String 타입으로 통일

    public static DueDateDetailResponse of(String tblSeq, List<DueDateVO> list) {
        return DueDateDetailResponse.builder()
                .tblSeq(tblSeq)
                .dueDateList(list != null ? list : new java.util.ArrayList<>())
                .build();
    }
}
