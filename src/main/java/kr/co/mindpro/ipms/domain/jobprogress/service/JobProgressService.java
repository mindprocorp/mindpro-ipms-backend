package kr.co.mindpro.ipms.domain.jobprogress.service;


import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.jobprogress.dto.request.JobProgressRequest;
import kr.co.mindpro.ipms.domain.jobprogress.dto.response.JobProgressResponse;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressMergeVO;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressVO;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Service Interface] 관계자 관리 서비스
 *
 * @author   : min
 * @fileName : ParticipantService.java
 * @since    : 2026. 01. 07.
 */
public interface JobProgressService {
    @Transactional(readOnly = true)
    JobProgressResponse.JobProgressDetail getProgressDetail(String progressSeq);

    BaseSearchResponse<JobProgressResponse.JobProgressDetail> getProgressList(BaseSearchRequest  request);;


    @Transactional(rollbackFor = Exception.class)
    String registerProgressForAi(String systemSeq, String fileSeq, String summary, String docSeq, String officeSeq, String userSeq);

    JobProgressResponse.JobProgressDetail registerProgress(JobProgressRequest.JobProgressDetail vo, List<MultipartFile> targetFiles);

    void softDeleteProgress(String tblSeq, String progSeq);

    void softDeleteProgressByList(String tblSeq, List<String> progSeqList);

    void hardDeleteProgress(String tblSeq, String progSeq);

    void hardDeleteProgressByList(String tblSeq, List<String> progSeqList);
}

