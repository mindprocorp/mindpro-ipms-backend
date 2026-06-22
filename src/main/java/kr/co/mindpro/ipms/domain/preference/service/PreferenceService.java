package kr.co.mindpro.ipms.domain.preference.service;


import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.preference.dto.request.PreferenceRequest;
import kr.co.mindpro.ipms.domain.preference.dto.response.PreferenceResponse;
import kr.co.mindpro.ipms.domain.preference.vo.PreferenceVO;

import java.util.List;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : InvoiceService.java
 * @since    : 2026. 01. 07.
 */
public interface PreferenceService {
    BaseSearchResponse<PreferenceResponse.PreferenceDetail> getPreferenceList(String appSeq);
    void saveAllPreferences(String appSeq, List<PreferenceVO> list);
     void registerPreference(PreferenceRequest.PreferenceDetail vo);
    PreferenceResponse.PreferenceDetail getPreference(String preferenceSeq);

    void softDeletePreference(String appSeq, String preferenceSeq);

    /**
     * [삭제] 우선권 다건 논리 삭제
     */
    void softDeletePreferenceByList(String appSeq, List<String> preferenceSeqList);

    /**
     * [삭제] 우선권 단건 물리 삭제
     */
    void hardDeletePreference(String appSeq, String preferenceSeq);

    /**
     * [삭제] 우선권 다건 물리 삭제
     */
    void hardDeletePreferenceByList(String appSeq, List<String> preferenceSeqList);
}