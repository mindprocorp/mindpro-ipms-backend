package kr.co.mindpro.ipms.domain.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.util.CommonMapping;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import kr.co.mindpro.ipms.domain.invoice.dto.request.InvoiceRequest;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 청구서 테이블 통합 매핑 객체
 * 8개 섹션의 모든 필드를 포함하며 DB 조회/저장 시 사용됩니다.
 *
 * @author   : min
 * @fileName : InvoiceMergeVO.java
 * @since    : 2026. 01. 07.
 */
@Getter @Setter
public class InvoiceMergeVO extends BaseVO {

    @Schema(description = "사무소 일련번호", example = "OFFICE20260000001")
    private String officeSeq;

    @Schema(description = "청구서 일련번호", example = "INV20260000001", format = "SEQ")
    private String invoiceSeq;

    @Schema(description = "사건 일련번호", example = "APP20260000001", format = "SEQ")
    private String appSeq;

    @Schema(description = "고객 일련번호", example = "CUST20260000001", format = "SEQ")
    private String customerSeq;

    @Schema(description = "사업자정보 일련번호", example = "BIZ20260000001", format = "SEQ")
    private String bizInfoSeq;

    // --- [1. 기본 및 청구 정보] ---
    @Schema(description = "청구 구분 코드", example = "BILL01")
    private String invCategoryCode;

    @Schema(description = "청구 구분 명칭", example = "내국청구")
    private String invCategoryName;

    @Schema(description = "사건 구분 코드 (국내외)", example = "NAT01")
    private String caseCategoryCode;

    @Schema(description = "사건 구분 명칭", example = "국내")
    private String caseCategoryName;

    @Schema(description = "청구 분류 코드", example = "CLASS01")
    private String invClassCode;

    @Schema(description = "청구 분류 명칭", example = "분할청구")
    private String invClassName;

    @Schema(description = "청구 유형 코드", example = "TYPE01")
    private String invTypeCode;

    @Schema(description = "청구 유형 명칭", example = "수수료")
    private String invTypeName;

    @Schema(description = "국내외 구분 (INV: 국내, INV_INC: 국외 수신, INV_OUT: 국외 발신)", example = "INV")
    private String inOutType;

    @CommonMapping(type="DATE", group="INV", description="청구일")
    @Schema(description = "청구일 (YYYY-MM-DD)", example = "2026-02-10")
    private String invDate;

    @Schema(description = "청구번호", example = "INV-2026-001")
    private String invNo;

    @CommonMapping(type="DATE", group="INV", description="청구서발송일")
    @Schema(description = "청구서 발송일 (YYYY-MM-DD)", example = "2026-02-11")
    private String invSendDate;

    @CommonMapping(type="PERSON", group="INV", description="비용담당자")
    @Schema(description = "비용 담당자 일련번호", example = "USR20260000001", format = "SEQ")
    private String invMgr;

    @Schema(description = "비용 담당자 명칭", example = "홍길동")
    private String invMgrName;

    @Schema(description = "관리번호 (Our Ref)", example = "MP-2026-0001")
    private String ourRef;

    @Schema(description = "상대방 관리번호 (Your Ref)", example = "AGENT-REF-99")
    private String yourRef;

    @Schema(description = "고객사 관리번호 (Client Ref)", example = "C-REF-123")
    private String clientRef;

    @Schema(description = "부서명", example = "법무팀")
    private String deptName;

    @CommonMapping(type="PERSON", group="INV", description="관리담당자")
    @Schema(description = "관리 담당자 일련번호", example = "USR20260000002", format = "SEQ")
    private String adminMgr;

    @Schema(description = "관리 담당자 명칭", example = "이관리")
    private String adminMgrName;

    @CommonMapping(type="PERSON", group="INV", description="사건담당자")
    @Schema(description = "사건 담당자 일련번호", example = "USR20260000003", format = "SEQ")
    private String caseMgr;

    @Schema(description = "사건 담당자 명칭", example = "김사건")
    private String caseMgrName;

