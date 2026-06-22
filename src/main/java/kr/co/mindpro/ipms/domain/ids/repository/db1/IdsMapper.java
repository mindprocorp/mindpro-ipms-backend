package kr.co.mindpro.ipms.domain.ids.repository.db1;

import kr.co.mindpro.ipms.domain.ids.vo.IdsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : seokho
 * @fileName : IdsMapper.java
 * @since : 2026. 3. 12.
 */
@Mapper
public interface IdsMapper {

    /** [저장] IDS 단건 등록 */
    int insertIds(IdsVO vo);

    /** [조회] 출원에 연결된 IDS 리스트 조회 */
    List<IdsVO> getIdsList(String officeSeq, String appSeq);

    IdsVO getIdsDetail(String officeSeq, String appSeq, String idsSeq);

    int getDuplicateIdsSeqCnt(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("idsSeq") String idsSeq
    );

    int updateIds(IdsVO vo);

    int softDeleteByIdsSeq(@Param("officeSeq") String officeSeq,
                           @Param("appSeq") String appSeq,
                           @Param("idsSeq") String idsSeq,
                           @Param("userSeq") String userSeq
    );

    int softDeleteByIdsSeqList(
            @Param("officeSeq") String officeSeq,
            @Param("userSeq") String userSeq,
            @Param("appSeq") String appSeq,
            @Param("idsSeqList") List<String> idsSeqList
    );

    int hardDeleteByIdsSeq(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("idsSeq") String idsSeq
    );

    int hardDeleteByIdsSeqList(
            @Param("officeSeq") String officeSeq,
            @Param("appSeq") String appSeq,
            @Param("idsSeqList") List<String> idsSeqList
    );
}
