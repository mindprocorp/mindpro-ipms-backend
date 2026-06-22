package kr.co.mindpro.ipms.domain.board.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardConfigVO {

    private String configSeq;               // 설정 시퀀스
    private String officeSeq;               // 회사 시퀀스
    private String parentSeq;               // 부모 시퀀스 (상위 카테고리)
    private String boardType;               // CATEGORY, BOARD, LINK
    private String boardName;               // 게시판/카테고리명
    private String description;             // 게시판 설명
    private String shareScope;              // ALL, MEMBER
    private String adminWriteOnlyYn;        // Y/N 관리자/마스터만 글쓰기
    private String noticeAuthType;          // ALL, ADMIN 공지 작성 권한
    private String newPostAlertYn;          // Y/N 새글 알림 기본 설정
    private String useReactionYn;           // Y/N 리액션 기능 허용
    private String prefixTags;              // 말머리 (JSON 또는 쉼표 구분)
    private String viewType;                // LIST, PREVIEW, ALBUM
    private String status;                  // ACTIVE, ARCHIVED
    private Integer dispOrd;                // 정렬 순서
    private String delYn;                   // Y/N 삭제 여부

    private String createUser;
    private String createAt;
    private String updateUser;
    private String updateAt;

    // 계층형 트리를 위한 자식 노드 리스트
    private List<BoardConfigVO> children;

    // 카테고리 마스터 리스트 조인을 위한 필드
    private List<BoardMasterVO> masters;

    // 권한 목록
    private List<BoardPermissionVO> permissions;

    // 현재 로그인 유저 권한 (조회 시 계산)
    private Boolean canWrite;
    private Boolean canRead;
}
