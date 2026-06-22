package kr.co.mindpro.ipms.domain.gracePeriod.repository.db1;

import kr.co.mindpro.ipms.domain.gracePeriod.vo.GracePeriodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author : seokho
 * @fileName : GracePeriodMapper.java
 * @since : 2026. 2. 3.
 */
@Mapper
public interface GracePeriodMapper {

    /** [저장] 공지예외 단건 tbl 등록 */
    int insertGracePeriod(GracePeriodVO vo);

    /** [조회] 출원에 연결된 공지예외 리스트 조회 */
    List<GracePeriodVO> findAllByWork(String officeSeq, String appSeq);

    GracePeriodVO getGracePeriodDetail(String officeSeq, String appSeq, String gracePeriodSeq);

    int getDuplicateGracePeriodCnt(GracePeriodVO vo);

    int updateGracePeriod(GracePeriodVO vo);

    int softDeleteGracePeriod(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("gracePeriodSeq") String gracePeriodSeq,
            @Param("userSeq") String userSeq);

    int softDeleteGracePeriodByList(
            @Param("officeSeq") String officeSeq,
            @Param("userSeq") String userSeq,
            @Param("appSeq") String appSeq,
            @Param("gracePeriodSeqList") List<String> gracePeriodSeqList
    );

    int hardDeleteGracePeriod(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("gracePeriodSeq") String gracePeriodSeq);

    int hardDeleteGracePeriodByList(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("gracePeriodSeqList") List<String> gracePeriodSeqList
    );
}
