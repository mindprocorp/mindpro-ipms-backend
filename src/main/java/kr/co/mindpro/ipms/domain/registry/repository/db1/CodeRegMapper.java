package kr.co.mindpro.ipms.domain.registry.repository.db1;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.mindpro.ipms.domain.registry.vo.CodeDetailVO;

/**
 * 공통 코드 마스터 및 상세 정보 조회를 위한 매퍼
 */
@Mapper
public interface CodeRegMapper {

    /**
     * 마스터 테이블에 유효한 그룹 코드가 존재하는지 확인
     */
    boolean existsMasterGroup(@Param("grpCd") String grpCd);

    /**
     * 특정 그룹 코드에 속한 활성 상세 코드 목록 조회
     */
    List<CodeDetailVO> findDetailsByGroup(@Param("grpCd") String grpCd);
}
