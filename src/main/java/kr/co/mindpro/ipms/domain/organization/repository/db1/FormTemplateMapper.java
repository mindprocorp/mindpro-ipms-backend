package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateVO;

@Mapper
public interface FormTemplateMapper {

    List<FormTemplateVO> selectTemplateList(@Param("officeSeq") String officeSeq,
                                            @Param("categoryCode") String categoryCode,
                                            @Param("templateName") String templateName,
                                            @Param("userMstSeq") String userMstSeq);

    Optional<FormTemplateVO> selectTemplateBySeq(@Param("formTemplateSeq") String formTemplateSeq);

    int insertTemplate(FormTemplateVO vo);

    int updateTemplate(FormTemplateVO vo);

    int deleteTemplate(@Param("formTemplateSeq") String formTemplateSeq);
}
