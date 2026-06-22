package kr.co.mindpro.ipms.domain.conflict.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 분쟁/심판(Conflict) 통합 매핑 VO
 */
@Data
@Builder
@Schema(description = "분쟁/심판 통합 등록 및 수정 데이터 객체")
public class ParticipantSearchVO {

    private String participantCode; // SearchFieldVO의 filterCode (예: P001, P002)
    private String userInfoSeq;     // 화면에서 넘어온 사용자 시퀀스 (예: USERIF...)

}