package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaBasicAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppListResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaBasicAppResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : seokho
 * @fileName : OverseaBasicService.java
 * @since : 2026. 1. 27.
 */
public interface OverseaBasicService {

    /**
     * 해외 기본 리스트를 조회 합니다.
     * */
    BaseSearchResponse<OverseaBasicAppListResponse.BasicListDetailResponse> getBasicList(BaseSearchRequest request);

    /**
     * 해외 기본에 연결된 해외출원 리스트를 조회합니다.
     * */
    BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse> getBasicChainOverseaAppList(BaseSearchRequest request);
    /**
     * 신규 해외 출원 - 기본을 등록합니다.
     * @param request 해외 출원 - 기본 등록 정보
     */
    String createBasicApp(OverseaBasicAppRequest.CreateOverseaBasicApp request, MultipartFile file);

    /**
     * 해외 출원 - 기본 상세를 조회합니다.
     * @param extSeq 해외 출원 - 기본 seq 키
     */
    OverseaBasicAppResponse.OverseaBasicAppDetailResponse getOverseaBasicAppDetail(String extSeq);

    void softDeleteBasicApp(String extSeq);
}
