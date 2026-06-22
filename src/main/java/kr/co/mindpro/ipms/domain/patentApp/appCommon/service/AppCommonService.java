package kr.co.mindpro.ipms.domain.patentApp.appCommon.service;

import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;

/**
 * @author : seokho
 * @fileName : AppCommonService.java
 * @since : 2026. 1. 21.
 */

public interface AppCommonService {

    /** 출원사건관리 + 출원기본정보 컴포넌트 조회  */
    AppBasicInfoVO getBasicInfoDetail(String officeSeq, String appSeq);

    void hardDeleteAppCommon(String appSeq);

    void softDeleteAppCommon(String appSeq);

    /**
     * 출원 첨부 이미지를 논리 삭제합니다. (공통)
     * @param appSeq  출원 마스터 Seq
     * @param fileSeq 삭제할 파일 Seq
     */
    void deleteAppImageFile(String appSeq, String fileSeq);
}
