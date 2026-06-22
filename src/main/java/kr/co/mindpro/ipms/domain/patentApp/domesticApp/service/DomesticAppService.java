package kr.co.mindpro.ipms.domain.patentApp.domesticApp.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticTrademarkRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response.DomesticAppDetailResponse;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.response.DomesticAppListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface DomesticAppService {

    /**
     * 국내출원 검색 리스트 조회
     * */
    BaseSearchResponse<DomesticAppListResponse.AppListResponse> getDomesticAppSearchList(BaseSearchRequest request);

    /**
     *  신규 HardIp (특허/실용신안) 등록합니다.
     * */
    String createHardIpApp(DomesticHardIpAppRequest.CreateHardIpAppRequest request, MultipartFile file);


    /**
     * 신규 디자인 출원을 등록합니다.
     * */
    String createDesignApp(DomesticDesignAppRequest.CreateDomesticDesignAppRequest request, MultipartFile file);

    /**
     * 신규 상표 출원을 등록합니다.
     * */
    String createTrademarkApp(DomesticTrademarkRequest.CreateTrademarkAppRequest request, MultipartFile file);

    /**
     * 국내 출원 상세를 조회합니다.
     * @param appSeq 국내 출원 상세 조회
     */
    DomesticAppDetailResponse getDomesticAppDetail(String appSeq);

    /**
     * 국내 출원 첨부 이미지를 논리 삭제합니다.
     * @param appSeq  출원 마스터 Seq
     * @param fileSeq 삭제할 파일 Seq
     */
    void deleteAppImageFile(String appSeq, String fileSeq);

}
