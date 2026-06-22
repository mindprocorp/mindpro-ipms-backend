package kr.co.mindpro.ipms.domain.example.repository.db1;

import kr.co.mindpro.ipms.domain.example.dto.response.ExampleList;
import kr.co.mindpro.ipms.domain.example.vo.ExampleMstVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 이의심판 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : OppoMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface ExampleMapper {

    void insertConflictMst(ExampleMstVO build);

    ExampleMstVO findConflictBySeq(String conflictSeq);

    List<ExampleList> findConflictMstList();
}