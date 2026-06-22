package kr.co.mindpro.ipms.domain.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.customer.dto.request.CustomerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.ManagerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.WrapperMandateRequest;
import kr.co.mindpro.ipms.domain.customer.dto.response.CustomerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.ManagerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.WrapperMandateResponse;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Controller] 고객 API
 *
 * @author	 : min
 * @fileName : CustomerController.java
 * @since	 : 2026. 01. 07.
*/
    @Slf4j
    @Tag(name = "고객 API", description = "고객 정보 관리(CRUD) API")
    @RestController
    @RequestMapping("/api/customer")
    @RequiredArgsConstructor
    public class CustomerController {


    private final CustomerService customerService;

    /**
     * 고객 및 사업자 정보 저장 (파일 포함)
     */
    @Operation(summary = "고객 저장", description = "고객 상세 정보와 첨부파일을 함께 저장합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CustomerResponse.CustomerDetail>> saveCustomer(
            @Valid @RequestPart("data") CustomerRequest.CustomerDetail data,
            @RequestPart(value = "customerFile", required = false) MultipartFile customerFile) {

        CustomerResponse.CustomerDetail result = customerService.saveCustomer(data, customerFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "고객 정보 저장 성공", result));
    }

    /**
     * 고객 상세 조회
     */
    @Operation(summary = "고객 상세 조회")
    @GetMapping("/{customerSeq}")
    public ResponseEntity<ApiResponse<CustomerResponse.CustomerDetail>> getCustomerDetail(
            @PathVariable String customerSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 상세 조회 성공", customerService.getCustomerDetail(customerSeq)));
    }

    /**
     * 고객 검색 목록 조회
     */
    @Operation(summary = "고객 목록 검색")
    @PostMapping("/list/search")
    public ResponseEntity<ApiResponse<BaseSearchResponse<CustomerResponse.CustomerDetail>>> getCustomerList(@Valid @RequestBody BaseSearchRequest request) {
        BaseSearchResponse<CustomerResponse.CustomerDetail> response = customerService.getCustomerList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 목록 조회 성공",response));
    }

    /**
     * 고객 목록 검색 API (모달용)
     * */
    @Operation(summary = "고객 목록 검색")
    @PostMapping("/modal/list/search")
    public ResponseEntity<ApiResponse<BaseSearchResponse<CustomerResponse.CustomerSearchList>>> getModalCustomerSearchList(@Valid @RequestBody BaseSearchRequest request) {
        BaseSearchResponse<CustomerResponse.CustomerSearchList> response = customerService.getModalCustomerSearchList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 목록 조회 성공",response));
    }

    /**
     * 고객 역할별 목록 검색 API (모달용)
     * */
    @Operation(summary = "고객 역할별 목록 검색")
    @PostMapping("/modal/list/{customerCategoryCode}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<CustomerResponse.CustomerSearchList>>> getModalCustomerSearchList(
            @Valid @RequestBody BaseSearchRequest request, @PathVariable String customerCategoryCode
    ) {
        BaseSearchResponse<CustomerResponse.CustomerSearchList> response = customerService.getModalCustomerSearchListByCategoryCode(request, customerCategoryCode);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 목록 조회 성공",response));
    }

    /**
     * [신규] 고객 마스터 자유 검색 — 출원 PK 무관, 사무소 범위 utb_customer 풀 카테고리별 LIKE.
     * 출원 화면 모달의 "고객관리에서 등록된 담당자 리스트" 표시용.
     */
    @Operation(summary = "고객 마스터 자유 검색",
            description = "출원 PK 무관. categoryCode: client/applicant/regMgr/foreignAgent")
    @GetMapping("/master")
    public ResponseEntity<ApiResponse<java.util.List<CustomerResponse.CustomerSearchList>>> searchCustomerMaster(
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            kr.co.mindpro.ipms.security.vo.CustomUserDetails user,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offSet) {
        var list = customerService.searchCustomerMaster(user.getOfficeSeq(), categoryCode, keyword, pageSize, offSet);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 마스터 검색 성공", list));
    }

    /* =========================================================================
     * [탭] 포괄위임 (Wrapper Mandate) API
     * ========================================================================= */

    @Operation(summary = "포괄위임 목록 조회")
    @PostMapping("/mandate/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<WrapperMandateResponse.WrapperMandateDetail>>> getMandateList(@Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "위임 목록 조회 성공", customerService.getMandateList(request)));
    }

    @Operation(summary = "포괄위임 저장")
    @PostMapping(value = "/mandate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<WrapperMandateResponse.WrapperMandateDetail>> saveMandate(
            @Valid @RequestPart("data") WrapperMandateRequest.WrapperMandateDetail data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "포괄위임 정보 저장 성공", customerService.saveMandate(data)));
    }

    @Operation(summary = "포괄위임 상세 조회")
    @GetMapping("/mandate/detail/{wrappermandateSeq}")
    public ResponseEntity<ApiResponse<WrapperMandateResponse.WrapperMandateDetail>> getMandateDetail(@PathVariable String wrappermandateSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "포괄위임 상세 조회 성공", customerService.getMandateDetail(wrappermandateSeq)));
    }

    /* =========================================================================
     * [탭] 담당자 (Manager) API
     * ========================================================================= */

    @Operation(summary = "담당자 저장")
    @PostMapping("/manager")
    public ResponseEntity<ApiResponse<ManagerResponse.CustomerManagerResponse>> saveManager(@Valid @RequestBody ManagerRequest.CustomerManagerRequest data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "담당자 정보 저장 성공", customerService.saveCustomerManager(data)));
    }

    @Operation(summary = "담당자 목록 조회")
    @PostMapping("/manager/list/search")
    public ResponseEntity<ApiResponse<BaseSearchResponse<ManagerResponse.CustomerManagerResponse>>> getManagerList(@Valid @RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "담당자 목록 조회 성공", customerService.getManagerList(request)));
    }

    @Operation(summary = "담당자 상세 조회", description = "담당자 일련번호(participantSeq)를 통해 상세 정보를 조회합니다.")
    @GetMapping("/manager/detail/{participantSeq}")
    public ResponseEntity<ApiResponse<ManagerResponse.CustomerManagerResponse>> getManagerDetail(@PathVariable String participantSeq) {

        log.debug("담당자 상세 조회 요청 - participantSeq: {}", participantSeq);
        ManagerResponse.CustomerManagerResponse result = customerService.getManagerDetail(participantSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "담당자 상세 조회 성공", result)
        );
    }
    /* =========================================================================
     * [탭] 관련 고객사 (Mapping) API
     * ========================================================================= */

    @Operation(summary = "관련 고객사 등록")
    @PostMapping("/mapping/save") // /save 중복 방지
    public ResponseEntity<ApiResponse<CustomerResponse.CustomerMappDetail>> saveMapping(@RequestBody CustomerRequest.CustomerMappDetail request) {
        CustomerResponse.CustomerMappDetail result = customerService.registerMapping(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "관련 고객사가 등록되었습니다.",result));
    }

    @Operation(summary = "관련 고객사 리스트 조회")
    @PostMapping("/mapping/list") // /list 중복 방지
    public ResponseEntity<ApiResponse<BaseSearchResponse<CustomerResponse.CustomerMappDetail>>> getMappingList(BaseSearchRequest request, @RequestParam(required = false) String customerCategoryCode) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "관련 고객사 목록 조회 성공", customerService.getMappingList(request, customerCategoryCode)));
    }

    @Operation(summary = "관련 고객사 단건 조회")
    @GetMapping("/mapping/detail/{customerMappSeq}")
    public ResponseEntity<ApiResponse<CustomerResponse.CustomerMappDetail>> getMappingDetail(@PathVariable String customerMappSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "매핑 상세 조회 성공", customerService.getMappingDetail(customerMappSeq)));
    }

    /*
     * 삭제 API
     * ========================================================================= */
    @Operation(summary = "고객 삭제")
    @DeleteMapping("/{customerSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable String customerSeq) {
        customerService.deleteCustomer(customerSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 삭제 성공"));
    }

    @Operation(summary = "포괄위임 삭제")
    @DeleteMapping("/mandate/{wrappermandateSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteMandate(@PathVariable String wrappermandateSeq) {
        customerService.deleteMandate(wrappermandateSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "포괄위임 삭제 성공"));
    }

    @Operation(summary = "담당자 삭제")
    @DeleteMapping("/manager/{participantSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerManager(@PathVariable String participantSeq) {
        customerService.deleteCustomerManager(participantSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "담당자 삭제 성공"));
    }

    @Operation(summary = "관련 고객사 삭제")
    @DeleteMapping("/mapping/{customerMappSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerMapp(@PathVariable String customerMappSeq) {
        customerService.deleteCustomerMapp(customerMappSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "관련 고객사 삭제 성공"));
    }

    @Operation(summary = "고객 일괄 삭제")
    @PostMapping("/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerList(@RequestBody List<String> ids) {
        customerService.deleteCustomerList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고객 일괄 삭제 성공"));
    }

    @Operation(summary = "포괄위임 일괄 삭제")
    @PostMapping("/mandate/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteMandateList(@RequestBody List<String> ids) {
        customerService.deleteMandateList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "포괄위임 일괄 삭제 성공"));
    }

    @Operation(summary = "담당자 일괄 삭제")
    @PostMapping("/manager/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerManagerList(@RequestBody List<String> ids) {
        customerService.deleteCustomerManagerList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "담당자 일괄 삭제 성공"));
    }

    @Operation(summary = "관련 고객사 일괄 삭제")
    @PostMapping("/mapping/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerMappList(@RequestBody List<String> ids) {
        customerService.deleteCustomerMappList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "관련 고객사 일괄 삭제 성공"));
    }

    @Operation(summary = "고객 이미지 삭제", description = "고객 정보에 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/file/{customerSeq}/{fileSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerFile(
            @PathVariable String customerSeq,
            @PathVariable String fileSeq) {

        customerService.deleteCustomerFile(customerSeq, fileSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "이미지 삭제 성공"));
    }
}
