package kr.co.mindpro.ipms.domain.board.vo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPermissionVO {

    private String targetSeq;               // 시퀀스 (PK)
    private String configSeq;               // 게시판 설정 시퀀스 (FK)
    private String targetRole;              // 권한 역할 (WRITE_AUTH, READ_AUTH 등)
    private String targetType;              // 대상 유형 (EMPLOYEE, DEPT 등)
    private String refSeq;                  // 대상 식별자 (사용자/부서 seq)
    private String note;

    private String createUser;
    private String createAt;
    private String updateUser;
    private String updateAt;
    private String delYn;
}