    @CommonMapping(type="PERSON", group="INV", description="담당변리사")
    @Schema(description = "담당 변리사 일련번호", example = "USR20260000004", format = "SEQ")
    private String attorney;

    @Schema(description = "담당 변리사 명칭", example = "박변리")
    private String attorneyName;

    // --- [검색 전용 필드 (Alias 대응)] ---
    private String adminMgrNm;
    private String caseMgrNm;
    private String attorneyNm;

    @CommonMapping(type="DATE", group="INV", description="대리인청구일")
    @Schema(description = "대리인 청구일 (YYYY-MM-DD)", example = "2026-02-05")
    private String agentInvDate;

    @CommonMapping(type="DATE", group="INV", description="DEBIT접수일")
    @Schema(description = "DEBIT 접수일 (YYYY-MM-DD)", example = "2026-02-06")
    private String debitReceiptDate;

    @Schema(description = "DEBIT 번호", example = "DB-999")
    private String debitNo;

    // --- [2. 출원정보] ---
    @Schema(description = "권리 구분 코드", example = "PATENT")
    private String rightTypeCode;

    @Schema(description = "권리 구분 명칭", example = "특허")
    private String rightTypeName;

    @CommonMapping(type="DATE", group="APP", description="출원일")
    @Schema(description = "출원일 (YYYY-MM-DD)", example = "2025-01-01")
    private String appDate;

    @Schema(description = "출원번호", example = "10-2025-1234567")
    private String appNo;

    @CommonMapping(type="DATE", group="APP", description="등록일")
    @Schema(description = "등록일 (YYYY-MM-DD)", example = "2026-01-01")
    private String regDate;

    @Schema(description = "등록번호", example = "10-1234567-0000")
    private String regNo;

    @CommonMapping(type="DATE", group="APP", description="공고일")
    @Schema(description = "공고일 (YYYY-MM-DD)", example = "2025-06-01")
    private String pubDate;

    @Schema(description = "국가 코드", example = "KR")
    private String countryCode;

    @Schema(description = "국가 명칭", example = "대한민국")
    private String countryName;

    @Schema(description = "국가 명칭 (국문)", example = "대한민국")
    private String countryNameKo;

    @Schema(description = "해외 대리인 일련번호(또는 이름)", example = "Global Law")
    private String foreignAgent;

    private String foreignAgentSeq; // 매퍼(foreignAgentSeq) 대응용

    @Schema(description = "해외 대리인 명칭", example = "Global Law")
    private String foreignAgentName;

    @Schema(description = "출원인 일련번호 (조회 전용)", example = "CUST20260000002", format = "SEQ")
    private String applicant;

    private String applicantSeq; // 매퍼 대응용

    @Schema(description = "출원인 명칭", example = "(주)마인드프로")
    private String applicantName;

    @Schema(description = "의뢰인 일련번호(또는 이름)", example = "마인드프로")
    private String client;

    private String clientSeq; // 매퍼(clientSeq) 대응용

    @Schema(description = "의뢰인 명칭", example = "마인드프로")
    private String clientName;

    private String applicantNm;
    private String clientNm;

    @Schema(description = "국문 명칭 (Title)", example = "인공지능 시스템")
    private String titleKo;

    @Schema(description = "영문 명칭 (Title)", example = "AI System")
    private String titleEn;

    @Schema(description = "NICE 분류", example = "09")
    private String niceClass;

    @Schema(description = "사건 등급", example = "A")
    private String grade;

    @Schema(description = "독립항 수", example = "3")
    private String independentClaims;

    @Schema(description = "종속항 수", example = "10")
    private String dependentClaims;

    @Schema(description = "해외 명세서 면수", example = "25")
    private String overseaSpecCount;

    @Schema(description = "도면 수", example = "5")
    private String drawingCount;

    @Schema(description = "도수 (Figures)", example = "8")
    private String figureCount;

    @Schema(description = "국내 명세서 면수", example = "20")
    private String specCount;

    @Schema(description = "국내 등록 결정일 (YYYYMMDD)", example = "20260101")
    private String domesticRegDecisionDate;

    @Schema(description = "국내 등록일 (YYYYMMDD)", example = "20260101")
    private String domesticRegDate;

