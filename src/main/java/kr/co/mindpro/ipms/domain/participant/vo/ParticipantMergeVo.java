package kr.co.mindpro.ipms.domain.participant.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : ParticipantVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ParticipantMergeVo extends BaseVO {

    private String tblSeq;      // 업무 일련번호
    private String officeSeq;   // 사무소 일련번호
    private String createUser;  // 생성자 (SYSTEM)
    private String updateUser;  // 변경자 (SYSTEM)


    private String participantCode;  // 관계자 구분 코드 (공통코드 연동)


    // key: 공통코드(예: 'APP', 'CLI'), value: userInfoSeq
    private Map<String, String> participantMap;

}