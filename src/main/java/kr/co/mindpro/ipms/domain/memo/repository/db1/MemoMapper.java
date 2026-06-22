package kr.co.mindpro.ipms.domain.memo.repository.db1;

import kr.co.mindpro.ipms.domain.memo.dto.response.MemoResponse;
import kr.co.mindpro.ipms.domain.memo.vo.MemoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : DuedateMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface MemoMapper {
    /**
     * [조회] 업무별 전체 메모 리스트 조회
     * @param tblSeq 업무 일련번호
     * @param officeSeq 사무소 일련번호
     */
    List<MemoResponse.MemoDetail> findAllByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    MemoResponse.MemoDetail findById(@Param("memoSeq") String memoSeq, @Param("officeSeq") String officeSeq);

    /**
     * [삭제 1] 메모 마스터(Mst) 논리 삭제
     */
    int softDeleteMstByWork(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq,
            @Param("userSeq") String userSeq,
            @Param("memoSeq") String memoSeq);

    /**
     * [삭제 2] 메모 매핑(Mapp) 논리 삭제
     */
    int softDeleteMappByWork(@Param("tblSeq") String tblSeq,
                         @Param("officeSeq") String officeSeq,
                         @Param("userSeq") String userSeq,
                         @Param("memoSeq") String memoSeq);

    /**
     * [삭제 1] 메모 리스트 마스터(Mst) 논리 삭제
     */
    int softDeleteMstByWorkList(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq,
            @Param("userSeq") String userSeq,
            @Param("memoSeqList") List<String> memoSeqList);

    /**
     * [삭제 2] 메모 리스트 매핑(Mapp) 논리 삭제
     */
    int softDeleteMappByWorkList(@Param("tblSeq") String tblSeq,
                             @Param("officeSeq") String officeSeq,
                             @Param("userSeq") String userSeq,
                             @Param("memoSeqList") List<String> memoSeqList);

    /**
     * [삭제 1] 메모 마스터(Mst) 물리 삭제
     */
    int hardDeleteMstByWork(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq,
            @Param("memoSeq") String memoSeq);

    /**
     * [삭제 2] 메모 매핑(Mapp) 물리 삭제
     */
    int hardDeleteMappByWork(@Param("tblSeq") String tblSeq,
                             @Param("officeSeq") String officeSeq,
                             @Param("memoSeq") String memoSeq);

    /**
     * [삭제 1] 메모 리스트 마스터(Mst) 물리 삭제
     */
    int hardDeleteMstByWorkList(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq,
            @Param("memoSeqList") List<String> memoSeqList);

    /**
     * [삭제 2] 메모 리스트 매핑(Mapp) 물리 삭제
     */
    int hardDeleteMappByWorkList(@Param("tblSeq") String tblSeq,
                                 @Param("officeSeq") String officeSeq,
                                 @Param("memoSeqList") List<String> memoSeqList);

    /**
     * [저장] 메모 통합 등록 (Mst + Mapp 일괄 처리)
     * XML의 selectKey를 통해 memoSeq가 채워집니다.
     */
    int insertMemo(MemoVO vo);

    /**
     * [검색] 다양한 조건으로 메모 조회
     */
    List<MemoVO> findMemoList(MemoVO searchVO);

    /**
     * [조회] 요청에 memoSeq 가 있을 경우 이미 저장되어있는 seq 인지확인
     * */
    int getDuplicateMemo(MemoVO vo);

    int updateMemo(MemoVO vo);
}