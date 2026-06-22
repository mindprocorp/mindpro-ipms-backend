package kr.co.mindpro.ipms.domain.searchcondition.repository.db1;

import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchConditionVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchFieldVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : SearchConditionMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface SearchConditionMapper {
    /**
     * 검색 조건 저장 (selectKey 사용)
     */
    int insertCondition(SearchConditionVO vo);

    /**
     * 사용자별/메뉴별 검색 조건 목록 조회
     */
    List<SearchConditionVO> selectConditionList(@Param("userInfoSeq") String userInfoSeq, @Param("menuCode") String menuCode);

    /**
     * 특정 검색 조건 JSON 데이터 단건 조회
     */
    SearchConditionVO selectConditionDetail(
            @Param("conditionSeq") String conditionSeq
    );

    /**
     * 검색 조건 삭제 (논리 삭제)
     */
    int deleteCondition(
            @Param("conditionSeq") String conditionSeq,
            @Param("officeSeq") String officeSeq,
            @Param("updateUser") String updateUser
    );
     List<SearchFieldVO> selectFieldList(
            @Param("menuCode") String menuCode

    );
}