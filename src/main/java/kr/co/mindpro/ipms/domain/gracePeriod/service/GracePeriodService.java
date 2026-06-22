package kr.co.mindpro.ipms.domain.gracePeriod.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.request.GracePeriodRequest;
import kr.co.mindpro.ipms.domain.gracePeriod.dto.response.GracePeriodResponse;
import kr.co.mindpro.ipms.domain.gracePeriod.vo.GracePeriodVO;

import java.util.List;

/**
 * @author : seokho
 * @fileName : GracePeriod.java
 * @since : 2026. 2. 3.
 */
public interface GracePeriodService {

    /**
     * [저장] 필요 시 개별 호출을 위한 단건 등록 메서드
     */
    void registerGracePeriod(GracePeriodRequest.SaveRequest request);

    /**
     * [조회] 공지예외 리스트 조회
     * */
    BaseSearchResponse<GracePeriodResponse.DetailResponse> getGracePeriodListByWork(String appSeq);

    GracePeriodResponse.DetailResponse getGracePeriodDetail(String appSeq, String gracePeriodSeq);

    /**
     * [삭제] 공지예외 단건 논리적 삭제
     * */
    void softDeleteGracePeriod(String appSeq, String gracePeriodSeq);

    /**
     * [삭제] 공지예외 다건 논리적 삭제
     * */
    void softDeleteGracePeriodByList(String appSeq, List<String> gracePeriodSeqList);

    /**
     * [삭제] 공지예외 단건 물리적 삭제
     * */
    void hardDeleteGracePeriod(String appSeq, String gracePeriodSeq);

    /**
     * [삭제] 공지예외 다건 물리적 삭제
     * */
    void hardDeleteGracePeriodByList(String appSeq, List<String> gracePeriodSeqList);
}
