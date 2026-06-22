package kr.co.mindpro.ipms.domain.product.service.impl;

import static kr.co.mindpro.ipms.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static kr.co.mindpro.ipms.common.util.IdGenerator.generateTSID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.mindpro.ipms.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.product.dto.request.ProductRequest;
import kr.co.mindpro.ipms.domain.product.dto.response.ProductResponse;
import kr.co.mindpro.ipms.domain.product.repository.db1.ProductMapper;
import kr.co.mindpro.ipms.domain.product.service.ProductService;
import kr.co.mindpro.ipms.domain.product.vo.AppProductVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceClassVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : seokho
 * @fileName : ProductServiceImpl.java
 * @since : 2026. 2. 9.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 기본 설정 (성능 최적화)
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllProduct(ProductRequest.SaveAllProductList prodListReq) {

        if (prodListReq == null || prodListReq.prodList() == null || prodListReq.prodList().isEmpty()) {
            throw new BusinessException("locarnoList cannot be null or empty", INTERNAL_SERVER_ERROR);
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String appSeq = prodListReq.prodList().get(0).appSeq();

        String productGroupId = prodListReq.productGroupId();

        if (productGroupId != null && !productGroupId.isEmpty()) {
            int existCount = productMapper.getDuplicateProductCnt(officeSeq, appSeq, productGroupId);

            if (existCount > 0) {
                int deleteResult = productMapper.softDeleteProductGroup(officeSeq, appSeq, productGroupId, userSeq);

                if (deleteResult <= 0) {
                    throw new RuntimeException("product group soft delete failed.");
                }
            } else {
                throw new BusinessException("존재하지 않거나 이미 삭제된 지정상품 그룹입니다.", ErrorCode.INVALID_INPUT_VALUE);
            }
        } else {
            productGroupId = generateTSID("PRODUCT");
        }

        for (ProductRequest.SaveProduct saveProduct : prodListReq.prodList()) {

            AppProductVO prodVO = AppProductVO.builder()
                    .officeSeq(officeSeq)
                    .appSeq(saveProduct.appSeq())
                    .productId(saveProduct.productId())
                    .niceVersion(saveProduct.niceVersion())
                    .productClass(saveProduct.ProductClass())
                    .productGroupId(productGroupId)
                    .productCount(saveProduct.productCount())
                    .productNameKo(saveProduct.productNameKo())
                    .productNameEn(saveProduct.productNameEn())

                    .createUser(userSeq)
                    .delYn("N")
                    .note(saveProduct.note())
                    .build();

            int result = productMapper.insertProduct(prodVO);

            if(result <= 0) {
                throw new BusinessException("상품류 등록에 실패하였습니다.", INTERNAL_SERVER_ERROR);
            }
        }

    }

    @Override
    public BaseSearchResponse<ProductResponse.ListDetail> getProductListByAppSeq(String appSeq) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        List<AppProductVO> list = productMapper.getProductList(officeSeq, appSeq);

        List<ProductResponse.ListDetail> res = list.stream()
                .map(vo -> ProductResponse.ListDetail.builder()
                        .appSeq(vo.getAppSeq())
                        .productGroupId(vo.getProductGroupId())
                        .productClass(vo.getProductClass())
                        .productCount(vo.getProductCount())
                        .productSummaryKo(vo.getProductSummaryKo())
                        .productSummaryEn(vo.getProductSummaryEn())
                        .build()
                ).toList();

        return BaseSearchResponse.of(res, 1, 99);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteProduct(String appSeq, String productGroupId) {
        String userSeq = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = productMapper.softDeleteProductGroup(officeSeq, appSeq, productGroupId, userSeq);

        if (deleteResult <= 0) {
            throw new RuntimeException("product group soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteProductByList(String appSeq, List<String> productGroupIdList) {
        if (productGroupIdList == null || productGroupIdList.isEmpty()) {
            throw new BusinessException("지정상품 그룹 목록이 비어 있습니다.", INTERNAL_SERVER_ERROR);
        }

        String userSeq = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = productMapper.softDeleteProductGroupList(officeSeq, appSeq, productGroupIdList, userSeq);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (deleteResult <= 0) {
            throw new RuntimeException("product group list soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteProduct(String appSeq, String productGroupId) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = productMapper.hardDeleteProductGroup(officeSeq, appSeq, productGroupId);

        if (deleteResult <= 0) {
            throw new RuntimeException("product group hard delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteProductByList(String appSeq, List<String> productGroupIdList) {
        if (productGroupIdList == null || productGroupIdList.isEmpty()) {
            throw new BusinessException("지정상품 그룹 목록이 비어 있습니다.", INTERNAL_SERVER_ERROR);
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = productMapper.hardDeleteProductGroupList(officeSeq, appSeq, productGroupIdList);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (deleteResult <= 0) {
            throw new RuntimeException("product group list hard delete failed.");
        }
    }

    /**
     * 나이스 분류 마스터 목록 전체 조회
     */
    @Override
    public List<NiceClassVO> getNiceClassList() {
        // XML의 <if> 조건들이 타지 않도록 빈 객체를 넘겨 전체를 조회합니다.
        return productMapper.getNiceClassList(new NiceClassVO());
    }    
    
    /**
     * 나이스 분류 마스터 버전별 목록 조회
     */
    @Override
    public List<NiceClassVO> getNiceClassListByVersion(String niceVersion) {
        NiceClassVO searchVO = NiceClassVO.builder()
                .niceVersion(niceVersion)
                .build();
        
        return productMapper.getNiceClassList(searchVO);
    }

    @Override
    public List<NiceProductVO> getNiceProductListByClass(String classNo) {
        // 분류번호만 빌드하여 매퍼 호출
        return productMapper.getNiceProductList(NiceProductVO.builder().classNo(classNo).build());
    }

    @Override
    public List<NiceProductVO> getNiceProductListByVersionAndClass(String niceVersion, String classNo) {
        // 버전과 분류번호 모두 빌드하여 매퍼 호출
        return productMapper.getNiceProductList(NiceProductVO.builder()
                .niceVersion(niceVersion)
                .classNo(classNo)
                .build());
    }

    @Override
    public ProductResponse.Detail getProductInfoByGroupId(String appSeq, String productGroupId) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<AppProductVO> voList = productMapper.getProductInfoByGroupId(officeSeq, appSeq, productGroupId);

        if (voList == null || voList.isEmpty()) {
            return null;
        }

        String groupId = voList.get(0).getProductGroupId();
        String productClass = voList.get(0).getProductClass();

        List<ProductResponse.ProdInfo> prodInfos = voList.stream()
                .map(vo -> new ProductResponse.ProdInfo(
                        vo.getProductId(),
                        vo.getNiceVersion(),
                        vo.getProductNameKo(),
                        vo.getProductNameEn(),
                        vo.getNote()
                ))
                .collect(Collectors.toList());

        return ProductResponse.Detail.builder()
                .productGroupId(groupId)
                .productClass(productClass)
                .prodList(prodInfos)
                .build();
    }

}