    @Schema(description = "국제 등록일 (YYYYMMDD)", example = "20260101")
    private String intlRegDate;

    @Schema(description = "국내 등록번호", example = "2026-1234567")
    private String domesticRegNo;

    // --- [3. 고객정보] ---
    @Schema(description = "고객 명칭", example = "삼성전자")
    private String customerName;

    @CommonMapping(type="PERSON", group="INV")
    @Schema(description = "고객 담당자 일련번호", example = "USR20260000005", format = "SEQ")
    private String customerContact;

    @Schema(description = "고객 담당자 명칭", example = "정본부장")
    private String customerContactName;

    // --- [4. OA 및 계산서 정보] ---
    @Schema(description = "OA 대상 서류 코드", example = "OA01")
    private String oaDocument;

    @Schema(description = "청구 내용", example = "출원비용 청구")
    private String invContent;

    @CommonMapping(type="DATE", group="INV", description="계산서발행일")
    @Schema(description = "계산서 발행일 (YYYY-MM-DD)", example = "2026-02-15")
    private String taxBillDate;

    @Schema(description = "발행 번호", example = "TX-999")
    private String taxBillNo;

    @Schema(description = "발행 구분 코드", example = "TB01")
    private String taxBillTypeCode;

    @Schema(description = "발행 구분 명칭", example = "세금계산서")
    private String taxBillTypeName;

    @Schema(description = "계산서 구분 코드", example = "TC02")
    private String taxBillCategoryCode;

    @Schema(description = "계산서 구분 명칭", example = "영수")
    private String taxBillCategoryName;

    @Schema(description = "사업장 상호", example = "(주)마인드프로")
    private String bizName;

    @Schema(description = "대표자 성명", example = "홍길동")
    private String bizCeo;

    @Schema(description = "사업자 등록번호", example = "123-45-67890")
    private String bizRegNo;

    @Schema(description = "종사업장 번호", example = "0000")
    private String bizWorkplaceNo;

    @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
    private String bizAddr;

    @Schema(description = "업태", example = "서비스")
    private String bizType;

    @Schema(description = "종목", example = "소프트웨어")
    private String bizItem;

    @Schema(description = "사업자 담당자 성명", example = "이지금")
    private String bizContactName;

    @Schema(description = "사업자 부서명", example = "경리부")
    private String bizDeptName;

    @Schema(description = "사업자 이메일", example = "acc@mindpro.co.kr")
    private String bizEmail;

    @Schema(description = "비고", example = "특이사항 없음")
    private String note;

    @Schema(description = "대리인 청구 구분 코드", example = "AGT01")
    private String agentInvCategoryCode;

    @Schema(description = "대리인 청구 구분 명칭", example = "국내대리인")
    private String agentInvCategoryName;

    // --- [5. 외화 정보] ---
    @Schema(description = "화폐 단위 코드", example = "USD")
    private String currencyUnitCode;

    @Schema(description = "화폐 단위 명칭", example = "미국 달러")
    private String currencyUnitName;

    @CommonMapping(type="DATE", group="INV", description="환율적용일")
    @Schema(description = "환율 적용일 (YYYY-MM-DD)", example = "2026-02-10")
    private String exchangeRateDate;

    @Schema(description = "적용 환율", example = "1350.5")
    private String exchangeRate;

    @Schema(description = "외화 환산 비용", example = "100.0")
    private String foreignCostAmount;

    @CommonMapping(type="COST", group="INV", description="원화금액")
    @Schema(description = "원화 금액", example = "135050")
    private String krwAmount;

    @CommonMapping(type="COST", group="INV", description="환차손익")
    private String exchangeDiffAmount;

    @CommonMapping(type="COST", group="INV", description="송금 외화 수수료")
    private String remitForeignFee;

    @CommonMapping(type="COST", group="INV", description="송금 원화 수수료")
    private String remitKrwFee;

    // --- [6. 비용 및 실적] ---
    @CommonMapping(type="COST", group="INV", description="관납료")
    private String govFee;

    @CommonMapping(type="COST", group="INV", description="수수료")
    private String agencyFee;

