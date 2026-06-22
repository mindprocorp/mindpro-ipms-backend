package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service;

import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualDesignAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.request.OverseaIndividualTrademarkAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualDesignAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualHardIpAppResponse;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.dto.response.OverseaIndividualTrademarkAppResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : seokho
 * @fileName : OverseaIndividualService.java
 * @since : 2026. 1. 29.
 */
public interface OverseaIndividualService {

    /**
     * 신규 해외 출원 - 특/실을 등록합니다.
     * @param request 해외 출원 - 특/실 등록 정보
     */
    String createOverseaHardIpApp(OverseaIndividualHardIpAppRequest.CreateHardIpRequest request, MultipartFile file);

    String createOverseaDesignApp(OverseaIndividualDesignAppRequest.CreateDesignAppRequest request, MultipartFile file);

    String createOverseaTrademarkApp(OverseaIndividualTrademarkAppRequest.CreateTrademarkAppRequest request, MultipartFile file);

    /**
     * 해외 출원 - 개국 특/실 상세를 조회합니다.
     * @param appSeq 해외 출원 seq 키
     */
    OverseaIndividualHardIpAppResponse.HardIpAppDetailResponse getOverseaHardIpAppDetail(String appSeq);

    OverseaIndividualDesignAppResponse.DesignAppDetailResponse getOverseaDesignAppDetail(String appSeq);

    OverseaIndividualTrademarkAppResponse.TrademarkAppDetailResponse getOverseaTrademarkAppDetail(String appSeq);

}
