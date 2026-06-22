package kr.co.mindpro.ipms.domain.rnd.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.rnd.dto.request.RndRequest;
import kr.co.mindpro.ipms.domain.rnd.dto.response.RndResponse;
import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RndService.java
 * @since : 2026. 2. 5.
 */
public interface RndService {

    /**
     * [저장] 필요 시 개별 호출을 위한 단건 등록 메서드
     */
    void saveRnd(RndRequest.RnbRequestDetail request);

    BaseSearchResponse<RndResponse.RndResponseDetail> getRndList(String appSeq);

    RndResponse.RndResponseDetail getRndDetail(String appSeq, String rndSeq);

    void softDeleteRnd(String appSeq, String rndSeq);

    /**
     * [삭제] 연구과제 다건 논리 삭제
     */
    void softDeleteRndByList(String appSeq, List<String> rndSeqList);

    /**
     * [삭제] 연구과제 단건 물리 삭제
     */
    void hardDeleteRnd(String appSeq, String rndSeq);

    /**
     * [삭제] 연구과제 다건 물리 삭제
     */
    void hardDeleteRndByList(String appSeq, List<String> rndSeqList);
}