    @CommonMapping(type="COST", group="INV", description="부가세")
    @Schema(description = "부가세", example = "30000")
    private String vat;

    @CommonMapping(type="COST", group="INV", description="기타 비용")
    @Schema(description = "기타 비용", example = "10000")
    private String etcFee;

    @CommonMapping(type="COST", group="INV", description="번역료")
    @Schema(description = "번역료", example = "50000")
    private String transFee;

    @CommonMapping(type="COST", group="INV", description="청구 금액")
    @Schema(description = "청구 금액", example = "490000")
    private String totalInvAmount;

    @CommonMapping(type="COST", group="INV", description="입금액")
    @Schema(description = "입금액", example = "0")
    private String depAmount;

    @CommonMapping(type="COST", group="INV", description="미수금")
    @Schema(description = "미수금", example = "490000")
    private String unpaidAmount;

    @CommonMapping(type="DATE", group="INV", description="포기일자")
    @Schema(description = "포기 일자 (YYYY-MM-DD)", example = "")
    private String abandonDate;

    @CommonMapping(type="COST", group="INV", description="포기금액")
    @Schema(description = "포기금액", example = "0")
    private String abandonAmount;

    @Schema(description = "포기 내용", example = "")
    private String abandonContent;

    @CommonMapping(type="DATE", group="INV", description="관납료납부일")
    @Schema(description = "관납료 납부일 (YYYY-MM-DD)", example = "2026-02-12")
    private String govFeePayDate;

    @CommonMapping(type="COST", group="INV", description="관납료납부액")
    @Schema(description = "관납료 납부일 (YYYY-MM-DD)", example = "2026-02-12")
    private String govFeePayAmount;

    @CommonMapping(type="DATE", group="INV", description="부가세납부일")
    @Schema(description = "부가세 납부일 (YYYY-MM-DD)", example = "2026-02-12")
    private String vatPayDate;

    @CommonMapping(type="DATE", group="INV", description="외주송금일")
    @Schema(description = "외주 송금일 (YYYY-MM-DD)", example = "2026-02-20")
    private String outsourceDate;

    @Schema(description = "외주 내역", example = "번역외주")
    private String outsourceContent;

    @CommonMapping(type="COST", group="INV", description="외주비용")
    @Schema(description = "외주비용", example = "50000")
    private String outsourceCost;

    @CommonMapping(type="COST", group="INV", description="외주부가세")
    @Schema(description = "외주부가세", example = "5000")
    private String outsourceVat;

    @CommonMapping(type="DATE", group="INV", description="실적인정일")
    @Schema(description = "실적 인정일 (YYYY-MM-DD)", example = "2026-02-15")
    private String perfDate;

    @CommonMapping(type="COST", group="INV", description="실적금액")
    @Schema(description = "실적금액", example = "300000")
    private String perfAmount;

    @Schema(description = "입금일 (YYYY-MM-DD)", example = "2026-02-28")
    private String depositDate;

        /* =========================================================================
         * [Mapping Logic] - FULL VERSION
         * ========================================================================= */

