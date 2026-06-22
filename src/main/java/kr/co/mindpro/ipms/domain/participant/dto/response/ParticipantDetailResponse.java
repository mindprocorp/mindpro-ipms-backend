package kr.co.mindpro.ipms.domain.participant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 관계자 상세 정보 응답 DTO
 */
@Data
@Builder
@Schema(description = "관계자 상세 정보 응답")
public class ParticipantDetailResponse {

    @Schema(description = "업무 일련번호")
    private String tblSeq;

    @Schema(description = "관계자 데이터 목록 (Map 대신 List 사용)")
    private List<ParticipantVO> participantList;

    /**
     * 리스트가 null일 경우 빈 리스트로 방어해주는 정적 팩토리 메서드
     */
    public static ParticipantDetailResponse of(String tblSeq, List<ParticipantVO> list) {
        return ParticipantDetailResponse.builder()
                .tblSeq(tblSeq)
                .participantList(list != null ? list : new java.util.ArrayList<>())
                .build();
    }
}