package kr.co.mindpro.ipms.domain.paper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.paper.dto.request.PaperRequest;
import kr.co.mindpro.ipms.domain.paper.dto.response.PaperResponse;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperDossierVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Controller] 전자포대 관련 API
 *
 * @author	 : min
 * @fileName : PaperController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "전자포대(서류) API", description = "전자포대 관련 CRUD API")
@RestController
@RequestMapping("/api/fileList")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;


    /**
     * 포대관리 조회 API (검색 포함)
     * */
    @Operation(summary = "포대관리 조회 API", description = "포대관리 조회 API 입니다.")
    @PostMapping("/dossier-archives")
    public ResponseEntity<ApiResponse<BaseSearchResponse<PaperResponse.DossierArchiveListResponse>>> getDossierList(
            @RequestBody BaseSearchRequest request
    ) {

        BaseSearchResponse<PaperResponse.DossierArchiveListResponse> response = paperService.getDossierArchiveList(request);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대관리 검색 리스트 조회", response));
    }

    /**
     * 전자포대 단건 등록 API
     * */
    @Operation(summary = "전자포대 단건(파일 여러개) 등록", description = "각 업무의 전자포대 단건 등록 API 입니다.")
    @PostMapping(value = "/dossier/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> registerDossier(
            @RequestPart("data") PaperRequest.DossierRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {

        paperService.registerDossier(request, files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "전자포대 단건(파일 여러개) 등록이 완료되었습니다."));
    }

    /**
     * 전자포대 탭 조회를 위한 API
     * */
    @Operation(summary = "전자포대 탭 조회", description = "각 업무의 전자포대 조회 API 입니다.")
    @GetMapping("/dossier/{tblSeq}")
    public ResponseEntity<ApiResponse<BaseSearchResponse<PaperResponse.DossierDetailResponse>>> getDossierList(@PathVariable(value = "tblSeq") String tblSeq) {

        BaseSearchResponse<PaperResponse.DossierDetailResponse> res = paperService.getDossierByWork(tblSeq, SecurityUtil.getOfficeSeq());

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대 조회 성공", res)
        );
    }

    /**
     * 전자포대 상세 조회 API
     * */
    @Operation(summary = "전자포대 상세조회", description = "각 업무의 전자포대 상세조회 API 입니다.")
    @GetMapping("/dossier/{tblSeq}/{fileMappSeq}")
    public ResponseEntity<ApiResponse<PaperResponse.DossierDetailResponse>> getDossierDetail(@PathVariable(value = "tblSeq") String tblSeq, @PathVariable(value = "fileMappSeq") String fileMappSeq) {

        PaperResponse.DossierDetailResponse result = paperService.getDossierDetail(tblSeq, fileMappSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대 상세 조회 성공", result)
        );
    }

    /**
     * [삭제] 전자포대 탭 단건 삭제를 위한 API
     * */
    @Operation(summary = "전자포대 탭 단건 논리 삭제", description = "각 업무의 전자포대 단건 논리 삭제 API 입니다.")
    @DeleteMapping("/dossier/delete/soft/{tblSeq}/{fileMappSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteDossier(@PathVariable(value = "tblSeq") String tblSeq, @PathVariable(value = "fileMappSeq") String fileMappSeq) {

        paperService.softDeleteDossier(tblSeq, fileMappSeq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(), "전자포대 단건 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 전자포대 탭 다건 논리 삭제
     * */
    @Operation(summary = "전자포대 탭 다건 논리 삭제", description = "각 업무의 전자포대 다건 논리 삭제 API 입니다.")
    @DeleteMapping("/dossier/multi-delete/soft/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteDossier(
            @PathVariable(value = "tblSeq") String tblSeq,
            @RequestBody List<String> fileMappSeqList) {

        paperService.softDeleteDossierByList(tblSeq, fileMappSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대 다건 논리 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 전자포대 탭 단건 물리 삭제
     * */
    @Operation(summary = "전자포대 탭 단건 물리 삭제", description = "각 업무의 전자포대 단건 물리 삭제 API 입니다.")
    @DeleteMapping("/dossier/delete/hard/{tblSeq}/{fileMappSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteDossier(
            @PathVariable(value = "tblSeq") String tblSeq,
            @PathVariable(value = "fileMappSeq") String fileMappSeq) {

        paperService.hardDeleteDossier(tblSeq, fileMappSeq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대 단건 물리 삭제가 완료되었습니다."));
    }

    /**
     * [삭제] 전자포대 탭 다건 물리 삭제
     * */
    @Operation(summary = "전자포대 탭 다건 물리 삭제", description = "각 업무의 전자포대 다건 물리 삭제 API 입니다.")
    @DeleteMapping("/dossier/multi-delete/hard/{tblSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteDossier(
            @PathVariable(value = "tblSeq") String tblSeq,
            @RequestBody List<String> fileMappSeqList) {

        paperService.hardDeleteDossierByList(tblSeq, fileMappSeqList);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "전자포대 다건 물리 삭제가 완료되었습니다."));
    }

}