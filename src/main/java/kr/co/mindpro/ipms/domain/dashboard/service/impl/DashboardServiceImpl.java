package kr.co.mindpro.ipms.domain.dashboard.service.impl;

import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.DashboardOverview;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.SummaryData;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.ChartData;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.MonthlyData;
import kr.co.mindpro.ipms.domain.dashboard.repository.db1.DashboardMapper;
import kr.co.mindpro.ipms.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    public DashboardOverview getDashboardSummary(String officeSeq, String startDate, String endDate, Integer year) {

        // 1. 총진행현황
        SummaryData summaryData = dashboardMapper.getSummaryData(officeSeq, startDate, endDate);
        if (summaryData == null) {
            summaryData = new SummaryData(0, 0, 0, 0, 0, 0, 0);
        }

        // 2. 상태별 현황 (출원완료, 진행중, 취하/포기)
        List<ChartData> statusData = dashboardMapper.getStatusData(officeSeq, startDate, endDate);

        // 3. 출원국가 현황 (국내, 해외)
        List<ChartData> countryData = dashboardMapper.getCountryData(officeSeq, startDate, endDate);

        // 4. 권리구분 현황 (특허, 실용신안, 디자인, 상표, 기타)
        List<ChartData> rightData = dashboardMapper.getRightData(officeSeq, startDate, endDate);

        // 5. 기간(월)별 현황
        List<MonthlyData> monthlyData = dashboardMapper.getMonthlyData(officeSeq, year);

        // 6. 최근 사건 리스트
        List<kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.RecentCaseData> recentList =
            dashboardMapper.getRecentList(officeSeq, startDate, endDate);
        if (recentList == null) {
            recentList = new ArrayList<>();
        }

        return new DashboardOverview(summaryData, statusData, countryData, rightData, monthlyData, recentList);
    }
}
