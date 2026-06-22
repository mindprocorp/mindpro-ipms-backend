package kr.co.mindpro.ipms.domain.rnd.repository.db1;

import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RndMapper.java
 * @since : 2026. 2. 5.
 */
@Mapper
public interface RndMapper {

    /** [저장] 연구과제 tbl 등록 */
    int insertRnd(RndVO vo);

    List<RndVO> getRndList(String officeSeq, String appSeq);

    RndVO getRndDetail(String officeSeq, String appSeq, String rndSeq);

    int getRndCount(String officeSeq, String appSeq);

    int getDuplicateRndCnt(RndVO vo);

    int updateRnd(RndVO vo);

    int softDeleteRnd(String officeSeq, String appSeq, String rndSeq, String userSeq);

    /**
     * [삭제] 연구과제 다건 논리 삭제
     */
    int softDeleteRndList(@Param("officeSeq") String officeSeq,
                          @Param("appSeq") String appSeq,
                          @Param("rndSeqList") List<String> rndSeqList,
                          @Param("userSeq") String userSeq);

    /**
     * [삭제] 연구과제 단건 물리 삭제
     */
    int hardDeleteRnd(@Param("officeSeq") String officeSeq,
                      @Param("appSeq") String appSeq,
                      @Param("rndSeq") String rndSeq);

    /**
     * [삭제] 연구과제 다건 물리 삭제
     */
    int hardDeleteRndList(@Param("officeSeq") String officeSeq,
                          @Param("appSeq") String appSeq,
                          @Param("rndSeqList") List<String> rndSeqList);
}
