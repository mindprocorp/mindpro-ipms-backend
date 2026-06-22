package kr.co.mindpro.ipms.domain.invoice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.domain.invoice.vo.InvoiceMergeVO;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import lombok.Builder;
import lombok.Data;

@Data
public class InvoiceResponse {

    @Builder
    @Schema(description = "청구서 저장 요청")
    public record InvoiceDomesticDetail(
            @Schema(description = "출원 시퀀스") String appSeq,
            @Schema(description = "고객 시퀀스") String customerSeq,
            @Schema(description = "사업자 시퀀스") String bizInfoSeq,
            @Schema(description = "시퀀스") String invoiceSeq,

            // 코드 및 담당자 객체화
            @Schema(description = "청구구분") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "청구분류") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "청구종류") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "비용담당자") CommonRecordResponse.PersonInfo invMgr,
            @Schema(description = "관리담당자") CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자") CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사") CommonRecordResponse.PersonInfo attorney,


            @Schema(description = "청구일") String invDate,
            @Schema(description = "청구번호") String invNo,
            @Schema(description = "청구서발송일") String invSendDate,
            @Schema(description = "OurRef") String ourRef,
            @Schema(description = "출원인 관리번호") String clientRef,
            @Schema(description = "부서") String deptName,

            // 출원정보
            @Schema(description = "사건구분(국내외)") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "권리") CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "출원일") String appDate,
            @Schema(description = "출원번호") String appNo,
            @Schema(description = "등록일") String regDate,
            @Schema(description = "등록번호") String regNo,
            @Schema(description = "등급") String grade,
            @Schema(description = "독립항") String independentClaims,
            @Schema(description = "종속항") String dependentClaims,
            @Schema(description = "명세서") String specCount,
            @Schema(description = "도면수") String drawingCount,
            @Schema(description = "도수") String figureCount,
            @Schema(description = "출원인") String applicantName,
            @Schema(description = "의뢰인") String clientName,
            @Schema(description = "국문명칭") String titleKo,
            @Schema(description = "영문명칭") String titleEn,
            @Schema(description = "류") String niceClass,

            // 고객 및 OA/계산서
            @Schema(description = "고객명") CommonRecordResponse.CustomerInfo customer,
            @Schema(description = "고객담당자") CommonRecordResponse.PersonInfo customerContact,
            @Schema(description = "OA대상서류") String oaDocument,
            @Schema(description = "청구내용") String invContent,
            @Schema(description = "계산서발행일") String taxBillDate,
            @Schema(description = "발행번호") String taxBillNo,
            @Schema(description = "발행구분") CommonRecordResponse.CodeInfo taxBillType,
            @Schema(description = "계산서구분") CommonRecordResponse.CodeInfo taxBillCategory,

            // 사업자정보
            @Schema(description = "상호") String bizName,
            @Schema(description = "대표자") String bizCeo,
            @Schema(description = "사업자번호") String bizRegNo,
            @Schema(description = "종사업장번호") String bizWorkplaceNo,
            @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
            String bizAddr,
            @Schema(description = "업태") String bizType,
            @Schema(description = "종목") String bizItem,
            @Schema(description = "담당자") String bizContactName,
            @Schema(description = "부서") String bizDeptName,
            @Schema(description = "이메일") String bizEmail,
            @Schema(description = "비고") String note,

            // 비용 및 실적
            @Schema(description = "관납료") String govFee,
            @Schema(description = "수수료") String agencyFee,
            @Schema(description = "부가세") String vat,
            @Schema(description = "기타비용") String etcFee,
            @Schema(description = "청구금액") String totalInvAmount,
            @Schema(description = "입금액") String depAmount,
            @Schema(description = "미수금") String unpaidAmount,
            @Schema(description = "포기일자") String abandonDate,
            @Schema(description = "포기금액") String abandonAmount,
            @Schema(description = "포기내용") String abandonContent,
            @Schema(description = "관납료납부일") String govFeePayDate,
            @Schema(description = "관납료납부액") String govFeePayAmount,
            @Schema(description = "외주송금일") String outsourceDate,
            @Schema(description = "외주내역") String outsourceContent,
            @Schema(description = "외주금액") String outsourceAmount,
            @Schema(description = "외주부가세") String outsourceVat,
            @Schema(description = "실적인정일") String perfDate,
            @Schema(description = "실적금액") String perfAmount,
            @Schema(description = "입금일") String depositDate,
            @Schema(description = "등록일시 (createAt)", example = "2026-04-25 13:30:00")
            String createAt
    ) {
        private static String nvl0(String val) {
            return org.springframework.util.StringUtils.hasText(val) ? val : "0";
        }

        public static InvoiceDomesticDetail from(InvoiceMergeVO vo) {
            return InvoiceDomesticDetail.builder()
                    .appSeq(vo.getAppSeq())
                    .customerSeq(vo.getCustomerSeq())
                    .bizInfoSeq(vo.getBizInfoSeq())
                    .invoiceSeq(vo.getInvoiceSeq())
                    .invCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getInvCategoryCode()).codeName(vo.getInvCategoryName()).build())
                    .invClass(CommonRecordResponse.CodeInfo.builder().code(vo.getInvClassCode()).codeName(vo.getInvClassName()).build())
                    .invType(CommonRecordResponse.CodeInfo.builder().code(vo.getInvTypeCode()).codeName(vo.getInvTypeName()).build())
                    .invMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getInvMgr()).userName(vo.getInvMgrName()).build())
                    .adminMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrName()).build())
                    .caseMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                    .attorney(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())
                    .caseCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getCaseCategoryCode()).codeName(vo.getCaseCategoryName()).build())
                    .invDate(vo.getInvDate())
                    .invNo(vo.getInvNo())
                    .invSendDate(vo.getInvSendDate())
                    .ourRef(vo.getOurRef())
                    .clientRef(vo.getClientRef())
                    .deptName(vo.getDeptName())
                    .rightType(CommonRecordResponse.CodeInfo.builder().code(vo.getRightTypeCode()).codeName(vo.getRightTypeName()).build())
                    .appDate(vo.getAppDate())
                    .appNo(vo.getAppNo())
                    .regDate(vo.getRegDate())
                    .regNo(vo.getRegNo())
                    .grade(vo.getGrade())
                    .independentClaims(vo.getIndependentClaims())
                    .dependentClaims(vo.getDependentClaims())
                    .specCount(vo.getSpecCount())
                    .drawingCount(vo.getDrawingCount())
                    .figureCount(vo.getFigureCount())
                    .applicantName(vo.getApplicantName())
                    .clientName(vo.getClientName())
                    .titleKo(vo.getTitleKo())
                    .titleEn(vo.getTitleEn())
                    .niceClass(vo.getNiceClass())
                    .customer(CommonRecordResponse.CustomerInfo.builder()
                            .customerSeq(vo.getCustomerSeq())
                            .customerName(vo.getCustomerName())
                            .build())
                    .customerContact(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCustomerContact()).userName(vo.getCustomerContactName()).build())
                    .oaDocument(vo.getOaDocument())
                    .invContent(vo.getInvContent())
                    .taxBillDate(vo.getTaxBillDate())
                    .taxBillNo(vo.getTaxBillNo())
                    .taxBillType(CommonRecordResponse.CodeInfo.builder().code(vo.getTaxBillTypeCode()).codeName(vo.getTaxBillTypeName()).build())
                    .taxBillCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getTaxBillCategoryCode()).codeName(vo.getTaxBillCategoryName()).build())
                    .bizName(vo.getBizName())
                    .bizCeo(vo.getBizCeo())
                    .bizRegNo(vo.getBizRegNo())
                    .bizWorkplaceNo(vo.getBizWorkplaceNo())
                    .bizAddr(vo.getBizAddr())
                    .bizType(vo.getBizType())
                    .bizItem(vo.getBizItem())
                    .bizContactName(vo.getBizContactName())
                    .bizDeptName(vo.getBizDeptName())
                    .bizEmail(vo.getBizEmail())
                    .note(vo.getNote())
                    .govFee(nvl0(vo.getGovFee()))
                    .agencyFee(nvl0(vo.getAgencyFee()))
                    .vat(nvl0(vo.getVat()))
                    .etcFee(nvl0(vo.getEtcFee()))
                    .totalInvAmount(nvl0(vo.getTotalInvAmount()))
                    .depAmount(nvl0(vo.getDepAmount()))
                    .unpaidAmount(nvl0(vo.getUnpaidAmount()))
                    .abandonAmount(nvl0(vo.getAbandonAmount()))
                    .govFeePayAmount(nvl0(vo.getGovFeePayAmount()))
                    .outsourceAmount(nvl0(vo.getOutsourceCost()))
                    .outsourceVat(nvl0(vo.getOutsourceVat()))
                    .perfAmount(nvl0(vo.getPerfAmount()))
                    .depositDate(vo.getDepositDate())
                    .createAt(DataConvertUtil.formatOffsetDateTime(vo.getCreateAt()))
                    .build();
        }
    }

    /** [인커밍] InvoiceMergeVO -> InvoiceIncomingDetail */
    @Builder
    @Schema(description = "인커밍 청구서 상세 응답")
    public record InvoiceIncomingDetail(
            @Schema(description = "출원 시퀀스") String appSeq,
            @Schema(description = "고객 시퀀스") String customerSeq,
            @Schema(description = "시퀀스") String invoiceSeq,

            @Schema(description = "청구구분") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "청구분류") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "청구종류") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "비용담당자") CommonRecordResponse.PersonInfo invMgr,
            @Schema(description = "관리담당자") CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자") CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사") CommonRecordResponse.PersonInfo attorney,


            @Schema(description = "청구일") String invDate,
            @Schema(description = "청구번호") String invNo,
            @Schema(description = "청구서발송일") String invSendDate,
            @Schema(description = "OurRef") String ourRef,
            @Schema(description = "YourRef") String yourRef,
            @Schema(description = "출원인관리번호") String clientRef,
            @Schema(description = "부서") String deptName,

            // 출원 및 고객 정보
            @Schema(description = "사건구분코드") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "권리") CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "출원일") String appDate,
            @Schema(description = "출원번호") String appNo,
            @Schema(description = "등록일") String regDate,
            @Schema(description = "등록번호") String regNo,
            @Schema(description = "국가코드") CommonRecordResponse.CodeInfo country,
            //@Schema(description = "국가명(국문)") String countryNameKo,
            @Schema(description = "출원인") String applicantName,
            @Schema(description = "해외대리인") String foreignAgentName,
            @Schema(description = "의뢰인") String clientName,
            @Schema(description = "고객명") CommonRecordResponse.CustomerInfo customer,
            @Schema(description = "고객담당자") CommonRecordResponse.PersonInfo customerContact,

            // 명칭 및 외화
            @Schema(description = "국문명칭") String titleKo,
            @Schema(description = "영문명칭") String titleEn,
            @Schema(description = "류") String niceClass,
            @Schema(description = "OA항목") String oaDocument,
            @Schema(description = "청구내용") String invContent,
            @Schema(description = "비고") String note,
            @Schema(description = "화폐단위") CommonRecordResponse.CodeInfo currencyUnit,
            @Schema(description = "환율적용일") String exchangeRateDate,
            @Schema(description = "환율") String exchangeRate,
            @Schema(description = "외화환산비용") String foreignCostAmount,
            @Schema(description = "원화금액") String krwAmount,
            @Schema(description = "환차손익") String exchangeDiffAmount,

            // 비용 및 실적
            @Schema(description = "관납료") String govFee,
            @Schema(description = "수수료") String agencyFee,
            @Schema(description = "부가세") String vat,
            @Schema(description = "번역료") String transFee,
            @Schema(description = "기타비용", example = "20000") String etcFee,
            @Schema(description = "청구금액") String totalInvAmount,
            @Schema(description = "입금액") String depAmount,
            @Schema(description = "미수금") String unpaidAmount,
            @Schema(description = "포기일자") String abandonDate,
            @Schema(description = "포기내용") String abandonContent,
            @Schema(description = "포기금액") String abandonAmount,
            @Schema(description = "관납료납부일") String govFeePayDate,
            @Schema(description = "관납료납부액") String govFeePayAmount,
            @Schema(description = "부가세납부일") String vatPayDate,
            @Schema(description = "외주송금일") String outsourceDate,
            @Schema(description = "외주내역") String outsourceContent,
            @Schema(description = "외주비용") String outsourceCost,
            @Schema(description = "실적인정일") String perfDate,
            @Schema(description = "실적인정금액") String perfAmount,
            @Schema(description = "입금일") String depositDate,
            @Schema(description = "등록일시 (createAt)", example = "2026-04-25 13:30:00")
            String createAt
    ) {
        private static String nvl0(String val) {
            return org.springframework.util.StringUtils.hasText(val) ? val : "0";
        }

        public static InvoiceIncomingDetail from(InvoiceMergeVO vo) {
            return InvoiceIncomingDetail.builder()
                    .appSeq(vo.getAppSeq())
                    .customerSeq(vo.getCustomerSeq())
                    .invoiceSeq(vo.getInvoiceSeq())
                    .invCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getInvCategoryCode()).codeName(vo.getInvCategoryName()).build())
                    .invClass(CommonRecordResponse.CodeInfo.builder().code(vo.getInvClassCode()).codeName(vo.getInvClassName()).build())
                    .invType(CommonRecordResponse.CodeInfo.builder().code(vo.getInvTypeCode()).codeName(vo.getInvTypeName()).build())
                    .invMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getInvMgr()).userName(vo.getInvMgrName()).build())
                    .adminMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAdminMgr()).userName(vo.getAdminMgrName()).build())
                    .caseMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                    .attorney(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())
                    .caseCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getCaseCategoryCode()).codeName(vo.getCaseCategoryName()).build())
                    .invDate(vo.getInvDate())
                    .invNo(vo.getInvNo())
                    .invSendDate(vo.getInvSendDate())
                    .ourRef(vo.getOurRef())
                    .yourRef(vo.getYourRef())
                    .clientRef(vo.getClientRef())
                    .deptName(vo.getDeptName())
                    .rightType(CommonRecordResponse.CodeInfo.builder().code(vo.getRightTypeCode()).codeName(vo.getRightTypeName()).build())
                    .appDate(vo.getAppDate())
                    .appNo(vo.getAppNo())
                    .regDate(vo.getRegDate())
                    .regNo(vo.getRegNo())
                    .country(CommonRecordResponse.CodeInfo.builder().code(vo.getCountryCode()).codeName(vo.getCountryName()).build())
                    //.countryNameKo(vo.getCountryNameKo())
                    .applicantName(vo.getApplicantName())
                    .foreignAgentName(vo.getForeignAgentName())
                    .clientName(vo.getClientName())
                    .customer(CommonRecordResponse.CustomerInfo.builder()
                            .customerSeq(vo.getCustomerSeq())
                            .customerName(vo.getCustomerName())
                            .build())
                    .customerContact(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCustomerContact()).userName(vo.getCustomerContactName()).build())
                    .titleKo(vo.getTitleKo())
                    .titleEn(vo.getTitleEn())
                    .niceClass(vo.getNiceClass())
                    .oaDocument(vo.getOaDocument())
                    .invContent(vo.getInvContent())
                    .note(vo.getNote())
                    .currencyUnit(CommonRecordResponse.CodeInfo.builder().code(vo.getCurrencyUnitCode()).codeName(vo.getCurrencyUnitName()).build())
                    .exchangeRateDate(vo.getExchangeRateDate())
                    .exchangeRate(vo.getExchangeRate())
                    .foreignCostAmount(nvl0(vo.getForeignCostAmount()))
                    .krwAmount(nvl0(vo.getKrwAmount()))
                    .exchangeDiffAmount(nvl0(vo.getExchangeDiffAmount()))
                    .govFee(nvl0(vo.getGovFee()))
                    .agencyFee(nvl0(vo.getAgencyFee()))
                    .vat(nvl0(vo.getVat()))
                    .transFee(nvl0(vo.getTransFee()))
                    .etcFee(nvl0(vo.getEtcFee()))
                    .totalInvAmount(nvl0(vo.getTotalInvAmount()))
                    .depAmount(nvl0(vo.getDepAmount()))
                    .unpaidAmount(nvl0(vo.getUnpaidAmount()))
                    .abandonAmount(nvl0(vo.getAbandonAmount()))
                    .govFeePayAmount(nvl0(vo.getGovFeePayAmount()))
                    .outsourceCost(nvl0(vo.getOutsourceCost()))
                    .perfAmount(nvl0(vo.getPerfAmount()))
                    .depositDate(vo.getDepositDate())
                    .createAt(DataConvertUtil.formatOffsetDateTime(vo.getCreateAt()))
                    .build();
        }
    }

    /** [아웃고잉] InvoiceMergeVO -> InvoiceOutgoingDetail */
    @Builder
    @Schema(description = "아웃고잉 청구서 상세 응답")
    public record InvoiceOutgoingDetail(
            @Schema(description = "출원 시퀀스") String appSeq,
            @Schema(description = "고객 시퀀스") String customerSeq,
            @Schema(description = "사업자 시퀀스") String bizInfoSeq,
            @Schema(description = "시퀀스") String invoiceSeq,

            @Schema(description = "청구구분") CommonRecordResponse.CodeInfo invCategory,
            @Schema(description = "청구분류") CommonRecordResponse.CodeInfo invClass,
            @Schema(description = "청구종류") CommonRecordResponse.CodeInfo invType,
            @Schema(description = "비용담당자") CommonRecordResponse.PersonInfo invMgr,


            @Schema(description = "청구일") String invDate,
            @Schema(description = "청구번호") String invNo,
            @Schema(description = "청구서발송일") String invSendDate,
            @Schema(description = "대리인청구일") String agentInvDate,
            @Schema(description = "DEBIT접수일") String debitReceiptDate,
            @Schema(description = "DEBIT번호") String debitNo,
            @Schema(description = "OurRef") String ourRef,
            @Schema(description = "YourRef") String yourRef,
            @Schema(description = "출원인관리번호") String clientRef,

            // 고객 및 출원정보
            @Schema(description = "사건구분코드") CommonRecordResponse.CodeInfo caseCategory,
            @Schema(description = "고객명") CommonRecordResponse.CustomerInfo customer,
            @Schema(description = "고객담당자") CommonRecordResponse.PersonInfo customerContact,
            @Schema(description = "국가코드") CommonRecordResponse.CodeInfo country,
            @Schema(description = "해외대리인") String foreignAgentName,
            @Schema(description = "등록일") String regDate,
            @Schema(description = "등록번호") String regNo,
            @Schema(description = "권리") CommonRecordResponse.CodeInfo rightType,
            @Schema(description = "출원일") String appDate,
            @Schema(description = "출원번호") String appNo,
            @Schema(description = "부서") String deptName,
            @Schema(description = "관리담당자") CommonRecordResponse.PersonInfo adminMgr,
            @Schema(description = "사건담당자") CommonRecordResponse.PersonInfo caseMgr,
            @Schema(description = "담당변리사") CommonRecordResponse.PersonInfo attorney,
            @Schema(description = "출원인") String applicantName,
            @Schema(description = "의뢰인") String clientName,
            @Schema(description = "국문명칭") String titleKo,
            @Schema(description = "영문명칭") String titleEn,
            @Schema(description = "류") String niceClass,
            @Schema(description = "등급") String grade,
            @Schema(description = "독립항") String independentClaims,
            @Schema(description = "종속항") String dependentClaims,
            @Schema(description = "명세서") String figureCount,
            @Schema(description = "도면수") String drawingCount,
            @Schema(description = "국내명세서") String specCount,

            // 사업자 및 외화
            @Schema(description = "발행번호") String taxBillNo,
            @Schema(description = "발행구분") CommonRecordResponse.CodeInfo taxBillType,
            @Schema(description = "계산서구분") CommonRecordResponse.CodeInfo taxBillCategory,
            @Schema(description = "상호") String bizName,
            @Schema(description = "대표자") String bizCeo,
            @Schema(description = "사업자번호") String bizRegNo,
            @Schema(description = "종사업장번호") String bizWorkplaceNo,
            @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
            String bizAddr,
            @Schema(description = "업태") String bizType,
            @Schema(description = "종목") String bizItem,
            @Schema(description = "담당자") String bizContactName,
            @Schema(description = "부서") String bizDeptName,
            @Schema(description = "이메일") String bizEmail,
            @Schema(description = "비고") String note,
            @Schema(description = "화폐단위") CommonRecordResponse.CodeInfo currencyUnit,
            @Schema(description = "환율적용일") String exchangeRateDate,
            @Schema(description = "환율") String exchangeRate,
            @Schema(description = "외화환산비용") String foreignCostAmount,
            @Schema(description = "원화금액") String krwAmount,
            @Schema(description = "송금외화수수료") String remitForeignFee,
            @Schema(description = "송금원화수수료") String remitKrwFee,

            // 비용 및 실적
            @Schema(description = "OA대상서류") String oaDocument,
            @Schema(description = "청구내용") String invContent,
            @Schema(description = "계산서발행일") String taxBillDate,
            @Schema(description = "대리인청구구분") CommonRecordResponse.CodeInfo agentInvCategory,
            @Schema(description = "관납료") String govFee,
            @Schema(description = "수수료") String agencyFee,
            @Schema(description = "부가세") String vat,
            @Schema(description = "기타비용") String etcFee,
            @Schema(description = "청구금액") String totalInvAmount,
            @Schema(description = "입금액") String depAmount,
            @Schema(description = "미수금") String unpaidAmount,
            @Schema(description = "포기일자") String abandonDate,
            @Schema(description = "포기금액") String abandonAmount,
            @Schema(description = "포기내용") String abandonContent,
            @Schema(description = "관납료납부일") String govFeePayDate,
            @Schema(description = "관납료납부액") String govFeePayAmount,
            @Schema(description = "외주송금일") String outsourceDate,
            @Schema(description = "외주내역") String outsourceContent,
            @Schema(description = "외주비용") String outsourceCost,
            @Schema(description = "외주부가세비용") String outsourceVat,
            @Schema(description = "실적인정일") String perfDate,
            @Schema(description = "실적인정금액") String perfAmount,
            @Schema(description = "입금일") String depositDate,
            @Schema(description = "등록일시 (createAt)", example = "2026-04-25 13:30:00")
            String createAt
    ) {
        private static String nvl0(String val) {
            return org.springframework.util.StringUtils.hasText(val) ? val : "0";
        }

        public static InvoiceOutgoingDetail from(InvoiceMergeVO vo) {
            return InvoiceOutgoingDetail.builder()
                    .appSeq(vo.getAppSeq())
                    .customerSeq(vo.getCustomerSeq())
                    .bizInfoSeq(vo.getBizInfoSeq())
                    .invoiceSeq(vo.getInvoiceSeq())
                    .invCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getInvCategoryCode()).codeName(vo.getInvCategoryName()).build())
                    .invClass(CommonRecordResponse.CodeInfo.builder().code(vo.getInvClassCode()).codeName(vo.getInvClassName()).build())
                    .invType(CommonRecordResponse.CodeInfo.builder().code(vo.getInvTypeCode()).codeName(vo.getInvTypeName()).build())
                    .invMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getInvMgr()).userName(vo.getInvMgrName()).build())
                    .caseMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCaseMgr()).userName(vo.getCaseMgrName()).build())
                    .caseCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getCaseCategoryCode()).codeName(vo.getCaseCategoryName()).build())
                    .invDate(vo.getInvDate())
                    .invNo(vo.getInvNo())
                    .invSendDate(vo.getInvSendDate())
                    .agentInvDate(vo.getAgentInvDate())
                    .debitReceiptDate(vo.getDebitReceiptDate())
                    .debitNo(vo.getDebitNo())
                    .ourRef(vo.getOurRef())
                    .yourRef(vo.getYourRef())
                    .clientRef(vo.getClientRef())
                    .customer(CommonRecordResponse.CustomerInfo.builder()
                            .customerSeq(vo.getCustomerSeq())
                            .customerName(vo.getCustomerName())
                            .build())
                    .customerContact(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCustomerContact()).userName(vo.getCustomerContactName()).build())
                    .country(CommonRecordResponse.CodeInfo.builder().code(vo.getCountryCode()).codeName(vo.getCountryName()).build())
                    .foreignAgentName(vo.getForeignAgentName())
                    .applicantName(vo.getApplicantName())
                    .clientName(vo.getClientName())
                    .titleKo(vo.getTitleKo())
                    .titleEn(vo.getTitleEn())
                    .niceClass(vo.getNiceClass())
                    .grade(vo.getGrade())
                    .independentClaims(vo.getIndependentClaims())
                    .dependentClaims(vo.getDependentClaims())
                    .specCount(vo.getSpecCount())
                    .drawingCount(vo.getDrawingCount())
                    .figureCount(vo.getOverseaSpecCount())
                    .taxBillNo(vo.getTaxBillNo())
                    .taxBillType(CommonRecordResponse.CodeInfo.builder().code(vo.getTaxBillTypeCode()).codeName(vo.getTaxBillTypeName()).build())
                    .taxBillCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getTaxBillCategoryCode()).codeName(vo.getTaxBillCategoryName()).build())
                    .bizName(vo.getBizName())
                    .bizAddr(vo.getBizAddr())
                    .bizCeo(vo.getBizCeo())
                    .bizRegNo(vo.getBizRegNo())
                    .bizWorkplaceNo(vo.getBizWorkplaceNo())
                    .bizType(vo.getBizType())
                    .bizItem(vo.getBizItem())
                    .bizContactName(vo.getBizContactName())
                    .bizDeptName(vo.getBizDeptName())
                    .bizEmail(vo.getBizEmail())
                    .note(vo.getNote())
                    .currencyUnit(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCurrencyUnitCode())
                            .codeName(vo.getCurrencyUnitName())
                            .build())
                    .exchangeRateDate(vo.getExchangeRateDate())
                    .exchangeRate(nvl0(vo.getExchangeRate()))
                    .foreignCostAmount(nvl0(vo.getForeignCostAmount()))
                    .krwAmount(nvl0(vo.getKrwAmount()))
                    .remitForeignFee(nvl0(vo.getRemitForeignFee()))
                    .remitKrwFee(nvl0(vo.getRemitKrwFee()))
                    .oaDocument(vo.getOaDocument())
                    .invContent(vo.getInvContent())
                    .taxBillDate(vo.getTaxBillDate())
                    .agentInvCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getAgentInvCategoryCode()).codeName(vo.getAgentInvCategoryName()).build())
                    .govFee(nvl0(vo.getGovFee()))
                    .agencyFee(nvl0(vo.getAgencyFee()))
                    .vat(nvl0(vo.getVat()))
                    .etcFee(nvl0(vo.getEtcFee()))
                    .totalInvAmount(nvl0(vo.getTotalInvAmount()))
                    .depAmount(nvl0(vo.getDepAmount()))
                    .unpaidAmount(nvl0(vo.getUnpaidAmount()))
                    .abandonDate(vo.getAbandonDate())
                    .abandonAmount(nvl0(vo.getAbandonAmount()))
                    .abandonContent(vo.getAbandonContent())
                    .govFeePayDate(vo.getGovFeePayDate())
                    .govFeePayAmount(nvl0(vo.getGovFeePayAmount()))
                    .outsourceDate(vo.getOutsourceDate())
                    .outsourceContent(vo.getOutsourceContent())
                    .outsourceCost(nvl0(vo.getOutsourceCost()))
                    .outsourceVat(nvl0(vo.getOutsourceVat()))
                    .perfDate(vo.getPerfDate())
                    .perfAmount(nvl0(vo.getPerfAmount()))
                    .adminMgr(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getAdminMgr())
                            .userName(vo.getAdminMgrName())
                            .build())
                    .invMgr(CommonRecordResponse.PersonInfo.builder()
                            .userSeq(vo.getInvMgr())
                            .userName(vo.getInvMgrName())
                            .build())
                    .attorney(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAttorney()).userName(vo.getAttorneyName()).build())
                    .regDate(vo.getRegDate())
                    .regNo(vo.getRegNo())
                    .appDate(vo.getAppDate())
                    .appNo(vo.getAppNo())
                    .deptName(vo.getDeptName())
                    .rightType(CommonRecordResponse.CodeInfo.builder().code(vo.getRightTypeCode()).codeName(vo.getRightTypeName()).build())
                    .depositDate(vo.getDepositDate())
                    .createAt(DataConvertUtil.formatOffsetDateTime(vo.getCreateAt()))
                    .build();
        }
    }

    @Builder
    public record InvoiceClaimDetail(
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            String invoiceClaimSeq,
            CommonRecordResponse.CodeInfo costCategory,  // 비용구분
            String itemContent,       // 청구내용
            String quantity,      // 수량

            CommonRecordResponse.CodeInfo unit,           //단위
            String unitPrice,     // 단가

            String amount,         // 금액
            String vatAmount,     // 부가세
            String totalAmount,    // 합계
            String note,               // 비고
            String claimKind          // 청구종류 (대리인청구, 당소청구)
    ) {}

    @Builder
    public record InvoiceBankingDetail(
            @Schema(description = "시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
             String bankingSeq,
            String bankingCategory, // 'DEPOSIT' (입금), 'PREPAY' (선수금) 등
            String depositSendDate,
            String depositCheckDate,
            String depositAmount,
            String depositName,
            String depositBank,
            String depositFee,
            String note,
            // ── 선수금 ──
            String prepaymentDepositNo,
            String generalPrepaymentBalance,
            String generalPrepaymentUsedAmount,
            String designatedPrepaymentBalance,
            String designatedPrepaymentUsedAmount
    ) {}
    @Builder
    public record PerformanceDetail(
            @Schema(description = "송장 마스터 시퀀스", example = "INVMST20260000001", format = "SEQ") String invoiceSeq,
            @Schema(description = "실적 일련번호", example = "PERF202600001", format = "SEQ") String performanceSeq,
            @Schema(description = "실적 구분", example = "PERF202600001", format = "SEQ") CommonRecordResponse.CodeInfo performanceCategory,
            @Schema(description = "부서", example = "IP부서") String deptCategory,
            @Schema(description = "담당자", example = "{ \"userSeq\": \"USERIF20260000002\", \"userName\": \"홍길동\" })") CommonRecordResponse.PersonInfo staff,
            @Schema(description = "실적 상세 내용", example = "사건 담당 실적") String performanceContent,
            @Schema(description = "실적인정일", example = "20260215", format = "YYYYMMDD") String performancePerfDate,
            @Schema(description = "실적 분배 금액", example = "150000") String performanceAmount,
            @Schema(description = "실적인정금액(마스터)", example = "1000000") String masterPerfAmount,
            @Schema(description = "분배 비율 (0~1)", example = "0.5") String shareRatio,
            @Schema(description = "비고", example = "메인 변리사 실적") String note
    ) {}

    @Builder
    public record AppDetail(
            String appSeq,
            String appNo,
            String titleKo,
            String titleEn,
            CommonRecordResponse.CodeInfo rightType,     // 권리구분
            CommonRecordResponse.CodeInfo country,       // 국가 정보
            //String countryCode,                          // 국가코드(필요시)
            //String countryNameKo,                        // 국가명(국문)
            String ourRef,
            String yourRef,
            String clientRef,                             //출원인 관리번호
            String deptName,
            CommonRecordResponse.CodeInfo caseCategory,  // 사건구분
            String regNo,
            String niceClass,
            String grade,                                // 등급

            // --- [추가된 규격 및 수량 필드] ---
            String independentClaims,                    // 독립항
            String dependentClaims,                      // 종속항
            String drawingCount,                         // 도면수
            String figureCount,                          // 도수
            String specCount,                            // 명세서
            String domesticRegNo,                         // 국내 등록번호

            // --- [추가된 날짜 필드] ---
            String appDate,                              // 출원일
            String regDate,                              // 등록일
            String pubDate,                              // 공고일
            String domesticRegDecisionDate,              // 국내 등록결정일
            String domesticRegDate,                      // 국내 등록일
            String intlRegDate,                          // 국제 등록일

            // --- [추가된 인적 정보 필드 (객체 타입)] ---

            CommonRecordResponse.PersonInfo adminMgr,
            CommonRecordResponse.PersonInfo caseMgr,
            CommonRecordResponse.PersonInfo attorney,
            String applicantName,
            String clientName,
            String foreignAgentName
    ) {}
    @Builder
    @Schema(description = "해외 송금 상세 응답")
    public record InvoiceForeignBankingDetail(
            @Schema(description = "송금 일련번호 (수정 시)", example = "INVBAK20260000011", format = "SEQ")
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

            @Schema(description = "송금 방식", example = "{ \"code\": \"WIRE\", \"codeName\": \"전신송금(T/T)\" }", format = "CODE")
            CommonRecordResponse.CodeInfo depositWay,

            @Schema(description = "비고", example = "해외 대리인 특허 출원 비용 송금건")
            String note
    ) {}


    @Builder
    @Schema(description = "사건별 청구 목록 상세 응답")
    public record InvoiceListDetail(
            @Schema(description = "청구서 일련번호 (상세 조회용)", example = "INVMST20260000001", format = "SEQ")
            String invoiceSeq,

            @Schema(description = "구분 (사건구분)", example = "{ \"code\": \"DOMESTIC\", \"codeName\": \"내국\" }")
            CommonRecordResponse.CodeInfo caseCategory,

            @Schema(description = "청구분류", example = "{ \"code\": \"BILL01\", \"codeName\": \"출원청구\" }")
            CommonRecordResponse.CodeInfo invCategory,

            @Schema(description = "청구일", example = "20260326", format = "YYYYMMDD")
            String invDate,

            @Schema(description = "청구번호", example = "INV-2026-0001")
            String invoiceNo,

            @Schema(description = "청구내용", example = "특허 출원 비용 청구")
            String invContent,

            // --- [금액 항목: 쿼리의 fn_get_cost_amount 결과 및 VO 필드 대응] ---
            @Schema(description = "관납료 (govFee)", example = "150000")
            String officialFee,

            @Schema(description = "수수료 (agencyFee)", example = "300000")
            String agencyFee,

            @Schema(description = "부가세 (vat)", example = "30000")
            String vatAmount,

            @Schema(description = "번역료 (transFee)", example = "50000")
            String transFee,

            @Schema(description = "기타비용 (etcFee)", example = "10000")
            String etcFee,

            @Schema(description = "청구금액 합계 (totalInvAmount)", example = "490000")
            String totalAmount,

            @Schema(description = "입금액 합계 (depAmount)", example = "0")
            String depositAmount,

            @Schema(description = "미수금액 (unpaidAmount)", example = "490000")
            String outstandingAmount,

            @Schema(description = "포기금액 (abandonAmount)", example = "0")
            String abandonAmount,

            // --- [날짜 정보] ---
            @Schema(description = "계산서 발행일 (taxBillDate)", example = "20260327", format = "YYYYMMDD")
            String taxBillDate,

            @Schema(description = "등록일시 (createAt)", example = "2026-04-25 13:30:00")
            String createAt


    ) {
        /**
         * InvoiceMergeVO로부터 목록용 레코드 생성
         */
        private static String nvl0(String val) {
            return org.springframework.util.StringUtils.hasText(val) ? val : "0";
        }

        public static InvoiceListDetail from(InvoiceMergeVO vo) {
            return InvoiceListDetail.builder()
                    .invoiceSeq(vo.getInvoiceSeq())
                    // 코드 정보 객체화 (CodeInfo)
                    .caseCategory(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getCaseCategoryCode())
                            .codeName(vo.getCaseCategoryName())
                            .build())
                    .invCategory(CommonRecordResponse.CodeInfo.builder()
                            .code(vo.getInvCategoryCode())
                            .codeName(vo.getInvCategoryName())
                            .build())
                    .invDate(vo.getInvDate())
                    .invoiceNo(vo.getInvNo())          // VO 필드: invNo
                    .invContent(vo.getInvContent())    // VO 필드: invContent
                    // 금액 필드 매핑 (VO의 String 필드 그대로 사용)
                    .officialFee(nvl0(vo.getGovFee()))       // VO 필드: govFee
                    .agencyFee(nvl0(vo.getAgencyFee()))      // VO 필드: agencyFee
                    .vatAmount(nvl0(vo.getVat()))            // VO 필드: vat
                    .transFee(nvl0(vo.getTransFee()))        // VO 필드: transFee
                    .etcFee(nvl0(vo.getEtcFee()))            // VO 필드: etcFee
                    .totalAmount(nvl0(vo.getTotalInvAmount())) // VO 필드: totalInvAmount
                    .depositAmount(nvl0(vo.getDepAmount()))  // VO 필드: depAmount
                    .outstandingAmount(nvl0(vo.getUnpaidAmount())) // VO 필드: unpaidAmount
                    .abandonAmount(nvl0(vo.getAbandonAmount())) // VO 필드: abandonAmount
                    .taxBillDate(vo.getTaxBillDate())
                    .createAt(DataConvertUtil.formatOffsetDateTime(vo.getCreateAt()))
                    //.depositDate(vo.getDepositDate())
                    .build();
        }
    }
}
