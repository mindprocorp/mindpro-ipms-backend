package kr.co.mindpro.ipms.domain.locarno.controller;

import java.util.List;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.locarno.dto.request.LocarnoRequest;
import kr.co.mindpro.ipms.domain.locarno.dto.response.LocarnoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.locarno.service.LocarnoService; // 경로 확인 필요
import kr.co.mindpro.ipms.domain.locarno.vo.LocarnoVO;
import lombok.RequiredArgsConstructor;

/**
 * [Controller] 로카르노 관리 API
 *
 * @author	 : mindpro
 * @fileName	 : LocarnoController.java
 * @since	 : 2026. 2. 4.
 */
@Tag(name = "Locarno", description = "로카르노 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locarno") // 적절한 엔드포인트 경로 설정
public class LocarnoController {

    private final LocarnoService locarnoService;

    /**
     * 로카르노 물품류 목록 조회
     * 요청 본문(RequestBody)의 조건에 따라 목록을 조회합니다.
     */
    @Operation(summary = "로카르노 물품류 목록 조회", description = "로카르노 물품류 전체 조회를 수행합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoList() {
        
        // Service를 통해 목록 조회 로직 수행
        List<LocarnoVO> list = locarnoService.getLocarnoList();

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 목록 조회 성공", list)
        );
    }
    

    /**
     * 로카르노 물품류 목록 버전별 조회 (추가)
     * URL 예시: /api/v1/locarno/list/14
     */
    @Operation(summary = "로카르노 버전별 물품류 목록 조회", description = "특정 버전(locarnoVersion)에 해당하는 로카르노 물품류 목록을 조회합니다.")
    @PostMapping("/list/{locarnoVersion}")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoListByVersion(
            @Parameter(description = "로카르노 버전", example = "15")
            @PathVariable("locarnoVersion") String locarnoVersion) {
        
        // Service의 신규 메서드 호출
        List<LocarnoVO> list = locarnoService.getLocarnoListByVersion(locarnoVersion);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "버전별 로카르노 목록 조회 성공", list)
        );
    } 
    
    /**
     * 로카르노 물품류별 소분류 목록 조회 (추가)
     * URL 예시: /api/locarno/sub-list/07
     */
    @Operation(summary = "로카르노 물품류별 소분류 목록 조회", description = "특정 물품류(classNo)에 해당하는 소분류 목록을 조회합니다.")
    @PostMapping("/sub-list/{classNo}")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoSubclassList(
            @Parameter(description = "물품류 번호", example = "07")
            @PathVariable("classNo") String classNo) {
        
        // 특정 물품류에 대한 소분류 목록 조회 (버전은 전체 조회)
        List<LocarnoVO> list = locarnoService.getLocarnoSubclassList(classNo, null);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "물품류별 소분류 목록 조회 성공", list)
        );
    }

    /**
     * 로카르노 버전 및 물품류별 소분류 목록 조회 (추가)
     * URL 예시: /api/locarno/sub-list/14/07
     */
    @Operation(summary = "로카르노 버전 및 물품류별 소분류 목록 조회", description = "특정 버전과 물품류에 해당하는 소분류 목록을 조회합니다.")
    @PostMapping("/sub-list/{locarnoVersion}/{classNo}")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoSubclassListByVersion(
            @Parameter(description = "로카르노 버전", example = "15")
            @PathVariable("locarnoVersion") String locarnoVersion,
            @Parameter(description = "물품류 번호", example = "07")
            @PathVariable("classNo") String classNo) {
        
        // 버전과 물품류를 모두 조건으로 조회
        List<LocarnoVO> list = locarnoService.getLocarnoSubclassList(classNo, locarnoVersion);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "버전 및 물품류별 소분류 목록 조회 성공", list)
        );
    }    
    
    /**
     * 로카르노 물품류 및 소분류별 물품 목록 조회 (추가)
     * URL 예시: /api/locarno/goods-list/07/01
     */
    @Operation(summary = "로카르노 물품류 및 소분류별 물품 목록 조회", description = "특정 물품류(classNo)와 소분류(subclassNo)에 해당하는 물품 목록을 조회합니다.")
    @PostMapping("/goods-list/{classNo}/{subclassNo}")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoGoodsList(
            @Parameter(description = "물품류 번호", example = "07")
            @PathVariable("classNo") String classNo,
            @Parameter(description = "소분류 번호", example = "01")
            @PathVariable("subclassNo") String subclassNo) {
        
        // 버전 조건 없이 특정 물품류/소분류의 물품 목록 조회
        List<LocarnoVO> list = locarnoService.getLocarnoGoodsList(classNo, subclassNo, null);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "물품 목록 조회 성공", list)
        );
    }

    /**
     * 로카르노 버전/물품류/소분류별 물품 목록 조회 (추가)
     * URL 예시: /api/locarno/goods-list/15/07/01
     */
    @Operation(summary = "로카르노 버전별 물품 목록 조회", description = "특정 버전, 물품류, 소분류에 해당하는 상세 물품 목록을 조회합니다.")
    @PostMapping("/goods-list/{locarnoVersion}/{classNo}/{subclassNo}")
    public ResponseEntity<ApiResponse<List<LocarnoVO>>> getLocarnoGoodsListByVersion(
            @Parameter(description = "로카르노 버전", example = "15")
            @PathVariable("locarnoVersion") String locarnoVersion,
            @Parameter(description = "물품류 번호", example = "07")
            @PathVariable("classNo") String classNo,
            @Parameter(description = "소분류 번호", example = "01")
            @PathVariable("subclassNo") String subclassNo) {
        
        // 모든 조건을 포함하여 상세 조회
        List<LocarnoVO> list = locarnoService.getLocarnoGoodsList(classNo, subclassNo, locarnoVersion);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "버전별 물품 목록 조회 성공", list)
        );
    }    

