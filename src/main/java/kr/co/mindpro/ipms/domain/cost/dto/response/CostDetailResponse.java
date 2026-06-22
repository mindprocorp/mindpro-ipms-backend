package kr.co.mindpro.ipms.domain.cost.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.cost.dto.request.CostSaveRequest;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [CostDetailResponse] 조회 응답용 DTO
 */
@Getter
@Setter // 방어적으로 필드 수정이 가능하도록 추가
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비용 상세 정보 응답")
public class CostDetailResponse {

    @Schema(description = "업무 일련번호", example = "PAT20260001")
    private String tblSeq;

    @Schema(description = "비용 데이터 맵 (Key: 카테고리코드, Value: 금액)", example = "{\"FEE01\": \"150000\"}")
    private Map<String, String> costMap;

    /**
     * static 팩토리 메서드
     */
    public static CostDetailResponse of(String tblSeq, List<CostVO> list) {
        Map<String, String> map = (list == null) ? new java.util.HashMap<>() :
                list.stream()
                        .filter(vo -> "N".equals(vo.getDelYn()) && vo.getKrwAmount() != null)
                        .collect(Collectors.toMap(
                                CostVO::getCostCategoryCode,
                                vo -> String.valueOf(vo.getKrwAmount()),
                                (existing, replacement) -> existing
                        ));

        // 빌더로 1차 생성
        return CostDetailResponse.builder()
                .tblSeq(tblSeq)
                .costMap(map)
                .build();
    }

    @Builder
    public record TrademarkRenewalResponse (

            @Schema(description = "출원 식별자")
            String appSeq,

            @Schema(description = "비용 식별자")
            String costSeq,

            @Schema(description = "차수")
            Integer remittanceCount,

            @Schema(description = "납부구분")
            String paymentDiv,

            @Schema(description = "갱신출원일/신청일")
            String requestDate,

            @Schema(description = "갱신출원번호")
            String appNo,

            @Schema(description = "갱신등록일/납부일")
            String costRemittanceDate,

            @Schema(description = "납부금액")
            Long krwAmount,

            @Schema(description = "비고")
            String note
    ) {}
}