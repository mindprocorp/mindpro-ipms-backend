package kr.co.mindpro.ipms.domain.dashboard.service;

import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.DashboardOverview;

public interface DashboardService {
    DashboardOverview getDashboardSummary(String officeSeq, String startDate, String endDate, Integer year);
}
