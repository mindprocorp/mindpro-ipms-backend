package kr.co.mindpro.ipms.domain.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.ai.dto.request.ChatRequest;
import kr.co.mindpro.ipms.domain.ai.dto.response.ChatResponse;
import kr.co.mindpro.ipms.domain.ai.dto.response.DocumentAnalysisResponse;
import kr.co.mindpro.ipms.domain.ai.service.ModelService;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.user.dto.request.UserRequest;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import kr.co.mindpro.ipms.domain.ai.vo.AiConnectVo;
import java.util.List;

import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;

@Slf4j
@Tag(name = "AI API", description = "AI 호출 API")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ModelService modelService;
    private final RagService ragService;
    private final kr.co.mindpro.ipms.domain.ai.service.AiDocumentAgentService aiDocumentAgentService;

    @Operation(summary = "AI 스트리밍 대화")
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return modelService.chatStream(request, userDetails.getUserMstSeq());
    }

    @Operation(summary = "AI 질문 (단답형)")
    @PutMapping
    public ResponseEntity<ApiResponse<ChatResponse>> callQuestion(@Valid @RequestBody ChatRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String message = modelService.callQuestion(request, userDetails.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                ChatResponse.builder().resultMessage(message).build()));
    }

    @Operation(summary = "RAG 문서 강제 주입")
    @PostMapping("/rag/document")
    public ResponseEntity<ApiResponse<Void>> addRagDocument(@RequestParam String content, @RequestParam(defaultValue = "수동입력") String source, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ragService.addDocument(userDetails.getOfficeSeq(), content, source);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "주입 성공", null));
    }

    @Operation(summary = "사건 데이터 Vector DB 일괄 동기화")
    @PostMapping("/rag/sync/conflict/batch")
    public ResponseEntity<ApiResponse<String>> syncConflictBatch(@AuthenticationPrincipal CustomUserDetails userDetails) {
        int totalCount = modelService.syncAllConflictData(userDetails.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "총 " + totalCount + "건 동기화 요청 완료", null));
    }

    @Operation(summary = "사용자 AI 모델 목록")
    @GetMapping("/models")
    public ResponseEntity<ApiResponse<List<AiConnectVo>>> getMyModels(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                modelService.getAllAiModels(userDetails.getUserMstSeq())));
    }

    @Operation(summary = "파일 내용 기반 AI 분류 및 자동 등록 에이전트")
    @PostMapping(value = "/agent/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentAnalysisResponse>> analyzeAndRegisterFile(
            @RequestParam("file") MultipartFile file) {

        // 비즈니스 파라미터(appSeq, officeSeq) 없이 파일만 딱 던집니다.
        var result = aiDocumentAgentService.analyzeAndRegisterDocument(file);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "파일 분석 시작", result));
    }

    @Operation(summary = "사용자 AI 모델 등록")
    @PostMapping("/models")
    public ResponseEntity<ApiResponse<AiConnectVo>> createAiModel(@RequestBody AiConnectVo vo, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "등록 성공", modelService.createAiModel(vo)));
    }

    @Operation(summary = "사용자 AI 모델 수정")
    @PutMapping("/models/{connectionSeq}")
    public ResponseEntity<ApiResponse<AiConnectVo>> updateAiModel(@PathVariable("connectionSeq") Long connectionSeq, @RequestBody AiConnectVo vo, @AuthenticationPrincipal CustomUserDetails userDetails) {
        vo.setConnectionSeq(connectionSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "수정 성공", modelService.updateAiModel(vo)));
    }

    @Operation(summary = "사용자 AI 모델 삭제")
    @DeleteMapping("/models/{connectionSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteAiModel(@PathVariable("connectionSeq") Long connectionSeq, @AuthenticationPrincipal CustomUserDetails userDetails) {
        modelService.deleteAiModel(connectionSeq, userDetails.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공", null));
    }
}
