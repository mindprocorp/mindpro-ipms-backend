package kr.co.mindpro.ipms.domain.jobprogress.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.jobprogress.dto.request.JobProgressRequest;
import kr.co.mindpro.ipms.domain.jobprogress.dto.response.JobProgressResponse;
import kr.co.mindpro.ipms.domain.jobprogress.repository.db1.JobProgressMapper;
import kr.co.mindpro.ipms.domain.jobprogress.service.JobProgressService;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressMergeVO;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressVO;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


import kr.co.mindpro.ipms.domain.ai.service.RagService;

/**
 * 관계자 비즈니스 로직 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobProgressServiceImpl implements JobProgressService {

    private final ParticipantService participantService;
    private final DueDateService dueDateService;
    private final PaperService paperService;
    private final JobProgressMapper jobProgressMapper;
    private final RagService ragService;

    /**
     * 업무 진행 단건 상세 조회
     */
    @Transactional(readOnly = true)
    @Override
    public JobProgressResponse.JobProgressDetail getProgressDetail(String progressSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 1. 뷰 조인 쿼리로 모든 정보를 한 번에 조회
        JobProgressMergeVO vo = jobProgressMapper.findProgressDetail(progressSeq, officeSeq);

        if (vo == null) {
            throw new RuntimeException("해당 진행 내역을 찾을 수 없습니다.");
        }

        // 2. 파일 리스트 조회
        List<PaperResponseVO> fileList = paperService.getFileMappByWork(progressSeq, officeSeq);

        // 3. 공통 변환 메서드 호출하여 반환
        return convertToDetail(vo, fileList);
    }


    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<JobProgressResponse.JobProgressDetail> getProgressList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // [1] 파라미터 세팅 (XML의 searchCondition에서 request.xxx로 접근하므로 명확히 설정)
        request.setOfficeSeq(officeSeq);
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // [2] DB 조회 - 이제 쿼리 한 방에 기일/관계자 정보가 다 포함되어 있습니다.
        // 결과 타입을 JobProgressMergeVO로 바로 받아서 Cast 오류를 방지합니다.
        List<JobProgressMergeVO> dbList = jobProgressMapper.findProgressList(request);
        int totalCount = jobProgressMapper.totalCount(request);

        // 데이터가 없을 경우 처리
        if (ObjectUtils.isEmpty(dbList)) {

        }

        // [3] 데이터 변환 (이제 DataConvertUtil.injectData 호출 없이 바로 Build)
        List<JobProgressResponse.JobProgressDetail> detailList = dbList.stream().map(vo -> {
            String progSeq = vo.getProgressSeq();

            // 파일 리스트는 조인이 무거우므로 기존처럼 별도 조회 유지
            List<PaperResponseVO> fileList = paperService.getFileMappByWork(progSeq, officeSeq);

            // 최종 응답 Record 빌드 (vo에 이미 JOIN된 noticeDate, examinerName 등이 있음)
            return JobProgressResponse.JobProgressDetail.builder()
                    .progressSeq(vo.getProgressSeq())

                    /* 문서 정보 */
                    .receiptDoc(CommonRecordResponse.DocumentInfo.builder()
                            .docSeq(vo.getReceiptDocSeq())
                            .docName(vo.getReceiptDocName())
                            .build())
                    .submitDoc(CommonRecordResponse.DocumentInfo.builder()
                            .docSeq(vo.getSubmitDocSeq())
                            .docName(vo.getSubmitDocName())
                            .build())

                    /* 1. 통지 / 접수 */
                    .noticeDate(vo.getNoticeDate())
                    .agentReceiptDate(vo.getAgentReceiptDate())
                    .examiner(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getExaminer())
                            .userName(vo.getExaminerName())
                            .build())
                    .receiptDocContent(vo.getReceiptDocContent())

                    /* 2. 접수 보고 */
                    .receiptReportLimitDate(vo.getReceiptReportLimitDate())
                    .receiptReportDate(vo.getReceiptReportDate())
                    .receiptReportManager(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getReceiptReportManager())
                            .userName(vo.getReceiptReportManagerName())
                            .build())

                    /* 3. 검토 */
                    .reviewOpinionLimitDate(vo.getReviewOpinionLimitDate())
                    .reviewReportDate(vo.getReviewReportDate())
                    .reviewReportManager(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getReviewReportManager())
                            .userName(vo.getReviewReportManagerName())
                            .build())

                    /* 4. 지시 */
                    .instructionDate(vo.getInstructionDate())
                    .instructionContent(vo.getInstructionContent())

                    /* 5. 기연 / 제출 */
                    .extensionCount(vo.getExtensionCount())
                    .documentLimitDate(vo.getDocumentLimitDate())
                    .documentSubmitDate(vo.getDocumentSubmitDate())
                    .PaperFiles(CommonRecordResponse.FileInfo.from(fileList))

                    /* 6. 제출 대상 / 담당 / 비고 */
                    .target(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getTargetCode())
                            .codeName(vo.getTargetCodeName())
                            .build())
                    .deptName(vo.getDeptName())
                    .submitManager(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getSubmitManager())
                            .userName(vo.getSubmitManagerName())
                            .build())
                    .note(vo.getNote())

                    /* 7. 제출 보고 */
                    .submitReportLimitDate(vo.getSubmitReportLimitDate())
                    .submitReportDate(vo.getSubmitReportDate())
                    .submitReportManager(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getSubmitReportManager())
                            .userName(vo.getSubmitReportManagerName())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        // [4] 최종 반환
        return BaseSearchResponse.of(detailList, totalCount, request.getPage(), request.getPageSize());
    }


    /**
     * VO -> Response DTO 변환 공통 로직 (목록/상세 공용)
     */
    private JobProgressResponse.JobProgressDetail convertToDetail(JobProgressMergeVO vo, List<PaperResponseVO> fileList) {
        return JobProgressResponse.JobProgressDetail.builder()
                .progressSeq(vo.getProgressSeq())
                .receiptDoc(CommonRecordResponse.DocumentInfo.builder()
                        .docSeq(vo.getReceiptDocSeq()).docName(vo.getReceiptDocName()).build())
                .submitDoc(CommonRecordResponse.DocumentInfo.builder()
                        .docSeq(vo.getSubmitDocSeq()).docName(vo.getSubmitDocName()).build())

                .noticeDate(vo.getNoticeDate())
                .agentReceiptDate(vo.getAgentReceiptDate())
                .examiner(CommonRecordResponse.PersonInfo.builder()
                        .userSeq(vo.getExaminer()).userName(vo.getExaminerName()).build())
                .receiptDocContent(vo.getReceiptDocContent())

                .receiptReportLimitDate(vo.getReceiptReportLimitDate())
                .receiptReportDate(vo.getReceiptReportDate())
                .receiptReportManager(CommonRecordResponse.PersonInfo.builder()
                        .userSeq(vo.getReceiptReportManager()).userName(vo.getReceiptReportManagerName()).build())

                .reviewOpinionLimitDate(vo.getReviewOpinionLimitDate())
                .reviewReportDate(vo.getReviewReportDate())
                .reviewReportManager(CommonRecordResponse.PersonInfo.builder()
                        .userSeq(vo.getReviewReportManager()).userName(vo.getReviewReportManagerName()).build())

                .instructionDate(vo.getInstructionDate())
                .instructionContent(vo.getInstructionContent())

                .extensionCount(vo.getExtensionCount())
                .documentLimitDate(vo.getDocumentLimitDate())
                .documentSubmitDate(vo.getDocumentSubmitDate())
                .PaperFiles(CommonRecordResponse.FileInfo.from(fileList))

                .target(CommonRecordResponse.CodeInfo.builder()
                        .code(vo.getTargetCode()).codeName(vo.getTargetCodeName()).build())
                .deptName(vo.getDeptName())
                .submitManager(CommonRecordResponse.PersonInfo.builder()
                        .userSeq(vo.getSubmitManager()).userName(vo.getSubmitManagerName()).build())
                .note(vo.getNote())

                .submitReportLimitDate(vo.getSubmitReportLimitDate())
                .submitReportDate(vo.getSubmitReportDate())
                .submitReportManager(CommonRecordResponse.PersonInfo.builder()
                        .userSeq(vo.getSubmitReportManager()).userName(vo.getSubmitReportManagerName()).build())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerProgressForAi(String systemSeq, String fileSeq, String summary, String docSeq, String officeSeq, String userSeq) {
        try {
            log.info(">>>> [DB 입력 시작] 사건번호: {}, 파일번호: {}", systemSeq, fileSeq);

            // 1. 진행사항 마스터 VO 조립 (빌더 패턴 활용)
            JobProgressVO vo = JobProgressVO.builder()
                    .officeSeq(officeSeq)
                    .tblCode("PROGRS")
                    .tblSeq(systemSeq)           // 상위 사건 번호 (APPMST...)
                    .receiptDocSeq(docSeq)       // 문서 유형 코드 (99 등)
                    .receiptDocContent(summary)  // AI 분석 요약 내용
                    .progressState("진행중")
                    .note("AI 에이전트 자동 등록 건")
                    .build();

            // BaseVO 공통 필드 강제 세팅 (AI 스레드 대응)
            vo.setCreateUser(userSeq);
            vo.setUpdateUser(userSeq);
            vo.setDelYn("N");

            // 2. DB 저장 (utb_progress)
            // MyBatis selectKey를 통해 vo.progressSeq에 신규 번호가 채워짐
            jobProgressMapper.insertProgress(vo);
            String newProgSeq = vo.getProgressSeq();

            // 3. S3 파일 연결 (매핑 테이블 INSERT)
            paperService.linkFileToWork(newProgSeq, fileSeq, officeSeq, userSeq, docSeq);

            // 4. AI 검색용 RAG 동기화 (방금 넣은 데이터를 AI가 학습)
            ragService.syncVectorData(officeSeq, "JOB_PROGRESS", newProgSeq, "진행사항", vo);

            log.info(">>>> [DB 입력 완료] 신규 번호: {}", newProgSeq);

            // [핵심 수정] AI가 파싱할 수 있도록 성공 코드와 핵심 ID들을 구분자(|)로 묶어서 리턴합니다.
            return String.format("SUCCESS|%s|%s|%s", newProgSeq, systemSeq, summary);

        } catch (Exception e) {
            log.error(">>>> [DB 입력 실패] 원인: ", e);
            // 실패 시에도 AI가 인지할 수 있게 에러 메시지 리턴
            return "ERROR|" + e.getMessage();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JobProgressResponse.JobProgressDetail registerProgress(JobProgressRequest.JobProgressDetail request, List<MultipartFile> targetFiles) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        String progSeq = request.progressSeq();

        int duplicateSeq = 0;

        if (progSeq != null && !progSeq.isBlank()) {
            duplicateSeq = jobProgressMapper.getDuplicateProgressCnt(progSeq);
        }

        JobProgressVO vo = JobProgressVO.builder()
                .officeSeq(officeSeq)
                .tblCode("PROGRS")
                .tblSeq(request.tblSeq())
                .targetCode(request.target() != null ? request.target().code() : "")
                .targetCodeName(request.target() != null ? request.target().codeName() : "")
                .deptName(request.deptName())
                .receiptDocSeq(request.receiptDoc() != null ? request.receiptDoc().docSeq() : "")
                .submitDocSeq(request.submitDoc() != null ? request.submitDoc().docSeq() : "")
                .progressState(request.progressState())
                .instructionContent(request.instructionContent())
                .receiptDocContent(request.receiptDocContent())
                .extensionCount(request.extensionCount())
                .note(request.note())
                .build();

        if (duplicateSeq > 0) { // 중복된 식별키가 있을 경우 - 수정 진행.
            vo.setProgressSeq(progSeq);
            vo.setUpdateUser(loginUser);

            jobProgressMapper.updateProgressByProgSeq(vo);

            if (request.deleteFileSeqList() != null && !request.deleteFileSeqList().isEmpty()) {
                paperService.softDeleteFilesByFileSeqList(progSeq, request.deleteFileSeqList());
            }
        } else {        // 중복된 식별키가 없을 경우 - 신규 등록.
            vo.setCreateUser(loginUser);

            // MyBatis selectKey를 통해 vo.progressSeq에 PK가 할당됨
            jobProgressMapper.insertProgress(vo);
            progSeq = vo.getProgressSeq();
        }

        // 2. [추출 전용] MergeVO 빌드 (@CommonMapping 필드만 채움)
        JobProgressMergeVO mergeVO = JobProgressMergeVO.builder()
                .noticeDate(request.noticeDate())
                .agentReceiptDate(request.agentReceiptDate())
                // 주의: NPE 방지를 위해 하위 객체 null 체크 권장 (아래는 예시)
                .examiner(request.examiner() != null ? request.examiner().userSeq() : null)
                .receiptReportLimitDate(request.receiptReportLimitDate())
                .receiptReportDate(request.receiptReportDate())
                .receiptReportManager(request.receiptReportManager() != null ? request.receiptReportManager().userSeq() : null)
                .reviewOpinionLimitDate(request.reviewOpinionLimitDate())
                .reviewReportDate(request.reviewReportDate())
                .reviewReportManager(request.reviewReportManager() != null ? request.reviewReportManager().userSeq() : null)
                .instructionDate(request.instructionDate())
                .extensionCount(request.extensionCount())
                .documentLimitDate(request.documentLimitDate())
                .documentSubmitDate(request.documentSubmitDate())
                .submitManager(request.submitManager() != null ? request.submitManager().userSeq() : null)
                .submitReportLimitDate(request.submitReportLimitDate())
                .submitReportDate(request.submitReportDate())
                .submitReportManager(request.submitReportManager() != null ? request.submitReportManager().userSeq() : null)
                .build();

        // 3. 기한(Date) 데이터 추출 및 저장
        dueDateService.saveAllDueDates(
                DataConvertUtil.extractDueDates(mergeVO, progSeq, officeSeq, "PRG")
        );

        // 4. 관계자(Person) 데이터 추출 및 저장
        participantService.saveAllParticipants(
                DataConvertUtil.extractParticipants(mergeVO, progSeq, officeSeq, "PRG")
        );

        // 5. [파일 매핑 저장] 리스트 순회 처리
        if (targetFiles != null && !targetFiles.isEmpty()) {
            for (MultipartFile file : targetFiles) {
                if (!file.isEmpty()) {
                    // 여러 파일을 저장할 때 카테고리를 유동적으로 주거나 공통 카테고리를 사용
                    // 여기서는 "PRG_FILE" 이라는 공통 카테고리로 예시를 듭니다.
                    paperService.saveFileMapping(
                            createFileReq(
                                    progSeq,
                                    officeSeq,
                                    loginUser,
                                    file,
                                    "PRG_FILE",
                                    request.receiptDoc() != null ? request.receiptDoc().docSeq() : "",
                                    request.submitDoc() != null ? request.submitDoc().docSeq() : ""
                            )
                    );
                }
            }
        }
        
        // 6. Vector Store 동기화 (AI RAG)
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "JOB_PROGRESS", progSeq,"진행사항", request);
        return this.getProgressDetail(progSeq);
    }
    private PaperRequestVO createFileReq(String seq, String office, String user, MultipartFile file, String category, String receiptDocSeq, String submitDocSeq) {
        return PaperRequestVO.builder()
                .officeSeq(office)
                .tblSeq(seq)
                .file(file)
                .receiptDocSeq(receiptDocSeq)
                .submitDocSeq(submitDocSeq)
                .fileCategoryCode(category)
                .createUser(user)
                .updateUser(user)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteProgress(String tblSeq, String progSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        int result = jobProgressMapper.softDeleteProgress(officeSeq, tblSeq, progSeq, loginUser);

        if (result <= 0) throw new RuntimeException("Failed to soft delete progress");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteProgressByList(String tblSeq, List<String> progSeqList) {
        if (progSeqList == null || progSeqList.isEmpty()) {
            throw new RuntimeException("progSeqList is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        int result = jobProgressMapper.softDeleteProgressByList(officeSeq, tblSeq, progSeqList, loginUser);

        if (result != progSeqList.size()) throw new RuntimeException("Failed to soft delete progress list");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteProgress(String tblSeq, String progSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = jobProgressMapper.hardDeleteProgress(officeSeq, tblSeq, progSeq);

        if (result <= 0) throw new RuntimeException("Failed to hard delete progress");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteProgressByList(String tblSeq, List<String> progSeqList) {
        if (progSeqList == null || progSeqList.isEmpty()) {
            throw new RuntimeException("progSeqList is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = jobProgressMapper.hardDeleteProgressByList(officeSeq, tblSeq, progSeqList);

        if (result != progSeqList.size()) throw new RuntimeException("Failed to hard delete progress list");

    }
}
