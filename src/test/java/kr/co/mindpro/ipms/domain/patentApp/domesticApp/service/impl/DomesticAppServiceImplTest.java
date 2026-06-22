package kr.co.mindpro.ipms.domain.patentApp.domesticApp.service.impl;

import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : seokho
 * @fileName : DomesticAppServiceImplTest.java
 * @since : 2026. 1. 12.
 */
@SpringBootTest
@Transactional
public class DomesticAppServiceImplTest {

    @Autowired
    private AppCommonService appCommonService;

    @Test
    @DisplayName("국내 출원 상세 정보 조회 테스트")
    void getBasicInfoDetailTest() {
        // 1. Given: 테스트에 사용할 키값 (실제 DB에 있는 값으로 세팅해)
        String officeSeq = "PGOKR20260000002";
        String appSeq = "APPMST20260000088";

        // 2. When: 서비스 호출
        AppBasicInfoVO result = appCommonService.getBasicInfoDetail(officeSeq, appSeq);

        // 3. Then: JUnit 5 기본 Assertions로 검증
        Assertions.assertNotNull(result, "조회 결과가 null입니다. DB 데이터를 확인하세요.");
        Assertions.assertEquals(appSeq, result.getAppSeq(), "출원 식별자가 일치하지 않습니다.");
        Assertions.assertEquals(officeSeq, result.getOfficeSeq(), "사무소 식별자가 일치하지 않습니다.");

        // 동적 컬럼 데이터 출력 (콘솔에서 직접 눈으로 확인용)
        System.out.println("=========================================");
        System.out.println("출원일(appDate): " + result.getAppDate());
        System.out.println("출원지시일(appOrderDate): " + result.getAppOrderDate());
        System.out.println("초안마감일(draftDeadline): " + result.getDraftDeadline());
        System.out.println("=========================================");

        // 날짜 데이터가 하나라도 들어왔는지 검증
        Assertions.assertTrue(result.getAppDate() != null || result.getAppOrderDate() != null,
                "기일 데이터가 하나도 조회되지 않았습니다.");
    }
}
