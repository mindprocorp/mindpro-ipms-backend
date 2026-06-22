package kr.co.mindpro.ipms.domain.ai.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * ai모델 목록
 */
@Getter
@Setter
public class AiConnectVo extends BaseVO {
        private Long connectionSeq;   // connection_seq (PK)
        private String userMstSeq;    // user_mst_seq (FK)
        private String aiType;        // ai_type
        private String aiBaseUrl;     // ai_base_url
        private String aiModelNm;     // ai_model_nm
        private String aiApiKey;      // ai_api_key
        private Double aiTemperature; // ai_temperature
}