/**
     * 로카르노 물품류 일괄 등록
     * URL 예시 : /api/v1/locarno/tab/register
     * */
    @Operation(summary = "로카르노 물품류 등록", description = "로카르노 물품류 목록을 일괄 등록합니다.")
    @PostMapping("/tab/register")
    public ResponseEntity<ApiResponse<Void>> saveAllLocarno(@RequestBody LocarnoRequest.SaveAllLocarno locarnoListReq) {

        locarnoService.saveAllLocarno(locarnoListReq);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "로카르노 일괄 등록이 완료되었습니다."));
    }

    @Operation(summary = "로카르노 물품류 조회", description = "출원키에 연결된 로카르노 물품류 목록을 일괄 조회합니다.")
    @PostMapping("/tab/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<LocarnoResponse.Detail>>> getLocarnoListByAppSeq(@PathVariable String appSeq) {

        BaseSearchResponse<LocarnoResponse.Detail> res = locarnoService.getLocarnoListByAppSeq(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 리스트 조회 성공", res));
    }

    @Operation(summary = "로카르노 그룹 상세 조회", description = "locarnoGroupId에 속한 개별 항목 목록을 조회합니다. (수정 모달 초기 데이터용)")
    @GetMapping("/tab/detail/{appSeq}/{locarnoGroupId}")
    public ResponseEntity<ApiResponse<List<LocarnoResponse.GroupItem>>> getLocarnoGroupDetail(
            @PathVariable String appSeq,
            @PathVariable String locarnoGroupId) {

        List<LocarnoResponse.GroupItem> result = locarnoService.getLocarnoGroupDetail(appSeq, locarnoGroupId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 그룹 상세 조회 성공", result));
    }

    @Operation(summary = "로카르노 단일 논리적 삭제", description = "출원키에 연결된 로카르노 물품류 단건을 논리적으로 삭제합니다.")
    @DeleteMapping("/tab/delete/soft/{appSeq}/{locarnoGroupId}")
    public ResponseEntity<ApiResponse<Void>> softDeleteLocarno(@PathVariable String appSeq, @PathVariable String locarnoGroupId) {

        locarnoService.softDeleteLocarnoGroup(appSeq, locarnoGroupId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 그룹별 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "로카르노 다건 논리적 삭제", description = "출원키에 연결된 로카르노 물품류 다건을 논리적으로 삭제합니다.")
    @DeleteMapping("/tab/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteLocarno(@PathVariable String appSeq, @RequestBody List<String> locarnoGroupIdList) {

        locarnoService.softDeleteLocarnoGroupByList(appSeq, locarnoGroupIdList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 그룹별 다건 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "로카르노 단일 물리적 삭제", description = "출원키에 연결된 로카르노 물품류 단건을 물리적으로 삭제합니다.")
    @DeleteMapping("/tab/delete/hard/{appSeq}/{locarnoGroupId}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteLocarno(@PathVariable String appSeq, @PathVariable String locarnoGroupId) {

        locarnoService.hardDeleteLocarnoGroup(appSeq, locarnoGroupId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 그룹별 물리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "로카르노 다건 물리적 삭제", description = "출원키에 연결된 로카르노 물품류 다건을 물리적으로 삭제합니다.")
    @DeleteMapping("/tab/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteLocarno(@PathVariable String appSeq, @RequestBody List<String> locarnoGroupIdList) {

        locarnoService.hardDeleteLocarnoGroupByList(appSeq, locarnoGroupIdList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로카르노 그룹별 다건 물리적 삭제가 완료되었습니다."));
    }
}
