package kr.co.mindpro.ipms.domain.jobprogress.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressMergeVO;
import kr.co.mindpro.ipms.domain.jobprogress.vo.JobProgressVO;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : PaperMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface JobProgressMapper {

    /**
     * [목록 조회] 뷰 조인 및 공통 검색 반영
     * 파라미터로 BaseSearchRequest를 전달하여 XML 내부의 <include> 및 페이징 변수 활용
     */
    List<JobProgressMergeVO> findProgressList(@Param("request")BaseSearchRequest request);
    int totalCount(@Param("request")BaseSearchRequest request);
    void insertProgress(JobProgressVO vo);

    int getDuplicateProgressCnt(String progressSeq);

    int updateProgressByProgSeq(JobProgressVO vo);

    int softDeleteProgress(@Param("officeSeq") String officeSeq,
                           @Param("tblSeq") String tblSeq,
                           @Param("progSeq") String progSeq,
                           @Param("loginUser") String loginUser);

    int softDeleteProgressByList(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("progSeqList") List<String> progSeqList,
            @Param("loginUser") String loginUser);

    int hardDeleteProgress(@Param("officeSeq") String officeSeq,
                           @Param("tblSeq") String tblSeq,
                           @Param("progSeq") String progSeq
                           );

    int hardDeleteProgressByList(
            @Param("officeSeq") String officeSeq,
            @Param("tblSeq") String tblSeq,
            @Param("progSeqList") List<String> progSeqList
            );

    JobProgressMergeVO findProgressDetail(String progressSeq, String officeSeq);
}