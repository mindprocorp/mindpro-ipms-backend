package kr.co.mindpro.ipms.domain.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.preference.dto.request.PreferenceRequest;
import kr.co.mindpro.ipms.domain.preference.dto.response.PreferenceResponse;
import kr.co.mindpro.ipms.domain.preference.service.PreferenceService;
import kr.co.mindpro.ipms.domain.preference.vo.PreferenceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : MemoController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "우선권 API", description = "우선권 CRUD API")
@RestController
@RequestMapping("/api/preference")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(summary = "우선권 목록 조회", description = "출원번호(appSeq)에 해당하는 우선권 목록을 조회합니다.")
    @GetMapping("/list/{appSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<PreferenceResponse.PreferenceDetail>>> getList(@PathVariable String appSeq) {

        // 서비스에서 BaseSearchResponse 타입으로 결과를 받아옵니다.
        BaseSearchResponse<PreferenceResponse.PreferenceDetail> response = preferenceService.getPreferenceList(appSeq);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "우선권 목록 조회 성공",
                        response
                )
        );
    }

    @Operation(summary = "우선권 일괄 저장", description = "기존 우선권을 삭제하고 새로운 목록을 저장합니다.")
    @PostMapping("/saveAll/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> saveAll(@PathVariable String appSeq, @RequestBody List<PreferenceVO> list) {
        preferenceService.saveAllPreferences(appSeq, list);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "우선권 정보가 저장되었습니다."));
    }

    @Operation(summary = "우선권 단건 저장", description = "우선권 정보를 저장하고 기존 정보는 이력 처리합니다.")
    @PostMapping("/save/{appSeq}") // 단건 저장을 위한 경로 구분
    public ResponseEntity<ApiResponse<Void>> saveOne(
            @RequestBody PreferenceRequest.PreferenceDetail vo) {


        preferenceService.registerPreference(vo); // 단건 저장 서비스 호출
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "우선권 정보가 저장되었습니다."));
    }


    @Operation(summary = "우선권 단건 조회", description = "우선권 일련번호로 상세 정보를 조회합니다.")
    @GetMapping("/detail/{preferenceSeq}") // getById와 경로 구분
    public ResponseEntity<ApiResponse<PreferenceResponse.PreferenceDetail>> getById(@PathVariable String preferenceSeq) {
        PreferenceResponse.PreferenceDetail result = preferenceService.getPreference(preferenceSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "우선권 상세 조회 성공", result));
    }

    /**
     * [삭제] 우선권 논리적 삭제
     * */
    @Operation(summary = "우선권 단건 논리 삭제")
    @DeleteMapping("/delete/soft/{appSeq}/{preferenceSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeletePreference(
            @PathVariable String appSeq,
            @PathVariable String preferenceSeq) {

        preferenceService.softDeletePreference(appSeq, preferenceSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "우선권 삭제 성공"));
    }

    /**
     * [삭제] 우선권 다건 논리 삭제
     * */
    @Operation(summary = "우선권 다건 논리 삭제")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeletePreference(
            @PathVariable String appSeq,
            @RequestBody List<String> preferenceSeqList) {

        preferenceService.softDeletePreferenceByList(appSeq, preferenceSeqList);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "우선권 다건 논리 삭제 성공"));
    }

    /**
     * [삭제] 우선권 단건 물리 삭제
     * */
    @Operation(summary = "우선권 단건 물리 삭제")
    @DeleteMapping("/delete/hard/{appSeq}/{preferenceSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeletePreference(
            @PathVariable String appSeq,
            @PathVariable String preferenceSeq) {

        preferenceService.hardDeletePreference(appSeq, preferenceSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "우선권 단건 물리 삭제 성공"));
    }

    /**
     * [삭제] 우선권 다건 물리 삭제
     * */
    @Operation(summary = "우선권 다건 물리 삭제")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeletePreference(
            @PathVariable String appSeq,
            @RequestBody List<String> preferenceSeqList) {

        preferenceService.hardDeletePreferenceByList(appSeq, preferenceSeqList);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "우선권 다건 물리 삭제 성공"));
    }
}