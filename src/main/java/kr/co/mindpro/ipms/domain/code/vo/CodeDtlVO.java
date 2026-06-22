package kr.co.mindpro.ipms.domain.code.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 공통 코드 상세 테이블 매핑 객체
 * DB 테이블 utb_code_dtl 의 컬럼과 1:1 대응됩니다.
 *
 * @author   : mindpro
 * @fileName : CodeDtlVO.java
 * @since    : 2026. 01. 19.
 */
@Getter
@Setter
@ToString
// [방어] 프론트가 timezone 없는 LocalDateTime 형식 timestamp를 보내도 무시 (deserialize 차단).
//       getter는 살려서 직렬화는 정상 (allowGetters=true).
@JsonIgnoreProperties(value = {"createAt", "updateAt"}, allowGetters = true)
public class CodeDtlVO extends BaseVO {
    private String codeSeq;    // 코드 일련번호 (PK)
    private String grpCd;      // 그룹 코드
    private String dtlCd;      // 상세 코드
    private String cdNm;       // 코드 명
    private String kipoCd;     // KIPO 코드 (특허청 대응)
    private String refVal1;    // 참조 값 1
    private String refVal2;    // 참조 값 2
    private Integer dispOrd;   // 표시 순서
    private String useYn;      // 사용 여부 (Y/N) — 임시 비활성화용
    private String delYn;      // 삭제 여부 (Y/N) — soft delete 마커
    private String note;       // 비고

    /* 관리 화면 처리를 위한 추가 필드 */
    private String rowStatus;  // 행 상태 (I: 추가, U: 수정, D: 삭제)
}