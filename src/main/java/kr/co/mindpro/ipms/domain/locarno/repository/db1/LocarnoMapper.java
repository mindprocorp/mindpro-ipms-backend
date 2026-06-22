package kr.co.mindpro.ipms.domain.locarno.repository.db1;

import java.util.List;

import kr.co.mindpro.ipms.domain.locarno.dto.response.LocarnoResponse;
import kr.co.mindpro.ipms.domain.locarno.vo.AppLocarnoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.locarno.vo.LocarnoVO;

/**
 * [Mapper Interface] 로카르노 관리 데이터 접근 레이어
 *
 * @author	 : intst
 * @fileName	 : LocarnoMapper.java
 * @since	 : 2026. 2. 4.
 */
@Mapper
public interface LocarnoMapper {

	/**
     * 로카르노 목록 조회 (전체 또는 버전별)
     * 
     * @param locarnoVersion 로카르노 버전 (null 또는 빈값일 경우 전체 조회)
     * @return 로카르노 결과 리스트
     */
    List<LocarnoVO> selectLocarnoList(@Param("locarnoVersion") String locarnoVersion);
    
    /**
     * 특정 물품류(class_no)에 속한 소분류 목록 조회
     * 
     * @param classNo 물품류 번호
     * @param locarnoVersion 로카르노 버전 (특정 버전 필터링용, 선택사항)
     * @return 로카르노 소분류 결과 리스트
     */
    List<LocarnoVO> selectLocarnoSubclassList(@Param("classNo") String classNo, @Param("locarnoVersion") String locarnoVersion);
    
    /**
     * 특정 물품류 및 소분류에 속한 물품 목록 조회
     * 
     * @param classNo 물품류 번호 (필수)
     * @param subclassNo 소분류 번호 (필수)
     * @param locarnoVersion 로카르노 버전 (선택)
     * @return 로카르노 물품 결과 리스트
     */
    List<LocarnoVO> selectLocarnoGoodsList(@Param("classNo") String classNo, @Param("subclassNo") String subclassNo, @Param("locarnoVersion") String locarnoVersion);    

	/**
     * 로카르노 일괄 등록
     * */
    int insertLocarno(AppLocarnoVO appLocarnoVO);

    /**
     * 로카르노 리스트 조회
     * */
    List<AppLocarnoVO> getLocarnoList(String officeSeq, String appSeq);

    List<AppLocarnoVO> selectLocarnoByGroup(@Param("officeSeq") String officeSeq,
                                            @Param("appSeq") String appSeq,
                                            @Param("locarnoGroupId") String locarnoGroupId);

    int getDuplicateLocarnoCnt(String officeSeq, String appSeq, String locarnoGroupId);

    int softDeleteLocarnoGroup(String officeSeq, String appSeq, String locarnoGroupId, String userSeq);

    int softDeleteLocarnoGroupByList(String officeSeq, String appSeq, List<String> locarnoGroupIdList, String userSeq);

    int hardDeleteLocarnoGroup(String officeSeq, String appSeq, String locarnoGroupId);

    int hardDeleteLocarnoGroupByList(String officeSeq, String appSeq, List<String> locarnoGroupIdList);


}
