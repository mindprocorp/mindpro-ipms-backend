package kr.co.mindpro.ipms.domain.dashboard.dto.response;

import java.util.List;

public class DashboardResponse {
    
    public record SummaryData(
        long total,
        int newRequest,
        int modifiedRequest,
        int domesticAccept,
        int overseasAccept,
        int inProgress,
        int completed
    ) {}

    public record ChartData(
        String name,
        long value,
        String fill
    ) {}

    public record MonthlyData(
        String month,
        int newRequest,
        int domesticAccept,
        int overseasAccept,
        int quotationRequest,
        int searchRequest
    ) {}

    public static class CodeInfo {
        private String code;
        private String codeName;

        public CodeInfo() {}
        public CodeInfo(String code, String codeName) {
            this.code = code;
            this.codeName = codeName;
        }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getCodeName() { return codeName; }
        public void setCodeName(String codeName) { this.code = codeName; }
    }

    public static class RecentCaseData {
        private String appSeq;
        private String appNo;
        private String titleKo;
        private String appState;     // Code
        private String appStateName; // Name
        private String state;        // Code
        private String stateName;    // Name
        private String createDate;
        private String rightCategory;

        public RecentCaseData() {}
        public String getAppSeq() { return appSeq; }
        public void setAppSeq(String appSeq) { this.appSeq = appSeq; }
        public String getAppNo() { return appNo; }
        public void setAppNo(String appNo) { this.appNo = appNo; }
        public String getTitleKo() { return titleKo; }
        public void setTitleKo(String titleKo) { this.titleKo = titleKo; }
        public String getAppState() { return appState; }
        public void setAppState(String appState) { this.appState = appState; }
        public String getAppStateName() { return appStateName; }
        public void setAppStateName(String appStateName) { this.appStateName = appStateName; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getStateName() { return stateName; }
        public void setStateName(String stateName) { this.stateName = stateName; }
        public String getCreateDate() { return createDate; }
        public void setCreateDate(String createDate) { this.createDate = createDate; }
        public String getRightCategory() { return rightCategory; }
        public void setRightCategory(String rightCategory) { this.rightCategory = rightCategory; }
    }

    public record DashboardOverview(
        SummaryData summaryData,
        List<ChartData> statusData,
        List<ChartData> countryData,
        List<ChartData> rightData,
        List<MonthlyData> monthlyData,
        List<RecentCaseData> recentList
    ) {}
}
