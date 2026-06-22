package kr.co.mindpro.ipms.domain.requiredDoc.repository.db1;

import kr.co.mindpro.ipms.domain.requiredDoc.vo.RequiredDocVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RequiredDocMapper.java
 * @since : 2026. 4. 1.
 */
@Mapper
public interface RequiredDocMapper {

    int getDuplicateRequiredDoc(RequiredDocVO requiredDocVO);

    int insertRequiredDoc(RequiredDocVO requiredDocVO);

    int updateRequiredDoc(RequiredDocVO requiredDocVO);

    RequiredDocVO getRequiredDocDetail(@Param("officeSeq") String officeSeq,
                                       @Param("requiredDocSeq") String requiredDocSeq,
                                       @Param("appSeq") String appSeq,
                                       @Param("userSeq") String userSeq
    );

    List<RequiredDocVO> getRequiredDocListByAppSeq(@Param("appSeq") String appSeq, @Param("officeSeq") String officeSeq);

    int getRequiredDocListCnt(@Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq);

    int softDeleteRequiredDoc(@Param("appSeq") String appSeq, @Param("requiredDocSeq") String requiredDocSeq, @Param("officeSeq") String officeSeq, @Param("userSeq") String userSeq);

    /**
     * [삭제] 구비서류 다건 논리 삭제
     */
    int softDeleteRequiredDocList(@Param("appSeq") String appSeq,
                                  @Param("requiredDocSeqList") List<String> requiredDocSeqList,
                                  @Param("officeSeq") String officeSeq,
                                  @Param("userSeq") String userSeq);

    /**
     * [삭제] 구비서류 단건 물리 삭제
     */
    int hardDeleteRequiredDoc(@Param("appSeq") String appSeq,
                              @Param("requiredDocSeq") String requiredDocSeq,
                              @Param("officeSeq") String officeSeq);

    /**
     * [삭제] 구비서류 다건 물리 삭제
     */
    int hardDeleteRequiredDocList(@Param("appSeq") String appSeq,
                                  @Param("requiredDocSeqList") List<String> requiredDocSeqList,
                                  @Param("officeSeq") String officeSeq);
}
