package kr.co.mindpro.ipms.domain.code.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 공통 코드 마스터 테이블 매핑 객체
 * DB 테이블 utb_code_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author   : mindpro
 * @fileName : CodeMstVO.java
 * @since    : 2026. 01. 19.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createAt", "updateAt"}, allowGetters = true)
public class CodeMstVO extends BaseVO {
    private String codeSeq;    // 코드 일련번호 (PK)
    private String grpCd;      // 그룹 코드 (Unique)
    private String cdNm;       // 코드 명
    private Integer dispOrd;   // 표시 순서
    private String useYn;      // 사용 여부 (Y/N) — 임시 비활성화용
    private String delYn;      // 삭제 여부 (Y/N) — soft delete 마커
    private String note;       // 비고
    
    /* BaseVO에 아래와 같은 공통 필드가 포함되어 있을 것으로 예상됩니다:
     * - private String createUser;
     * - private LocalDateTime createAt;
     * - private String updateUser;
     * - private LocalDateTime updateAt;
     */
}