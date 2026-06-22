package kr.co.mindpro.ipms.domain.duedate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "기일 정보 저장 요청")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DueDateSaveRequest {

    @Schema(description = "업무 일련번호", example = "CFTINF20260000005")
    private String tblSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE202600001")
    private String officeSeq;

    @Schema(description = "등록자 아이디", example = "admin_user")
    private String createUser;

    @Schema(description = "기일 데이터 맵 (Key: 기일코드, Value: 날짜)",
            example = "{ \"REQ\": \"2026-01-13\", \"DUE\": \"2026-12-31\", \"SUB\": \"2026-02-01\" }")
    private List<DueDateVO> dueDateList; // String 타입으로 통일
}