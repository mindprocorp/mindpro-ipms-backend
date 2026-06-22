package kr.co.mindpro.ipms.domain.dispatch.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class DispatchRequest {

    @Getter
    @Setter
    @Schema(name = "DispatchDetailRequest")
    public static class DispatchDetail {
        @Schema(description = "문서수발 일련번호")
        private String dispatchSeq;

        @Schema(description = "구분 (송신/수신)")
        private String category;

        @Schema(description = "문서 종류")
        private String docType;

        @Schema(description = "일자 (YYYY-MM-DD)")
        private String dispatchDate;

        @Schema(description = "거래처명")
        private String client;

        @Schema(description = "담당자명")
        private String manager;

        @Schema(description = "문서 내용")
        private String docContent;

        @Schema(description = "발송/수신 방법 (우편, 퀵 등)")
        private String method;

        @Schema(description = "발송일 (YYYY-MM-DD)")
        private String sendDate;

        @Schema(description = "등기번호/추적번호")
        private String regNo;

        @Schema(description = "수신확인 여부 (Y/N)")
        private String ackYn;

        @Schema(description = "우편주소")
        private String postAddr;

        @Schema(description = "비고")
        private String note;
    }
}
