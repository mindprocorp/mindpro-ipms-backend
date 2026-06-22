package kr.co.mindpro.ipms.domain.cost.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.cost.dto.request.AnnuityYearRequest;
import kr.co.mindpro.ipms.domain.cost.dto.request.CostSaveRequest;
import kr.co.mindpro.ipms.domain.cost.dto.response.AnnuityYearResponse;
import kr.co.mindpro.ipms.domain.cost.dto.response.CostDetailResponse;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;

import java.util.List;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : CostService.java
 * @since    : 2026. 01. 07.
 */
public interface CostService {
    /**
     * [저장] 화면에서 넘어온 비용 리스트를 일괄 등록 (기존 데이터 삭제 후 재등록)
     */
    void saveAllCosts(String tblSeq, List<CostVO> costList);

    /**
     * [저장] 필요 시 개별 호출을 위한 단건 등록 메서드
     */
    void saveCost(CostVO vo);

    /**
     * [저장] 연차관리 단건 등록 메서드
     * */
    void saveAnnuityYear(AnnuityYearRequest.AnnuityYearTabRequest vo);

    /**
     * [저장] 상표 - 갱신관리 정보 등록 메서드
     * */
    void saveRenewalMng(CostSaveRequest.TrademarkRenewalRequest request);

    /**
     * [조회] 특정 업무키(tblSeq)로 매핑된 비용 리스트 반환
     */
    BaseSearchResponse<CostVO> getCostListByWork(String tblSeq);

    /**
     * [조회] 단건 연차관리 조회
     * */
    AnnuityYearResponse.AnnuityYearDetailResponse getAnnuityYearDetail(String tblSeq, String costSeq);

    /**
     * [조회] 특정 검색 조건에 관한 비용 리스트 조회
     */
    List<CostVO> getCostList(CostVO searchVO);

    /**
     * [조회] 출원 연차관리 탭 리스트 조회
     * */
    BaseSearchResponse<AnnuityYearResponse.AnnuityYearDetailResponse> getAnnuityYearListByWork(String tblSeq);

    /**
     * [조회] 상표 - 갱신관리 탭 리스트 조회
     * */
    BaseSearchResponse<CostDetailResponse.TrademarkRenewalResponse> getRenewalMngList(String appSeq);

    CostDetailResponse.TrademarkRenewalResponse getRenewalMngDetail(String tblSeq, String costSeq);

    /**
     * [삭제] 비용 논리적 삭제
     * */
    void softDeleteCostWithCostMapp(String tblSeq, String costSeq);

    /**
     * [삭제] 비용관련 탭 다중 논리적 삭제
     * */
    void softDeleteCostWithCostMappByList(String tblSeq, List<String> targetSeqList);

    /**
     * [삭제] 비용 물리적 삭제
     * */
    void hardDeleteCostWithCostMapp(String tblSeq, String costSeq);

    /**
     * [삭제] 비용관련 탭 다중 물리적 삭제
     * */
    void hardDeleteCostWithCostMappByList(String tblSeq, List<String> targetSeqList);
}