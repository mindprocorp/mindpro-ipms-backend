package kr.co.mindpro.ipms.domain.invoice.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.bizinfo.dto.request.BizInfoRequest;
import kr.co.mindpro.ipms.domain.bizinfo.service.BizInfoService;
import kr.co.mindpro.ipms.domain.cost.service.CostService;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.invoice.dto.request.InvoiceRequest;
import kr.co.mindpro.ipms.domain.invoice.dto.response.InvoiceResponse;
import kr.co.mindpro.ipms.domain.invoice.repository.db1.InvoiceMapper;
import kr.co.mindpro.ipms.domain.invoice.service.InvoiceService;
import kr.co.mindpro.ipms.domain.invoice.vo.*;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.cost.repository.db1.CostMapper;
import kr.co.mindpro.ipms.domain.duedate.repository.db1.DueDateMapper;
import kr.co.mindpro.ipms.domain.participant.repository.db1.ParticipantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import kr.co.mindpro.ipms.domain.ai.service.RagService;

/**
 * 청구서 비즈니스 로직 구현체
 *
 * @author : min
 * @fileName : InvoiceServiceImpl.java
 * @since : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceMapper invoiceMapper;
    private final DueDateService dueDateService;
    private final ParticipantService participantService;
    private final CostService costService;
    private final BizInfoService bizInfoService;
    private final RagService ragService;
    private final HistoryService historyService;

    // 상세 테이블 조작을 위한 Mapper 주입
    private final CostMapper costMapper;
    private final DueDateMapper dueDateMapper;
    private final ParticipantMapper participantMapper;

    /*
     * =========================================================================
     * [저장/생성] - 타입별 WorkType 주입 및 전체 데이터 동기화
     * =========================================================================
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceDomesticDetail createDomesticInvoice(InvoiceRequest.InvoiceDomesticDetail request) {
        InvoiceMergeVO mergeVO = new InvoiceMergeVO();
        mergeVO.fillFromDomestic(request);
        // [핵심] invoiceSeq 명시 세팅 — saveCommonInvoiceCore가 INSERT/UPDATE를 이 값으로 분기.
        // 누락 시 저장할 때마다 새 row 생성되어 중복 저장 발생.
        mergeVO.setInvoiceSeq(request.invoiceSeq());

        String invSeq = saveCommonInvoiceCore(mergeVO, "INV");
        InvoiceResponse.InvoiceDomesticDetail detail = this.getDomesticDetail(invSeq);
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "INVOICE_DOMESTIC",
                invSeq, "국내 청구서", detail);
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceIncomingDetail createIncomingInvoice(InvoiceRequest.InvoiceIncomingDetail request) {
        InvoiceMergeVO mergeVO = new InvoiceMergeVO();
        mergeVO.fillFromIncoming(request);
        mergeVO.setInvoiceSeq(request.invoiceSeq());

        String invSeq = saveCommonInvoiceCore(mergeVO, "INV_INC");
        InvoiceResponse.InvoiceIncomingDetail detail = this.getIncomingDetail(invSeq);
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "INVOICE_INCOMING",
                invSeq, "외국 청구서", detail);
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceOutgoingDetail createOutgoingInvoice(InvoiceRequest.InvoiceOutgoingDetail request) {
        InvoiceMergeVO mergeVO = new InvoiceMergeVO();
        mergeVO.fillFromOutgoing(request);
        mergeVO.setInvoiceSeq(request.invoiceSeq());

        String invSeq = saveCommonInvoiceCore(mergeVO, "INV_OUT");
        InvoiceResponse.InvoiceOutgoingDetail detail = this.getOutgoingDetail(invSeq);
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "INVOICE_OUTGOING",
                invSeq, "해외 청구서", detail);
        return detail;
    }

    /**
     * 공통 저장 엔진 (Master + DueDate + Participant + Cost 연동)
     */
    private String saveCommonInvoiceCore(InvoiceMergeVO mergeVO, String workType) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // [추가] 히스토리용 기존 데이터 조회
        InvoiceMstVO oldVo = null;
        if (StringUtils.hasText(mergeVO.getInvoiceSeq())) {
            oldVo = invoiceMapper.selectInvoiceMst(mergeVO.getInvoiceSeq(), officeSeq);
        }

        // 2. BizInfo 생성 로직
        String bizInfoSeq = null;

        if (!StringUtils.hasText(mergeVO.getBizInfoSeq()) && mergeVO.getBizRegNo() != null
                && !mergeVO.getBizRegNo().isEmpty()) {

            mergeVO.setBizInfoSeq(bizInfoService.saveBizInfo(BizInfoRequest.BizInfoDetail.builder()
                    .bizRegNo(mergeVO.getBizRegNo())
                    .bizCorpName(mergeVO.getBizName())
                    .ceoName(mergeVO.getBizCeo())
                    .bizWorkplaceNo(mergeVO.getBizWorkplaceNo())
                    .bizAddr(mergeVO.getBizAddr())
                    .bizType(mergeVO.getBizType())
                    .bizKind(mergeVO.getBizItem())
                    .bizContactName(mergeVO.getBizContactName())
                    .bizDeptName(mergeVO.getBizDeptName())
                    .bizEmail(mergeVO.getBizEmail())
                    .build())
                    // 저장한 키를 추출
                    .bizInfoSeq());

            if (StringUtils.hasText(mergeVO.getBizContactName()) && StringUtils.hasText(mergeVO.getCustomerSeq())) {
                // todo customerService.saveManager 세금처리 담당자 생성

            }

        }
        InvoiceMstVO mstVO = InvoiceMstVO.builder()
                .officeSeq(officeSeq)
                .invoiceSeq(mergeVO.getInvoiceSeq())
                .appSeq(mergeVO.getAppSeq())
                .customerSeq(mergeVO.getCustomerSeq())
                .bizInfoSeq(mergeVO.getBizInfoSeq())
                .invoiceNo(mergeVO.getInvNo())
                .invoiceCategoryCode(mergeVO.getInvCategoryCode())
                .inOutType(workType)
                .invoiceTypeCode(mergeVO.getInvTypeCode())
                .invoiceClassCode(mergeVO.getInvClassCode())
                .caseCategoryCode(mergeVO.getCaseCategoryCode())
                .foreignAgent(mergeVO.getForeignAgent())
                .client(mergeVO.getClient())
                .ourRef(mergeVO.getOurRef())
                .yourRef(mergeVO.getYourRef())
                .clientRef(mergeVO.getClientRef())
                .deptName(mergeVO.getDeptName())
                .debitNo(mergeVO.getDebitNo())
                .oaDocument(mergeVO.getOaDocument())
                .invoiceContent(mergeVO.getInvContent())
                .agentInvoiceCategoryCode(mergeVO.getAgentInvCategoryCode())
                .currencyUnit(mergeVO.getCurrencyUnitCode())
                .exchangeRate((mergeVO.getExchangeRate() != null && !mergeVO.getExchangeRate().trim().isEmpty())
                        ? new BigDecimal(mergeVO.getExchangeRate())
                        : BigDecimal.ZERO)
                .giveUpContent(mergeVO.getAbandonContent())
                .outsourceContent(mergeVO.getOutsourceContent())
                // 계산서
                .currencyUnit(mergeVO.getCurrencyUnitCode())
                .taxBillNo(mergeVO.getTaxBillNo())
                .taxBillTypeCode(mergeVO.getTaxBillTypeCode())
                .taxBillCategoryCode(mergeVO.getTaxBillCategoryCode())
                .note(mergeVO.getNote())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        // 1. Invoice Master 저장
        if (StringUtils.hasText(mstVO.getInvoiceSeq())) {
            invoiceMapper.updateInvoiceMst(mstVO);
        } else {
            invoiceMapper.insertInvoiceMst(mstVO);
        }

        // 2. 결과 시퀀스 추출
        String invSeq = mstVO.getInvoiceSeq();

        // [추가] 히스토리 기록
        historyService.compareAndLog(invSeq, "청구서수정", oldVo, mstVO);

        // 3. 하위 상세 테이블 데이터 동기화 (각 서비스 내부 로직에서 처리)
        dueDateService.saveAllDueDates(DataConvertUtil.extractDueDates(mergeVO, invSeq, officeSeq, "INV"));
        participantService.saveAllParticipants(DataConvertUtil.extractParticipants(mergeVO, invSeq, officeSeq, "INV"));
        costService.saveAllCosts(invSeq, DataConvertUtil.extractCosts(mergeVO, invSeq, officeSeq, "INV"));

        return invSeq;
    }

    /*
     * =========================================================================
     * [상세 조회] - DB 로드 후 기일/관계자/비용 최신 데이터 주입
     * =========================================================================
     */

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceDomesticDetail getDomesticDetail(String invoiceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return InvoiceResponse.InvoiceDomesticDetail.from(fetchFullInvoiceVo(invoiceSeq, "INV"));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceIncomingDetail getIncomingDetail(String invoiceSeq) {
        return InvoiceResponse.InvoiceIncomingDetail.from(fetchFullInvoiceVo(invoiceSeq, "INV_INC"));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceOutgoingDetail getOutgoingDetail(String invoiceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return InvoiceResponse.InvoiceOutgoingDetail.from(fetchFullInvoiceVo(invoiceSeq, "INV_OUT"));
    }

    /**
     * 마스터 데이터 조회 후 하위 테이블(기일, 관계자, 비용) 정보를 VO에 역주입(Inject)
     */
    private InvoiceMergeVO fetchFullInvoiceVo(String invoiceSeq, String workType) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // [최적화] Mapper 쿼리에서 조인(due, pp 뷰) 및 함수(fn_get_cost_amount)를 통해 
        // 필요한 모든 데이터(기일, 관계자, 비용 합계)를 한 번에 가져오므로 별도의 역주입 로직 제거
        InvoiceMergeVO vo = invoiceMapper.findInvoiceBySeq(invoiceSeq, officeSeq);

        if (vo == null) {
            throw new BusinessException("존재하지 않는 청구서입니다.", ErrorCode.RESOURCE_NOT_FOUND);
        }

        return vo;
    }

    /*
     * =========================================================================
     * [목록 조회] - 각 타입별 메서드 내부에서 직접 변환 처리
     * =========================================================================
     */

    /**
     * 사건(tblSeq)별 청구 내역 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceListDetail> getInvoiceListByCase(BaseSearchRequest request) {
        // [1] 오피스 및 페이징 세팅
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // [2] 전용 쿼리 호출 (selectInvoiceListByCase)
        List<InvoiceMergeVO> dbList = invoiceMapper.selectInvoiceListByCase(request);
        int totalCount = invoiceMapper.selectInvoiceListByCaseCount(request);

        // [3] Record 변환 (정적 메서드 from 활용)
        List<InvoiceResponse.InvoiceListDetail> dtoList = dbList.stream()
                .map(InvoiceResponse.InvoiceListDetail::from)
                .collect(Collectors.toList());

        // [4] 표준 페이징 응답 반환
        return BaseSearchResponse.of(dtoList, request.getPage(), totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceDomesticDetail> getDomesticList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // 1. DB에서 내국(DOMESTIC) 리스트 및 전체 건수 조회
        List<InvoiceMergeVO> dbList = invoiceMapper.selectInvoiceListByCategory(request, "INV");
        int totalCount = invoiceMapper.selectInvoiceListByCategoryCount(request, "INV");

        // 2. VO -> DomesticDetail Record 변환
        List<InvoiceResponse.InvoiceDomesticDetail> dtoList = dbList.stream()
                .map(InvoiceResponse.InvoiceDomesticDetail::from)
                .collect(Collectors.toList());

        // 3. 공통 페이징 래퍼에 담아 반환
        return BaseSearchResponse.of(dtoList, request.getPage(), totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceIncomingDetail> getIncomingList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // 1. DB에서 인커밍(INCOMING) 리스트 및 전체 건수 조회
        List<InvoiceMergeVO> dbList = invoiceMapper.selectInvoiceListByCategory(request, "INV_INC");
        int totalCount = invoiceMapper.selectInvoiceListByCategoryCount(request, "INV_INC");

        // 2. VO -> IncomingDetail Record 변환
        List<InvoiceResponse.InvoiceIncomingDetail> dtoList = dbList.stream()
                .map(InvoiceResponse.InvoiceIncomingDetail::from)
                .collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, request.getPage(), totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceOutgoingDetail> getOutgoingList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // 1. DB에서 아웃고잉(OUTGOING) 리스트 및 전체 건수 조회
        List<InvoiceMergeVO> dbList = invoiceMapper.selectInvoiceListByCategory(request, "INV_OUT");
        int totalCount = invoiceMapper.selectInvoiceListByCategoryCount(request, "INV_OUT");

        // 2. VO -> OutgoingDetail Record 변환
        List<InvoiceResponse.InvoiceOutgoingDetail> dtoList = dbList.stream()
                .map(InvoiceResponse.InvoiceOutgoingDetail::from)
                .collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, request.getPage(), totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceClaimDetail getClaimDetail(String claimSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return invoiceMapper.selectClaimDetail(claimSeq, officeSeq)
                .map(vo -> InvoiceResponse.InvoiceClaimDetail.builder()
                        .invoiceClaimSeq(vo.getInvoiceClaimSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .costCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCostCategoryCode())
                                .codeName(vo.getCostCategoryName())
                                .build())
                        .itemContent(vo.getItemContent())
                        .unit(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getUnitCode())
                                .codeName(vo.getUnitName())
                                .build())
                        .unitPrice(String.valueOf(vo.getUnitPrice()))
                        .quantity(String.valueOf(vo.getQuantity()))
                        .amount(String.valueOf(vo.getAmount()))
                        .vatAmount(String.valueOf(vo.getVatAmount()))
                        .totalAmount(String.valueOf(vo.getTotalAmount()))
                        .note(vo.getNote())
                        .claimKind(vo.getClaimKind())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceClaimDetail saveInvoiceClaim(InvoiceRequest.InvoiceClaimDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 0. 기존 데이터 조회
        InvoiceClaimVO oldVo = null;
        if (StringUtils.hasText(request.invoiceClaimSeq())) {
            oldVo = invoiceMapper.selectClaimDetail(request.invoiceClaimSeq(), officeSeq).orElse(null);
        }

        InvoiceClaimVO vo = InvoiceClaimVO.builder()
                .invoiceClaimSeq(request.invoiceClaimSeq())
                .invoiceSeq(request.invoiceSeq())
                .officeSeq(officeSeq)
                .costCategoryCode(request.costCategory().code())
                .itemContent(request.itemContent())
                .unitPrice(new BigDecimal(request.unitPrice()))
                .quantity(new BigDecimal(request.quantity()))
                .unitCode(request.unit().code())
                .amount(new BigDecimal(request.amount()))
                .vatAmount(new BigDecimal(request.vatAmount()))
                .totalAmount(new BigDecimal(request.totalAmount()))
                .claimKind("청구내역")
                .note(request.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(vo.getInvoiceClaimSeq())) {
            invoiceMapper.updateInvoiceClaim(vo);
        } else {
            invoiceMapper.insertInvoiceClaim(vo);
        }

        // 히스토리 기록
        historyService.compareAndLog(vo.getInvoiceClaimSeq(), "청구내역수정", oldVo, vo);

        return this.getClaimDetail(vo.getInvoiceClaimSeq());
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> getInvoiceClaimList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // 1. VO 리스트 조회
        List<InvoiceClaimVO> list = invoiceMapper.selectInvoiceClaimList(request, "청구내역");

        // 2. 요청하신 빌더 구조에 맞춰 매핑
        List<InvoiceResponse.InvoiceClaimDetail> dtoList = list.stream()
                .map(vo -> InvoiceResponse.InvoiceClaimDetail.builder()
                        .invoiceSeq(vo.getInvoiceSeq())
                        .invoiceClaimSeq(vo.getInvoiceClaimSeq())
                        .costCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCostCategoryCode())
                                .codeName(vo.getCostCategoryName())
                                .build())

                        .itemContent(vo.getItemContent())
                        .unit(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getUnitCode())
                                .codeName(vo.getUnitName())
                                .build())
                        .unitPrice(vo.getUnitPrice() != null ? vo.getUnitPrice().toString() : "0")
                        .quantity(vo.getQuantity() != null ? vo.getQuantity().toString() : "0")
                        .amount(vo.getAmount() != null ? vo.getAmount().toString() : "0")
                        .vatAmount(vo.getVatAmount() != null ? vo.getVatAmount().toString() : "0")
                        .totalAmount(vo.getTotalAmount() != null ? vo.getTotalAmount().toString() : "0")
                        .note(vo.getNote() != null ? vo.getNote() : "") // nvl 처리
                        .claimKind(vo.getClaimKind())
                        .build())
                .toList();

        return BaseSearchResponse.of(dtoList, request.getPage(), dtoList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> saveInvoiceClaimList(String invoiceSeq,
            List<InvoiceRequest.InvoiceClaimDetail> requests) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        invoiceMapper.deleteInvoiceClaims(invoiceSeq, officeSeq);

        if (!ObjectUtils.isEmpty(requests)) {
            List<InvoiceClaimVO> claimList = requests.stream().map(req -> InvoiceClaimVO.builder()
                    .invoiceSeq(invoiceSeq)
                    .officeSeq(officeSeq)
                    .costCategoryCode(req.costCategory().code())
                    .itemContent(req.itemContent())
                    .unitPrice(new BigDecimal(req.unitPrice()))
                    .quantity(new BigDecimal(req.quantity()))
                    .amount(new BigDecimal(req.amount()))
                    .vatAmount(new BigDecimal(req.vatAmount()))
                    .totalAmount(new BigDecimal(req.totalAmount()))

                    .note(req.note())
                    .createUser(loginUser)
                    .updateUser(loginUser)
                    .delYn("N")
                    .build()).collect(Collectors.toList());

            invoiceMapper.insertInvoiceClaimBatch(claimList);
        }
        return this.getInvoiceClaimList(BaseSearchRequest.builder()
                .tblSeq(invoiceSeq)
                .build());
    }

    /**
     * 1. 대리인 청구 내역 저장 (등록 및 수정)
     * 기존 insertInvoiceClaim, updateInvoiceClaim 쿼리 활용
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceClaimDetail saveAgentClaim(InvoiceRequest.InvoiceClaimDetail data) {
        String loginUser = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        // DTO -> VO 변환 (기존 쿼리 필드명에 맞춤)
        InvoiceClaimVO vo = InvoiceClaimVO.builder()
                .invoiceClaimSeq(data.invoiceClaimSeq())
                .invoiceSeq(data.invoiceSeq())
                .officeSeq(officeSeq)
                .costCategoryCode(data.costCategory().code())
                .itemContent(data.itemContent())
                // 기존 쿼리에 unit_category_code 필드가 있다면 추가 (없을시 세팅 생략)
                .unitPrice(new BigDecimal(data.unitPrice()))
                .quantity(new BigDecimal(data.quantity()))
                .amount(new BigDecimal(data.amount()))
                .vatAmount(new BigDecimal(data.vatAmount()))
                .totalAmount(new BigDecimal(data.totalAmount()))
                .claimKind("대리인청구") // 구분을 위해 명시
                .note(data.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(vo.getInvoiceClaimSeq())) {
            invoiceMapper.updateInvoiceClaim(vo); // 기존 쿼리 사용
        } else {
            invoiceMapper.insertInvoiceClaim(vo); // 기존 쿼리 사용
        }

        // 저장 후 상세 조회 (기존 selectClaimDetail 쿼리 활용)
        Optional<InvoiceClaimVO> resultOpt = invoiceMapper.selectClaimDetail(vo.getInvoiceClaimSeq(), officeSeq);

        if (resultOpt.isPresent()) {
            InvoiceClaimVO resultVo = resultOpt.get();
            // 직접 빌드해서 반환
            return InvoiceResponse.InvoiceClaimDetail.builder()
                    .invoiceSeq(resultVo.getInvoiceSeq())
                    .invoiceClaimSeq(resultVo.getInvoiceClaimSeq())
                    .costCategory(CommonRecordResponse.CodeInfo.builder()
                            .code(resultVo.getCostCategoryCode())
                            .codeName(resultVo.getCostCategoryName())
                            .build())
                    .itemContent(resultVo.getItemContent())
                    .unit(CommonRecordResponse.CodeInfo.builder()
                            .code(resultVo.getUnitCode())
                            .codeName(resultVo.getUnitName())
                            .build())
                    .unitPrice(resultVo.getUnitPrice() != null ? resultVo.getUnitPrice().toString() : "0")
                    .quantity(resultVo.getQuantity() != null ? resultVo.getQuantity().toString() : "0")
                    .amount(resultVo.getAmount() != null ? resultVo.getAmount().toString() : "0")
                    .vatAmount(resultVo.getVatAmount() != null ? resultVo.getVatAmount().toString() : "0")
                    .totalAmount(resultVo.getTotalAmount() != null ? resultVo.getTotalAmount().toString() : "0")
                    .note(resultVo.getNote() != null ? resultVo.getNote() : "")
                    .build();
        } else {
            // 데이터가 없을 경우 처리 (예: null 리턴)
            return null;
        }
    }

    /**
     * 2. 대리인 청구 내역 목록 조회 (BaseSearchRequest 기반)
     * 기존 selectInvoiceClaimList 쿼리 활용
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceClaimDetail> getAgentClaimList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // 기존 쿼리 호출 시 claimKind에 "대리인청구" 전달
        List<InvoiceClaimVO> list = invoiceMapper.selectInvoiceClaimList(request, "대리인청구");

        // 직접 stream 내부에서 빌드
        List<InvoiceResponse.InvoiceClaimDetail> dtoList = list.stream()
                .map(vo -> InvoiceResponse.InvoiceClaimDetail.builder()
                        .invoiceSeq(vo.getInvoiceSeq())
                        .invoiceClaimSeq(vo.getInvoiceClaimSeq())
                        .costCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCostCategoryCode())
                                .codeName(vo.getCostCategoryName())
                                .build())
                        .itemContent(vo.getItemContent())
                        .unit(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getUnitCode())
                                .codeName(vo.getUnitName())
                                .build())
                        .unitPrice(vo.getUnitPrice() != null ? vo.getUnitPrice().toString() : "0")
                        .quantity(vo.getQuantity() != null ? vo.getQuantity().toString() : "0")
                        .amount(vo.getAmount() != null ? vo.getAmount().toString() : "0")
                        .vatAmount(vo.getVatAmount() != null ? vo.getVatAmount().toString() : "0")
                        .totalAmount(vo.getTotalAmount() != null ? vo.getTotalAmount().toString() : "0")
                        .note(vo.getNote() != null ? vo.getNote() : "")
                        .build())
                .toList();

        return BaseSearchResponse.of(dtoList, request.getPage(), dtoList.size());
    }

    /**
     * 3. 대리인 청구 내역 단건 상세 조회
     * 기존 selectClaimDetail 쿼리 활용
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceClaimDetail getAgentClaimDetail(String claimSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // orElseThrow 대신 orElse(null)로 에러 없이 가져오기
        InvoiceClaimVO vo = invoiceMapper.selectClaimDetail(claimSeq, officeSeq).orElse(null);

        // 데이터가 없을 경우에 대한 방어 코드
        if (vo == null) {
            return null;
        }

        // 헬퍼 메서드 없이 직접 빌드
        return InvoiceResponse.InvoiceClaimDetail.builder()
                .invoiceSeq(vo.getInvoiceSeq())
                .invoiceClaimSeq(vo.getInvoiceClaimSeq())
                .costCategory(CommonRecordResponse.CodeInfo.builder()
                        .code(vo.getCostCategoryCode())
                        .codeName(vo.getCostCategoryName())
                        .build())
                .itemContent(vo.getItemContent())
                .unit(CommonRecordResponse.CodeInfo.builder()
                        .code(vo.getUnitCode()) // 기존 쿼리의 컬럼명(unit_category_code) 반영
                        .codeName(vo.getUnitName())
                        .build())
                .unitPrice(vo.getUnitPrice() != null ? vo.getUnitPrice().toString() : "0")
                .quantity(vo.getQuantity() != null ? vo.getQuantity().toString() : "0")
                .amount(vo.getAmount() != null ? vo.getAmount().toString() : "0")
                .vatAmount(vo.getVatAmount() != null ? vo.getVatAmount().toString() : "0")
                .totalAmount(vo.getTotalAmount() != null ? vo.getTotalAmount().toString() : "0")
                .note(vo.getNote() != null ? vo.getNote() : "") // nvl 처리
                .build();
    }
    /*
     * =========================================================================
     * [탭 2] 입금 및 선수금 (Banking)
     * =========================================================================
     */

    /**
     * 일반 입금/선수금 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail> getInvoiceBankingList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        request.setOfficeSeq(officeSeq);

        // 페이징 방어 로직 (NPE 방지)
        int page = (request.getPage() != 0) ? request.getPage() : 1;
        int pageSize = (request.getPageSize() != 0) ? request.getPageSize() : 10;
        request.setOffSet((page - 1) * pageSize);

        // 매퍼 호출: 일반 입금(10), 해외송금 제외(null)
        List<InvoiceBankingVO> dbList = invoiceMapper.selectInvoiceBankingListFiltered(request, "10", null);

        List<InvoiceResponse.InvoiceBankingDetail> dtoList = dbList.stream()
                .map(vo -> InvoiceResponse.InvoiceBankingDetail.builder()
                        .bankingSeq(vo.getBankingSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .bankingCategory(vo.getBankingCategory())
                        .depositCheckDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositCheckDate()))
                        .depositSendDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositSendDate()))
                        .depositAmount(vo.getDepositAmount() != null ? vo.getDepositAmount().toString() : "0")
                        .depositName(vo.getDepositName())
                        .depositBank(vo.getDepositBank())
                        .depositFee(vo.getDepositFee())
                        .note(vo.getNote())
                        .prepaymentDepositNo(vo.getPrepaymentDepositNo())
                        .generalPrepaymentBalance(longToStr(vo.getGeneralPrepaymentBalance()))
                        .generalPrepaymentUsedAmount(longToStr(vo.getGeneralPrepaymentUsedAmount()))
                        .designatedPrepaymentBalance(longToStr(vo.getDesignatedPrepaymentBalance()))
                        .designatedPrepaymentUsedAmount(longToStr(vo.getDesignatedPrepaymentUsedAmount()))
                        .build())
                .toList();

        return BaseSearchResponse.of(dtoList, page, dtoList.size());
    }

    /**
     * 입금/선수금 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.InvoiceBankingDetail getBankingDetail(String bankingSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return invoiceMapper.selectBankingDetail(bankingSeq, officeSeq)
                .map(vo -> InvoiceResponse.InvoiceBankingDetail.builder()
                        .bankingSeq(vo.getBankingSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .bankingCategory(vo.getBankingCategory())
                        .depositCheckDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositCheckDate()))
                        .depositSendDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositSendDate()))
                        .depositAmount(vo.getDepositAmount() != null ? vo.getDepositAmount().toString() : "0")
                        .depositName(vo.getDepositName())
                        .depositBank(vo.getDepositBank())
                        .depositFee(vo.getDepositFee())
                        .note(vo.getNote())
                        .prepaymentDepositNo(vo.getPrepaymentDepositNo())
                        .generalPrepaymentBalance(longToStr(vo.getGeneralPrepaymentBalance()))
                        .generalPrepaymentUsedAmount(longToStr(vo.getGeneralPrepaymentUsedAmount()))
                        .designatedPrepaymentBalance(longToStr(vo.getDesignatedPrepaymentBalance()))
                        .designatedPrepaymentUsedAmount(longToStr(vo.getDesignatedPrepaymentUsedAmount()))
                        .build())
                .orElse(null);
    }

    /** Long → String ("0" 기본). 응답 DTO가 String이라 변환 필요. */
    private static String longToStr(Long v) {
        return v == null ? "0" : v.toString();
    }

    /** 콤마/공백 제거 후 Long 변환 — 빈 값/파싱 실패 시 0L. */
    private static Long parseLongOrZero(String v) {
        if (v == null) return 0L;
        String s = v.replaceAll(",", "").trim();
        if (s.isEmpty()) return 0L;
        try { return (long) Double.parseDouble(s); } catch (NumberFormatException e) { return 0L; }
    }

    /**
     * 입금/선수금 저장
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.InvoiceBankingDetail saveInvoiceBanking(InvoiceRequest.InvoiceBankingDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 0. 기존 데이터 조회
        InvoiceBankingVO oldVo = null;
        if (StringUtils.hasText(request.bankingSeq())) {
            oldVo = invoiceMapper.selectBankingDetail(request.bankingSeq(), officeSeq).orElse(null);
        }

        InvoiceBankingVO vo = InvoiceBankingVO.builder()
                .bankingSeq(request.bankingSeq())
                .invoiceSeq(request.invoiceSeq())
                .officeSeq(officeSeq)
                .bankingCategory(request.bankingCategory().code())
                .depositSendDate(DataConvertUtil.parseToOffsetDateTime(request.depositSendDate()))
                .depositCheckDate(DataConvertUtil.parseToOffsetDateTime(request.depositCheckDate()))
                .depositAmount(
                        StringUtils.hasText(request.depositAmount()) ? Integer.valueOf(request.depositAmount()) : 0)
                .depositName(request.depositName())
                .depositBank(request.depositBank())
                .depositFee(request.depositFee())
                .note(request.note())
                // ── 선수금 5종 (문자열 → Long 안전 파싱) ──
                .prepaymentDepositNo(request.prepaymentDepositNo())
                .generalPrepaymentBalance(parseLongOrZero(request.generalPrepaymentBalance()))
                .generalPrepaymentUsedAmount(parseLongOrZero(request.generalPrepaymentUsedAmount()))
                .designatedPrepaymentBalance(parseLongOrZero(request.designatedPrepaymentBalance()))
                .designatedPrepaymentUsedAmount(parseLongOrZero(request.designatedPrepaymentUsedAmount()))
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(vo.getBankingSeq())) {
            invoiceMapper.updateInvoiceBanking(vo);
        } else {
            invoiceMapper.insertInvoiceBanking(vo);
        }

        // 히스토리 기록
        historyService.compareAndLog(vo.getBankingSeq(), "입금내역수정", oldVo, vo);

        return this.getBankingDetail(vo.getBankingSeq());
    }

    /**
     * 입금/선수금 일괄 저장
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseSearchResponse<InvoiceResponse.InvoiceBankingDetail> saveInvoiceBankingList(String invoiceSeq,
            List<InvoiceRequest.InvoiceBankingDetail> requests) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 1. 기존 데이터 삭제
        invoiceMapper.deleteInvoiceBankings(invoiceSeq, officeSeq);

        if (!ObjectUtils.isEmpty(requests)) {
            // 2. .toList()를 사용하여 간결하게 리스트 생성 (Java 16+)
            List<InvoiceBankingVO> bankingList = requests.stream()
                    .<InvoiceBankingVO>map(req -> // 여기에 타입을 명시하세요
                    InvoiceBankingVO.builder()
                            .invoiceSeq(invoiceSeq)
                            .officeSeq(officeSeq)
                            .bankingCategory(req.bankingCategory() != null ? req.bankingCategory().code() : null)
                            .depositSendDate(DataConvertUtil.parseToOffsetDateTime(req.depositSendDate()))
                            .depositCheckDate(DataConvertUtil.parseToOffsetDateTime(req.depositCheckDate()))
                            .depositAmount(
                                    StringUtils.hasText(req.depositAmount()) ? Integer.valueOf(req.depositAmount()) : 0)
                            .depositName(req.depositName())
                            .depositBank(req.depositBank())
                            .depositFee(req.depositFee())
                            .note(req.note())
                            .createUser(loginUser)
                            .updateUser(loginUser)
                            .delYn("N")
                            .build())
                    .collect(Collectors.toList());

            // 3. 배치 저장
            invoiceMapper.insertInvoiceBankingBatch(bankingList);
        }

        // 4. 저장 후 목록 재조회 (NPE 방지를 위해 페이징 값 명시)
        return this.getInvoiceBankingList(BaseSearchRequest.builder()
                .tblSeq(invoiceSeq)
                .page(1)
                .pageSize(100)
                .build());
    }

    /*
     * =========================================================================
     * [탭 3] 해외송금 (Foreign Banking)
     * =========================================================================
     */

    /**
     * 해외 송금 저장 (카테고리 20 고정)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public InvoiceResponse.InvoiceForeignBankingDetail saveForeignBanking(
            InvoiceRequest.InvoiceForeignBankingDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 0. 기존 데이터 조회
        InvoiceBankingVO oldVo = null;
        if (StringUtils.hasText(request.bankingSeq())) {
            oldVo = invoiceMapper.selectBankingDetail(request.bankingSeq(), officeSeq).orElse(null);
        }

        InvoiceBankingVO vo = InvoiceBankingVO.builder()
                .bankingSeq(request.bankingSeq())
                .invoiceSeq(request.invoiceSeq())
                .officeSeq(officeSeq)
                .bankingCategory("20")
                .bankingKind("해외대리인송금")
                .currencyUnit(Optional.ofNullable(request.currencyUnit()).map(CommonRecordResponse.CodeInfo::code)
                        .orElse(null))
                .depositWay(
                        Optional.ofNullable(request.depositWay()).map(CommonRecordResponse.CodeInfo::code).orElse(null))
                .depositSendDate(DataConvertUtil.parseToOffsetDateTime(request.depositSendDate()))
                .exchangeAmount(StringUtils.hasText(request.exchangeAmount()) ? new BigDecimal(request.exchangeAmount())
                        : BigDecimal.ZERO)
                .exchangeRatio(StringUtils.hasText(request.exchangeRatio()) ? new BigDecimal(request.exchangeRatio())
                        : BigDecimal.ZERO)
                .depositAmount(
                        StringUtils.hasText(request.depositAmount()) ? Integer.valueOf(request.depositAmount()) : 0)
                .depositFee(request.depositFee())
                .note(request.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(vo.getBankingSeq())) {
            invoiceMapper.updateInvoiceBanking(vo);
        } else {
            invoiceMapper.insertInvoiceBanking(vo);
        }

        // 히스토리 기록
        historyService.compareAndLog(vo.getBankingSeq(), "입금내역수정", oldVo, vo);

        return this.getForeignBankingDetail(vo.getBankingSeq());
    }

    /**
     * 해외 송금 목록 조회 (직접 빌더 매핑)
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.InvoiceForeignBankingDetail> getForeignBankingList(
            BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        request.setOfficeSeq(officeSeq);

        int page = (request.getPage() != 0) ? request.getPage() : 1;
        int pageSize = (request.getPageSize() != 0) ? request.getPageSize() : 10;
        request.setOffSet((page - 1) * pageSize);

        List<InvoiceBankingVO> dbList = invoiceMapper.selectInvoiceBankingListFiltered(request, "20", "해외대리인송금");

        List<InvoiceResponse.InvoiceForeignBankingDetail> dtoList = dbList.stream()
                .map(vo -> InvoiceResponse.InvoiceForeignBankingDetail.builder()
                        .bankingSeq(vo.getBankingSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .currencyUnit(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCurrencyUnit())
                                .codeName(vo.getCurrencyUnitName()) // DB 명칭 매핑
                                .build())
                        .depositWay(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getDepositWay())
                                .codeName(vo.getDepositWayName()) // DB 명칭 매핑
                                .build())
                        .depositSendDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositSendDate()))
                        .exchangeAmount(vo.getExchangeAmount() != null ? vo.getExchangeAmount().toString() : "0")
                        .exchangeRatio(vo.getExchangeRatio() != null ? vo.getExchangeRatio().toString() : "0")
                        .depositAmount(vo.getDepositAmount() != null ? vo.getDepositAmount().toString() : "0")
                        .depositFee(vo.getDepositFee())
                        .note(vo.getNote())
                        .build())
                .toList();

        return BaseSearchResponse.of(dtoList, page, dtoList.size());
    }

    /**
     * 해외 송금 상세 조회 (직접 빌더 매핑)
     */
    @Transactional(readOnly = true)
    @Override
    public InvoiceResponse.InvoiceForeignBankingDetail getForeignBankingDetail(String bankingSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return invoiceMapper.selectBankingDetail(bankingSeq, officeSeq)
                .map(vo -> InvoiceResponse.InvoiceForeignBankingDetail.builder()
                        .bankingSeq(vo.getBankingSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .currencyUnit(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCurrencyUnit())
                                .codeName(vo.getCurrencyUnitName())
                                .build())
                        .depositWay(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getDepositWay())
                                .codeName(vo.getDepositWayName())
                                .build())
                        .depositSendDate(DataConvertUtil.formatMinusHoursString8(vo.getDepositSendDate()))
                        .exchangeAmount(vo.getExchangeAmount() != null ? vo.getExchangeAmount().toString() : "0")
                        .exchangeRatio(vo.getExchangeRatio() != null ? vo.getExchangeRatio().toString() : "0")
                        .depositAmount(vo.getDepositAmount() != null ? vo.getDepositAmount().toString() : "0")
                        .depositFee(vo.getDepositFee())
                        .note(vo.getNote())
                        .build())
                .orElse(null);
    }

    /*
     * =========================================================================
     * [탭 3] 실적 분배 (Performance)
     * =========================================================================
     */

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<InvoiceResponse.PerformanceDetail> getPerformanceList(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        
        // 마스터의 실적인정금액 조회
        InvoiceMergeVO masterVo = invoiceMapper.findInvoiceBySeq(request.getTblSeq(), officeSeq);
        String masterPerfAmount = (masterVo != null) ? masterVo.getPerfAmount() : "0";

        List<InvoicePerformanceVO> dbList = invoiceMapper.selectPerformanceList(request.getTblSeq(), officeSeq,
                request.getOffSet(), request.getPageSize());

        List<InvoiceResponse.PerformanceDetail> dtoList = dbList.stream()
                .map(vo -> InvoiceResponse.PerformanceDetail.builder()
                        .performanceSeq(vo.getPerformanceSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .staff(CommonRecordResponse.PersonInfo.builder()
                                .userSeq(vo.getStaff())
                                .userName(vo.getStaffName())
                                .build())
                        .performancePerfDate(vo.getPerformancePerfDate())
                        .performanceCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getPerformanceCategoryCode())
                                .codeName(vo.getPerformanceCategoryName())
                                .build())
                        .deptCategory(vo.getDeptCategory())
                        .performanceContent(vo.getPerformanceContent())
                        .performanceAmount(vo.getPerformanceAmount() != null ? vo.getPerformanceAmount().toString() : "0")
                        .masterPerfAmount(masterPerfAmount)
                        .shareRatio(vo.getShareRatio() != null ? vo.getShareRatio().toString() : "0")
                        .note(vo.getNote())
                        .build())
                .collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, 1, dtoList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse.PerformanceDetail getPerformanceDetail(String performanceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        return invoiceMapper.selectPerformanceDetail(performanceSeq, officeSeq)
                .map(vo -> InvoiceResponse.PerformanceDetail.builder()
                        .performanceSeq(vo.getPerformanceSeq())
                        .invoiceSeq(vo.getInvoiceSeq())
                        .staff(CommonRecordResponse.PersonInfo.builder()
                                .userSeq(vo.getStaff())
                                .userName(vo.getStaffName())
                                .build())
                        .performancePerfDate(vo.getPerformancePerfDate())
                        .performanceCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getPerformanceCategoryCode())
                                .codeName(vo.getPerformanceCategoryName())
                                .build())
                        // .deptCategory(CommonRecordResponse.CodeInfo.builder()
                        // .code(vo.getDeptCategoryCode())
                        // .codeName(vo.getDeptCategoryName())
                        // .build())
                        .deptCategory(vo.getDeptCategory())
                        .performanceAmount(vo.getPerformanceAmount() != null ? vo.getPerformanceAmount().toString() : "0")
                        .performanceContent(vo.getPerformanceContent())
                        .shareRatio(vo.getShareRatio() != null ? vo.getShareRatio().toString() : "0")
                        .note(vo.getNote())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse.PerformanceDetail savePerformance(InvoiceRequest.PerformanceDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 1. 마스터 실적인정금액 조회 및 검증
        InvoiceMergeVO masterVo = invoiceMapper.findInvoiceBySeq(request.invoiceSeq(), officeSeq);
        if (masterVo == null || !StringUtils.hasText(masterVo.getPerfAmount()) || new BigDecimal(masterVo.getPerfAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("실적금액이 존재하지 않습니다. 마스터 정보의 실적인정금액을 확인해주세요.", ErrorCode.INVALID_INPUT_VALUE);
        }
        BigDecimal masterPerfAmount = new BigDecimal(masterVo.getPerfAmount());

        // 2. 현재 저장된 다른 실적들의 합계 조회 (현재 수정 중인 건 제외)
        List<InvoicePerformanceVO> currentList = invoiceMapper.selectPerformanceList(request.invoiceSeq(), officeSeq, 0, 100);
        BigDecimal otherTotal = currentList.stream()
                .filter(p -> !p.getPerformanceSeq().equals(request.performanceSeq()))
                .map(p -> p.getPerformanceAmount() != null ? p.getPerformanceAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newAmount = StringUtils.hasText(request.performanceAmount()) ? new BigDecimal(request.performanceAmount()) : BigDecimal.ZERO;
        
        if (otherTotal.add(newAmount).compareTo(masterPerfAmount) > 0) {
            throw new BusinessException("실적분배금액의 합계가 실적인정금액(" + masterPerfAmount + ")을 초과할 수 없습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 0. 기존 데이터 조회
        InvoicePerformanceVO oldVo = null;
        if (StringUtils.hasText(request.performanceSeq())) {
            oldVo = invoiceMapper.selectPerformanceDetail(request.performanceSeq(), officeSeq).orElse(null);
        }

        InvoicePerformanceVO vo = InvoicePerformanceVO.builder()
                .performanceSeq(request.performanceSeq())
                .invoiceSeq(request.invoiceSeq())
                .officeSeq(officeSeq)
                .staff(request.staff().userSeq())
                .performanceAmount(newAmount)
                .performanceContent(request.performanceContent())
                .deptCategory(request.deptCategory())
                .performanceCategoryCode(request.performanceCategory().code())
                .performancePerfDate(request.performancePerfDate())
                .shareRatio(new BigDecimal(request.shareRatio()))
                .note(request.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (StringUtils.hasText(vo.getPerformanceSeq())) {
            invoiceMapper.updatePerformance(vo);
        } else {
            invoiceMapper.insertPerformance(vo);
        }

        // 히스토리 기록
        historyService.compareAndLog(vo.getPerformanceSeq(), "실적내역수정", oldVo, vo);

        return this.getPerformanceDetail(vo.getPerformanceSeq());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseSearchResponse<InvoiceResponse.PerformanceDetail> savePerformanceList(String invoiceSeq,
            List<InvoiceRequest.PerformanceDetail> requests) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        // 1. 마스터 실적인정금액 조회 및 검증
        InvoiceMergeVO masterVo = invoiceMapper.findInvoiceBySeq(invoiceSeq, officeSeq);
        BigDecimal masterPerfAmount = BigDecimal.ZERO;
        if (masterVo == null || !StringUtils.hasText(masterVo.getPerfAmount()) || new BigDecimal(masterVo.getPerfAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("실적금액이 존재하지 않습니다. 마스터 정보의 실적인정금액을 확인해주세요.", ErrorCode.INVALID_INPUT_VALUE);
        }
        masterPerfAmount = new BigDecimal(masterVo.getPerfAmount());

        // 2. 분배 금액 합계 검증
        if (!ObjectUtils.isEmpty(requests)) {
            BigDecimal totalRequestAmount = requests.stream()
                    .map(req -> StringUtils.hasText(req.performanceAmount()) ? new BigDecimal(req.performanceAmount()) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalRequestAmount.compareTo(masterPerfAmount) > 0) {
                throw new BusinessException("실적분배금액의 합계(" + totalRequestAmount + ")가 실적인정금액(" + masterPerfAmount + ")을 초과할 수 없습니다.", ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        invoiceMapper.deletePerformances(invoiceSeq, officeSeq);

        if (!ObjectUtils.isEmpty(requests)) {
            List<InvoicePerformanceVO> perfList = requests.stream().map(req -> InvoicePerformanceVO.builder()
                    .invoiceSeq(invoiceSeq)
                    .officeSeq(officeSeq)
                    .staff(req.staff().userSeq())
                    .performanceAmount(new BigDecimal(req.performanceAmount()))
                    .performancePerfDate(req.performancePerfDate())
                    .performanceContent(req.performanceContent())
                    .performanceCategoryCode(req.performanceCategory().code())
                    .deptCategory(req.deptCategory())
                    .shareRatio(new BigDecimal(req.shareRatio()))
                    .note(req.note())
                    .createUser(loginUser)
                    .updateUser(loginUser)
                    .delYn("N")
                    .build()).collect(Collectors.toList());

            invoiceMapper.insertPerformanceBatch(perfList);
        }
        return this.getPerformanceList(BaseSearchRequest.builder()
                .tblSeq(invoiceSeq)
                .pageSize(20)
                .offSet(0)
                .build());
    }

    @Override
    public BaseSearchResponse<InvoiceResponse.AppDetail> getAppDetailList(BaseSearchRequest request) {
        // 1. 매퍼를 통해 뷰 조인 결과(VO) 가져오기

        String officeSeq = SecurityUtil.getOfficeSeq();

        request.setOfficeSeq(officeSeq);
        request.setOffSet(request.getOffSet());

        List<InvoiceResponse.AppDetail> dtoList = invoiceMapper.findAppDetailList(request)
                .stream()
                .map(vo -> InvoiceResponse.AppDetail.builder()
                        // --- [기본 정보] ---
                        .appSeq(vo.getAppSeq())
                        .appNo(vo.getAppNo())
                        .titleKo(vo.getTitleKo())
                        .titleEn(vo.getTitleEn())
                        .country(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCountryCode())
                                .codeName(vo.getCountryName())
                                .build())
                        .ourRef(vo.getOurRef())
                        .yourRef(vo.getYourRef())
                        .clientRef(vo.getClientRef())
                        .regNo(vo.getRegNo())
                        .deptName(vo.getDeptName())
                        .niceClass(vo.getNiceClass())
                        .adminMgr(CommonRecordResponse.PersonInfo.builder()
                                .userSeq(vo.getAdminMgr())
                                .userName(vo.getAdminMgrNm())
                                .build())
                        .caseMgr(CommonRecordResponse.PersonInfo.builder()
                                .userSeq(vo.getCaseMgr())
                                .userName(vo.getCaseMgrNm())
                                .build())
                        .attorney(CommonRecordResponse.PersonInfo.builder()
                                .userSeq(vo.getAttorney())
                                .userName(vo.getAttorneyNm())
                                .build())
                        // --- [규격/수량 정보] ---
                        .grade(vo.getGrade())
                        .independentClaims(vo.getIndependentClaims())
                        .dependentClaims(vo.getDependentClaims())
                        .drawingCount(vo.getDrawingCount())
                        .figureCount(vo.getFigureCount())
                        .specCount(vo.getSpecCount())
                        .domesticRegNo(vo.getDomesticRegNo())

                        // --- [날짜 정보] ---
                        .appDate(vo.getAppDate())
                        .regDate(vo.getRegDate())
                        .pubDate(vo.getPubDate())
                        .domesticRegDecisionDate(vo.getDomesticRegDecisionDate())
                        .domesticRegDate(vo.getDomesticRegDate())
                        .intlRegDate(vo.getIntlRegDate())

                        // --- [코드 정보 빌더 매핑] ---
                        .rightType(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getRightTypeCode())
                                .codeName(vo.getRightTypeName())
                                .build())

                        .caseCategory(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getCaseCategoryCode())
                                .codeName(vo.getCaseCategoryName())
                                .build())

                        // --- [인적 정보 매핑] ---
                        .applicantName(vo.getApplicantNm())
                        .clientName(vo.getClientNm())
                        .foreignAgentName(vo.getForeignAgentName())
                        .build())
                .toList();

        // 2. 공통 응답 객체로 반환
        return BaseSearchResponse.of(dtoList, 1, dtoList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInvoice(String invoiceSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteInvoiceMst(invoiceSeq, officeSeq, userId);
        // RAG 삭제 동기화
        ragService.syncVectorData(officeSeq, "INVOICE", invoiceSeq, "청구서", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClaim(String claimSeq) {
        Optional<InvoiceClaimVO> claimOpt = invoiceMapper.selectClaimDetail(claimSeq, SecurityUtil.getOfficeSeq());
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteInvoiceClaim(claimSeq, SecurityUtil.getOfficeSeq(), userId);
        // 부모 청구서 RAG 동기화
        claimOpt.ifPresent(vo -> syncParentInvoiceRag(vo.getInvoiceSeq()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBanking(String bankingSeq) {
        Optional<InvoiceBankingVO> bankingOpt = invoiceMapper.selectBankingDetail(bankingSeq,
                SecurityUtil.getOfficeSeq());
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteInvoiceBanking(bankingSeq, SecurityUtil.getOfficeSeq(), userId);
        // 부모 청구서 RAG 동기화
        bankingOpt.ifPresent(vo -> syncParentInvoiceRag(vo.getInvoiceSeq()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePerformance(String performanceSeq) {
        Optional<InvoicePerformanceVO> perfOpt = invoiceMapper.selectPerformanceDetail(performanceSeq,
                SecurityUtil.getOfficeSeq());
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteInvoicePerformance(performanceSeq, SecurityUtil.getOfficeSeq(), userId);
        // 부모 청구서 RAG 동기화
        perfOpt.ifPresent(vo -> syncParentInvoiceRag(vo.getInvoiceSeq()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInvoiceList(List<String> invoiceSeqs) {
        if (invoiceSeqs == null || invoiceSeqs.isEmpty())
            return;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteInvoiceList(officeSeq, invoiceSeqs, userId);
        // RAG 일괄 삭제 동기화
        for (String seq : invoiceSeqs) {
            ragService.syncVectorData(officeSeq, "INVOICE", seq, "청구서", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClaimList(List<String> claimSeqs, String invoiceSeq) {
        if (claimSeqs == null || claimSeqs.isEmpty())
            return;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteClaimList(officeSeq, claimSeqs, userId);
        syncParentInvoiceRag(invoiceSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBankingList(List<String> bankingSeqs, String invoiceSeq) {
        if (bankingSeqs == null || bankingSeqs.isEmpty())
            return;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deleteBankingList(officeSeq, bankingSeqs, userId);
        syncParentInvoiceRag(invoiceSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePerformanceList(List<String> performanceSeqs, String invoiceSeq) {
        if (performanceSeqs == null || performanceSeqs.isEmpty())
            return;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        invoiceMapper.deletePerformanceList(officeSeq, performanceSeqs, userId);
        syncParentInvoiceRag(invoiceSeq);
    }

    /**
     * 부모 청구서 데이터 RAG 동기화 헬퍼
     */
    private void syncParentInvoiceRag(String invoiceSeq) {
        if (!StringUtils.hasText(invoiceSeq))
            return;
        InvoiceMergeVO merged = invoiceMapper.findInvoiceBySeq(invoiceSeq, SecurityUtil.getOfficeSeq());
        if (merged != null) {
            ragService.syncVectorData(merged.getOfficeSeq(), "INVOICE", invoiceSeq, "청구서", merged);
        }
    }
}