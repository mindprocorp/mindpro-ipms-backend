        package kr.co.mindpro.ipms.domain.conflict.service.impl;
        import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
        import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
        import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
        import kr.co.mindpro.ipms.common.util.DataConvertUtil;

        import kr.co.mindpro.ipms.common.util.SecurityUtil;
        import kr.co.mindpro.ipms.domain.conflict.dto.request.*;

        import kr.co.mindpro.ipms.domain.conflict.dto.response.ConflictResponse;

        import kr.co.mindpro.ipms.domain.ai.service.RagService;
        import kr.co.mindpro.ipms.domain.conflict.repository.db1.ConflictMapper;
        import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;
        import kr.co.mindpro.ipms.domain.conflict.vo.*;
        import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
        import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
        import kr.co.mindpro.ipms.domain.paper.service.PaperService;
        import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
        import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
        import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
        import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;
        import kr.co.mindpro.ipms.domain.patentApp.domesticApp.repository.db1.DomesticAppMapper;
        import kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo.AppMstVO;

        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.util.ObjectUtils;
        import org.springframework.util.StringUtils;
        import org.springframework.web.multipart.MultipartFile;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;
        import java.util.stream.Collectors;

        /**
 * 이의심판 비즈니스 로직 구현체
 *
 * @author   : min
 * @fileName : ConflictServiceImpl.java
 * @since    : 2026. 01. 07.
 */

        @Service
        @RequiredArgsConstructor
        public class ConflictServiceImpl implements ConflictService {
            private final ConflictMapper conflictMapper;
            private final DomesticAppMapper domesticAppMapper;
            private final PaperService paperService;
            private final DueDateService dueDateService;
            private final ParticipantService participantService;
            private final RagService ragService;




            // 1. [핵심] 이의심판 상세 저장 (등록 및 수정 통합)
            @Override
            @Transactional(rollbackFor = Exception.class)
            public ConflictResponse.ConflictDetail createConflict(ConflictRequest.ConflictDetail request, MultipartFile appFile, MultipartFile citedFile) {
                ConflictMergeVO mergeVO = new ConflictMergeVO();

                // 1. VO 조각 채우기
                mergeVO.setEtcYn("N");
                mergeVO.fillCaseMng(request.cftCaseMng());
                mergeVO.fillAppBaseInfo(request.appBaseInfo());
                mergeVO.fillTitleInfo(request.appTitleInfo());
                mergeVO.fillGoodsInfo(request.appGoodsInfo());
                mergeVO.fillPartyInfo(request.appPartyInfo());
                mergeVO.fillLitigantInfo(request.cftLitigantInfo());
                mergeVO.fillJudgmentInfo(request.cftJudgmentInfo());
                mergeVO.fillManagerInfo(request.cftManagerInfo());
                mergeVO.fillNoteInfo(request.cftNoteInfo());

                // 2. 공통 마스터 엔진 가동 (여기서 Insert/Update 분기 처리)
                // request.conflictSeq()가 있으면 Update, 없으면 Insert
                String cftSeq = saveCommonConflictCore(mergeVO, request.conflictSeq());

                // 3. 일반 사건 전용: 판결 결과 목록 저장
                if (request.cftResultList() != null) {
                    saveConflictResultList(cftSeq, request.cftResultList().conflictResultList());
                }

                // 4. 일반 사건 전용: 파일 2개 저장
                saveConflictFiles(appFile, citedFile, cftSeq, SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq());

                // 5. 저장(또는 수정) 후 최신 데이터를 다시 조회해서 반환
                ConflictResponse.ConflictDetail response = this.getConflictDetail(cftSeq);
                
                // 6. Vector DB 비동기 동기화 (AI RAG 검색용)
                ragService.syncVectorData(SecurityUtil.getOfficeSeq(), "CONFLICT", cftSeq,"이의심판", response);
                
                return response;
            }

            /**
             * [2] 기타 사건 저장 (등록 및 수정 통합)
             */
            @Override
            @Transactional(rollbackFor = Exception.class)
            public ConflictResponse.ConflictEtcCaseDetail createEtcConflict(ConflictRequest.ConflictEtcCaseDetail request, MultipartFile etcConflictFile) {
                ConflictMergeVO mergeVO = new ConflictMergeVO();

                // 1. VO 조각 채우기
                mergeVO.setEtcYn("Y");
                mergeVO.fillEtcCaseMng(request.cftCaseMng());
                mergeVO.fillEtcAppBaseInfo(request.appBaseInfo());
                mergeVO.fillTitleInfo(request.appTitleInfo());
                mergeVO.fillGoodsInfo(request.appGoodsInfo());
                mergeVO.fillEtcPartyInfo(request.appPartyInfo());
                mergeVO.fillEtcLitigantInfo(request.cftLitigantInfo());
                mergeVO.fillJudgmentInfo(request.cftJudgmentInfo());
                mergeVO.fillManagerInfo(request.cftManagerInfo());
                mergeVO.fillNoteInfo(request.cftNoteInfo());

                // 2. 공통 마스터 엔진 가동 (여기서 Insert/Update 분기 처리)
                String cftSeq = saveCommonConflictCore(mergeVO, request.conflictSeq());

                // 3. 기타 사건 전용: 파일 저장
                if (etcConflictFile != null && !etcConflictFile.isEmpty()) {
                    paperService.saveFileMapping(createFileReq(cftSeq, SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq(), etcConflictFile, "etcConflictFile", "396"));
                }

                // 4. 저장(또는 수정) 후 최신 데이터를 다시 조회해서 반환
                ConflictResponse.ConflictEtcCaseDetail response = this.getEtcConflictDetail(cftSeq);
                
                // 5. Vector DB 비동기 동기화
                ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "ETC_CONFLICT", cftSeq,"기타사건", response);
                
                return response;
            }

            /**
             * [3] 공통 마스터 저장/수정 엔진
             * @param mergeVO 채워진 데이터 객체
             * @param existCftSeq 수정일 경우 존재할 시퀀스 번호
             */
            private String saveCommonConflictCore(ConflictMergeVO mergeVO, String existCftSeq) {
                String officeSeq = SecurityUtil.getOfficeSeq();
                String loginUser = SecurityUtil.getUserInfoSeq();
                mergeVO.setOfficeSeq(officeSeq);
                mergeVO.setCreateUser(loginUser);

                // Step 1: AppMst (출원 마스터) 처리
                String targetAppSeq = mergeVO.getAppSeq();
                /*
                if (!StringUtils.hasText(targetAppSeq)) {
                    CommonAppVO appVo = CommonAppVO.builder()
                            .officeSeq(officeSeq)
                            .countryCode(mergeVO.getCountryCode())
                            .categoryCode(mergeVO.getAppClassificationCode())
                            .goodsClass(mergeVO.getGoodsClass())
                            .domesticRegNo(mergeVO.getDomesticRegNo())
                            .titleKo(mergeVO.getTitleKo())
                            .titleEn(mergeVO.getTitleEn())
                            .appNo(mergeVO.getAppNo())
                            .rightTypeCode(mergeVO.getRightTypeCode())
                            .regNo(mergeVO.getRegNo())
                            .ourRef(mergeVO.getOurRef())
                            .yourRef(mergeVO.getYourRef())
                            .clientRef(mergeVO.getClientRef())
                            .statusCode("99")
                            .stateCode("")
                            .createUser(loginUser)
                            .build();
                    domesticAppMapper.insertDomesticApp(appVo);
                    targetAppSeq = appVo.getAppSeq();
                    mergeVO.setAppSeq(targetAppSeq);

                    dueDateService.saveAllDueDates(DataConvertUtil.extractDueDates(mergeVO, targetAppSeq, officeSeq, "APP"));
                    participantService.saveAllParticipants(DataConvertUtil.extractParticipants(mergeVO, targetAppSeq, officeSeq, "APP"));
                }
                */

                // Step 2: ConflictMst (분쟁 마스터) 준비
                ConflictMstVO cftVo = ConflictMstVO.builder()
                        .conflictSeq(existCftSeq) // 시퀀스가 있으면 Update 시 사용됨
                        .officeSeq(officeSeq)
                        .appSeq(targetAppSeq)
                        .ourRef(mergeVO.getOurRef())
                        .yourRef(mergeVO.getYourRef())
                        .introducer(mergeVO.getIntroducer())
                        .etcYn(mergeVO.getEtcYn())
                        .litigationCaseNo(mergeVO.getCaseNo())
                        .caseTypeCode(mergeVO.getCaseTypeCode())
                        .courtCategoryCode(mergeVO.getCourtCategoryCode())
                        .caseTitleKo(mergeVO.getCaseTitleKo())
                        .appClassificationCode(mergeVO.getAppClassificationCode())
                        .agentCategoryCode(mergeVO.getAgentCategoryCode())
                        .status(mergeVO.getStatusCode())
                        .deptName(mergeVO.getDeptName())
                        .petitionerType(mergeVO.getPetitionerType())
                        .petitionerMemo(mergeVO.getPetitionerMemo())
                        .respondentType(mergeVO.getRespondentType())
                        .respondentMemo(mergeVO.getRespondentMemo())
                        .preExamResult(mergeVO.getPreExamResult())
                        .finalResult(mergeVO.getFinalResult())
                        .decisionContent(mergeVO.getDecisionContent())
                        .abandonYn(mergeVO.getIsAbandoned())
                        .note(mergeVO.getNote())
                        .abandonContent(mergeVO.getAbandonContent())
                        .appealContent(mergeVO.getAppealContent())
                        .respondent(mergeVO.getRespondent())
                        .foreignAgent(mergeVO.getForeignAgentName())
                        .client(mergeVO.getClientName())
                        .createUser(loginUser)
                        .updateUser(loginUser) // 업데이트 시를 위해 추가
                        .delYn("N")
                        .build();

                // 분기 처리: 시퀀스 존재 여부에 따라 처리
                if (StringUtils.hasText(existCftSeq)) {
                    conflictMapper.updateConflictMst(cftVo);
                } else {
                    conflictMapper.insertConflictMst(cftVo);
                }

                String cftSeq = cftVo.getConflictSeq();

                // 권리(right_category) / 출원인관리번호(retain_seq) 는 utb_conflict_mst 가 아닌
                // utb_app_mst 에 저장되므로, 기존 출원을 그대로 쓸 때(=신규 등록 시 OurRef 검색했거나, 수정 시)
                // form 값과 출원 마스터를 동기화. 신규 출원을 만든 경우는 위쪽 INSERT 단계에서 이미 반영됨.
                if (StringUtils.hasText(mergeVO.getAppSeq())) {
                    conflictMapper.updateAppRightAndClientRef(
                            mergeVO.getAppSeq(),
                            officeSeq,
                            mergeVO.getRightTypeCode(),
                            mergeVO.getClientRef(),
                            loginUser
                    );
                }

                // Step 3: 기일 및 관계자 자동 추출 저장 (기존 데이터 삭제 후 재등록이 saveAll 내부에 있어야 함)
                dueDateService.saveAllDueDates(DataConvertUtil.extractDueDates(mergeVO, cftSeq, officeSeq, "CFT"));
                participantService.saveAllParticipants(DataConvertUtil.extractParticipants(mergeVO, cftSeq, officeSeq, "CFT"));

                return cftSeq;
            }

            /**
             * 판결 결과 목록 저장 (일반 사건용)
             */
            private void saveConflictResultList(String cftSeq, List<ConflictRequest.ConflictResultDetail> results) {
                String officeSeq = SecurityUtil.getOfficeSeq();
                String loginUser = SecurityUtil.getUserInfoSeq();

                // 기존 result seq 목록 조회 후 duedate_mapp 포함 전체 삭제 (중복 방지)
                List<ConflictResultMstVO> existingResults = conflictMapper.selectConflictResultList(officeSeq, cftSeq);
                for (ConflictResultMstVO existing : existingResults) {
                    dueDateService.deleteDueDateMappByTblSeq(existing.getConflictResultSeq(), officeSeq);
                }
                conflictMapper.deleteConflictResultByConflictSeq(cftSeq, officeSeq);

                if (results == null || results.isEmpty()) return;

                for (ConflictRequest.ConflictResultDetail result : results) {
                    ConflictResultMstVO resultVo = ConflictResultMstVO.builder()
                            .conflictSeq(cftSeq)
                            .officeSeq(officeSeq)
                            .judgmentCaseNo(result.judgmentCaseNo())
                            .judgmentContent(result.judgmentContent())
                            .judgmentSearchUrl(result.judgmentSearchUrl())
                            .judgmentCategoryCode(result.judgmentCategory() != null ? result.judgmentCategory().code() : null)
                            .note(result.note())
                            .createUser(loginUser)
                            .delYn("N")
                            .build();

                    conflictMapper.insertConflictResult(resultVo);

                    // 결과 결정일 기일 자동 등록
                    if (StringUtils.hasText(result.resultDecisionDate())) {
                        dueDateService.registerDueDate(DueDateVO.builder()
                                .officeSeq(officeSeq)
                                .tblSeq(resultVo.getConflictResultSeq())
                                .tblCode("CFTMST")
                                .duedateDate(DataConvertUtil.parseToOffsetDateTime(result.resultDecisionDate()))
                                .duedateCategoryCode("resultDecisionDate")
                                .createUser(loginUser)
                                .build());
                    }
                }
            }

            // 파일 저장 헬퍼 메서드 수정
            private void saveConflictFiles(MultipartFile appFile, MultipartFile citedFile, String cftSeq, String officeSeq, String loginUser) {
                // 1. 출원 상표 파일 저장
                //394 출원상표
                if (appFile != null && !appFile.isEmpty()) {
                    paperService.saveFileMapping(createFileReq(cftSeq, officeSeq, loginUser, appFile, "appTrademarkFile","394"));
                }

                // 2. 인용 상표 파일 저장
                //395 인용상표
                if (citedFile != null && !citedFile.isEmpty()) {
                    paperService.saveFileMapping(createFileReq(cftSeq, officeSeq, loginUser, citedFile, "citedTrademarkFile","395"));
                }
            }

            // 공통 VO 생성 로직 (유지)
            private PaperRequestVO createFileReq(String seq, String office, String user, MultipartFile file, String category, String docSeq) {
                return PaperRequestVO.builder()
                        .officeSeq(office)
                        .tblSeq(seq)
                        .tblCode("CFTMST")
                        .file(file)
                        .fileCategoryCode(category)
                        .docSeq(docSeq)
                        //.fileTypeCode("10") // 20: IMAGE
                        //.fileKindCode("90") // 90: ATTACH/UPLOAD
                        .createUser(user)
                        .build();
            }

            // 2. 상세 조회
            @Override
            @Transactional(readOnly = true)
            public ConflictResponse.ConflictDetail getConflictDetail(String conflictSeq) {
                String officeSeq = SecurityUtil.getOfficeSeq();
                ConflictMergeVO mergeVO = conflictMapper.findConflictBySeq(conflictSeq, officeSeq);

//                DataConvertUtil.injectData(mergeVO, participantService.getParticipantListByWork(conflictSeq, officeSeq), "CFT");
//                DataConvertUtil.injectData(mergeVO, dueDateService.getDueDateListByWork(conflictSeq, officeSeq), "CFT");

                List<PaperResponseVO> fileList = paperService.getFileMappByWork(conflictSeq, officeSeq);

                List<ConflictResultMstVO> conflictReultList = conflictMapper.selectConflictResultList(officeSeq, conflictSeq);
                List<ConflictResponse.ConflictResultDetail> dtoList = conflictReultList.stream()
                        .map(vo -> ConflictResponse.ConflictResultDetail.builder()
                                .conflictSeq(vo.getConflictSeq())
                                .judgmentCaseNo(vo.getJudgmentCaseNo())
                                .judgmentContent(vo.getJudgmentContent())
                                .judgmentSearchUrl(vo.getJudgmentSearchUrl())

                                // 코드 정보 객체 빌드
                                .judgmentCategory(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getJudgmentCategoryCode())
                                        .codeName(vo.getJudgmentCategoryName())
                                        .build())
                                .resultDecisionDate(vo.getResultDecisionDate())
                                .note(vo.getNote())
                                .build())
                        .toList();

                return ConflictResponse.ConflictDetail.builder()
                        .conflictSeq(mergeVO.getConflictSeq())
                        .cftCaseMng(ConflictResponse.ConflictCftCaseMng.from(mergeVO))
                        .appBaseInfo(ConflictResponse.ConflictAppBaseInfo.from(mergeVO))
                        .cftManagerInfo(ConflictResponse.ConflictCftManagerInfo.from(mergeVO))
                        .appPartyInfo(ConflictResponse.ConflictAppPartyInfo.from(mergeVO))
                        .appTitleInfo(ConflictResponse.ConflictAppTitleInfo.from(mergeVO))
                        .appGoodsInfo(ConflictResponse.ConflictAppGoodsInfo.from(mergeVO))
                        .cftLitigantInfo(ConflictResponse.ConflictCftLitigantInfo.from(mergeVO))
                        .cftJudgmentInfo(ConflictResponse.ConflictCftJudgmentInfo.from(mergeVO))
                        .cftNoteInfo(ConflictResponse.ConflictCftNoteInfo.from(mergeVO))
                        .cftFileList(ConflictResponse.ConflictFiles.from(fileList))
                        .cftResultList(ConflictResponse.ConflictResultList.builder()
                                .conflictResultList(dtoList)
                                .build())
                        .build();
            }

            @Override
            @Transactional(readOnly = true)
            public BaseSearchResponse<ConflictResponse.ConflictListDetail> getConflictList(BaseSearchRequest request) {
                request.setOfficeSeq(SecurityUtil.getOfficeSeq());

                // 1. 리스트가 아예 없으면 새로 생성
                if (request.getSearchCondition() == null) {
                    request.setSearchCondition(new java.util.ArrayList<>());
                }

                List<java.util.Map<String, String>> conditions = request.getSearchCondition();

                // 2. 리스트를 뒤져서 'etcYn' 키가 이미 있는지 확인
                boolean hasEtcYn = conditions.stream()
                        .anyMatch(cond -> "etcYn".equals(cond.get("codeName")));

                // 3. [중요] etcYn이 없으면 '추가', 있으면 '값 강제 보정'
                if (!hasEtcYn) {
                    // 프론트에서 안 보냈을 때: 안전하게 추가
                    java.util.Map<String, String> etcCond = new java.util.HashMap<>();
                    etcCond.put("codeName", "etcYn");
                    etcCond.put("codeValue", "N");
                    etcCond.put("andOrNOT", "AND");
                    conditions.add(etcCond);
                } else {
                    // 프론트에서 혹시라도 etcYn=Y를 보냈을 때: N으로 강제 덮어쓰기 (보안)
                    conditions.stream()
                            .filter(cond -> "etcYn".equals(cond.get("codeName")))
                            .forEach(cond -> cond.put("codeValue", "N"));
                }
                // 2. 통합 쿼리 호출 (Join & Pivot 완료된 MergeVO 리스트)
                List<ConflictMergeVO> dbList = conflictMapper.selectSearchConflictList(request);

                // 3. 스트림을 이용한 Record 변환
                List<ConflictResponse.ConflictListDetail> detailList = dbList.stream().map(vo ->
                        ConflictResponse.ConflictListDetail.builder()
                                /* --- [1. 사건 및 마스터 정보] --- */
                                .conflictSeq(vo.getConflictSeq())
                                .ourRef(vo.getOurRef())
                                .yourRef(vo.getYourRef())
                                .caseNo(vo.getCaseNo())
                                .note(vo.getNote())
                                .titleKo(vo.getTitleKo())
                                .isAbandoned(vo.getIsAbandoned())
                                .abandonContent(vo.getAbandonContent())

                                /* --- [2. CodeInfo 직접 빌드] --- */
                                .appClassification(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getAppClassificationCode()).codeName(vo.getAppClassificationCodeName()).build())
                                .status(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getStatusCode()).codeName(vo.getStatusCodeName()).build())
                                .caseType(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getCaseTypeCode()).codeName(vo.getCaseTypeCodeName()).build())
                                .courtCategoryCode(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getCourtCategoryCode()).codeName(vo.getCourtCategoryCodeName()).build())
                                .agentCategoryCode(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getAgentCategoryCode()).codeName(vo.getAgentCategoryCodeName()).build())
                                .country(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getCountryCode()).codeName(vo.getCountryCodeName()).build())
                                .rightType(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getRightTypeCode()).codeName(vo.getRightTypeCodeName()).build())

                                /* --- [3. PersonInfo 직접 빌드] --- */
                                .client(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getClient()).userName(vo.getClientName()).build())
                                .foreignAgent(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getForeignAgent()).userName(vo.getForeignAgentName()).build())
                                .petitioner(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getPetitioner()).userName(vo.getPetitionerName()).build())
                                .respondent(vo.getRespondent())
                                .adminMgr(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrName()).build())
                                .caseMgr(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                                .attorney(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())

                                /* --- [4. 날짜 및 기타 텍스트] --- */
                                .receiptDate(vo.getReceiptDate())
                                .dueLimitDate(vo.getDueLimitDate())
                                .claimDate(vo.getClaimDate())
                                .judgmentDate(vo.getJudgmentDate())
                                .decisionContent(vo.getDecisionContent())
                                .appealLimitDate(vo.getAppealLimitDate())
                                .appealDate(vo.getAppealDate())
                                .abandonDate(vo.getAbandonDate())
                                .appCountry(vo.getAppCountry())
                                .appNo(vo.getAppNo())
                                .appDate(vo.getAppDate())
                                .regNo(vo.getRegNo())
                                .regDate(vo.getRegDate())
                                .build()
                ).toList();

                // 4. 응답 반환 (전체 카운트는 필요시 별도 count 쿼리 연동)
                return BaseSearchResponse.of(detailList, detailList.size(), request.getPage(), request.getPageSize());
            }


            // 4. 원심/하급심 결과 목록 조회
            @Override
            @Transactional(readOnly = true)
            public ConflictResponse.ConflictResultList getConflictResultList(String conflictSeq) {
                String officeSeq = SecurityUtil.getOfficeSeq();


                // 1. 매퍼를 통해 리스트 조회
                List<ConflictResultMstVO> resultList = conflictMapper.selectConflictResultList(officeSeq, conflictSeq);

                List<ConflictResponse.ConflictResultDetail> dtoList = resultList.stream()
                        .map(vo -> ConflictResponse.ConflictResultDetail.builder()
                                .conflictSeq(vo.getConflictSeq())
                                .judgmentCaseNo(vo.getJudgmentCaseNo())
                                .judgmentContent(vo.getJudgmentContent())
                                .judgmentSearchUrl(vo.getJudgmentSearchUrl())
                                // 코드 정보 객체 빌드
                                .judgmentCategory(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getJudgmentCategoryCode())
                                        .codeName(vo.getJudgmentCategoryName())
                                        .build())
                                .resultDecisionDate(vo.getResultDecisionDate())
                                .build())
                        .toList();
                // 2. Response DTO 빌드 (내부 클래스 구조에 맞춰 생성)
                return ConflictResponse.ConflictResultList.builder()
                        .conflictResultList(dtoList)
                        .build();
            }
            @Override
            @Transactional(rollbackFor = Exception.class)
            public ConflictResponse.ConflictResultDetail createResultConflict(ConflictRequest.ResultDetail request) {
                String officeSeq = SecurityUtil.getOfficeSeq();
                String loginUser = SecurityUtil.getUserInfoSeq();

                // 1. 통합 MstVO 생성 (Record의 화면 키를 VO 필드에 매핑)
                ConflictResultMstVO mstVo = ConflictResultMstVO.builder()
                        .conflictResultSeq(request.conflictResultSeq()) // Seq 유무에 따라 Update/Insert 결정
                        .officeSeq(officeSeq)
                        .conflictSeq(request.conflictSeq())
                        .judgmentCaseNo(request.resultCaseNo())
                        .judgmentCategoryCode(request.judgmentCategory().code())
                        .judgmentContent(request.resultDecisionResult())
                        .judgmentSearchUrl(request.judgmentSearchUrl())
                        // 화면에서 받은 일자/관계자 데이터를 VO에 세팅
                        .resultDecisionDate(request.resultDecisionDate())
                        .resultRequestDate(request.resultRequestDate())
                        .resultPetitioner(request.resultPetitioner().userSeq())
                        .resultRespondent(request.resultRespondent().userSeq())
                        .note(request.note())
                        .createUser(loginUser)
                        .updateUser(loginUser)
                        .delYn("N")
                        .build();

                // 2. 마스터 데이터 저장 또는 수정
                if (StringUtils.hasText(mstVo.getConflictResultSeq())) {
                    conflictMapper.updateConflictResult(mstVo);
                } else {
                    conflictMapper.insertConflictResult(mstVo);
                }

                String resSeq = mstVo.getConflictResultSeq();

                // 3. VO의 어노테이션을 기반으로 기일/관계자 자동 추출 및 저장
                participantService.saveAllParticipants(DataConvertUtil.extractParticipants(mstVo, resSeq, officeSeq, "CFTRSL"));
                dueDateService.saveAllDueDates(DataConvertUtil.extractDueDates(mstVo, resSeq, officeSeq, "CFTRSL"));

                // 4. 저장/수정된 데이터 다시 조회
                ConflictResultMstVO resultVo = conflictMapper.selectConflictResultDetail(resSeq, officeSeq).orElse(null);

                if (resultVo == null) return null;

                // 5. DTO 변환 후 리턴
                return ConflictResponse.ConflictResultDetail.builder()
                        .conflictResultSeq(resultVo.getConflictResultSeq())
                        .conflictSeq(resultVo.getConflictSeq())
                        .judgmentCaseNo(resultVo.getJudgmentCaseNo())
                        .judgmentContent(resultVo.getJudgmentContent())
                        .judgmentSearchUrl(resultVo.getJudgmentSearchUrl())
                        .judgmentCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(resultVo.getJudgmentCategoryCode())
                                .codeName(resultVo.getJudgmentCategoryName())
                                .build())
                        .resultDecisionDate(resultVo.getResultDecisionDate())
