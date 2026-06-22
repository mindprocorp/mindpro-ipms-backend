package kr.co.mindpro.ipms.domain.bizinfo.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.bizinfo.dto.request.BizInfoRequest;
import kr.co.mindpro.ipms.domain.bizinfo.dto.response.BizInfoResponse;


/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : mindpro
 * @fileName : InvoiceService.java
 * @since    : 2026. 01. 07.
 */
public interface BizInfoService {


    // 저장 및 수정 (상세 레코드 반환)
    BizInfoResponse.BizInfoDetail saveBizInfo(BizInfoRequest.BizInfoDetail request);

    // 목록 조회 (리스트 + 카운트 래퍼 반환)
    BizInfoResponse.BizInfoList getBizInfoList( BaseSearchRequest request);

    // 상세 조회
    BizInfoResponse.BizInfoDetail getBizInfoDetail(String bizInfoSeq);

    // 삭제
    void deleteBizInfo(String bizInfoSeq);

}