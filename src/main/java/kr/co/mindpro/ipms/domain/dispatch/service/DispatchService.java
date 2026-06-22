package kr.co.mindpro.ipms.domain.dispatch.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.dispatch.dto.request.DispatchRequest;
import kr.co.mindpro.ipms.domain.dispatch.dto.response.DispatchResponse;

import java.util.List;

public interface DispatchService {
    BaseSearchResponse<DispatchResponse.DispatchDetail> getDispatchList(BaseSearchRequest request);
    DispatchResponse.DispatchDetail saveDispatch(DispatchRequest.DispatchDetail request);
    void deleteDispatch(String dispatchSeq);
    void deleteDispatchList(List<String> ids);
}
