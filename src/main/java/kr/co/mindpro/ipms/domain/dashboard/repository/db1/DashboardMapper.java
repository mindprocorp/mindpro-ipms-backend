package kr.co.mindpro.ipms.domain.dashboard.repository.db1;

import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.SummaryData;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.ChartData;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.MonthlyData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {

    SummaryData getSummaryData(@Param("officeSeq") String officeSeq, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ChartData> getStatusData(@Param("officeSeq") String officeSeq, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ChartData> getCountryData(@Param("officeSeq") String officeSeq, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ChartData> getRightData(@Param("officeSeq") String officeSeq, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<MonthlyData> getMonthlyData(@Param("officeSeq") String officeSeq, @Param("year") Integer year);

    List<kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.RecentCaseData> getRecentList(
        @Param("officeSeq") String officeSeq,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}
