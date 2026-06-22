package kr.co.mindpro.ipms.domain.paper.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.paper.dto.request.PaperRequest;
import kr.co.mindpro.ipms.domain.paper.dto.response.PaperResponse;
import kr.co.mindpro.ipms.domain.paper.vo.PaperDossierVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : PaperService.java
 * @since    : 2026. 01. 07.
 */
public interface PaperService {
    /** * [저장] 파일 매핑 리스트 일괄 저장
     * - 기존 업무 시퀀스(tblSeq)에 연결된 데이터를 삭제(Soft Delete) 후
     * - 전달받은 리스트를 순회하며 새롭게 매핑 정보를 등록합니다.
     */
    void saveAllFileMappings(List<PaperRequestVO> mappingList);

    FileResponse saveFileMapping(PaperRequestVO vo);

    void registerDossier(PaperRequest.DossierRequest request, List<MultipartFile> files);

    /** * [조회] 특정 업무에 연결된 모든 활성 파일 매핑 리스트 조회
     * @param tblSeq 업무 시퀀스 (예: CFT2026...)
     * @param officeSeq 사무소 시퀀스
     */
    List<PaperResponseVO> getFileMappByWork(String tblSeq, String officeSeq);

    /** * [조회] 특정 업무에 연결된 모든 활성 파일 매핑 리스트 조회
     * @param tblSeq 업무 시퀀스 (예: CFT2026...)
     * @param officeSeq 사무소 시퀀스
     */
    BaseSearchResponse<PaperResponse.DossierDetailResponse> getDossierByWork(String tblSeq, String officeSeq);

    /**
     * [조회] 전자포대 단건 조회
     * */
    PaperResponse.DossierDetailResponse getDossierDetail(String tblSeq, String fileMappSeq);

    /** * [조회] 파일 매핑 리스트 검색 (필터링 조건부 조회)
     * - 사무소별 또는 특정 파일 종류별 목록이 필요할 때 사용합니다.
     */
    List<PaperRequestVO> getFileMappList(PaperRequestVO searchVO);

    /** * [저장] 단건 파일 매핑 등록 (교체 모드)
     * - 동일한 파일 종류가 있을 경우 기존 것을 삭제하고 새 정보를 등록합니다.
     */
    void registerFileMapping(PaperRequestVO vo);

    /**
     * [조회] 전자포대관리 리스트 검색
     * */
    BaseSearchResponse<PaperResponse.DossierArchiveListResponse> getDossierArchiveList(BaseSearchRequest request);

    void softDeleteDossier(String appSeq, String fileMappSeq);

    /**
     * [삭제] 전자포대 다건 논리 삭제
     */
    void softDeleteDossierByList(String tblSeq, List<String> fileMappSeqList);

    /**
     * [삭제] 전자포대 다건 논리 삭제
     */
    void hardDeleteDossier(String tblSeq, String fileMappSeq);

    /**
     * [삭제] 전자포대 다건 물리 삭제
     */
    void hardDeleteDossierByList(String tblSeq, List<String> fileMappSeqList);

    @Transactional
    void linkFileToWork(String tblSeq, String fileSeq, String officeSeq, String userSeq, String docSeq);

    void softDeleteFilesByTblSeq(String tblSeq, String fileKindCode);

    void softDeleteFilesByFileSeqList(String tblSeq, List<String> fileSeqList);
}

