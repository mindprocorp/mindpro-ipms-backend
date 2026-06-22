package kr.co.mindpro.ipms.domain.conflict.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.conflict.dto.request.*;
import kr.co.mindpro.ipms.domain.conflict.dto.response.ConflictResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : mindpro
 * @fileName : ConflictService.java
 * @since    : 2026. 01. 07.
 */
public interface ConflictService {

    /**
     * 분쟁/심판 정보 저장 (등록 및 수정)
     * @param request ConflictRequest.ConflictDetail (통합 레코드)
     * @param appFile 출원상표 파일
     * @param citedFile 인용상표 파일
     * @return 저장/수정된 분쟁/심판 상세 정보
     */
    ConflictResponse.ConflictDetail createConflict(ConflictRequest.ConflictDetail request, MultipartFile appFile, MultipartFile citedFile);

    /**
     * 분쟁/심판 상세 조회
     * @param conflictSeq 일련번호
     * @return ConflictResponse.ConflictDetail (통합 응답 레코드)
     */
    ConflictResponse.ConflictDetail getConflictDetail(String conflictSeq);

    /**
     * 분쟁/심판 목록 조회
     */
    BaseSearchResponse<ConflictResponse.ConflictListDetail> getConflictList(BaseSearchRequest request);

    /**
     * 원하급심(결과) 리스트 조회
     */
    ConflictResponse.ConflictResultList getConflictResultList(String conflictSeq);

    /**
     * 원하급심(결과) 정보 저장 (등록 및 수정)
     * @param request 결과 상세 정보
     * @return 저장/수정된 결과 상세 정보
     */
    ConflictResponse.ConflictResultDetail createResultConflict(ConflictRequest.ResultDetail request);


    // --- 기타사건 관련 ---

    /**
     * 기타사건 정보 저장 (등록 및 수정)
     * @param request 기타사건 상세 정보
     * @param etcConflictFile 관련 파일
     * @return 저장/수정된 기타사건 상세 정보
     */
    ConflictResponse.ConflictEtcCaseDetail createEtcConflict(ConflictRequest.ConflictEtcCaseDetail request, MultipartFile etcConflictFile);

    /**
     * 기타사건 정보 조회
     */
    ConflictResponse.ConflictEtcCaseDetail getEtcConflictDetail(String conflictSeq);

    /**
     * 기타사건 목록 조회
     */
    ConflictResponse.ConflictEtcList getEtcConflictList(BaseSearchRequest request);

    /**
     * 분쟁/심판 삭제
     */
    void deleteConflict(String conflictSeq);

    /**
     * 분쟁/심판 또는 기타사건 이미지 삭제
     */
    void deleteConflictFile(String conflictSeq, String fileSeq);

}