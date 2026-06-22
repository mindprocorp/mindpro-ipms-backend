package kr.co.mindpro.ipms.domain.organization.repository.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mindpro.ipms.domain.organization.vo.OfficeCodeVO;

@Mapper
public interface DefaultCodeMapper {

    List<OfficeCodeVO> selectAllDefaults();
}
