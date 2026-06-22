package kr.co.mindpro.ipms.domain.requiredDoc.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.request.RequiredDocRequest;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.response.RequiredDocResponse;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RequiredDocService.java
 * @since : 2026. 4. 1.
 */
public interface RequiredDocService {

    void createRequiredDoc(RequiredDocRequest.createRequiredDocRequest request);

    RequiredDocResponse.RequiredDocDetailResponse getRequiredDocDetail(String appSeq, String requiredDocSeq);

    BaseSearchResponse<RequiredDocResponse.RequiredDocListResponse> getRequiredDocListByAppSeq(BaseSearchRequest request);

    void softDeleteRequiredDoc(String appSeq, String requiredDocSeq);

    /**
     * [삭제] 구비서류 다건 논리 삭제
     */
    void softDeleteRequiredDocByList(String appSeq, List<String> requiredDocSeqList);

    /**
     * [삭제] 구비서류 단건 물리 삭제
     */
    void hardDeleteRequiredDoc(String appSeq, String requiredDocSeq);

    /**
     * [삭제] 구비서류 다건 물리 삭제
     */
    void hardDeleteRequiredDocByList(String appSeq, List<String> requiredDocSeqList);
}
