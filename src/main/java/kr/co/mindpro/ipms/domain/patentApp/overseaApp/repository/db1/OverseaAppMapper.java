package kr.co.mindpro.ipms.domain.patentApp.overseaApp.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.vo.AppExtMstVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author : seokho
 * @fileName : OverseaAppMapper.java
 * @since : 2026. 1. 28.
 */
@Mapper
public interface OverseaAppMapper {

    /** 해외 기본 전체 리스트 조회 */
    List<CommonAppVO> getOverseaBasicList(@Param("request") BaseSearchRequest request);

    int getOverseaBasicListCnt(@Param("request") BaseSearchRequest request);

    int cntBasicChainOverseaAppList(@Param("request") BaseSearchRequest request);

    int insertBasicChainMap(CommonAppVO appMstVO);

    List<CommonAppVO> getBasicChainOverseaAppList(@Param("request") BaseSearchRequest request);

    /** 해외 출원 전체 리스트 조회 */
    List<CommonAppVO> getOverseaAppList(@Param("request") BaseSearchRequest request);

    int getOverseaAppCount(@Param("request") BaseSearchRequest request);

    /** 신규 해외 출원 - 기본 정보를 저장합니다. */
    int insertOverseaBasicApp(CommonAppVO appVO);

    /** 해외 출원 - 기본 정보를 조회합니다. */
    CommonAppVO getOverseaBasicDetail(String officeSeq, String appExtSeq);

    /** 신규 해외 출원 - 개국 app_mst 정보를 저장합니다. */
    int insertOverseaApp(CommonAppVO appMstVO);

    /** 해외 출원 - 개국 app_mst 정보를 조회합니다. */
    Optional<CommonAppVO> getOverseaAppDetail(String officeSeq, String appSeq);

    int updateOverseaAppMst(@Param("req") CommonAppVO appVO,
                            @Param("appSeq") String appSeq,
                            @Param("officeSeq") String officeSeq,
                            @Param("updateUser") String updateUser
    );

    int getDuplicateAppExtCnt(String officeSeq, String appExtSeq);

    int updateBasicApp(CommonAppVO appVO);

    int softDeleteAppExt(@Param("updateUser") String updateUser, @Param("appExtSeq") String appExtSeq, @Param("officeSeq") String officeSeq);
}
