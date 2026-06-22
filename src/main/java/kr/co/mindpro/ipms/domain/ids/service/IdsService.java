package kr.co.mindpro.ipms.domain.ids.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.ids.dto.request.IdsRequest;
import kr.co.mindpro.ipms.domain.ids.vo.IdsVO;

import java.util.List;

/**
 * @author : mindpro
 * @fileName : IdsService.java
 * @since : 2026. 3. 12.
 */
public interface IdsService {
    /**
     * [저장] 단건 등록 메서드
     * */
    void saveIds(IdsRequest.SaveIdsRequest request);

    /**
     * [조회] 단건 상세 조회 메서드
     * */
    BaseSearchResponse<IdsVO> getIdsList(String appSeq);

    IdsVO getIdsDetail(String appSeq, String idsSeq);

    void softDeleteByIdsSeq(String appSeq, String idsSeq);

    void softDeleteByIdsSeqList(String appSeq, List<String> idsSeqList);

    void hardDeleteByIdsSeq(String appSeq, String idsSeq);

    void hardDeleteByIdsSeqList(String appSeq, List<String> idsSeqList);
}
