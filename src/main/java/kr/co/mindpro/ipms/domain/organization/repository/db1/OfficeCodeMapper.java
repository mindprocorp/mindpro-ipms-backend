package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.organization.vo.OfficeCodeVO;

@Mapper
public interface OfficeCodeMapper {

    List<OfficeCodeVO> selectByClassAndOffice(@Param("codeClass") String codeClass,
                                              @Param("officeSeq") String officeSeq);

    int insertOfficeCode(OfficeCodeVO vo);

    int updateOfficeCode(OfficeCodeVO vo);

    int deleteOfficeCode(@Param("officeCodeSeq") String officeCodeSeq);
}
