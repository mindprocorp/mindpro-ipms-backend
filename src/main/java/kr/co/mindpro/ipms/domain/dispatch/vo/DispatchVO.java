package kr.co.mindpro.ipms.domain.dispatch.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * [VO] 문서수발(우편물 기록) 관리
 */
@Getter
@Setter
@ToString
public class DispatchVO {
    private String dispatchSeq;      // 일련번호
    private String officeSeq;        // 사무소 일련번호
    private String category;         // 구분 (송신/수신)
    private String docType;          // 종류
    private String dispatchDate;     // 일자
    private String client;           // 거래처
    private String manager;          // 담당자
    private String docContent;       // 문서내용
    private String method;           // 방법 (우편, 퀵 등)
    private String sendDate;         // 발송일
    private String regNo;            // 등기번호
    private String ackYn;           // 수신확인 (Y/N)
    private String postAddr;         // 우편주소
    private String note;             // 비고
    private String delYn;           // 삭제여부
    private String createUser;
    private String createAt;
    private String updateUser;
    private String updateAt;
    private String uploadUserName; // 추가
}
