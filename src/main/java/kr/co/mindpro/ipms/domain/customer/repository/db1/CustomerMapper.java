package kr.co.mindpro.ipms.domain.customer.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerMappVO;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerVO;
import kr.co.mindpro.ipms.domain.customer.vo.WrapperMandateVO;
import kr.co.mindpro.ipms.domain.customer.dto.response.ManagerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 고객 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : CustomerMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface CustomerMapper {

        /**
         * 고객 상세 조회 (BizInfo와 JOIN 권장)
         */
        CustomerVO selectCustomerDetail(@Param("customerSeq") String customerSeq,
                                        @Param("officeSeq") String officeSeq);

        /**
         * 사무소별 고객 목록 조회
         */
        List<CustomerVO> selectCustomerList(@Param("request") BaseSearchRequest request);

        List<CustomerVO> selectCustomerListByWork(@Param("req") BaseSearchRequest request
                , @Param("customerCategoryCode") String customerCategoryCode);

        /**
         * [신규] 고객 마스터 자유 검색 — 출원 PK 무관, 사무소 범위 utb_customer 풀에서 카테고리별 LIKE.
         * 출원 화면 모달의 "고객 관리에서 등록된 담당자 리스트" 표시용.
         */
        List<CustomerVO> searchCustomerMaster(@Param("officeSeq") String officeSeq,
                                              @Param("categoryCode") String categoryCode,
                                              @Param("keyword") String keyword,
                                              @Param("pageSize") Integer pageSize,
                                              @Param("offSet") Integer offSet);


        /**
         * 고객 정보 신규 삽입
         */
        int insertCustomer(CustomerVO vo);

        /**
         * 고객 정보 수정
         */
        int updateCustomer(CustomerVO vo);

        /**
         * 고객 정보 논리 삭제 (DEL_YN = 'Y' 및 수정자 업데이트)
         */
        int deleteCustomer(@Param("customerinfoSeq") String customerinfoSeq,
                           @Param("officeSeq") String officeSeq,
                           @Param("userId") String userId);

    /**
     * 인서트 작업 시 중복된 식별키가 들어왔는지 확인하는 seq check용 select
     * return : 중복된 seq가 존재하면 >= 1, 존재하지 않으면 0
     * */
    int checkDuplicateSeq(CustomerMappVO vo);

    int softDeleteCustomerMapp(CustomerMappVO vo);

    /**
     * 업무에 연결된 customerCategoryCode에 해당하는 당사자 리스트를 불러옵니다.
     * */
    List<CommonRecordResponse.CounterPartyInfo> getCounterPartyListByWork(
            @Param("tblSeq") String tblSeq,
            @Param("tblCode") String tblCode,
            @Param("customerCategoryCode") String customerCategoryCode
    );

    List<CommonRecordResponse.CounterPartyInfo> selectCounterPartyListByTblSeq(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("customerCategoryCode") String customerCategoryCode
    );

      /* =========================================================================
     * [탭] 포괄위임 (Wrapper Mandate) 관련 메서드 추가
     * ========================================================================= */

    /**
     * 고객별 포괄위임 목록 조회
     */
    List<WrapperMandateVO> findMandateListByCustomer(BaseSearchRequest request);

    /**
     * 포괄위임 상세 단건 조회
     */
    WrapperMandateVO findMandateDetail(@Param("wrappermandateSeq") String wrappermandateSeq,
                                       @Param("officeSeq") String officeSeq);

    /**
     * 포괄위임 정보 신규 삽입
     */
    int insertMandate(WrapperMandateVO vo);

    /**
     * 포괄위임 정보 수정
     */
    int updateMandate(WrapperMandateVO vo);

    /**
     * 포괄위임 정보 논리 삭제 (필요 시)
     */
    int deleteMandate(@Param("wrappermandateSeq") String wrappermandateSeq,
                      @Param("officeSeq") String officeSeq,
                      @Param("userId") String userId);



    /* =========================================================================
     * [탭] 담당자 (Manager) 관련 메서드 추가
     * ========================================================================= */

    /**
     * 고객별 담당자 목록 조회 (UserInfo JOIN)
     */
    List<ManagerResponse.CustomerManagerResponse> selectCustomerManagerList(
            @Param("request") BaseSearchRequest request, @Param("participantCode") String participantCode);

    /**
     * 담당자 상세 조회 (UserInfo JOIN)
     */
    ManagerResponse.CustomerManagerResponse selectCustomerManagerDetail(
            @Param("participantSeq") String participantSeq,
            @Param("officeSeq") String officeSeq);

    /* =========================================================================
     * [탭] 관련 고객사 매핑 (Customer Mapping)
     * ========================================================================= */
    int insertCustomerMapp(CustomerMappVO vo);
    int updateCustomerMapp(CustomerMappVO vo);
    List<kr.co.mindpro.ipms.domain.customer.dto.response.CustomerResponse.CustomerMappDetail> selectCustomerMappList(@Param("req")BaseSearchRequest request, @Param("customerCategoryCode")String customerCategoryCode);
    kr.co.mindpro.ipms.domain.customer.dto.response.CustomerResponse.CustomerMappDetail selectCustomerMappDetail(@Param("customerMappSeq") String customerMappSeq, @Param("officeSeq") String officeSeq);



    /* =========================================================================
     * 삭제
     * ========================================================================= */
    void deleteCustomerMst(@Param("customerSeq") String customerSeq, @Param("officeSeq") String officeSeq);

    void deleteMandate(@Param("wrappermandateSeq") String wrappermandateSeq, @Param("officeSeq") String officeSeq);

    void deleteCustomerManager(@Param("participantSeq") String participantSeq, @Param("officeSeq") String officeSeq);

    void deleteCustomerMapp(@Param("customerMappSeq") String customerMappSeq, @Param("officeSeq") String officeSeq);

    /**
     * 고객 마스터 일괄 논리 삭제
     */
    int updateCustomerDelYn(@Param("ids") List<String> ids,
                            @Param("officeSeq") String officeSeq,
                            @Param("userId") String userId);

    /**
     * 포괄위임 일괄 논리 삭제
     */
    int updateMandateDelYn(@Param("ids") List<String> ids,
                           @Param("officeSeq") String officeSeq,
                           @Param("userId") String userId);

    /**
     * 담당자 일괄 논리 삭제
     */
    int updateCustomerManagerDelYn(@Param("ids") List<String> ids,
                                   @Param("officeSeq") String officeSeq,
                                   @Param("userId") String userId);

    /**
     * 관련고객사 매핑 일괄 논리 삭제
     */
    int updateCustomerMappDelYn(@Param("ids") List<String> ids,
                                @Param("officeSeq") String officeSeq,
                                @Param("userId") String userId);

    /**
     * 고객 이미지 파일 논리 삭제
     */
    int softDeleteCustomerFileByFileSeq(
            @Param("officeSeq") String officeSeq,
            @Param("fileSeq") String fileSeq,
            @Param("updateUser") String updateUser
    );
}
