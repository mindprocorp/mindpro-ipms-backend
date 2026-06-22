package kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMergeVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.dto.request.DomesticHardIpAppRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppDesignVO;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppPatentVO;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppTrademarkVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author : seokho
 * @fileName : DomesticAppMapper.java
 * @since : 2026. 1. 9.
 */
@Mapper
public interface DomesticAppMapper {

    /** 신규 국내 출원 정보를 저장합니다. */
    int insertDomesticApp(CommonAppVO appVO);

    /** 신규 특허/실용신안 정보를 저장합니다. */
    void insertPatentApp(AppPatentVO appPatentVO);

    /** 국내 출원 상세데이터를 조회합니다. */
    Optional<CommonAppVO> getDomesticAppDetail(String officeSeq, String appSeq);

    /** 신규 디자인 출원 정보를 저장합니다. */
    void insertDesignApp(AppDesignVO appDesignVO);

    /** 신규 상표 출원 정보를 저장합니다. */
    void insertTrademarkApp(AppTrademarkVO appTrademarkVO);

    /** 출원마스터 정보 수정 */
    int updateDomesticAppMst(
            @Param("req") CommonAppVO appVO,
            @Param("appSeq") String appSeq,
            @Param("officeSeq") String officeSeq,
            @Param("updateUser") String updateUser
    );

    /** 공통 - 출원기본정보, 출원사건관리 컴포넌트 데이터 단건 조회 */
    Optional<AppBasicInfoVO> getBasicInfoDetail(String officeSeq, String appSeq, List<String> dueDateCateCodeList, List<String> participantCateCodeList);

    /** 국내 출원 검색 리스트 조회 */
    List<CommonAppVO> getDomesticAppSearchList(@Param("request") BaseSearchRequest request);

    /** 국내 출원 검색 리스트 토탈 카운트 조회 */
    int getDomesticAppSearchListTotalCount(@Param("request") BaseSearchRequest request);

    /** 출원마스터에 연결된 특허 테이블 업데이트 */
    int updateHardIpApp(AppPatentVO appPatentVO);

    int updateDesignApp(AppDesignVO appDesignVO);

    int updateTrademarkApp(AppTrademarkVO appTrademarkVO);





//                  분쟁

    /** 분쟁/심판 연동용 출원 상세 정보 단독 조회 (기일, 관계자 포함)     * @param appSeq 국내 출원 seq 키     * @return Optional<ConflictMergeVO>     */
    Optional<ConflictMergeVO> getConflictAppDetail(String officeSeq ,String appSeq);
}
