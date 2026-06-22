package kr.co.mindpro.ipms.domain.cost.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Schema(description = "비용 정보 저장 요청 객체")
public class CostSaveRequest {

    @Schema(description = "업무 일련번호 (특허/상표 등)", example = "PAT20260001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tblSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String officeSeq;

    @Schema(description = "생성자 식별값 (사용자 ID)", example = "admin_user")
    private String createUser;

    @Schema(description = "비용 데이터 맵 (Key: 카테고리코드, Value: 금액 문자열)",
            example = "{\"FEE01\": \"150000\", \"TAX01\": \"15000\"}"
            )
    private Map<String, String> costMap;

    public record TrademarkRenewalRequest(

            @Schema(description = "출원 식별자", example = "APPMST20260000259")
            String appSeq,

            @Schema(description = "비용 식별자")
            String costSeq,

            @Schema(description = "차수", example = "1")
            Integer remittanceCount,

            @Schema(description = "납부구분 코드(10: 1~5년납, 20: 6~10년납, 30: 1~10년납)", example = "10")
            String paymentDiv,

            @Schema(description = "출원일/신청일", example = "2024-05-20T14:30:00+09:00")
            String requestDate,

            @Schema(description = "출원번호")
            String appNo,

            @Schema(description = "납부금액", example = "15000000")
            Long krwAmount,

            @Schema(description = "등록일/납부일", example = "2024-05-20T14:30:00+09:00")
            String costRemittanceDate,

            @Schema(description = "비고")
            String note
    ) {}
}


