package kr.co.mindpro.ipms.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 용자 관련 요청 객체
 * 각 용도별로 내부 record를 선언하여 관리 포인트의 효율성을 높임
 *
 * @author	 : intst
 * @fileName	 : UserRequest.java
 * @since	 : 2025. 12. 24.
 */
@Data
public class UserInfoResponse {


    @Builder
    public record UserDetailResponse(
            String userInfoSeq,      // 일련번호
            String appInventorSeq,
            String tblSeq,           // 매핑된 사건번호 (app_seq 등)

            // 성명 (다국어)
            String userNameKo,       // 고안자(국문)
            String userNameEn,       // 고안자(영문)
            String userNameZh,       // 고안자(한문)
            String userNameJa,       // 고안자(일문)

            // 개인정보
            String residentNo,       // 주민등록번호
            String countryCode,      // 국적

            // 주소 (다국어)
            String userAddr,         // 주소(국문)
            String userAddrEn,       // 주소(영문)
            String userAddrZh,       // 주소(한문)
            String userAddrJa,       // 주소(일문)

            // 매핑 및 기타 정보
            String sort,         // 순서
            String note             // 비고

    ) {
        // 필요한 경우 팩토리 메소드나 추가 로직을 넣을 수 있습니다.
    }
    public record UserListResponse(
            String userInfoSeq,
            String userNameKo,
            String userNameEn,
            String userNameZh,
            String userNameJa,
            String residentNo,
            String userAddr,
            Integer sortSeq
    ) {}
}
