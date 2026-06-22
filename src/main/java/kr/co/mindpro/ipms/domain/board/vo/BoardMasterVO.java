package kr.co.mindpro.ipms.domain.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMasterVO {

    private String configSeq;               // 매핑된 설정 시퀀스
    private String masterUserId;            // 마스터 권한을 가진 유저 아이디
    private String masterUserName;          // 조인용 마스터 이름
    
    private String createUser;
    private String createAt;
}
