package kr.co.mindpro.ipms.domain.invoice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;
import lombok.Data;

@Data
public class InvoiceRequest {

    @Builder
    @Schema(description = "청구서 저장 요청")
    public record InvoiceDomesticDetail(
            @Schema(description = "출원 시퀀스", example = "APPMST20260000282", format = "SEQ") String appSeq,
            @Schema(description = "고객 시퀀스", example = "CUSTMR20260000010", format = "SEQ") String customerSeq,
            @Schema(description = "사업자 시퀀스", example = "BIZINF20260000038", format = "SEQ") String bizInfoSeq,


            // --- [1. 기본 및 청구 정보] ---
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "청구구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "청구분류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "사건구분(국내외)", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "청구종류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "청구일", example = "20260210", format = "YYYYMMDD") String invDate,
            @Schema(description = "청구번호", example = "INV-2026-001") String invNo,
            @Schema(description = "청구서발송일", example = "20260211", format = "YYYYMMDD") String invSendDate,
            @Schema(description = "비용담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo invMgr,
            @Schema(description = "OurRef", example = "MP-2026-0001") String ourRef,
            @Schema(description = "출원인 관리번호", example = "C-REF-123") String clientRef,
            @Schema(description = "부서", example = "법무팀") String deptName,
            @Schema(description = "관리담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo attorney,
//
//          // --- [3. 고객정보] ---
            @Schema(description = "고객명", example = "{ \"customerSeq\": \"CUSTMR20260000010\", \"customerName\": \"홍길동\" })")  CommonRecordResponse.CustomerInfo customer,
            @Schema(description = "고객담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo customerContact,
            // --- [4. OA 및 계산서 정보] ---
            @Schema(description = "OA대상서류코드", example = "oa청구서류") String oaDocument,
            @Schema(description = "청구내용", example = "출원료 청구") String invContent,
            @Schema(description = "계산서발행일", example = "20260215", format = "YYYYMMDD") String taxBillDate,
            @Schema(description = "발행번호", example = "TX-999") String taxBillNo,
            @Schema(description = "발행구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo taxBillType,
            @Schema(description = "계산서구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo taxBillCategory,

            // --- [5. 사업자정보] ---
            @Schema(description = "상호", example = "(주)마인드프로") String bizName,
            @Schema(description = "대표자", example = "홍길동")   String bizCeo,
            @Schema(description = "사업자번호", example = "123-45-67890") String bizRegNo,
            @Schema(description = "종사업장번호", example = "0000") String bizWorkplaceNo,
            @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
            String bizAddr,
            @Schema(description = "업태", example = "서비스") String bizType,
            @Schema(description = "종목", example = "소프트웨어개발") String bizItem,
            @Schema(description = "담당자", example = "이지금") String bizContactName,
            @Schema(description = "부서", example = "경리부") String bizDeptName,
            @Schema(description = "이메일", example = "acc@mindpro.co.kr") String bizEmail,
            @Schema(description = "비고", example = "특이사항 없음") String note,

            // --- [6. 비용 및 실적] ---
            @Schema(description = "관납료", example = "150000") String govFee,
            @Schema(description = "수수료", example = "300000") String agencyFee,
            @Schema(description = "부가세", example = "30000") String vat,
            @Schema(description = "기타비용", example = "10000") String etcFee,
            @Schema(description = "청구금액", example = "490000") String totalInvAmount,
            @Schema(description = "입금액", example = "0") String depAmount,
            @Schema(description = "미수금", example = "490000") String unpaidAmount,
            @Schema(description = "포기일자", example = "20261231", format = "YYYYMMDD") String abandonDate,
            @Schema(description = "포기금액", example = "0") String abandonAmount,
            @Schema(description = "포기내용", example = "관리 포기") String abandonContent,
            @Schema(description = "관납료납부일", example = "20260212", format = "YYYYMMDD") String govFeePayDate,
            @Schema(description = "관납료납부액", example = "150000") String govFeePayAmount,
            @Schema(description = "외주송금일", example = "20260220", format = "YYYYMMDD") String outsourceDate,
            @Schema(description = "외주내역", example = "번역외주") String outsourceContent,
            @Schema(description = "외주금액", example = "50000") String outsourceAmount,
            @Schema(description = "외주부가세", example = "5000") String outsourceVat,
            @Schema(description = "실적인정일", example = "20260215", format = "YYYYMMDD") String perfDate,
            @Schema(description = "실적금액", example = "300000") String perfAmount,
            @Schema(description = "의뢰인", example = "마인드프로") String clientName,
            @Schema(description = "출원인", example = "(주)마인드프로") String applicantName
    ) {}

    /** [인커밍] 외국청구서 저장 요청 */
    @Builder
    @Schema(description = "외국청구서(인커밍) 저장 요청")
    public record InvoiceIncomingDetail(
            @Schema(description = "출원 시퀀스", example = "APPMST20260000335", format = "SEQ") String appSeq,
            @Schema(description = "고객 시퀀스", example = "CUSTMR20260000010", format = "SEQ") String customerSeq,
            @Schema(description = "사업자 시퀀스", example = "BIZINF20260000001", format = "SEQ") String bizInfoSeq,
            // --- [1. 기본 및 청구 정보] ---
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "청구구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "사건구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "청구분류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "청구종류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "청구일", example = "20261212", format = "YYYYMMDD") String invDate,
            @Schema(description = "청구번호", example = "INC-2026-001") String invNo,
            @Schema(description = "청구서발송일", example = "20261212", format = "YYYYMMDD") String invSendDate,
            @Schema(description = "비용담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo invMgr,
            @Schema(description = "청구내용", example = "일본 특허 출원") String invContent,
//            @Schema(description = "OurRef", example = "MP-INC-001") String ourRef,
//            @Schema(description = "YourRef", example = "AG-REF-99") String yourRef,
            @Schema(description = "출원인관리번호", example = "C-123") String clientRef,
            @Schema(description = "부서", example = "해외팀") String deptName,
            @Schema(description = "관리담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo attorney,
                //고객 담당자
            @Schema(description = "고객명", example = "{ \"customerSeq\": \"CUSTMR20260000010\", \"customerName\": \"홍길동\" })")  CommonRecordResponse.CustomerInfo customer,
            @Schema(description = "고객담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo customerContact,
            @Schema(description = "OA대상서류코드", example = "oa청구서류") String oaDocument,
//
            @Schema(description = "비고", example = "긴급건") String note,
            @Schema(description = "국가코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo country,
            @Schema(description = "해외대리인", example = "Global Law") String foreignAgentName,
            @Schema(description = "출원인", example = "(주)마인드프로") String applicantName,
            @Schema(description = "의뢰인", example = "마인드프로") String clientName,

            // --- [5. 외화정보] ---
            @Schema(description = "화폐단위", example = "{ \"code\": \"USD\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo currencyUnit,
            @Schema(description = "환율적용일", example = "20261212", format = "YYYYMMDD") String exchangeRateDate,
            @Schema(description = "환율", example = "1350.50") String exchangeRate,
            @Schema(description = "외화환산비용", example = "100.00") String foreignCostAmount,
            @Schema(description = "원화금액", example = "135050") String krwAmount,
            @Schema(description = "환차손익", example = "500") String exchangeDiffAmount,

            // --- [6. 비용 및 실적] ---
            @Schema(description = "관납료", example = "50000") String govFee,
            @Schema(description = "수수료", example = "80000") String agencyFee,
            @Schema(description = "부가세", example = "8000") String vat,
            @Schema(description = "번역료", example = "20000") String transFee,
            @Schema(description = "기타비용", example = "20000") String etcFee,
            @Schema(description = "청구금액", example = "158000") String totalInvAmount,
            @Schema(description = "입금액", example = "0") String depAmount,
            @Schema(description = "미수금", example = "158000") String unpaidAmount,
            @Schema(description = "포기일자", example = "20261212", format = "YYYYMMDD") String abandonDate,
            @Schema(description = "포기내용", example = "기일경과 포기") String abandonContent,
            @Schema(description = "포기금액", example = "0") String abandonAmount,
            @Schema(description = "관납료납부일", example = "20261212", format = "YYYYMMDD") String govFeePayDate,
            @Schema(description = "관납료납부액", example = "50000") String govFeePayAmount,
            @Schema(description = "부가세납부일", example = "20261212", format = "YYYYMMDD") String vatPayDate,
            @Schema(description = "외주송금일", example = "20261220", format = "YYYYMMDD") String outsourceDate,
            @Schema(description = "외주내역", example = "대리인 선결제") String outsourceContent,
            @Schema(description = "외주비용", example = "0") String outsourceCost,
            @Schema(description = "실적인정일", example = "20261212", format = "YYYYMMDD") String perfDate,
            @Schema(description = "실적인정금액", example = "80000") String perfAmount
    ) {}

    /** [아웃고잉] 외국청구서 저장 요청 */
    @Builder
    @Schema(description = "외국청구서(아웃고잉) 저장 요청")
    public record InvoiceOutgoingDetail(
            @Schema(description = "출원 시퀀스", example = "APPMST20260000335", format = "SEQ") String appSeq,
            @Schema(description = "고객 시퀀스", example = "CUSTMR20260000010", format = "SEQ") String customerSeq,
            @Schema(description = "사업자 시퀀스", example = "BIZINF20260000001", format = "SEQ") String bizInfoSeq,

            // --- [1. 기본 및 청구 정보] ---
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "청구구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "사건구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "청구분류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "청구종류코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "청구일", example = "20260210", format = "YYYYMMDD") String invDate,
            @Schema(description = "청구번호", example = "OUT-2026-001") String invNo,
            @Schema(description = "청구서발송일", example = "20260211", format = "YYYYMMDD") String invSendDate,
            @Schema(description = "비용담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo invMgr,
            @Schema(description = "대리인청구일", example = "20260115", format = "YYYYMMDD") String agentInvDate,
            @Schema(description = "DEBIT접수일", example = "20260120", format = "YYYYMMDD") String debitReceiptDate,
            @Schema(description = "DEBIT번호", example = "DB-12345") String debitNo,
            @Schema(description = "OurRef", example = "MP-OUT-001") String ourRef,
            @Schema(description = "YourRef", example = "AGENT-999") String yourRef,
            @Schema(description = "출원인관리번호", example = "C-OUT-001") String clientRef,

//            // --- [2. 고객정보] ---

            @Schema(description = "고객담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo customerContact,
            @Schema(description = "고객명", example = "{ \"customerSeq\": \"CUSTMR20260000010\", \"customerName\": \"홍길동\" })") CommonRecordResponse.CustomerInfo customer,
//

            // --- [3. 사건 및 규격 정보 (출원정보)] ---
            @Schema(description = "담당변리사", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo attorney,
            @Schema(description = "국가코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo countryCode,
            @Schema(description = "해외대리인", example = "Global Law") String foreignAgentName,
            @Schema(description = "출원인", example = "(주)마인드프로") String applicantName,
            @Schema(description = "의뢰인", example = "마인드프로") String clientName,
            @Schema(description = "국문명칭", example = "검색 알고리즘") String titleKo,
            @Schema(description = "영문명칭", example = "Search Algo") String titleEn,
            @Schema(description = "류", example = "42") String niceClass,
            @Schema(description = "등급", example = "S") String grade,
            @Schema(description = "독립항", example = "1") String independentClaims,
            @Schema(description = "종속항", example = "5") String dependentClaims,
            @Schema(description = "명세서", example = "15") String overseaSpecCount,
            @Schema(description = "도면수", example = "3") String drawingCount,
            @Schema(description = "국내명세서", example = "12") String specCount,

            // --- [4. 사업자정보] ---

            @Schema(description = "발행번호", example = "TAX-JP-01") String taxBillNo,
            @Schema(description = "발행구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo taxBillType,
            @Schema(description = "계산서구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo taxBillCategory,
            @Schema(description = "상호", example = "(주)마인드프로") String bizName,
            @Schema(description = "대표자", example = "홍길동")   String bizCeo,
            @Schema(description = "사업자번호", example = "123-45-67890") String bizRegNo,
            @Schema(description = "종사업장번호", example = "0000") String bizWorkplaceNo,
            @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...") String bizAddr,
            @Schema(description = "업태", example = "서비스") String bizType,
            @Schema(description = "종목", example = "소프트웨어") String bizItem,
            @Schema(description = "담당자", example = "김이지") String bizContactName,
            @Schema(description = "부서", example = "관리부") String bizDeptName,
            @Schema(description = "이메일", example = "admin@mindpro.co.kr") String bizEmail,
            @Schema(description = "비고", example = "일본 대리인 요청건") String note,

            // --- [5. 외화정보] ---
            @Schema(description = "화폐단위", example = "{ \"code\": \"USD\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo currencyUnit,
            @Schema(description = "환율적용일", example = "20260210", format = "YYYYMMDD") String exchangeRateDate,
            @Schema(description = "환율", example = "9.50") String exchangeRate,
            @Schema(description = "외화환산비용", example = "10000.00") String foreignCostAmount,
            @Schema(description = "원화금액", example = "95000") String krwAmount,
            @Schema(description = "송금외화수수료", example = "500.00") String remitForeignFee,
            @Schema(description = "송금원화수수료", example = "5000") String remitKrwFee,

            // --- [6. 청구 및 비용] ---
            @Schema(description = "OA대상서류코드", example = "oa청구서류") String oaDocument,
            @Schema(description = "청구내용", example = "일본 특허 출원") String invContent,
            @Schema(description = "계산서발행일", example = "20260220", format = "YYYYMMDD") String taxBillDate,
            @Schema(description = "대리인청구구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE") CommonRecordResponse.CodeInfo agentInvCategory,
            @Schema(description = "관납료", example = "30000") String govFee,
            @Schema(description = "수수료", example = "50000") String agencyFee,
            @Schema(description = "부가세", example = "5000") String vat,
            @Schema(description = "기타비용", example = "2000") String etcFee,
            @Schema(description = "청구금액", example = "87000") String totalInvAmount,
            @Schema(description = "입금액", example = "0") String depAmount,
            @Schema(description = "미수금", example = "87000") String unpaidAmount,
            @Schema(description = "포기일자", example = "20261231", format = "YYYYMMDD") String abandonDate,
            @Schema(description = "포기금액", example = "0") String abandonAmount,
            @Schema(description = "포기내용", example = "비용부담 포기") String abandonContent,
            @Schema(description = "관납료납부일", example = "20260212", format = "YYYYMMDD") String govFeePayDate,
            @Schema(description = "관납료납부액", example = "30000") String govFeePayAmount,
            @Schema(description = "외주송금일", example = "20260225", format = "YYYYMMDD") String outsourceDate,
            @Schema(description = "외주내역", example = "현지 관납료 송금") String outsourceContent,
            @Schema(description = "외주비용", example = "30000") String outsourceCost,
            @Schema(description = "외주부가세비용", example = "0") String outsourceVat,
            @Schema(description = "실적인정일", example = "20260215", format = "YYYYMMDD") String perfDate,
            @Schema(description = "실적인정금액", example = "50000") String perfAmount
    ) {}
    @Builder
    public record InvoiceClaimDetail(
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "청구내역 시퀀스", example = "20260215", format = "SEQ")
            String invoiceClaimSeq,
            @Schema(description = "비용구분코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo costCategory,  // 비용구분
            @Schema(description = "청구 내용", example = "기타비용 청구")
            String itemContent,       // 청구내용
            @Schema(description = "단가", example = "1000")
            String unitPrice,     // 단가
            @Schema(description = "단위코드", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")
            CommonRecordResponse.CodeInfo unit,           //단위
            @Schema(description = "수량", example = "2")
            String quantity,      // 수량
            @Schema(description = "금액", example = "1000")
            String amount,         // 금액
            @Schema(description = "부가세", example = "1000")
            String vatAmount,     // 부가세
            @Schema(description = "총합계금액", example = "2000")
            String totalAmount,    // 합계
            @Schema(description = "비고", example = "현금으로 납부")
            String note               // 비고
    ) {}

    @Builder
    public record InvoiceBankingDetail(
            @Schema(description = "송장 마스터 시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "입금 일련번호", example = "BNK202600001", format = "SEQ") String bankingSeq,
            @Schema(description = "입금구분 (DEPOSIT/PREPAY/REMIT)", example = "{ \"code\": \"10\", \"codeName\": \"\" }", format = "CODE") CommonRecordResponse.CodeInfo bankingCategory,
            @Schema(description = "입금/송금일 (YYYYMMDD)", example = "20260220") String depositSendDate,
            @Schema(description = "입금/송금액", example = "500000") String depositAmount,
            @Schema(description = "입금자/수취인명", example = "홍길동") String depositName,
            @Schema(description = "금융기관명", example = "국민은행") String depositBank,
            @Schema(description = "입금 수수료", example = "1000") String depositFee,
            @Schema(description = "수표/ réception일 (YYYYMMDD)", example = "20260220") String depositCheckDate,
            @Schema(description = "비고", example = "계좌이체 확인") String note,
            // ── 선수금 ──
            @Schema(description = "선수금 입금번호") String prepaymentDepositNo,
            @Schema(description = "일반선수금 잔액") String generalPrepaymentBalance,
            @Schema(description = "일반선수금 사용액") String generalPrepaymentUsedAmount,
            @Schema(description = "지정선수금 잔액") String designatedPrepaymentBalance,
            @Schema(description = "지정선수금 사용액") String designatedPrepaymentUsedAmount
    ) {}

    @Builder
    public record PerformanceDetail(
            @Schema(description = "송장 마스터 시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "실적 일련번호", example = "PERF202600001", format = "SEQ") String performanceSeq,
            @Schema(description = "실적 구분", example = "{ \"code\": \"10\", \"codeName\": \"\" })", format = "CODE")  CommonRecordResponse.CodeInfo performanceCategory,
            @Schema(description = "부서", example = "IP부서") String deptCategory,
            @Schema(description = "담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo staff,
            @Schema(description = "실적 상세 내용", example = "사건 담당 실적") String performanceContent,
            @Schema(description = "실적인정일", example = "20260215", format = "YYYYMMDD") String performancePerfDate,
            @Schema(description = "실적 분배 금액", example = "150000") String performanceAmount,
            @Schema(description = "분배 비율 (0~100)", example = "50") String shareRatio,
            @Schema(description = "비고", example = "메인 변리사 실적") String note
    ) {}

    @Builder
    @Schema(description = "해외 송금 저장 요청")
    public record InvoiceForeignBankingDetail(
            @Schema(description = "송금 일련번호 (수정 시)", example = "INVBAK20260000010", format = "SEQ")
            String bankingSeq,

            @Schema(description = "청구서 일련번호", example = "INVMST20260000152", format = "SEQ")
            String invoiceSeq,

            @Schema(description = "화폐단위", example = "{ \"code\": \"USD\", \"codeName\": \"미국 달러\" }", format = "CODE")
            CommonRecordResponse.CodeInfo currencyUnit,

            @Schema(description = "송금일(발송일)", example = "20260319", format = "YYYYMMDD")
            String depositSendDate,

            @Schema(description = "외화송금액 (실제 송금 통화 금액)", example = "1500.50")
            String exchangeAmount,

            @Schema(description = "적용 환율", example = "1350.20")
            String exchangeRatio,

            @Schema(description = "원화환산비용 (최종 원화 금액)", example = "2025975")
            String depositAmount,

            @Schema(description = "송금 수수료 (원화)", example = "5000")
            String depositFee,

            @Schema(description = "송금 방식", example = "{ \"code\": \"10\", \"codeName\": \"전신송금(T/T)\" }", format = "CODE")
            CommonRecordResponse.CodeInfo depositWay,

            @Schema(description = "비고", example = "해외 대리인 특허 출원 비용 송금건")
            String note
    ) {}


}
