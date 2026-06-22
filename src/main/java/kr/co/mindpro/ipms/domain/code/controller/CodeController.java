package kr.co.mindpro.ipms.domain.code.controller;

import java.util.List;
import java.util.Map;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.DocumentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.request.GroupCodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeDtlResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeMstResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeResponse;
import kr.co.mindpro.ipms.domain.code.service.CodeService;
import kr.co.mindpro.ipms.domain.code.vo.CodeDtlVO;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "공통코드 API", description = "공통코드 CRUD API")
@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    /**
     * 공통코드 조회
     * @param codeRequest
     * @return
     */
    @Operation(summary = "공통코드 조회", description = "공통코드 조회를 한다.")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<CodeResponse>>> getCommonCodeDetail(@ModelAttribute CodeRequest codeRequest ) {
        List<CodeResponse> codeResponse = codeService.getCommonCodeDetail(codeRequest);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "공통코드 조회 성공", codeResponse));
    }


    /**
     * 코드 생성/수정
     * @param codeRequest
     * @return
     */
    @Operation(summary = "코드 생성/수정", description = "공통코드 생성/수정 한다. ")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> createAndModifyCommonCode(@RequestBody @Valid CodeRequest codeRequest ) {
        codeService.createAndModifyCommonCode(codeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "이의심판 등록이 완료되었습니다."));
    }

    /**
     * 그룹코드 생성/수정
     * @param codeRequest
     * @return
     */
    @Operation(summary = "그룹코드 생성/수정", description = "공통코드 생성/수정 한다. ")
    @PostMapping("/group")
    public ResponseEntity<ApiResponse<Void>> createAndModifyGroupCommonCode(@RequestBody @Valid GroupCodeRequest groupCodeRequest ) {
        codeService.createAndModifyGroupCommonCode(groupCodeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "그룹코드 등록이 완료되었습니다."));
    }

    @Operation(summary = "그룹코드 삭제", description = "그룹(코드 분류) 논리 삭제. 같은 grp_cd의 디테일도 함께 삭제됨.")
    @DeleteMapping("/group/{codeSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteGroupCommonCode(@PathVariable("codeSeq") String codeSeq) {
        codeService.deleteCodeMst(codeSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "그룹코드가 삭제되었습니다."));
    }
    
    @Operation(summary = "마스터 코드 목록 조회(NEW)")
    @GetMapping("/master")
    public ResponseEntity<List<CodeMstResponse>> getMasterList(CodeMstVO search) {
        return ResponseEntity.ok(codeService.getCodeMstList(search));
    }

    @Operation(summary = "상세 코드 목록 조회(NEW)")
    @GetMapping("/detail/{grpCd}")
    public ResponseEntity<List<CodeDtlResponse>> getDetailList(@PathVariable String grpCd) {
        return ResponseEntity.ok(codeService.getCodeDtlList(grpCd));
    }

    @Operation(summary = "상세 코드 일괄 저장(NEW)", description = "I/U/D 상태값에 따라 상세 코드를 일괄 처리합니다.")
    @PostMapping("/detail/save")
    public ResponseEntity<String> saveDetailList(@RequestBody List<CodeDtlVO> dtlList) {
        codeService.saveCodeDtlList(dtlList);
        return ResponseEntity.ok("정상적으로 저장되었습니다.");
    }
    
    @Operation(summary = "그룹별 상세 코드 맵 조회(NEW)", description = "여러 그룹 코드를 받아 그룹별 리스트 맵을 반환합니다.")
    @GetMapping("/details/map")
    public ResponseEntity<ApiResponse<Map<String, List<CodeDtlResponse>>>> getDetailsMap(
        @RequestParam(name = "grpCdList") List<String> grpCdList
    ) {
        Map<String, List<CodeDtlResponse>> codeMap = codeService.getCodeDtlMapByGrpCodes(grpCdList);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "공통코드 조회 성공", codeMap));
    }

    @Operation(summary = "서류 항목 조회", description = "진입구분, 권리구분, 서류구분에 따른 서류 리스트를 조회한다.")
    @GetMapping("/documents") // 중복된 /me 대신 /documents 사용
    public ResponseEntity<ApiResponse<BaseSearchResponse<DocumentResponse>>> getDocumentList(
            @RequestParam(required = false) String entryType,
            @RequestParam(required = false) String patType,
            @RequestParam(required = false) String docDiv) {

        // CommonService(또는 codeService)를 통해 Record 리스트를 직접 받아옴
        BaseSearchResponse<DocumentResponse> documents = codeService.getDocumentList(entryType, patType, docDiv);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "서류 항목 조회 성공", documents));
    }
}
