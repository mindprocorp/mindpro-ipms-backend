package kr.co.mindpro.ipms.domain.dispatch.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.dispatch.vo.DispatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DispatchMapper {
    List<DispatchVO> findList(@Param("request") BaseSearchRequest request);
    long countList(@Param("request") BaseSearchRequest request);
    void insert(DispatchVO vo);
    void update(DispatchVO vo);
    void delete(@Param("dispatchSeq") String dispatchSeq, @Param("userSeq") String userSeq);
}
