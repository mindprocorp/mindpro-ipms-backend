package kr.co.mindpro.ipms.domain.preference.repository.db1;


import kr.co.mindpro.ipms.domain.preference.dto.response.PreferenceResponse;
import kr.co.mindpro.ipms.domain.preference.vo.PreferenceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : DuedateMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface PreferenceMapper {
    /**
     * 특정 출원의 우선권 목록 조회
     * @param appSeq 출원 일련번호
     * @param officeSeq 사무소 일련번호
     * @return 우선권 상세 목록
     */
    List<PreferenceResponse.PreferenceDetail> findAllByApp(@Param("appSeq") String appSeq,
                                                           @Param("officeSeq") String officeSeq);

    //단건조회
    PreferenceResponse.PreferenceDetail findById(@Param("preferenceSeq") String preferenceSeq,
                                                 @Param("officeSeq") String officeSeq);
    /**
     * 특정 출원의 모든 우선권 정보 삭제 (Soft Delete)
     * @param appSeq 출원 일련번호
     * @param officeSeq 사무소 일련번호
     * @param userId 수정자 ID
     * @return 삭제된 행 수
     */
    int deleteByApp(@Param("appSeq") String appSeq,
                    @Param("officeSeq") String officeSeq,
                    @Param("userId") String userId);

    /**
     * 우선권 단건 저장
     * @param vo 우선권 정보 (selectKey를 통해 preferenceSeq가 채워짐)
     * @return 저장된 행 수
     */
    int insertPreference(PreferenceVO vo);

    PreferenceResponse.PreferenceDetail updatePreference(@Param("preferenceSeq") String preferenceSeq,
                                                @Param("officeSeq") String officeSeq);


    // 단건일 경우 논리적 삭제
    int deletePreference(
            @Param("appSeq") String appSeq,
            @Param("preferenceSeq") String preferenceSeq,
            @Param("officeSeq") String officeSeq,
            @Param("userId") String userId);

    /**
     * [삭제] 우선권 다건 논리 삭제
     */
    int softDeletePreferenceList(@Param("appSeq") String appSeq,
                                 @Param("preferenceSeqList") List<String> preferenceSeqList,
                                 @Param("officeSeq") String officeSeq,
                                 @Param("userId") String userId);

    /**
     * [삭제] 우선권 단건 물리 삭제
     */
    int hardDeletePreference(
            @Param("appSeq") String appSeq,
            @Param("preferenceSeq") String preferenceSeq,
            @Param("officeSeq") String officeSeq);

    /**
     * [삭제] 우선권 다건 물리 삭제
     */
    int hardDeletePreferenceList(@Param("appSeq") String appSeq,
                                 @Param("preferenceSeqList") List<String> preferenceSeqList,
                                 @Param("officeSeq") String officeSeq);

}