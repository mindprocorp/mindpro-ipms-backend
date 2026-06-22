package kr.co.mindpro.ipms.domain.customer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CustomerRequest {

    @Builder
    @Schema(description = "고객 상세 정보 요청 객체")
    public record CustomerDetail(
            @Schema(description = "고객 일련번호", example = "CUST20260000001", format = "SEQ")
            String customerSeq,
            @Schema(description = "의뢰인 구분 (코드)", example = "C001")
            String clientCategory,
            @Schema(description = "출원인 구분 (코드)", example = "A001")
            String applicantCategory,
            @Schema(description = "법인 구분 (코드)", example = "CORP01")
            String corpCategory,
            @Schema(description = "대리인 구분 (코드)", example = "ATT01")
            String attorneyCategory,
            @Schema(description = "고객명(한글)", example = "홍길동")
            String clientNameKo,
            @Schema(description = "고객명(영문)", example = "Gildong Hong")
            String clientNameEn,
            @Schema(description = "고객명(중문)", example = "洪吉童")
            String clientNameCh,
            @Schema(description = "고객명(일문)", example = "ホン・ギルドン")
            String clientNameJp,
            @Schema(description = "회사명", example = "(주)마인드프로")
            String companyName,
            @Schema(description = "부서명", example = "지식재산부")
            String deptName,
            @Schema(description = "직위", example = "팀장")
            String position,
            @Schema(description = "출원인 우편번호", example = "06134")
            String appZipCode,
            @Schema(description = "출원인 주소", example = "서울특별시 강남구 테헤란로")
            String appAddress,
            @Schema(description = "출원인 전화번호", example = "02-123-4567")
            String appTel,
            @Schema(description = "출원인 팩스", example = "02-123-4568")
            String appFax,
            @Schema(description = "연락처 우편번호", example = "06134")
            String contactZipCode,
            @Schema(description = "연락처 주소", example = "서울특별시 강남구 테헤란로 123")
            String contactAddress,
            @Schema(description = "담당자명", example = "이순신")
            String contactPerson,
            @Schema(description = "담당자 전화번호", example = "010-1234-5678")
            String contactTel,
            @Schema(description = "담당자 팩스", example = "02-987-6543")
            String contactFax,
            @Schema(description = "국가코드", example = "KR")
            String countryCode,
            @Schema(description = "주민등록번호", example = "800101-1******")
            String residentRegNo,
            @Schema(description = "법인등록번호", example = "110111-1234567")
            String corpRegNo,
            @Schema(description = "특허고객번호", example = "1-2026-123456-7")
            String kipoClientNo,
            @Schema(description = "관리자명", example = "관리자")
            String managerName,
            @Schema(description = "포괄위임번호", example = "202612345678")
            String generalMandateNo,
            @Schema(description = "등록일", example = "20260210", format = "YYYYMMDD")
            String registrationDate,
            @Schema(description = "휴대폰번호", example = "010-1111-2222")
            String mobile,
            @Schema(description = "홈페이지", example = "https://www.mindpro.co.kr")
            String homepage,
            @Schema(description = "이메일", example = "help@mindpro.co.kr")
            String email,
            @Schema(description = "기타 우편번호", example = "12345")
            String etcZipCode,
            @Schema(description = "기타 주소", example = "경기도 성남시 분당구")
            String etcAddress,
            @Schema(description = "기타 전화번호", example = "031-123-4567")
            String etcTel,
            @Schema(description = "기타 팩스", example = "031-123-4568")
            String etcFax,
            @Schema(description = "해외 우편번호", example = "90210")
            String overseaZipCode,
            @Schema(description = "해외 주소", example = "Beverly Hills, CA")
            String overseaAddress,
            @Schema(description = "해외 전화번호", example = "+1-310-123-4567")
            String overseaTel,
            @Schema(description = "해외 팩스", example = "+1-310-123-4568")
            String overseaFax,
            @Schema(description = "비고", example = "주요 VIP 고객")
            String note,
            @Schema(description = "사업자정보 일련번호", example = "BIZ20260000001", format = "SEQ")
            String bizInfoSeq,
            @Schema(description = "사업자등록번호", example = "123-81-12345")
            String bizRegNo,
            @Schema(description = "종사업장번호", example = "0000")
            String subBizRegNo,
            @Schema(description = "사업장상호", example = "(주)마인드프로")
            String bizName,
            @Schema(description = "대표자명", example = "강감찬")
            String bizCEO,
            @Schema(description = "사업장주소", example = "서울특별시 강남구")
            String bizAddress,
            @Schema(description = "업태", example = "서비스")
            String bizType,
            @Schema(description = "종목", example = "소프트웨어 개발")
            String bizItem,
            @Schema(description = "감면 대상(%)", example = "70")
            String reliefTarget,
            @Schema(description = "감면 사류/사유")
            String reliefReason,
            @Schema(description = "감면 발급일", example = "20260101", format = "YYYYMMDD")
            String reliefIssueDate,
            @Schema(description = "감면 면제기간 (까지)", example = "20271231", format = "YYYYMMDD")
            String reliefExemptionDate
    ) {}

    @Builder
    @Schema(description = "관련 고객사 매핑 요청 객체")
    public record CustomerMappDetail(
            @Schema(description = "고객 매핑 일련번호", example = "MAP20260000001", format = "SEQ")
            String customerMappSeq,
            @Schema(description = "기준 고객 일련번호", example = "CUST20260000001", format = "SEQ")
            String customerSeq,
            @Schema(description = "연결된 업무 식별키", example = "CUST20260000005", format = "SEQ")
            String tblSeq,
            @Schema(description = "특허고객번호", example = "1-2026-123456-7")
            String kipoClientNo,        // 추가
            @Schema(description = "관계 코드 (코드그룹: REL00)", example = "REL01")
            String relationCode,
            @Schema(description = "비고", example = "자회사 관계")
            String note
    ) {}

    @Builder
    @Schema(description = "변경 사항 히스토리 요청 객체")
    public record ModifiedHistDetail(
            @Schema(description = "변경 이력 일련번호", example = "HIST20260000001", format = "SEQ")
            String modifiedHistSeq,
            @Schema(description = "대상 테이블 PK (고객 일련번호 등)", example = "CUST20260000001", format = "SEQ")
            String tblSeq,
            @Schema(description = "변경 전 데이터", example = "서울시 강남구")
            String beforeValue,
            @Schema(description = "변경 후 데이터", example = "서울시 서초구")
            String afterValue,
            @Schema(description = "변경 내용 요약", example = "고객 주소지 변경")
            String modifiedContent,
            @Schema(description = "비고", example = "유선 요청으로 인한 수정")
            String note,
            @Schema(description = "변경일", example = "20261210" , format = "YYYYMMDD")
            String modifiedDate
    ) {}
}
