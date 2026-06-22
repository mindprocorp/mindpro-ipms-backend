package kr.co.mindpro.ipms.domain.patentApp.appCommon.repository.db1;

import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.CommonAppVO;

/**
 * @author : seokho
 * @fileName : AppCommonMapper.java
 * @since : 2026. 3. 23.
 */
import org.apache.ibatis.annotations.Param;

public interface AppCommonMapper {

    /**
     * 출원키 중복체크.
     */
    int checkDuplicateSeq(@Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq);

    /**
     * 출원키를 통한 국내/해외출원 삭제(물리적)
     * */
    int hardDeleteAppCommon(@Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq);

    /**
     * 출원키를 통한 국내/해외출원 삭제(논리적)
     * */
    int softDeleteAppCommon(@Param("officeSeq") String officeSeq, @Param("appSeq") String appSeq, @Param("userSeq") String userSeq);

    int insertAppHistory(CommonAppVO mstVO);
}
