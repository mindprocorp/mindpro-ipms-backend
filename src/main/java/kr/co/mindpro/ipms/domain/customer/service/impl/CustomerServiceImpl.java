package kr.co.mindpro.ipms.domain.customer.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.bizinfo.repository.db1.BizInfoMapper;
import kr.co.mindpro.ipms.domain.bizinfo.vo.BizInfoVO;
import kr.co.mindpro.ipms.domain.customer.dto.request.CustomerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.ManagerRequest;
import kr.co.mindpro.ipms.domain.customer.dto.request.WrapperMandateRequest;
import kr.co.mindpro.ipms.domain.customer.dto.response.CustomerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.ManagerResponse;
import kr.co.mindpro.ipms.domain.customer.dto.response.WrapperMandateResponse;
import kr.co.mindpro.ipms.domain.customer.repository.db1.CustomerMapper;
import kr.co.mindpro.ipms.domain.customer.service.CustomerService;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerMappVO;
import kr.co.mindpro.ipms.domain.customer.vo.CustomerVO;
import kr.co.mindpro.ipms.domain.customer.vo.WrapperMandateVO;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.paper.vo.PaperResponseVO;
import kr.co.mindpro.ipms.domain.participant.repository.db1.ParticipantMapper;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import kr.co.mindpro.ipms.domain.user.service.UserService;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [Service] 고객 관리 비즈니스 로직 구현체
 *
 * @author   : min
 * @fileName : CustomerServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final BizInfoMapper bizInfoMapper;
    private final PaperService paperService;
    private final UserMapper userMapper;
    private final ParticipantMapper participantMapper;
    private final RagService ragService;
    private final HistoryService historyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse.CustomerDetail saveCustomer(CustomerRequest.CustomerDetail request, MultipartFile customerFile) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        // 0. 기존 데이터 조회 (히스토리용)
        CustomerVO oldVo = null;
        if (StringUtils.hasText(request.customerSeq())) {
            oldVo = customerMapper.selectCustomerDetail(request.customerSeq(), officeSeq);
        }

        // 1. BizInfo 생성 로직
        String bizInfoSeq = request.bizInfoSeq();
        
        // request에 bizInfoSeq가 없더라도 기존 데이터(oldVo)에 있다면 그것을 사용함 (중복 생성 방지)
        if (!StringUtils.hasText(bizInfoSeq) && oldVo != null && StringUtils.hasText(oldVo.getBizInfoSeq())) {
            bizInfoSeq = oldVo.getBizInfoSeq();
        }

        boolean hasAnyBizInfo = StringUtils.hasText(request.bizRegNo())
                || StringUtils.hasText(request.subBizRegNo())
                || StringUtils.hasText(request.bizName())
                || StringUtils.hasText(request.bizCEO())
                || StringUtils.hasText(request.bizAddress())
                || StringUtils.hasText(request.bizType())
                || StringUtils.hasText(request.bizItem())
                || StringUtils.hasText(request.reliefTarget())
                || StringUtils.hasText(request.reliefReason())
                || StringUtils.hasText(request.reliefIssueDate())
                || StringUtils.hasText(request.reliefExemptionDate());

        if (StringUtils.hasText(bizInfoSeq)) {
            // 수정 모드
            BizInfoVO bizVo = BizInfoVO.builder()
                    .bizInfoSeq(bizInfoSeq)
                    .officeSeq(officeSeq)
                    .bizRegNo(request.bizRegNo())
                    .bizWorkplaceNo(request.subBizRegNo())
                    .bizCorpName(request.bizName())
                    .ceoName(request.bizCEO())
                    .bizAddr(request.bizAddress())
                    .bizType(request.bizType())
                    .bizKind(request.bizItem())
                    .regDiscountCode(request.reliefTarget())
                    .reductionReason(request.reliefReason())
                    .reductionIssueDate(request.reliefIssueDate())
                    .discountClosingDate(request.reliefExemptionDate())
                    .updateUser(userId)
                    .build();
            bizInfoMapper.updateBizInfo(bizVo);
        } else if (hasAnyBizInfo) {
            // 신규 생성 모드 (사업장정보/감면사유 중 하나라도 입력 시)
            BizInfoVO bizVo = BizInfoVO.builder()
                    .officeSeq(officeSeq)
                    .bizRegNo(request.bizRegNo())
                    .bizWorkplaceNo(request.subBizRegNo())
                    .bizCorpName(request.bizName())
                    .ceoName(request.bizCEO())
                    .bizAddr(request.bizAddress())
                    .bizType(request.bizType())
                    .bizKind(request.bizItem())
                    .regDiscountCode(request.reliefTarget())
                    .reductionReason(request.reliefReason())
                    .reductionIssueDate(request.reliefIssueDate())
                    .discountClosingDate(request.reliefExemptionDate())
                    .createUser(userId)
                    .build();

            bizInfoMapper.insertBizInfo(bizVo);
            bizInfoSeq = bizVo.getBizInfoSeq();
        }



        // 3. CustomerVO 빌드
        CustomerVO customerVo = CustomerVO.builder()
                .customerSeq(request.customerSeq())
                .officeSeq(officeSeq)
                .bizInfoSeq(bizInfoSeq)
                .clientCategoryCode(request.clientCategory())
                .applicantCategoryCode(request.applicantCategory())
                .corpCategoryCode(request.corpCategory())
                .attorneyCategoryCode(request.attorneyCategory())
                .clientNameKo(request.clientNameKo())
                .clientNameEn(request.clientNameEn())
                .clientNameCh(request.clientNameCh())
                .clientNameJp(request.clientNameJp())
                .companyName(request.companyName())
                .deptName(request.deptName())
                .customerPosition(request.position())
                .appZipCode(request.appZipCode())
                .appAddress(request.appAddress())
                .appTel(request.appTel())
                .appFax(request.appFax())
                .contactZipCode(request.contactZipCode())
                .contactAddress(request.contactAddress())
                .contactPerson(request.contactPerson())
                .contactTel(request.contactTel())
                .contactFax(request.contactFax())
                .etcZipCode(request.etcZipCode())
                .etcAddress(request.etcAddress())
                .etcTel(request.etcTel())
                .etcFax(request.etcFax())
                .overseaZipCode(request.overseaZipCode())
                .overseaAddress(request.overseaAddress())
                .overseaTel(request.overseaTel())
                .overseaFax(request.overseaFax())
                .countryCode(request.countryCode())
                .residentRegNo(request.residentRegNo())
                .corpRegNo(request.corpRegNo())
                .kipoClientNo(request.kipoClientNo())
                .managerName(request.managerName())
                .generalMandateNo(request.generalMandateNo())
                .mobile(request.mobile())
                .homepage(request.homepage())
                .email(request.email())
                .registrationDate(request.registrationDate())
                .note(request.note())
                .createUser(userId)
                .updateUser(userId)
                .bizRegNo(request.bizRegNo())
                .bizCorpName(request.bizName())
                .ceoName(request.bizCEO())
                .bizAddr(request.bizAddress())
                .bizType(request.bizType())
                .bizKind(request.bizItem())
                .build();

        // 4. 저장 또는 수정
        if (oldVo == null) {
            customerMapper.insertCustomer(customerVo);
        } else {
            customerMapper.updateCustomer(customerVo);
            historyService.compareAndLog(customerVo.getCustomerSeq(), "고객정보수정", oldVo, customerVo);
        }

        // [수정] 2. 파일 처리 로직 (고객 seq 가 생성된 이후에 처리해야 함)
        if (customerFile != null && !customerFile.isEmpty()) {
            paperService.saveFileMapping(PaperRequestVO.builder()
                    .officeSeq(officeSeq)
                    .tblSeq(bizInfoSeq != null ? bizInfoSeq : customerVo.getCustomerSeq())
                    .tblCode("CUSTOMER")
                    .file(customerFile)
                    .fileCategoryCode("customerFile")
                    .docSeq("397")
                    .createUser(userId)
                    .build());
        }

        List<PaperResponseVO> fileList = paperService.getFileMappByWork(customerVo.getBizInfoSeq() != null ? customerVo.getBizInfoSeq() : customerVo.getCustomerSeq(), SecurityUtil.getOfficeSeq());
        CustomerResponse.CustomerDetail detail = convertToDetail(customerVo, fileList);

        // 5. RAG 동기화
        ragService.syncVectorData(officeSeq, "CUSTOMER", detail.customerSeq(), "고객", detail);

        return detail;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse.CustomerDetail getCustomerDetail(String customerSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        CustomerVO vo = customerMapper.selectCustomerDetail(customerSeq, officeSeq);
        List<PaperResponseVO> fileList = paperService.getFileMappByWork(vo.getBizInfoSeq() != null ? vo.getBizInfoSeq() : customerSeq, officeSeq);
        return convertToDetail(vo, fileList);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<CustomerResponse.CustomerDetail> getCustomerList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<CustomerVO> voList = customerMapper.selectCustomerList(request);
        List<CustomerResponse.CustomerDetail> detailList = voList.stream()
                .map(vo -> convertToDetail(vo, null))
                .collect(Collectors.toList());
        return BaseSearchResponse.of(detailList, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<CustomerResponse.CustomerSearchList> getModalCustomerSearchList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<CustomerVO> voList = customerMapper.selectCustomerList(request);
        List<CustomerResponse.CustomerSearchList> detailList = voList.stream()
                .map(vo -> CustomerResponse.CustomerSearchList.builder()
                        .customerSeq(vo.getCustomerSeq())
                        .clientNameKo(vo.getClientNameKo())
                        .clientNameEn(vo.getClientNameEn())
                        .companyName(vo.getCompanyName())
                        .countryCode(vo.getCountryCode())
                        .kipoClientNo(vo.getKipoClientNo())
                        .bizRegNo(vo.getBizRegNo())
                        .orderNo(vo.getOrderNo())
                        .build())
                .collect(Collectors.toList());
        return BaseSearchResponse.of(detailList, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse.CustomerSearchList> searchCustomerMaster(
            String officeSeq, String categoryCode, String keyword, Integer pageSize, Integer offSet) {
        // [통합] CLIENT_DIV 분류로 인해 발명자도 utb_customer 도메인 — 단일 SQL 로 처리
        String resolvedOffice = (officeSeq != null && !officeSeq.isBlank()) ? officeSeq : SecurityUtil.getOfficeSeq();
        Integer ps = (pageSize == null || pageSize <= 0) ? 50 : pageSize;
        Integer os = (offSet == null || offSet < 0) ? 0 : offSet;
        List<CustomerVO> voList = customerMapper.searchCustomerMaster(resolvedOffice, categoryCode, keyword, ps, os);
        return voList.stream().map(vo -> CustomerResponse.CustomerSearchList.builder()
                .customerSeq(vo.getCustomerSeq())
                .clientNameKo(vo.getClientNameKo())
                .clientNameEn(vo.getClientNameEn())
                .companyName(vo.getCompanyName())
                .countryCode(vo.getCountryCode())
                .kipoClientNo(vo.getKipoClientNo())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<CustomerResponse.CustomerSearchList> getModalCustomerSearchListByCategoryCode(BaseSearchRequest request, String customerCategoryCode) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<CustomerVO> voList = customerMapper.selectCustomerListByWork(request, customerCategoryCode);
        List<CustomerResponse.CustomerSearchList> detailList = voList.stream()
                .map(vo -> CustomerResponse.CustomerSearchList.builder()
                        .customerSeq(vo.getCustomerSeq())
                        .clientNameKo(vo.getClientNameKo())
                        .clientNameEn(vo.getClientNameEn())
                        .companyName(vo.getCompanyName())
                        .countryCode(vo.getCountryCode())
                        .kipoClientNo(vo.getKipoClientNo())
                        .orderNo(vo.getOrderNo())
                        .bizRegNo(vo.getBizRegNo())
                        .build())
                .collect(Collectors.toList());
        return BaseSearchResponse.of(detailList, request.getPage(), request.getPageSize());
    }

    private CustomerResponse.CustomerDetail convertToDetail(CustomerVO vo, List<PaperResponseVO> fileList) {
        if (vo == null) return null;
        return CustomerResponse.CustomerDetail.builder()
                .customerSeq(vo.getCustomerSeq())
                .clientCategory(vo.getClientCategoryCode())
                .applicantCategory(vo.getApplicantCategoryCode())
                .corpCategory(vo.getCorpCategoryCode())
                .attorneyCategory(vo.getAttorneyCategoryCode())
                .clientNameKo(vo.getClientNameKo())
                .clientNameEn(vo.getClientNameEn())
                .clientNameCh(vo.getClientNameCh())
                .clientNameJp(vo.getClientNameJp())
                .companyName(vo.getCompanyName())
                .deptName(vo.getDeptName())
                .position(vo.getCustomerPosition())
                .appZipCode(vo.getAppZipCode())
                .appAddress(vo.getAppAddress())
                .appTel(vo.getAppTel())
                .appFax(vo.getAppFax())
                .contactZipCode(vo.getContactZipCode())
                .contactAddress(vo.getContactAddress())
                .contactPerson(vo.getContactPerson())
                .contactTel(vo.getContactTel())
                .contactFax(vo.getContactFax())
                .etcZipCode(vo.getEtcZipCode())
                .etcAddress(vo.getEtcAddress())
                .etcTel(vo.getEtcTel())
                .etcFax(vo.getEtcFax())
                .overseaZipCode(vo.getOverseaZipCode())
                .overseaAddress(vo.getOverseaAddress())
                .overseaTel(vo.getOverseaTel())
                .overseaFax(vo.getOverseaFax())
                .countryCode(vo.getCountryCode())
                .residentRegNo(vo.getResidentRegNo())
                .corpRegNo(vo.getCorpRegNo())
                .kipoClientNo(vo.getKipoClientNo())
                .managerName(vo.getManagerName())
                .generalMandateNo(vo.getGeneralMandateNo())
                .mobile(vo.getMobile())
                .homepage(vo.getHomepage())
                .email(vo.getEmail())
                .registrationDate(vo.getRegistrationDate())
                .note(vo.getNote())
                .bizInfoSeq(vo.getBizInfoSeq())
                .bizRegNo(vo.getBizRegNo())
                .subBizRegNo(vo.getBizWorkplaceNo())
                .bizName(vo.getBizCorpName())
                .bizCEO(vo.getCeoName())
                .bizAddress(vo.getBizAddr())
                .bizType(vo.getBizType())
                .bizItem(vo.getBizKind())
                .reliefTarget(vo.getRegDiscountCode())
                .reliefReason(vo.getReductionReason())
                .reliefIssueDate(vo.getReductionIssueDate())
                .reliefExemptionDate(vo.getDiscountClosingDate())
                .customerFileList(CommonRecordResponse.FileInfo.from(fileList))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<WrapperMandateResponse.WrapperMandateDetail> getMandateList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<WrapperMandateVO> dbList = customerMapper.findMandateListByCustomer(request);
        List<WrapperMandateResponse.WrapperMandateDetail> dtoList = dbList.stream()
                .map(WrapperMandateResponse.WrapperMandateDetail::from)
                .collect(Collectors.toList());
        return BaseSearchResponse.of(dtoList, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public WrapperMandateResponse.WrapperMandateDetail getMandateDetail(String wrappermandateSeq) {
        WrapperMandateVO vo = customerMapper.findMandateDetail(wrappermandateSeq, SecurityUtil.getOfficeSeq());
        return (vo != null) ? WrapperMandateResponse.WrapperMandateDetail.from(vo) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperMandateResponse.WrapperMandateDetail saveMandate(WrapperMandateRequest.WrapperMandateDetail request) {
        if (request == null) return null;
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        WrapperMandateVO oldVo = null;
        if (StringUtils.hasText(request.wrappermandateSeq())) {
            oldVo = customerMapper.findMandateDetail(request.wrappermandateSeq(), officeSeq);
        }

        WrapperMandateVO vo = WrapperMandateVO.builder()
                .officeSeq(officeSeq)
                .wrappermandateSeq(request.wrappermandateSeq())
                .customerSeq(request.customerSeq())
                .attorneyName(request.attorneyName())
                .designatedAttorney(request.designatedAttorney())
                .agentNo(request.agentNo())
                .mandateDate(request.mandateDate())
                .mandateWrapperNo(request.mandateWrapperNo())
                .patentCustomerNo(request.patentCustomerNo())
                .mandateRange(request.mandateRange())
                .sortOrder(request.sort())
                .note(request.note())
                .createUser(userId)
                .updateUser(userId)
                .delYn("N")
                .build();

        if (oldVo == null) {
            customerMapper.insertMandate(vo);
        } else {
            customerMapper.updateMandate(vo);
            historyService.compareAndLog(vo.getWrappermandateSeq(), "포괄위임수정", oldVo, vo);
        }

        WrapperMandateResponse.WrapperMandateDetail result = this.getMandateDetail(vo.getWrappermandateSeq());
        ragService.syncVectorData(officeSeq, "WRAPPER_MANDATE", result.wrappermandateSeq(), "포괄위임", result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ManagerResponse.CustomerManagerResponse saveCustomerManager(ManagerRequest.CustomerManagerRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String currentUser = SecurityUtil.getUserInfoSeq();

        ManagerResponse.CustomerManagerResponse oldDetail = null;
        if (StringUtils.hasText(request.participantSeq())) {
            oldDetail = customerMapper.selectCustomerManagerDetail(request.participantSeq(), officeSeq);
        }

        UserMasterVO userVo = UserMasterVO.builder()
                .userInfoSeq(request.userInfoSeq())
                .officeSeq(officeSeq)
                .userNameKo(request.userNameKo())
                .userMobileNo(request.userMobileNo())
                .userEmail(request.userEmail())
                .deptName(request.deptName())
                .userPosition(request.userPosition())
                .userTelNo(request.userTelNo())
                .userFaxNo(request.userFaxNo())
                .userPostNo(request.userPostNo())
                .userAddr(request.userAddr())
                .userAddrDetail(request.userAddrDetail())
                .etaxYn(request.etaxYn())
                .createUser(currentUser)
                .updateUser(currentUser)
                .build();

        String finalUserInfoSeq = request.userInfoSeq();
        String targetParticipantSeq = request.participantSeq();

        if (StringUtils.hasText(finalUserInfoSeq)) {
            if (userMapper.updateUserInfoToDeletedIfChanged(userVo) > 0) {
                if (StringUtils.hasText(request.participantSeq())) {
                    participantMapper.updateParticipantDelYnBySeq(request.participantSeq(), officeSeq, currentUser);
                    targetParticipantSeq = null; // 기존 것이 삭제되었으므로 새 레코드 생성을 위해 null 설정
                }
                userMapper.insertUserInfo(userVo);
                finalUserInfoSeq = userVo.getUserInfoSeq();
            }
        } else {
            userMapper.insertUserInfo(userVo);
            finalUserInfoSeq = userVo.getUserInfoSeq();
        }

        ParticipantVO partVo = ParticipantVO.builder()
                .participantSeq(targetParticipantSeq)
                .tblSeq(request.tblSeq())
                .userInfoSeq(finalUserInfoSeq)
                .officeSeq(officeSeq)
                .participantCode(request.participantCode())
                .note(request.note())
                .createUser(currentUser)
                .updateUser(currentUser)
                .mainYn("Y".equals(request.etaxYn()) ? "Y" : "N")
                .build();

        if (!StringUtils.hasText(partVo.getParticipantSeq())) {
            participantMapper.insertParticipant(partVo);
        } else {
            participantMapper.updateParticipant(partVo);
        }

        ManagerResponse.CustomerManagerResponse result = customerMapper.selectCustomerManagerDetail(partVo.getParticipantSeq(), officeSeq);
        
        // Log history if it was an update
        if (oldDetail != null) {
            historyService.compareAndLog(partVo.getParticipantSeq(), "담당자정보수정", oldDetail, result);
        }
        
        ragService.syncVectorData(officeSeq, "CUSTOMER_MANAGER", result.participantSeq(), "고객담당자", result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<ManagerResponse.CustomerManagerResponse> getManagerList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<ManagerResponse.CustomerManagerResponse> detailList = customerMapper.selectCustomerManagerList(request, null);
        return BaseSearchResponse.of(detailList, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerResponse.CustomerManagerResponse getManagerDetail(String participantSeq) {
        return customerMapper.selectCustomerManagerDetail(participantSeq, SecurityUtil.getOfficeSeq());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse.CustomerMappDetail registerMapping(CustomerRequest.CustomerMappDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        // 0. 기존 데이터 조회 (히스토리용)
        CustomerResponse.CustomerMappDetail oldDetail = null;
        if (StringUtils.hasText(request.customerMappSeq())) {
            oldDetail = customerMapper.selectCustomerMappDetail(request.customerMappSeq(), officeSeq);
        }

        // 1. VO 조립
        CustomerMappVO vo = CustomerMappVO.builder()
                .customerMappSeq(request.customerMappSeq())
                .officeSeq(officeSeq)
                .customerSeq(request.customerSeq())
                .tblSeq(request.tblSeq())
                .relationCode(request.relationCode())
                .note(request.note())
                .createUser(userId)
                .updateUser(userId)
                .build();

        // 2. 저장 또는 수정
        if (oldDetail == null) {
            customerMapper.insertCustomerMapp(vo);
        } else {
            customerMapper.updateCustomerMapp(vo);
            // 3. 히스토리 기록
            historyService.compareAndLog(vo.getCustomerMappSeq(), "관련고객사수정", oldDetail, request);
        }

        // 4. 결과 조회 및 RAG 동기화
        CustomerResponse.CustomerMappDetail result = customerMapper.selectCustomerMappDetail(vo.getCustomerMappSeq(), officeSeq);
        ragService.syncVectorData(officeSeq, "CUSTOMER_MAPPING", result.customerMappSeq(), "관련고객사", result);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<CustomerResponse.CustomerMappDetail> getMappingList(BaseSearchRequest request, String customerCategoryCode) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        List<CustomerResponse.CustomerMappDetail> detailList = customerMapper.selectCustomerMappList(request, customerCategoryCode);
        return BaseSearchResponse.of(detailList, request.getPage(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse.CustomerMappDetail getMappingDetail(String customerMappSeq) {
        return customerMapper.selectCustomerMappDetail(customerMappSeq, SecurityUtil.getOfficeSeq());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(String customerSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        customerMapper.deleteCustomerMst(customerSeq, officeSeq);
        ragService.syncVectorData(officeSeq, "CUSTOMER", customerSeq, "고객", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMandate(String wrappermandateSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        customerMapper.deleteMandate(wrappermandateSeq, officeSeq);
        ragService.syncVectorData(officeSeq, "WRAPPER_MANDATE", wrappermandateSeq, "포괄위임", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerManager(String participantSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        customerMapper.deleteCustomerManager(participantSeq, officeSeq);
        ragService.syncVectorData(officeSeq, "CUSTOMER_MANAGER", participantSeq, "고객담당자", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerMapp(String customerMappSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        customerMapper.deleteCustomerMapp(customerMappSeq, officeSeq);
        ragService.syncVectorData(officeSeq, "CUSTOMER_MAPPING", customerMappSeq, "관련고객사", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerList(List<String> ids) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        customerMapper.updateCustomerDelYn(ids, officeSeq, userId);
        for (String id : ids) {
            ragService.syncVectorData(officeSeq, "CUSTOMER", id, "고객", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMandateList(List<String> ids) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        customerMapper.updateMandateDelYn(ids, officeSeq, userId);
        for (String id : ids) {
            ragService.syncVectorData(officeSeq, "WRAPPER_MANDATE", id, "포괄위임", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerManagerList(List<String> ids) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        customerMapper.updateCustomerManagerDelYn(ids, officeSeq, userId);
        for (String id : ids) {
            ragService.syncVectorData(officeSeq, "CUSTOMER_MANAGER", id, "고객담당자", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerMappList(List<String> ids) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();
        customerMapper.updateCustomerMappDelYn(ids, officeSeq, userId);
        for (String id : ids) {
            ragService.syncVectorData(officeSeq, "CUSTOMER_MAPPING", id, "관련고객사", null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCustomerMappToWork(List<CustomerMappVO> request) {
        if (request == null || request.isEmpty()) return;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        // 1. 기존 고객 매핑 정보 soft delete
        // tblSeq와 customerCategoryCode가 동일한 조합은 한 번만 삭제 처리
        java.util.Set<String> deletedKeys = new java.util.HashSet<>();

        for (CustomerMappVO vo : request) {
            String key = vo.getTblSeq() + "_" + vo.getCustomerCategoryCode();
            if (!deletedKeys.contains(key)) {
                CustomerMappVO deleteTarget = CustomerMappVO.builder()
                        .officeSeq(officeSeq)
                        .tblSeq(vo.getTblSeq())
                        .customerCategoryCode(vo.getCustomerCategoryCode())
                        .updateUser(userId)
                        .build();
                
                customerMapper.softDeleteCustomerMapp(deleteTarget);
                deletedKeys.add(key);
            }
        }

        // 2. 새로운 고객 매핑 정보 insert
        for (CustomerMappVO vo : request) {
            vo.setOfficeSeq(officeSeq);
            vo.setCreateUser(userId);
            vo.setUpdateUser(userId);
            vo.setDelYn("N");
            customerMapper.insertCustomerMapp(vo);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerFile(String customerSeq, String fileSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        // 고객 이미지 파일 논리 삭제
        customerMapper.softDeleteCustomerFileByFileSeq(officeSeq, fileSeq, loginUser);
    }
}
