package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateTargetVO;

@Mapper
public interface FormTemplateTargetMapper {

    List<FormTemplateTargetVO> selectTargetsByTemplateSeq(@Param("formTemplateSeq") String formTemplateSeq);

    int insertTarget(FormTemplateTargetVO vo);

    int deleteTargetsByTemplateSeq(@Param("formTemplateSeq") String formTemplateSeq);
}
