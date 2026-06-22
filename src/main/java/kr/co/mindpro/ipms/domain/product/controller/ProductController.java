package kr.co.mindpro.ipms.domain.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.product.dto.request.ProductRequest;
import kr.co.mindpro.ipms.domain.product.dto.response.ProductResponse;
import kr.co.mindpro.ipms.domain.product.service.ProductService;
import kr.co.mindpro.ipms.domain.product.vo.NiceClassVO;
import kr.co.mindpro.ipms.domain.product.vo.NiceProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : seokho
 * @fileName : ProductController.java
 * @since : 2026. 2. 9.
 */

@Slf4j
@Tag(name = "상품류 API", description = "상품류 CRUD API")
@RestController
@RequestMapping(value = "/api/product", produces = "application/json; charset=UTF-8") // 전체 JSON 고정)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 업무별 지정상품 탭 등록 API
     * */
    @Operation(summary = "상품류 일괄 등록", description = "출원번호(appSeq)에 연결되는 상품류를 일괄 등록합니다.(상표전용)")
    @PostMapping("/tab/register")
    public ResponseEntity<ApiResponse<Void>> saveAllProduct(@RequestBody ProductRequest.SaveAllProductList prodListReq) {

        productService.saveAllProduct(prodListReq);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "상품류 일괄 등록이 완료되었습니다."));
    }

    @Operation(summary = "상품류 조회", description = "출원번호(appSeq)에 연결되는 상품류를 일괄 조회합니다.(상표전용)")
    @GetMapping("/tab/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<ProductResponse.ListDetail>>> getLocarnoListByAppSeq(@PathVariable String appSeq) {

        BaseSearchResponse<ProductResponse.ListDetail> res = productService.getProductListByAppSeq(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 리스트 조회 성공", res));
    }

    @Operation(summary = "상품류 그룹별 논리적 삭제", description = "출원에 연결된 상품그룹들을 논리적으로 삭제합니다.")
    @DeleteMapping("/tab/delete/soft/{appSeq}/{productGroupId}")
    public ResponseEntity<ApiResponse<Void>> softDeleteProductGroup(@PathVariable String appSeq, @PathVariable String productGroupId) {

        productService.softDeleteProduct(appSeq, productGroupId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "상품류 그룹별 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "상품류 그룹 다건 논리 삭제", description = "출원에 연결된 상품그룹들을 다건 논리적으로 삭제합니다.")
    @DeleteMapping("/tab/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteProductGroup(
            @PathVariable String appSeq,
            @RequestBody List<String> productGroupIdList) {

        productService.softDeleteProductByList(appSeq, productGroupIdList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "상품류 그룹 다건 논리 삭제가 완료되었습니다."));
    }

    @Operation(summary = "상품류 그룹 단건 물리 삭제", description = "출원에 연결된 상품그룹을 물리적으로 삭제합니다.")
    @DeleteMapping("/tab/delete/hard/{appSeq}/{productGroupId}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteProductGroup(
            @PathVariable String appSeq,
            @PathVariable String productGroupId) {

        productService.hardDeleteProduct(appSeq, productGroupId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "상품류 그룹 단건 물리 삭제가 완료되었습니다."));
    }

    @Operation(summary = "상품류 그룹 다건 물리 삭제", description = "출원에 연결된 상품그룹들을 다건 물리적으로 삭제합니다.")
    @DeleteMapping("/tab/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteProductGroup(
            @PathVariable String appSeq,
            @RequestBody List<String> productGroupIdList) {

        productService.hardDeleteProductByList(appSeq, productGroupIdList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "상품류 그룹 다건 물리 삭제가 완료되었습니다."));
    }
    
    @Operation(summary = "나이스 분류 마스터 조회", description = "나이스 상품/서비스업 분류 마스터 목록 전체를 조회합니다.")
    @GetMapping("/nice-class")
    public ResponseEntity<ApiResponse<List<NiceClassVO>>> getNiceClassList() {
        // 별도의 파라미터 없이 서비스 호출
        List<NiceClassVO> list = productService.getNiceClassList();
        
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "나이스 분류 조회 성공", list)
        );
    }
    
    /**
     * 나이스 분류 마스터 목록 버전별 조회
     * URL 예시: /api/product/nice-class/12-2024
     */
    @Operation(summary = "나이스 버전별 분류 목록 조회", description = "특정 버전(niceVersion)에 해당하는 나이스 상품/서비스업 분류 목록을 조회합니다.")
    @PostMapping("/nice-class/{niceVersion}")
    public ResponseEntity<ApiResponse<List<NiceClassVO>>> getNiceClassListByVersion(
            @Parameter(description = "나이스 버전", example = "13")
            @PathVariable("niceVersion") String niceVersion) {
        
        // Service의 버전별 조회 메서드 호출
        List<NiceClassVO> list = productService.getNiceClassListByVersion(niceVersion);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "버전별 나이스 분류 목록 조회 성공", list)
        );
    }
    
    /**
     * 나이스 지정상품 마스터 목록 분류별 조회 (버전 무관)
     * URL 예시: /api/product/master-product/class/09
     */
    @Operation(summary = "분류별 지정상품 목록 조회", description = "특정 분류 번호(classNo)에 해당하는 모든 버전의 지정상품 목록을 조회합니다.")
    @PostMapping("/master-product/class/{classNo}")
    public ResponseEntity<ApiResponse<List<NiceProductVO>>> getNiceProductListByClassOnly(
            @Parameter(description = "분류 번호", example = "09") @PathVariable("classNo") String classNo) {
        
        // 버전 없이 분류만 전달
        List<NiceProductVO> list = productService.getNiceProductListByClass(classNo);
        
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "분류별 지정상품 조회 성공", list));
    }

    /**
     * 나이스 지정상품 마스터 목록 버전 및 분류별 조회
     * URL 예시: /api/product/master-product/version/12-2024/09
     */
    @Operation(summary = "버전/분류별 지정상품 목록 조회", description = "특정 버전(niceVersion)과 분류 번호(classNo)에 해당하는 지정상품 목록을 조회합니다.")
    @PostMapping("/master-product/version/{niceVersion}/{classNo}")
    public ResponseEntity<ApiResponse<List<NiceProductVO>>> getNiceProductListByVersionAndClass(
            @Parameter(description = "나이스 버전", example = "13") @PathVariable("niceVersion") String niceVersion,
            @Parameter(description = "분류 번호", example = "09") @PathVariable("classNo") String classNo) {
        
        // 버전과 분류 모두 전달
        List<NiceProductVO> list = productService.getNiceProductListByVersionAndClass(niceVersion, classNo);
        
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "버전/분류별 지정상품 조회 성공", list));
    }

    /**
     * 지정상품 단건 상세조회
     * */
    @Operation(summary = "지정상품 상세조회", description = "출원에 연결된 특정 productGroupId에 해당하는 리스트를 조회합니다.")
    @GetMapping("/tab/{appSeq}/{productGroupId}")
    public ResponseEntity<ApiResponse<ProductResponse.Detail>> getProductInfoByGroupId(
            @Parameter(description = "연결된 출원 시퀀스") @PathVariable String appSeq,
            @Parameter(description = "상품 그룹 아이디") @PathVariable String productGroupId
    ) {
        ProductResponse.Detail res = productService.getProductInfoByGroupId(appSeq, productGroupId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹 ID에 해당하는 지정상품 조회 성공", res));
    }

}
