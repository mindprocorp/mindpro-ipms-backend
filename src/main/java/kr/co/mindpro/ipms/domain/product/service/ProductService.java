package kr.co.mindpro.ipms.domain.product.service;

import java.util.List;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.product.dto.request.ProductRequest;
import kr.co.mindpro.ipms.domain.product.dto.response.ProductResponse;
import kr.co.mindpro.ipms.domain.product.vo.NiceClassVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceProductVO;

/**
 * @author : seokho
 * @fileName : ProductService.java
 * @since : 2026. 2. 9.
 */
public interface ProductService {

    /**
     * 상품류 일괄 등록
     */
    void saveAllProduct(ProductRequest.SaveAllProductList prodListReq);

    /**
     * 출원번호별 상품 목록 조회
     */
    BaseSearchResponse<ProductResponse.ListDetail> getProductListByAppSeq(String appSeq);

    void softDeleteProduct(String appSeq, String productGroupId);

    /**
     * 지정상품 그룹 다건 논리 삭제
     */
    void softDeleteProductByList(String appSeq, List<String> productGroupIdList);

    /**
     * 지정상품 그룹 단건 물리 삭제
     */
    void hardDeleteProduct(String appSeq, String productGroupId);

    /**
     * 지정상품 그룹 다건 물리 삭제
     */
    void hardDeleteProductByList(String appSeq, List<String> productGroupIdList);

    /**
     * 나이스 분류 마스터 전체 목록 조회
     */
    List<NiceClassVO> getNiceClassList();
    
    /**
     * 나이스 분류 마스터 버전별 목록 조회
     * @param niceVersion 나이스 버전 (예: 12-2024)
     */
    List<NiceClassVO> getNiceClassListByVersion(String niceVersion);

    /**
     * 나이스 지정상품 마스터 분류별 목록 조회 (버전 무관)
     * @param classNo 분류 번호
     */
    List<NiceProductVO> getNiceProductListByClass(String classNo);

    /**
     * 나이스 지정상품 마스터 버전 및 분류별 목록 조회
     * @param niceVersion 나이스 버전
     * @param classNo 분류 번호
     */
    List<NiceProductVO> getNiceProductListByVersionAndClass(String niceVersion, String classNo);

    /**
     * 출원에 연결된 상품 그룹 아이디에 해당하는 데이터 상세조회
     * */
    ProductResponse.Detail getProductInfoByGroupId(String appSeq, String productGroupId);
    
}
