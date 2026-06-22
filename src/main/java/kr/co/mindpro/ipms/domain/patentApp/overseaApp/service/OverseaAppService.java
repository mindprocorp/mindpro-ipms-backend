package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.*;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : seokho
 * @fileName : OverseaAppService.java
 * @since : 2026. 2. 10.
 */
public interface OverseaAppService {

    BaseSearchResponse<OverseaAppListResponse.AppListDetailResponse> getOverseaList(BaseSearchRequest request);

    String savePct(OverseaPctAppRequest.CreatePctAppRequest request, MultipartFile mainDrawingFile);

    String saveEp(OverseaEpAppRequest.CreateEpAppRequest request, MultipartFile mainDrawingFile);

    String saveMadrid(OverseaMadridAppRequest.CreateMadridRequest request, MultipartFile trademarkImage);

    String saveInterDesign(OverseaInterDesignAppRequest.CreateInterDesignAppRequest request, MultipartFile mainImageFile);

    OverseaPctAppResponse.PctAppDetailResponse getPctDetail(String appSeq);

    OverseaEpAppResponse.EpAppDetailResponse getEpDetail(String appSeq);

    OverseaMadridAppResponse.MadridAppDetailResponse getMadridDetail(String appSeq);

    OverseaInterDesignAppResponse.InterDesignAppDetailResponse getInterDesignDetail(String appSeq);

    void deleteMadridImage(String fileSeq);
}