        public void fillFromDomestic(InvoiceRequest.InvoiceDomesticDetail req) {
            this.invoiceSeq = req.invoiceSeq();
            this.appSeq = req.appSeq();
            this.customerSeq = req.customerSeq();
            this.bizInfoSeq = req.bizInfoSeq();
            
            if (req.invCategory() != null) {
                this.invCategoryCode = req.invCategory().code();
                this.invCategoryName = req.invCategory().codeName();
            }
            if (req.caseCategory() != null) {
                this.caseCategoryCode = req.caseCategory().code();
                this.caseCategoryName = req.caseCategory().codeName();
            }
            if (req.invType() != null) {
                this.invTypeCode = req.invType().code();
                this.invTypeName = req.invType().codeName();
            }
            if (req.invClass() != null) {
                this.invClassCode = req.invClass().code();
                this.invClassName = req.invClass().codeName();
            }
            
            this.invDate = req.invDate();
            this.invNo = req.invNo();
            this.invSendDate = req.invSendDate();
            
            if (req.invMgr() != null) {
                this.invMgr = req.invMgr().userSeq();
                this.invMgrName = req.invMgr().userName();
            }
            
            this.ourRef = req.ourRef();
            this.clientRef = req.clientRef();
            this.deptName = req.deptName();
            
            if (req.adminMgr() != null) {
                this.adminMgr = req.adminMgr().userSeq();
                this.adminMgrName = req.adminMgr().userName();
            }
            if (req.caseMgr() != null) {
                this.caseMgr = req.caseMgr().userSeq();
                this.caseMgrName = req.caseMgr().userName();
            }
            if (req.attorney() != null) {
                this.attorney = req.attorney().userSeq();
                this.attorneyName = req.attorney().userName();
            }
            if (req.customerContact() != null) {
                this.customerContact = req.customerContact().userSeq();
                this.customerContactName = req.customerContact().userName();
            }

            this.oaDocument = req.oaDocument();
            this.invContent = req.invContent();
            this.taxBillDate = req.taxBillDate();
            this.taxBillNo = req.taxBillNo();
            
            if (req.taxBillType() != null) {
                this.taxBillTypeCode = req.taxBillType().code();
                this.taxBillTypeName = req.taxBillType().codeName();
            }
            if (req.taxBillCategory() != null) {
                this.taxBillCategoryCode = req.taxBillCategory().code();
                this.taxBillCategoryName = req.taxBillCategory().codeName();
            }
            
            this.bizName = req.bizName();
            this.bizCeo = req.bizCeo();
            this.bizRegNo = req.bizRegNo();
            this.bizWorkplaceNo = req.bizWorkplaceNo();
            this.bizAddr = req.bizAddr();
            this.bizType = req.bizType();
            this.bizItem = req.bizItem();
            this.bizContactName = req.bizContactName();
            this.bizDeptName = req.bizDeptName();
            this.bizEmail = req.bizEmail();
            this.note = req.note();
            this.govFee = req.govFee();
            this.agencyFee = req.agencyFee();
            this.vat = req.vat();
            this.etcFee = req.etcFee();
            this.totalInvAmount = req.totalInvAmount();
            this.depAmount = req.depAmount();
            this.unpaidAmount = req.unpaidAmount();
            this.abandonDate = req.abandonDate();
            this.abandonAmount = req.abandonAmount();
            this.abandonContent = req.abandonContent();
            this.govFeePayDate = req.govFeePayDate();
            this.govFeePayAmount = req.govFeePayAmount();
            this.outsourceDate = req.outsourceDate();
            this.outsourceContent = req.outsourceContent();
            this.outsourceCost = req.outsourceAmount();
            this.outsourceVat = req.outsourceVat();
            this.perfDate = req.perfDate();
            this.perfAmount = req.perfAmount();
            
            // 참가자 정보
            this.client = req.clientName();
            this.clientSeq = this.client;
            this.clientName = this.client;
            this.applicant = req.applicantName();
            this.applicantSeq = this.applicant;
            this.applicantName = this.applicant;
        }

