package kr.co.mindpro.ipms.domain.patentApp.overseaApp.service.impl;

// 필요한 Import들
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.service.AppCommonService;
import kr.co.mindpro.ipms.domain.patentApp.appCommon.vo.AppBasicInfoVO;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.repository.db1.OverseaAppMapper;
import kr.co.mindpro.ipms.domain.patentApp.overseaApp.vo.AppExtMstVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 메인 테스트 클래스 시작
class BasicAppServiceImplTest {

//    private BasicAppServiceImpl basicAppService;
//
//    // 가짜 객체 변수 선언
//    private FakeAppCommonService fakeAppCommonService;
//    private FakeOverseaAppMapper fakeMapper;
//    private FakeParticipantService fakeParticipantService;
//    private FakeDueDateService fakeDueDateService;
//
//    @BeforeEach
//    void setUp() {
//        // 1. 가짜 객체 생성 (이 클래스들이 아래에 정의되어 있어야 함!)
//        fakeAppCommonService = new FakeAppCommonService();
//        fakeMapper = new FakeOverseaAppMapper();
//        fakeParticipantService = new FakeParticipantService();
//        fakeDueDateService = new FakeDueDateService();
//
//        // 2. 서비스 생성 및 주입
//        basicAppService = new BasicAppServiceImpl(
//                fakeAppCommonService,
//                fakeMapper,
//                fakeParticipantService,
//                fakeDueDateService
//        );
//    }
//
//    @Test
//    @DisplayName("기본 해외출원 생성 통합 테스트")
//    void createBasicApp_Success() {
//        // given
//        OverseaAppRequest.CreateBasic request = createFullMockRequest();
//
//        // when
//        try {
//            basicAppService.createBasicApp(request);
//        } catch (Exception e) {
//            System.out.println("테스트 중 예외 발생 (SecurityUtil 등): " + e.getMessage());
//        }
//
//        // then
//        // 1. Mapper 검증
//        AppExtMstVO savedVO = fakeMapper.getCapturedVO();
//        if (savedVO != null) {
//            assertEquals("utb_app_ext_mst", savedVO.getAppExtSeq());
//            assertEquals("US,JP", savedVO.getIndividualCountryContent());
//        }
//
//        // 2. 참여자 검증
//        List<ParticipantVO> savedParticipants = fakeParticipantService.getCapturedList();
//        if (savedParticipants != null) {
//            assertEquals(9, savedParticipants.size());
//            ParticipantVO admin = findParticipant(savedParticipants, "adminMgr");
//            assertNotNull(admin);
//            assertEquals("ADMIN-SEQ-01", admin.getUserInfoSeq());
//        }
//
//        // 3. 기일 검증
//        List<DueDateVO> savedDueDates = fakeDueDateService.getCapturedList();
//        if (savedDueDates != null) {
//            assertEquals(4, savedDueDates.size());
//            DueDateVO receiptDate = findDueDate(savedDueDates, "receiptDate");
//            assertNotNull(receiptDate);
//            assertTrue(receiptDate.getDuedateDate().toString().contains("2026-01-20"));
//        }
//    }
//
//    // 1. Fake AppCommonService
//    static class FakeAppCommonService implements AppCommonService {
//        @Override
//        public OffsetDateTime parseDate(String date) {
//            // String -> OffsetDateTime 변환 로직 시뮬레이션
//            if (date != null && date.length() == 8) {
//                LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
//                return ld.atTime(LocalTime.MIN).atOffset(ZoneOffset.UTC);
//            }
//            return null;
//        }
//
//        // 인터페이스의 다른 메소드들은 껍데기만 구현
//        @Override public AppBasicInfoVO getBasicInfoDetail(String o, String a) { return null; }
//        @Override public DomesticAppResponse.DomesticAppDetailResponse getPatentAppDetail(String a) { return null; }
//    }
//
//    // 2. Fake OverseaAppMapper
//    static class FakeOverseaAppMapper implements OverseaAppMapper {
//        private AppExtMstVO capturedVO;
//        @Override
//        public void insertOverseaBasicApp(AppExtMstVO vo) {
//            this.capturedVO = vo; // 저장된 값 캡처
//        }
//        public AppExtMstVO getCapturedVO() { return capturedVO; }
//    }
//
//    // 3. Fake ParticipantService
//    static class FakeParticipantService implements ParticipantService {
//        private List<ParticipantVO> capturedList;
//        @Override
//        public void saveAllParticipants(List<ParticipantVO> list) {
//            this.capturedList = list; // 저장된 리스트 캡처
//        }
//        public List<ParticipantVO> getCapturedList() { return capturedList; }
//
//        // 껍데기 구현
//        @Override public List<ParticipantVO> getParticipantListByWork(String t, String o) { return List.of(); }
//        @Override public List<ParticipantVO> getParticipantsByCode(String t, String o, String p) { return List.of(); }
//        @Override public void insertParticipant(ParticipantVO vo) {}
//        @Override public List<String> getIdsByFilters(String o, String t, List<SearchParamVO> l) { return List.of(); }
//    }
//
//    // 4. Fake DueDateService
//    static class FakeDueDateService implements DueDateService {
//        private List<DueDateVO> capturedList;
//        @Override
//        public void saveAllDueDates(List<DueDateVO> list) {
//            this.capturedList = list; // 저장된 리스트 캡처
//        }
//        public List<DueDateVO> getCapturedList() { return capturedList; }
//
//        // 껍데기 구현
//        @Override public void registerDueDate(DueDateVO vo) {}
//        @Override public List<DueDateVO> getDueDateListByWork(String t, String o) { return List.of(); }
//        @Override public List<DueDateVO> getDueDateList(DueDateVO s) { return List.of(); }
//        @Override public List<DueDateVO> getUpcomingDueDates(String o) { return List.of(); }
//        @Override public List<String> getIdsByDateFilters(String o, String t, List<SearchParamVO> d) { return List.of(); }
//    }
//
//    // =================================================================
//    // Helper Methods
//    // =================================================================
//
//    private OverseaAppRequest.CreateBasic createFullMockRequest() {
//        return new OverseaAppRequest.CreateBasic(
//                "OFFICE_001", "APP_001",
//                new OverseaAppRequest.OverseaAppCaseMng("Route", "Patent", "New", "USA", "US", "Type", "OUR-REF", "YOUR-REF", "CLIENT-REF", "20260120", "20260121", new OverseaAppRequest.AppManagerInfo("APP-CONTACT-SEQ-01", "name"), "CASE-NO"),
//                new OverseaAppRequest.OverseaAppManagerInfo("DeptTeam",
//                        new OverseaAppRequest.AdminMgrInfo("ADMIN-SEQ-01", "name"),
//                        new OverseaAppRequest.CaseMgrInfo("CASE-MGR-SEQ-01", "name"),
//                        new OverseaAppRequest.AttorneyInfo("ATTY-SEQ-01", "name")
//                ),
//                new OverseaAppRequest.OverseaAppCounterPartyInfo(
//                        null,
//                        new OverseaAppRequest.ClientInfo("CLIENT-SEQ-01", "name"),
//                        new OverseaAppRequest.ClientContactInfo("CLIENT-CONTACT-SEQ-01", "name"),
//                        new OverseaAppRequest.ApplicantInfo("APPLICANT-SEQ-01", "name"),
//                        new OverseaAppRequest.InventorInfo("INVENTOR-SEQ-01", "name"),
//                        new OverseaAppRequest.RegMgrInfo("REG-MGR-SEQ-01", "name")
//                ),
//                new OverseaAppRequest.OverseaAppNameInfo("국문명", "EngTitle"),
//                new OverseaAppRequest.GoodsClass("09"),
//                new OverseaAppRequest.OverseaAppSpecificElement("S", "10", "5", null, "3", null),
//                new OverseaAppRequest.OverseaAppStrategy(
//                        new OverseaAppRequest.provisionalAppInfo("20260201", "PROV-NO"),
//                        null, null, null,
//                        new OverseaAppRequest.GlobalAppInfo("20260301", "PCT/KR2026/001")
//                ),
//                null, null,
//                new OverseaAppRequest.DesignatedStateInfo(List.of("US", "JP"), List.of("KR"), null, null, null),
//                new OverseaAppRequest.OverseaAppAbandonInfo("20261231", "GiveUp")
//        );
//    }
//
//    private ParticipantVO findParticipant(List<ParticipantVO> list, String code) {
//        return list.stream().filter(p -> code.equals(p.getParticipantCode())).findFirst().orElse(null);
//    }
//
//    private DueDateVO findDueDate(List<DueDateVO> list, String code) {
//        return list.stream().filter(d -> code.equals(d.getDuedateCategoryCode())).findFirst().orElse(null);
//    }
}