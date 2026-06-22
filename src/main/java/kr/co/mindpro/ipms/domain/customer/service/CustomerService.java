package kr.co.mindpro.ipms.domain.customer.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.customer.dto.request.CustomerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.ManagerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.WrapperMandateRequest;
import kr.co.mindpro.ipms.domain.customer.dto.response.CustomerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.ManagerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.WrapperMandateResponse;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerMappVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Service Interface] 고객관리 서비스
 *
 * @author   : min
 * @fileName : CustomerService.java
 * @since    : 2026. 01. 07.
 */
public interface CustomerService {
    /* =========================================================================
     * [기본] 고객 및 사업자 정보
     * ========================================================================= */
    CustomerResponse.CustomerDetail saveCustomer(CustomerRequest.CustomerDetail request, MultipartFile customerFile);
    CustomerResponse.CustomerDetail getCustomerDetail(String customerSeq);
    BaseSearchResponse<CustomerResponse.CustomerDetail> getCustomerList( BaseSearchRequest request);


    BaseSearchResponse<CustomerResponse.CustomerSearchList> getModalCustomerSearchList(BaseSearchRequest request);
    BaseSearchResponse<CustomerResponse.CustomerSearchList> getModalCustomerSearchListByCategoryCode( BaseSearchRequest request, String customerCategoryCode);

    /** [신규] 고객 마스터 자유 검색 (출원 PK 무관) — 출원 화면 모달의 담당자 리스트 */
    java.util.List<CustomerResponse.CustomerSearchList> searchCustomerMaster(
            String officeSeq, String categoryCode, String keyword, Integer pageSize, Integer offSet);

    /* =========================================================================
     * [탭] 포괄위임 (Wrapper Mandate)
     * ========================================================================= */
    BaseSearchResponse<WrapperMandateResponse.WrapperMandateDetail> getMandateList(BaseSearchRequest request);
    WrapperMandateResponse.WrapperMandateDetail getMandateDetail(String wrappermandateSeq);
    WrapperMandateResponse.WrapperMandateDetail saveMandate(WrapperMandateRequest.WrapperMandateDetail data);

    /* =========================================================================
     * [탭] 현업 담당자 (Manager)
     * ========================================================================= */
    ManagerResponse.CustomerManagerResponse saveCustomerManager(ManagerRequest.CustomerManagerRequest request);
    BaseSearchResponse<ManagerResponse.CustomerManagerResponse> getManagerList(BaseSearchRequest request);
    ManagerResponse.CustomerManagerResponse getManagerDetail(String participantSeq);

    /* =========================================================================
     * [탭] 관련 고객사 매핑 (Customer Mapping) - 추가
     * ========================================================================= */
    /** 관련 고객사 등록/수정 */
    CustomerResponse.CustomerMappDetail registerMapping(CustomerRequest.CustomerMappDetail request);

    /** 관련 고객사 목록 조회 */
    BaseSearchResponse<CustomerResponse.CustomerMappDetail> getMappingList(BaseSearchRequest request, String customerCategoryCode);

    /** 관련 고객사 상세 조회 */
    CustomerResponse.CustomerMappDetail getMappingDetail(String customerMappSeq);

    /*
     * [counter party register] 각 업무별 담당자 맵핑 서비스
     * ========================================================================= */
    void insertCustomerMappToWork(List<CustomerMappVO> request);

    /* =========================================================================
     * 삭제
     * ========================================================================= */
    void deleteCustomer(String customerSeq);

    void deleteMandate(String wrappermandateSeq);

    void deleteCustomerManager(String participantSeq);
    void deleteCustomerMapp(String customerMappSeq);

    /* =========================================================================
     * 일괄 삭제
     * ========================================================================= */
    void deleteCustomerList(List<String> ids);
    void deleteMandateList(List<String> ids);
    void deleteCustomerManagerList(List<String> ids);
    void deleteCustomerMappList(List<String> ids);
    /**
     * 고객 이미지 삭제
     */
    void deleteCustomerFile(String customerSeq, String fileSeq);
}