        public void fillFromIncoming(InvoiceRequest.InvoiceIncomingDetail req) {
            this.invoiceSeq = req.invoiceSeq();
            this.appSeq = req.appSeq();
            this.customerSeq = req.customer() != null ? req.customer().customerSeq() : null;
            this.bizInfoSeq = req.bizInfoSeq();
            
            if (req.invCategory() != null) {
                this.invCategoryCode = req.invCategory().code();
                this.invCategoryName = req.invCategory().codeName();
            }
            if (req.caseCategory() != null) {
                this.caseCategoryCode = req.caseCategory().code();
                this.caseCategoryName = req.caseCategory().codeName();
            }
            if (req.invClass() != null) {
                this.invClassCode = req.invClass().code();
                this.invClassName = req.invClass().codeName();
            }
            if (req.invType() != null) {
                this.invTypeCode = req.invType().code();
                this.invTypeName = req.invType().codeName();
            }
            
            this.invDate = req.invDate();
            this.invNo = req.invNo();
            this.invSendDate = req.invSendDate();
            
            if (req.invMgr() != null) {
                this.invMgr = req.invMgr().userSeq();
                this.invMgrName = req.invMgr().userName();
            }
            
            this.clientRef = req.clientRef();
            this.deptName = req.deptName();
            
            if (req.adminMgr() != null) {
                this.adminMgr = req.adminMgr().userSeq();
                this.adminMgrName = req.adminMgr().userName();
            }
            if (req.caseMgr() != null) {
                this.caseMgr = req.caseMgr().userSeq();
                this.caseMgrName = req.caseMgr().userName();
            }
            if (req.attorney() != null) {
                this.attorney = req.attorney().userSeq();
                this.attorneyName = req.attorney().userName();
            }
            if (req.customerContact() != null) {
                this.customerContact = req.customerContact().userSeq();
                this.customerContactName = req.customerContact().userName();
            }

            this.countryCode = req.country() != null ? req.country().code() : null;
            this.foreignAgent = req.foreignAgentName();
            this.foreignAgentSeq = this.foreignAgent;
            this.foreignAgentName = this.foreignAgent;
            this.applicant = req.applicantName();
            this.applicantSeq = this.applicant;
            this.applicantName = this.applicant;
            this.client = req.clientName();
            this.clientSeq = this.client;
            this.clientName = this.client;

            // 외화 및 비용
            this.oaDocument = req.oaDocument();
            this.invContent = req.invContent();
            this.note = req.note();
            
            if (req.currencyUnit() != null) {
                this.currencyUnitCode = req.currencyUnit().code();
                this.currencyUnitName = req.currencyUnit().codeName();
            }
            
            this.exchangeRateDate = req.exchangeRateDate();
            this.exchangeRate = req.exchangeRate();
            this.foreignCostAmount = req.foreignCostAmount();
            this.krwAmount = req.krwAmount();
            this.exchangeDiffAmount = req.exchangeDiffAmount();
            this.govFee = req.govFee();
            this.agencyFee = req.agencyFee();
            this.vat = req.vat();
            this.transFee = req.transFee();
            this.etcFee = req.etcFee();
            this.totalInvAmount = req.totalInvAmount();
            this.depAmount = req.depAmount();
            this.unpaidAmount = req.unpaidAmount();
            this.abandonDate = req.abandonDate();
            this.abandonContent = req.abandonContent();
            this.abandonAmount = req.abandonAmount();
            this.govFeePayDate = req.govFeePayDate();
            this.govFeePayAmount = req.govFeePayAmount();
            this.vatPayDate = req.vatPayDate();
            this.outsourceDate = req.outsourceDate();
            this.outsourceContent = req.outsourceContent();
            this.outsourceCost = req.outsourceCost();
            this.perfDate = req.perfDate();
            this.perfAmount = req.perfAmount();
        }

