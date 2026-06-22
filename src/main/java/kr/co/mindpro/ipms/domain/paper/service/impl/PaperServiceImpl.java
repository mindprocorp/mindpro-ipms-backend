package kr.co.mindpro.ipms.domain.paper.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.file.dto.request.FileRequest;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.file.service.FileService;
import kr.co.mindpro.ipms.common.util.SecurityUtil;

import kr.co.mindpro.ipms.domain.paper.dto.request.PaperRequest;
import kr.co.mindpro.ipms.domain.paper.dto.response.PaperResponse;
import kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.*;

/**
 * 청구서 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : PaperServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {
    private final PaperMapper paperMapper;
    private final FileService fileService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileResponse saveFileMapping(PaperRequestVO vo) {
        if (vo == null) return null;
        FileResponse res = null;

        vo.setOfficeSeq(StringUtils.hasText(vo.getOfficeSeq()) ? vo.getOfficeSeq() : SecurityUtil.getOfficeSeq());
        vo.setCreateUser(StringUtils.hasText(vo.getCreateUser()) ? vo.getCreateUser() : SecurityUtil.getUserInfoSeq());
        vo.setUpdateUser(StringUtils.hasText(vo.getUpdateUser()) ? vo.getUpdateUser() : SecurityUtil.getUserInfoSeq());

        String finalDocSeq = StringUtils.hasText(vo.getDocSeq()) ? vo.getDocSeq()
                : StringUtils.hasText(vo.getReceiptDocSeq()) ? vo.getReceiptDocSeq()
                : StringUtils.hasText(vo.getSubmitDocSeq()) ?vo.getSubmitDocSeq()
                : "99";  //기타파일

        // 1. 물리 파일이 있는 경우 S3 업로드 및 file_mst 시퀀스 획득
        if (vo.getFile() != null && !vo.getFile().isEmpty()) {
            FileRequest uploadReq = FileRequest.builder()
                    .note(vo.getNote())
                    .docSeq(Long.parseLong(finalDocSeq))
                    .build();

            // 3단 처리로 확정된 vo.getCreateUser()를 전달
            res = fileService.uploadFile(vo.getFile(), uploadReq, vo.getCreateUser());
            vo.setFileSeq(res.fileSeq());


        }

        // 2. 처리할 대상 시퀀스 수집 (docSeq, receipt, submit 중 존재하는 것들)
        List<String> targetDocSeqs = Stream.of(
                vo.getDocSeq(),
                vo.getReceiptDocSeq(),
                vo.getSubmitDocSeq()
        ).filter(StringUtils::hasText).toList();

        // 3. 루프 돌며 삭제 후 인서트
        if (vo.getFileSeq() != null) {
            for (String targetSeq : targetDocSeqs) {
                vo.setDocSeq(targetSeq); // 현재 루프의 시퀀스로 세팅
                paperMapper.deleteExistingMapping(vo);
                paperMapper.insert(vo);
            }
        }


        return res;
    }

    // 전자포대 등록을 위한 VO 만드는 메소드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerDossier(PaperRequest.DossierRequest request, List<MultipartFile> files) {

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String fileMappSeq = request.fileMappSeq();

        // 삭제 요청 처리
        if (request.deletedFileSeqList() != null && !request.deletedFileSeqList().isEmpty()) {
            this.softDeleteFilesByFileSeqList(request.tblSeq(), request.deletedFileSeqList());
        }

        int result = 0;

        PaperRequestVO paperVO = PaperRequestVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(request.tblSeq())
                .docSeq(StringUtils.hasText(request.docSeq()) ? request.docSeq() : "393")
                // todo 임시로 해당 컬럼 사용. ATTACH_DOC_DIV 10: 출원서류, 20: 중간서류, 30: 등록서류, 40: 고객서류, 99: 기타서류
                .fileKindCode(request.attachDocDiv())
                .note(request.summary())
                .inputCreateAt(parseToOffsetDateTime(request.inputCreateAt()))

                .build();

        // 중복된 file_mapp_seq 를 체크하고 중복된 seq 가 있으면 업데이트 로직. 없으면 인서트 로직.
        if (StringUtils.hasText(fileMappSeq)) {
            paperVO.setFileMappSeq(fileMappSeq);

            result = paperMapper.getDuplicateDossierCnt(paperVO);
        }

        if (result > 0) {
            paperVO.setUpdateUser(userSeq);

            // 1. 메타데이터 업데이트 (날짜, 서류구분, 요약 등) - 그룹 전체 업데이트
            paperMapper.updateDossier(paperVO);

            // 2. 파일 삭제 처리
            if (request.deletedFileSeqList() != null && !request.deletedFileSeqList().isEmpty()) {
                paperMapper.softDeleteFilesByFileSeqList(officeSeq, paperVO.getTblSeq(), request.deletedFileSeqList(), userSeq);
            }

            // 3. 신규 파일이 있는 경우 처리 (기존 로직 유지)
            List<MultipartFile> validFiles = (files == null) ? List.of() :
                    files.stream().filter(f -> f != null && !f.isEmpty()).toList();

            if (!validFiles.isEmpty()) {
                FileRequest uploadReq = FileRequest.builder()
                        .note(paperVO.getNote())
                        .docSeq(Long.parseLong(paperVO.getDocSeq()))
                        .build();

                for (MultipartFile file : validFiles) {
                    FileResponse uploadRes = fileService.uploadFile(file, uploadReq, userSeq);
                    
                    // 각 파일마다 새로운 레코드로 추가 (Append-only)
                    // pk_utb_file_mapp 제약조건 준수를 위해 각 파일마다 새로운 fileMappSeq 생성
                    PaperRequestVO newRow = PaperRequestVO.builder()
                            .officeSeq(officeSeq)
                            .tblSeq(paperVO.getTblSeq())
                            .docSeq(paperVO.getDocSeq())
                            .fileKindCode(paperVO.getFileKindCode())
                            .note(paperVO.getNote())
                            .inputCreateAt(paperVO.getInputCreateAt())
                            .createUser(userSeq)
                            .fileMappSeq(null) // Mapper에서 자동 생성하도록 null 설정
                            .fileSeq(uploadRes.fileSeq())
                            .build();
                    paperMapper.insert(newRow);
                }
            }
        } else {
            List<MultipartFile> validFiles = (files == null) ? List.of() :
                    files.stream().filter(f -> f != null && !f.isEmpty()).toList();

            FileRequest uploadReq = FileRequest.builder()
                    .note(paperVO.getNote())
                    .docSeq(Long.parseLong(paperVO.getDocSeq()))
                    .build();

            if (!validFiles.isEmpty()) {
                // 파일 업로드 후 매핑 등록
                for (MultipartFile file : validFiles) {
                    FileResponse uploadRes = fileService.uploadFile(file, uploadReq, userSeq);
                    
                    // 각 파일마다 새로운 객체와 고유한 file_mapp_seq 생성 보장
                    PaperRequestVO newRow = PaperRequestVO.builder()
                            .officeSeq(officeSeq)
                            .tblSeq(paperVO.getTblSeq())
                            .docSeq(paperVO.getDocSeq())
                            .fileKindCode(paperVO.getFileKindCode())
                            .note(paperVO.getNote())
                            .inputCreateAt(paperVO.getInputCreateAt())
                            .createUser(userSeq)
                            .fileMappSeq(null) // Mapper의 selectKey가 새로운 ID 생성
                            .fileSeq(uploadRes.fileSeq())
                            .build();
                    paperMapper.insert(newRow);
                }
            } else {
                // 파일 없이 메타데이터만 등록
                paperVO.setCreateUser(userSeq);
                paperVO.setFileMappSeq(null);
                paperMapper.insert(paperVO);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllFileMappings(List<PaperRequestVO> mappingList) {
        if (mappingList == null || mappingList.isEmpty()) return;

        for (PaperRequestVO vo : mappingList) {
            this.saveFileMapping(vo);
        }
        //log.info("업무 파일 매핑 일괄 저장 완료: tblSeq={}, count={}", tblSeq, mappingList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperResponseVO> getFileMappByWork(String tblSeq, String officeSeq) {
        return paperMapper.findAllByWork(tblSeq, officeSeq);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<PaperResponse.DossierDetailResponse> getDossierByWork(String tblSeq, String officeSeq) {

        List<PaperDossierVO> list = paperMapper.findAllByWorkDossier(tblSeq, officeSeq);

        List<PaperResponse.DossierDetailResponse> resList = list.stream()
                .map(vo ->
                        PaperResponse.DossierDetailResponse.builder()
                                .fileMappSeq(vo.getFileMappSeq())
                                .tblSeq(vo.getTblSeq())
                                .docSeq(vo.getDocSeq())
                                .docName(vo.getDocName())
                                .uploadAt(formatMinusHoursString8(vo.getCreateAt()))
                                .inputCreateAt(formatMinusHoursString8(vo.getInputCreateAt()))
                                .fileKindCode(vo.getFileKindCode())
                                .fileKindName(vo.getFileKindName())
                                .fileName(vo.getFileName())
                                .fileSize(vo.getFileDisplaySize())
                                // todo 일단 미리보기 속성에 다운로드 url 넣어놓음.
                                .fileViewUrl(vo.getDownloadUrl())
                                .fileDownloadUrl(vo.getDownloadUrl())
                                .summary(vo.getNote())
                                .uploadUser(vo.getUserNameKo())
                                .docCode(vo.getFileSeq())
                                .fileSeqs(vo.getFileSeqs())
                                .build()

                ).toList();

        return BaseSearchResponse.of(resList, 1, 99);
    }

    @Override
    public PaperResponse.DossierDetailResponse getDossierDetail(String tblSeq, String fileMappSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<PaperDossierVO> voList = paperMapper.getDossierDetail(officeSeq, tblSeq, fileMappSeq);

        if (voList == null || voList.isEmpty()) {
            throw new RuntimeException("존재하지 않는 전자포대 데이터입니다.");
        }

        PaperDossierVO vo = voList.get(0); // 공통 메타데이터는 첫 번째 데이터에서 추출

        List<PaperResponse.DossierFileItem> fileItems = voList.stream()
                .filter(item -> item.getFileName() != null) // 파일명이 있는 것만 (유령 파일 필터링)
                .map(item -> PaperResponse.DossierFileItem.builder()
                        .fileSeq(item.getFileSeq())
                        .fileName(item.getFileName())
                        .fileSize(item.getFileDisplaySize())
                        .fileViewUrl(item.getDownloadUrl())
                        .fileDownloadUrl(item.getDownloadUrl())
                        .build())
                .toList();

        return PaperResponse.DossierDetailResponse.builder()
                .fileMappSeq(vo.getFileMappSeq())
                .tblSeq(vo.getTblSeq())
                .docSeq(vo.getDocSeq())
                .docName(vo.getDocName())
                .uploadAt(formatMinusHoursString8(vo.getCreateAt()))
                .inputCreateAt(formatMinusHoursString8(vo.getInputCreateAt()))
                .fileKindCode(vo.getFileKindCode())
                .fileKindName(vo.getFileKindName())
                .fileName(vo.getFileName())
                .fileSize(vo.getFileDisplaySize())
                .fileViewUrl(vo.getDownloadUrl())
                .fileDownloadUrl(vo.getDownloadUrl())
                .summary(vo.getNote())
                .uploadUser(vo.getUserNameKo())
                .docCode(vo.getFileSeq())
                .files(fileItems)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperRequestVO> getFileMappList(PaperRequestVO searchVO) {
        // 사무소별 리스트 조회가 기본이므로 officeSeq 필수 체크
        return paperMapper.findListByOfficeSeq(searchVO.getOfficeSeq());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerFileMapping(PaperRequestVO vo) {
        // 단건 등록 시에도 기존 동일 종류 파일이 있다면 삭제 처리 후 등록 (교체 로직)
        if (vo.getCreateUser() == null) vo.setCreateUser(SecurityUtil.getUserInfoSeq());

        paperMapper.deleteExistingMapping(vo);
        paperMapper.insert(vo);
    }

    @Override
    public BaseSearchResponse<PaperResponse.DossierArchiveListResponse> getDossierArchiveList(BaseSearchRequest request) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        request.setOfficeSeq(officeSeq);

        int totalCount = paperMapper.CntDossierArchiveByOffice(request);

        List<PaperDossierArchiveVO> listVO = paperMapper.findDossierArchiveByOffice(request);

        List<PaperResponse.DossierArchiveListResponse> resList = listVO.stream()
                .map(vo ->
                            PaperResponse.DossierArchiveListResponse.builder()
                                    .parentSeq(vo.getParentSeq())
                                    .tblSeq(vo.getTblSeq())
                                    .docInfo(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getDocSeq(),
                                                    vo.getDocName()
                                            )
                                    )
                                    .caseClassification(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getCaseClassificationCode(),
                                                    vo.getCaseClassificationName()
                                            )
                                    )
                                    .caseCategory(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getCaseCategoryCode(),
                                                    vo.getCaseCategoryName()
                                            )
                                    )
                                    .rightType(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getRightCategoryCode(),
                                                    vo.getRightCategoryName()
                                            )
                                    )
                                    .ourRef(vo.getAssetNo())
                                    .appNo(vo.getAppNo())
                                    .regNo(vo.getRegNo())
                                    .uploadAt(formatOffsetDateTime(vo.getUploadDate()))
                                    .fileKind(
                                            new CommonRecordResponse.CodeInfo(
                                                    vo.getFileKindCode(),
                                                    vo.getFileKindName()
                                            )
                                    )
                                    .attachDocName(vo.getFileOriginalName())
                                    .fileSize(vo.getFileDisplaySize())
                                    .fileViewUrl(vo.getDownloadUrl())
                                    .fileDownloadUrl(vo.getDownloadUrl())
                                    .summary(vo.getNote())
                                    .uploadUser(vo.getUploadUserName())
                                    .build()
                    ).toList();

        return BaseSearchResponse.of(resList, totalCount, 1, 99);
    }

    @Override
    public void softDeleteDossier(String appSeq, String fileMappSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = paperMapper.softDeleteDossier(officeSeq, appSeq, fileMappSeq, userSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to soft delete dossier");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteDossierByList(String tblSeq, List<String> fileMappSeqList) {
        if (fileMappSeqList == null || fileMappSeqList.isEmpty()) {
            throw new RuntimeException("fileMappSeqList is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = paperMapper.softDeleteDossierList(officeSeq, tblSeq, fileMappSeqList, userSeq);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (result <= 0) {
            throw new RuntimeException("Failed to soft delete dossier list");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteDossier(String tblSeq, String fileMappSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = paperMapper.hardDeleteDossier(officeSeq, tblSeq, fileMappSeq);

        if (result <= 0) {
            throw new RuntimeException("Failed to hard delete dossier");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteDossierByList(String tblSeq, List<String> fileMappSeqList) {
        if (fileMappSeqList == null || fileMappSeqList.isEmpty()) {
            throw new RuntimeException("fileMappSeqList is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = paperMapper.hardDeleteDossierList(officeSeq, tblSeq, fileMappSeqList);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (result <= 0) {
            throw new RuntimeException("Failed to hard delete dossier list");
        }
    }

    @Override
    @Transactional
    public void linkFileToWork(String tblSeq, String fileSeq, String officeSeq, String userSeq, String docSeq) {
        // 1. PaperMstVO 대신 PaperRequestVO로 생성 (SuperBuilder 덕분에 필드는 똑같이 쓸 수 있습니다)
        PaperRequestVO mappVo = PaperRequestVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(tblSeq)             // 신규 진행사항 PK
                .tblCode("PROGRS")
                .fileSeq(fileSeq)           // S3 파일 PK
                .docSeq(docSeq)             // 문서유형
                .fileKindCode("10")         // 10: 접수
                .fileTypeCode("10")
                .fileCategoryCode("AI_REG")
                .note("AI 에이전트 자동 등록 첨부파일")
                .build();

        // BaseVO 필드 세팅
        mappVo.setCreateUser(userSeq);
        mappVo.setUpdateUser(userSeq);
        mappVo.setDelYn("N");

        // 2. 이제 Mapper 호출 시 타입 에러가 나지 않습니다.
        // Mapper Interface가 PaperRequestVO를 받도록 설계되어 있어도 정상 작동합니다.
        paperMapper.insert(mappVo);

        log.info(">>>> [빌드 성공] 타입 일치 완료: Work={} <-> File={}", tblSeq, fileSeq);
    }

    @Override
    public void softDeleteFilesByTblSeq(String tblSeq, String fileKindCode) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        paperMapper.softDeleteFilesByTblSeq(officeSeq, tblSeq, fileKindCode, loginUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteFilesByFileSeqList(String tblSeq, List<String> fileSeqList) {
        if (fileSeqList == null || fileSeqList.isEmpty()) return;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        paperMapper.softDeleteFilesByFileSeqList(officeSeq, tblSeq, fileSeqList, loginUser);
    }

}
