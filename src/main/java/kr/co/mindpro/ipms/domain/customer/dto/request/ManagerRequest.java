package kr.co.mindpro.ipms.domain.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class ManagerRequest {

    @Schema(description = "고객사 담당자 저장 요청 객체")
    @Builder
    public record CustomerManagerRequest(
            @Schema(description = "연결 대상 업무 일련번호 (고객/출원 등)", example = "CUST20260000001", format = "SEQ")
            String tblSeq,

            @Schema(description = "참여자(담당자) 매핑 일련번호", example = "PAR20260000001", format = "SEQ")
            String participantSeq,

            @Schema(description = "인적정보(사용자) 일련번호", example = "USR20260000055", format = "SEQ")
            String userInfoSeq,

            @Schema(description = "참여구분 코드 (예: 담당자 MGR01)", example = "MGR01")
            String participantCode,

            @Schema(description = "담당자 성명(한글)", example = "김철수")
            String userNameKo,

            @Schema(description = "담당자 휴대폰번호", example = "010-1234-5678")
            String userMobileNo,

            @Schema(description = "부서명", example = "법무팀")
            String deptName,

            @Schema(description = "전화번호", example = "02-123-4567")
            String userTelNo,

            @Schema(description = "직위", example = "과장")
            String userPosition,

            @Schema(description = "팩스번호", example = "02-123-4568")
            String userFaxNo,

            @Schema(description = "이메일", example = "chulsoo.kim@client.com")
            String userEmail,

            @Schema(description = "우편번호", example = "06134")
            String userPostNo,

            @Schema(description = "주소", example = "서울특별시 강남구 테헤란로")
            String userAddr,

            @Schema(description = "상세주소", example = "마인드빌딩 5층")
            String userAddrDetail,

            @Schema(description = "비고", example = "메인 연락 창구")
            String note,

            @Schema(description = "전자세금계산서 수신 여부", allowableValues = {"Y", "N"}, example = "Y", format = "YN")
            String etaxYn
    ) {}
}
