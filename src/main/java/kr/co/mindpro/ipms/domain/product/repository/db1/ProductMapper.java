package kr.co.mindpro.ipms.domain.product.repository.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.product.vo.AppProductVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceClassVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceProductVO;

/**
 * @author : seokho
 * @fileName : ProductMapper.java
 * @since : 2026. 2. 9.
 */
@Mapper
public interface ProductMapper {

    /**
     * 상품 정보 등록
     */
    int insertProduct(AppProductVO appProductVO);

    /**
     * 출원번호별 상품 목록 조회
     * MyBatis 매개변수가 2개 이상일 경우 @Param 어노테이션 사용을 권장합니다.
     */
    List<AppProductVO> getProductList(@Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq);
    
    /**
     * 나이스 분류 마스터 목록 조회 (전체 및 조건 검색 통합)
     * @param searchVO niceVersion, categoryGb 등의 검색 조건을 담은 객체
     */
    List<NiceClassVO> getNiceClassList(NiceClassVO searchVO);
    
    /**
     * 나이스 지정상품 마스터 목록 조회 (전체 및 조건 검색 통합)
     * @param searchVO niceVersion, classNo, similarityCode 등의 검색 조건을 담은 객체
     */
    List<NiceProductVO> getNiceProductList(NiceProductVO searchVO);

    int getDuplicateProductCnt(String officeSeq, String appSeq, String productGroupId);

    int softDeleteProductGroup(String officeSeq, String appSeq, String productGroupId, String userSeq);

    /**
     * [삭제] 지정상품 그룹 다건 논리 삭제
     */
    int softDeleteProductGroupList(@Param("officeSeq") String officeSeq,
                                   @Param("appSeq") String appSeq,
                                   @Param("productGroupIdList") List<String> productGroupIdList,
                                   @Param("userSeq") String userSeq);

    /**
     * [삭제] 지정상품 그룹 단건 물리 삭제
     */
    int hardDeleteProductGroup(@Param("officeSeq") String officeSeq,
                               @Param("appSeq") String appSeq,
                               @Param("productGroupId") String productGroupId);

    /**
     * [삭제] 지정상품 그룹 다건 물리 삭제
     */
    int hardDeleteProductGroupList(@Param("officeSeq") String officeSeq,
                                   @Param("appSeq") String appSeq,
                                   @Param("productGroupIdList") List<String> productGroupIdList);

    List<AppProductVO> getProductInfoByGroupId(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("productGroupId") String productGroupId
    );
}
