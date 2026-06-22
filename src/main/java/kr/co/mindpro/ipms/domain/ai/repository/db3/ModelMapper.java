package kr.co.mindpro.ipms.domain.ai.repository.db3;

import kr.co.mindpro.ipms.domain.ai.vo.AiConnectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ModelMapper {
    Optional<AiConnectVo> findByConnectionSeq(@Param("connectionSeq") Long connectionSeq, @Param("userMstSeq") String userMstSeq);
    List<AiConnectVo> findAllByUserMstSeq(@Param("userMstSeq") String userMstSeq);
    int insertModel(AiConnectVo vo);
    int updateModel(AiConnectVo vo);
    int deleteModel(@Param("connectionSeq") Long connectionSeq, @Param("userMstSeq") String userMstSeq, @Param("updateUser") String updateUser);
}
