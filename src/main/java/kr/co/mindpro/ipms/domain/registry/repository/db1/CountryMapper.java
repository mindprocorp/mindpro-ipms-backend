package kr.co.mindpro.ipms.domain.registry.repository.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.registry.vo.CountryVO;

/**
 * @author : seokho
 * @fileName : CountryMapper.java
 * @since : 2026. 04. 28.
 * @description : 국가 목록 조회 MyBatis Mapper
 */
@Mapper
public interface CountryMapper {

    /**
     * 전체 국가 목록 조회 (사용 가능한 국가만)
     */
    List<CountryVO> findAllCountries();

    /**
     * 검색어로 국가 목록 조회 (국가코드 또는 국가명)
     */
    List<CountryVO> findCountriesByKeyword(@Param("keyword") String keyword);
}
