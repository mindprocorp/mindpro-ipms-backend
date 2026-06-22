package kr.co.mindpro.ipms.domain.locarno.service;

import java.util.List;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.locarno.dto.request.LocarnoRequest;
import kr.co.mindpro.ipms.domain.locarno.dto.response.LocarnoResponse;
import kr.co.mindpro.ipms.domain.locarno.vo.LocarnoVO;

/**
 * [Service Interface] 로카르노 관리 서비스
 *
 * @author	 : mindpro
 * @fileName	 : LocarnoService.java
 * @since	 : 2026. 2. 4.
 */
public interface LocarnoService {
    /**
     * 로카르노 목록 조회
     * @return 로카르노 목록
     */
    List<LocarnoVO> getLocarnoList();
    
    /**
     * 로카르노 목록 버전별 조회 (추가)
     * @param locarnoVersion 로카르노 버전
     * @return 특정 버전의 로카르노 목록
     */
    List<LocarnoVO> getLocarnoListByVersion(String locarnoVersion);
    
    /**
     * [추가] 로카르노 소분류 목록 조회
     * @param classNo 물품류 번호 (필수)
     * @param locarnoVersion 로카르노 버전 (선택)
     * @return 물품류에 해당하는 소분류 목록
     */
    List<LocarnoVO> getLocarnoSubclassList(String classNo, String locarnoVersion);   
    
    /**
     * [추가] 로카르노 물품 목록 조회
     * @param classNo 물품류 번호 (필수)
     * @param subclassNo 소분류 번호 (필수)
     * @param locarnoVersion 로카르노 버전 (선택)
     * @return 물품류 및 소분류에 해당하는 물품 목록
     */
    List<LocarnoVO> getLocarnoGoodsList(String classNo, String subclassNo, String locarnoVersion);    

	/**
     * 로카르노 일괄 등록(출원에 연결)
     * @parem List<LocarnoRequest.SaveAllLocarno>
     * */
    void saveAllLocarno(LocarnoRequest.SaveAllLocarno locarnoList);

    /**
     * 로카르노 일괄 조회(출원에 연결된 로카르노 정보)
     *
     * @parem appSeq
     *
     */
    BaseSearchResponse<LocarnoResponse.Detail> getLocarnoListByAppSeq(String appSeq);

    List<LocarnoResponse.GroupItem> getLocarnoGroupDetail(String appSeq, String locarnoGroupId);

    void softDeleteLocarnoGroup(String appSeq, String locarnoGroupId);

    void softDeleteLocarnoGroupByList(String appSeq, List<String> locarnoGroupIdList);

    void hardDeleteLocarnoGroup(String appSeq, String locarnoGroupId);

    void hardDeleteLocarnoGroupByList(String appSeq, List<String> locarnoGroupIdList);

}