        public void fillFromOutgoing(InvoiceRequest.InvoiceOutgoingDetail req) {
            this.invoiceSeq = req.invoiceSeq();
            this.appSeq = req.appSeq();
            this.customerSeq = req.customerSeq();
            this.bizInfoSeq = req.bizInfoSeq();
            
            if (req.attorney() != null) {
                this.attorney = req.attorney().userSeq();
                this.attorneyName = req.attorney().userName();
            }
            if (req.invCategory() != null) {
                this.invCategoryCode = req.invCategory().code();
                this.invCategoryName = req.invCategory().codeName();
            }
            if (req.caseCategory() != null) {
                this.caseCategoryCode = req.caseCategory().code();
                this.caseCategoryName = req.caseCategory().codeName();
            }
            if (req.invClass() != null) {
                this.invClassCode = req.invClass().code();
                this.invClassName = req.invClass().codeName();
            }
            if (req.invType() != null) {
                this.invTypeCode = req.invType().code();
                this.invTypeName = req.invType().codeName();
            }
            
            this.invDate = req.invDate();
            this.invNo = req.invNo();
            this.invSendDate = req.invSendDate();
            
            if (req.invMgr() != null) {
                this.invMgr = req.invMgr().userSeq();
                this.invMgrName = req.invMgr().userName();
            }
            
            this.agentInvDate = req.agentInvDate();
            this.debitReceiptDate = req.debitReceiptDate();
            this.debitNo = req.debitNo();
            this.ourRef = req.ourRef();
            this.yourRef = req.yourRef();
            this.clientRef = req.clientRef();

            if (req.customerContact() != null) {
                this.customerContact = req.customerContact().userSeq();
                this.customerContactName = req.customerContact().userName();
            }

            // 사건 및 규격
            if (req.countryCode() != null) {
                this.countryCode = req.countryCode().code();
                this.countryName = req.countryCode().codeName();
            }
            
            this.foreignAgent = req.foreignAgentName();
            this.foreignAgentSeq = this.foreignAgent;
            this.foreignAgentName = this.foreignAgent;
            this.applicant = req.applicantName();
            this.applicantSeq = this.applicant;
            this.applicantName = this.applicant;
            this.client = req.clientName();
            this.clientSeq = this.client;
            this.clientName = this.client;
            
            this.titleKo = req.titleKo();
            this.titleEn = req.titleEn();
            this.niceClass = req.niceClass();
            this.grade = req.grade();
            this.independentClaims = req.independentClaims();
            this.dependentClaims = req.dependentClaims();
            this.overseaSpecCount = req.overseaSpecCount();
            this.drawingCount = req.drawingCount();
            this.specCount = req.specCount();

            // 사업자 및 비용
            this.taxBillNo = req.taxBillNo();
            
            if (req.taxBillType() != null) {
                this.taxBillTypeCode = req.taxBillType().code();
                this.taxBillTypeName = req.taxBillType().codeName();
            }
            if (req.taxBillCategory() != null) {
                this.taxBillCategoryCode = req.taxBillCategory().code();
                this.taxBillCategoryName = req.taxBillCategory().codeName();
            }
            
            this.bizName = req.bizName();
            this.bizCeo = req.bizCeo();
            this.bizRegNo = req.bizRegNo();
            this.bizAddr = req.bizAddr();
            this.bizWorkplaceNo = req.bizWorkplaceNo();
            this.bizType = req.bizType();
            this.bizItem = req.bizItem();
            this.bizContactName = req.bizContactName();
            this.bizDeptName = req.bizDeptName();
            this.bizEmail = req.bizEmail();
            this.note = req.note();

            // 외화 정보
            if (req.currencyUnit() != null) {
                this.currencyUnitCode = req.currencyUnit().code();
                this.currencyUnitName = req.currencyUnit().codeName();
            }
            
            this.exchangeRateDate = req.exchangeRateDate();
            this.exchangeRate = req.exchangeRate();
            this.foreignCostAmount = req.foreignCostAmount();
            this.krwAmount = req.krwAmount();
            this.remitForeignFee = req.remitForeignFee();
            this.remitKrwFee = req.remitKrwFee();

            // 청구 정보
            this.oaDocument = req.oaDocument();
            this.invContent = req.invContent();
            this.taxBillDate = req.taxBillDate();
            
            if (req.agentInvCategory() != null) {
                this.agentInvCategoryCode = req.agentInvCategory().code();
                this.agentInvCategoryName = req.agentInvCategory().codeName();
            }

            this.govFee = req.govFee();
            this.agencyFee = req.agencyFee();
            this.vat = req.vat();
            this.etcFee = req.etcFee();
            this.totalInvAmount = req.totalInvAmount();
            this.depAmount = req.depAmount();
            this.unpaidAmount = req.unpaidAmount();

            this.abandonDate = req.abandonDate();
            this.abandonAmount = req.abandonAmount();
            this.abandonContent = req.abandonContent();
            this.govFeePayDate = req.govFeePayDate();
            this.govFeePayAmount = req.govFeePayAmount();
            this.outsourceDate = req.outsourceDate();
            this.outsourceContent = req.outsourceContent();
            this.outsourceCost = req.outsourceCost();
            this.outsourceVat = req.outsourceVat();
            this.perfDate = req.perfDate();
            this.perfAmount = req.perfAmount();
        }
}
