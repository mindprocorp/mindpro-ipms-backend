package kr.co.mindpro.ipms.domain.history.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class HistoryResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistorySearchListDetail {
        private String seq;
        private String actionDateTime;
        private String actionType;
        private String caseCategoryCode;
        private String caseCategory;
        private String ourRef;
        private String appNo;
        private String regNo;
        private String fieldName;
        private String beforeValue;
        private String afterValue;
        private String actionUser;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifiedHistDetail {
        private String modifiedHistSeq;
        private String tblSeq;
        private String beforeValue;
        private String afterValue;
        private String modifiedContent;
        private String note;
        private String modifiedDate;
        private String createUser;
    }
}