//                        .resultRequestDate(resultVo.getResultRequestDate())
//                        .resultPetitioner(CommonRecordResponse.PersonInfo.builder()
//                                .userSeq(resultVo.getResultPetitioner())
//                                .userName(resultVo.getResultPetitionerName())
//                                .build())
//                        .resultRespondent(CommonRecordResponse.PersonInfo.builder()
//                                .userSeq(resultVo.getResultRespondent())
//                                .userName(resultVo.getResultRespondentName())
//                                .build())
                        .note(resultVo.getNote() != null ? resultVo.getNote() : "")
                        .build();
            }



            @Override
            public ConflictResponse.ConflictEtcCaseDetail getEtcConflictDetail(String conflictSeq) {
                String officeSeq = SecurityUtil.getOfficeSeq();

                // 1. 분쟁/기타사건 마스터 정보 조회
                ConflictMergeVO mergeVO = conflictMapper.findConflictBySeq(conflictSeq, officeSeq);



                // 2. 통합 매핑을 위한 MergeVO 생성 및 마스터 데이터 주입
//                ConflictMergeVO mergeVO = new ConflictMergeVO();
//                mergeVO.fillFromMaster(cftVo); // ConflictMstVO 데이터 평탄화
//
//                // 3. 연결된 출원(App) 상세 정보 조회 (국내등록번호, 결정일 등 포함)
//                // domesticAppMapper 또는 기존 appMapper를 사용하여 AppMstVO를 가져옵니다.
//                domesticAppMapper.getConflictAppDetail(officeSeq, cftVo.getAppSeq())
//                        .ifPresent(mergeVO::fillFromAppBasicInfo); // AppMstVO 데이터 평탄화 (국내등록필드 포함)
//
//                // 4. 기일(DueDate) 및 관계자(Participant) 정보 주입
//                // 공통 유틸리티(DataConvertUtil)를 사용하여 MergeVO의 각 필드에 날짜와 인명 주입
//                DataConvertUtil.injectData(mergeVO, participantService.getParticipantListByWork(conflictSeq, officeSeq), "CFT");
//                DataConvertUtil.injectData(mergeVO, dueDateService.getDueDateListByWork(conflictSeq, officeSeq), "CFT");

                // 5. 첨부파일 목록 조회
                List<PaperResponseVO> fileList = paperService.getFileMappByWork(conflictSeq, officeSeq);

                // 6. 기타사건 전용 Response DTO로 변환하여 반환
                // ConflictResponse.ConflictEtcCaseDetail.from 내부에서 mergeVO.toEtcAppBaseInfo()를 호출함
                return ConflictResponse.ConflictEtcCaseDetail.builder()
                        .conflictSeq(mergeVO.getConflictSeq())
                        // 사건 관리 (JSON의 cftCaseMng 섹션)
                        .cftCaseMng(ConflictResponse.ConflictEtcCftCaseMng.from(mergeVO))
                        // 기타사건 확장 출원 정보 (JSON의 etcAppBaseInfo 섹션 - 국내등록 3종 포함)
                        .appBaseInfo(ConflictResponse.EtcAppBaseInfo.from(mergeVO))
                        // 나머지 공통 섹션들
                        .appTitleInfo(ConflictResponse.ConflictAppTitleInfo.from(mergeVO))
                        .appGoodsInfo(ConflictResponse.ConflictAppGoodsInfo.from(mergeVO))
                        .appPartyInfo(ConflictResponse.ConflictEtcAppPartyInfo.from(mergeVO))
                        .cftLitigantInfo(ConflictResponse.ConflictEtcCftLitigantInfo.from(mergeVO))
                        .cftJudgmentInfo(ConflictResponse.ConflictCftJudgmentInfo.from(mergeVO))
                        .cftManagerInfo(ConflictResponse.ConflictCftManagerInfo.from(mergeVO))
                        .cftNoteInfo(ConflictResponse.ConflictCftNoteInfo.from(mergeVO))
                        // 기타사건 전용 단건 파일 (JSON의 etcConflictFile 섹션)
                        .etcConflictFile(ConflictResponse.ConflictFiles.from(fileList))
                        .build();
            }



            @Override
            @Transactional(readOnly = true)
            public ConflictResponse.ConflictEtcList getEtcConflictList(BaseSearchRequest request) {
                // 1. 사무소 시퀀스 설정 (보안 주입)
                request.setOfficeSeq(SecurityUtil.getOfficeSeq());

                // 2. 검색 조건 리스트 초기화 및 etcYn = 'Y' 강제 주입
                if (request.getSearchCondition() == null) {
                    request.setSearchCondition(new java.util.ArrayList<>());
                }

                List<java.util.Map<String, String>> conditions = request.getSearchCondition();

                // 기존 조건들 중 etcYn이 있는지 확인
                boolean hasEtcYn = conditions.stream()
                        .anyMatch(cond -> "etcYn".equals(cond.get("codeName")));

                if (!hasEtcYn) {
                    // 프론트에서 안 보냈을 경우: 안전하게 추가
                    java.util.Map<String, String> etcCond = new java.util.HashMap<>();
                    etcCond.put("codeName", "etcYn");
                    etcCond.put("codeValue", "Y"); // 기타 사건 리스트는 무조건 'Y'
                    etcCond.put("andOrNOT", "AND");
                    conditions.add(etcCond);
                } else {
                    // 프론트에서 보냈을 경우: 'Y'로 강제 교정 (기존 조건 유지하면서 값만 보정)
                    conditions.stream()
                            .filter(cond -> "etcYn".equals(cond.get("codeName")))
                            .forEach(cond -> cond.put("codeValue", "Y"));
                }

                // 3. 통합 쿼리 호출 (이제 '기존 필터들' + '강제된 etcYn=Y'가 합쳐진 상태)
                List<ConflictMergeVO> dbList = conflictMapper.selectSearchConflictList(request);

                if (ObjectUtils.isEmpty(dbList)) {
                    return ConflictResponse.ConflictEtcList.builder()
                            .etcList(new java.util.ArrayList<>())
                            .totalCount(0)
                            .build();
                }

                // 4. 스트림 변환 (VO -> DTO)
                List<ConflictResponse.ConflictEtcListDetail> detailList = dbList.stream().map(vo ->
                        ConflictResponse.ConflictEtcListDetail.builder()
                                .conflictSeq(vo.getConflictSeq())
                                .ourRef(vo.getOurRef())
                                .yourRef(vo.getYourRef())
                                .caseTitleKo(vo.getCaseTitleKo())
                                .note(vo.getNote())
                                .appClassification(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getAppClassificationCode()).codeName(vo.getAppClassificationCodeName()).build())
                                .status(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getStatusCode()).codeName(vo.getStatusCodeName()).build())
                                .caseType(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getCaseTypeCode()).codeName(vo.getCaseTypeCodeName()).build())
                                .receiptDate(vo.getReceiptDate())
                                .dueLimitDate(vo.getDueLimitDate())
                                .processDate(vo.getProcessDate())
                                .deadlineDate(vo.getDueLimitDate())
                                .finalResult(vo.getFinalResult())
                                .abandonDate(vo.getAbandonDate())
                                .abandonContent(vo.getAbandonContent())
                                .client(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getClient()).userName(vo.getClientName()).build())
                                .respondent(vo.getRespondent())
                                .foreignAgent(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getForeignAgent()).userName(vo.getForeignAgentName()).build())
                                .adminMgr(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrName()).build())
                                .caseMgr(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                                .attorney(CommonRecordResponse.PersonInfo.builder()
                                        .userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())
                                .rightType(CommonRecordResponse.CodeInfo.builder()
                                        .code(vo.getRightTypeCode()).codeName(vo.getRightTypeCodeName()).build())
                                .appNo(vo.getAppNo())
                                .appDate(vo.getAppDate())
                                .regNo(vo.getRegNo())
                                .regDate(vo.getRegDate())
                                .titleKo(vo.getTitleKo())
                                .titleEn(vo.getTitleEn())
                                .build()
                ).toList();

                return ConflictResponse.ConflictEtcList.builder()
                        .etcList(detailList)
                        .totalCount(detailList.size())
                        .build();
            }

            @Override
            @Transactional(rollbackFor = Exception.class)
            public void deleteConflict(String conflictSeq) {
                conflictMapper.deleteConflictMst(conflictSeq, SecurityUtil.getOfficeSeq());
            }

            @Override
            @Transactional(rollbackFor = Exception.class)
            public void deleteConflictFile(String conflictSeq, String fileSeq) {
                String officeSeq = SecurityUtil.getOfficeSeq();
                String loginUser = SecurityUtil.getUserInfoSeq();
                // 이의심판/기타사건 이미지 파일 논리 삭제 (fileSeq만으로 처리 가능)
                conflictMapper.softDeleteConflictFileByFileSeq(officeSeq, fileSeq, loginUser);
            }

        }

