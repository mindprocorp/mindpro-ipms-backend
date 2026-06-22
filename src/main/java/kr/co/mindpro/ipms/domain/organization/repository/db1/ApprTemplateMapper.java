package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateLineVO;

@Mapper
public interface ApprTemplateMapper {

    List<ApprTemplateVO> selectTemplateList(@Param("officeSeq") String officeSeq,
                                            @Param("templateName") String templateName);

    Optional<ApprTemplateVO> selectTemplateBySeq(@Param("apprTemplateSeq") String apprTemplateSeq);

    List<ApprTemplateLineVO> selectTemplateLines(@Param("apprTemplateSeq") String apprTemplateSeq);

    int insertTemplate(ApprTemplateVO vo);

    int updateTemplate(ApprTemplateVO vo);

    int deleteTemplate(@Param("apprTemplateSeq") String apprTemplateSeq);

    int insertTemplateLine(ApprTemplateLineVO vo);

    int deleteTemplateLines(@Param("apprTemplateSeq") String apprTemplateSeq);
}
