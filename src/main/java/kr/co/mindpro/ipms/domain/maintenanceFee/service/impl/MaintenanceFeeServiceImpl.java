package kr.co.mindpro.ipms.domain.maintenanceFee.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.request.MaintenanceFeeRequest;
import kr.co.mindpro.ipms.domain.maintenanceFee.dto.response.MaintenanceFeeResponse;
import kr.co.mindpro.ipms.domain.maintenanceFee.repository.db1.MaintenanceFeeMapper;
import kr.co.mindpro.ipms.domain.maintenanceFee.service.MaintenanceFeeService;
import kr.co.mindpro.ipms.domain.maintenanceFee.vo.MaintenanceFeeVO;
import kr.co.mindpro.ipms.domain.requiredDoc.vo.RequiredDocVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

/**
 * @author : seokho
 * @fileName : MaintenanceFeeServiceImpl.java
 * @since : 2026. 4. 2.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceFeeServiceImpl implements MaintenanceFeeService {

    private final MaintenanceFeeMapper maintenanceFeeMapper;

    private final DueDateService dueDateService;
    private final RagService ragService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMaintenanceFee(MaintenanceFeeRequest.CreateMaintenanceFeeRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String maintFeeSeq = request.maintenanceFeeSeq();
        String appSeq = request.appSeq();

        int result = 0;

        MaintenanceFeeVO maintenanceFeeVO = MaintenanceFeeVO.builder()
                .officeSeq(officeSeq)
                .appSeq(appSeq)
                .nextPaymentInstallment(request.nextPaymentInstallment())
                .note(request.note())
                .maintFeeDeadline(request.maintFeeDeadline())
                .maintFeePenaltyDeadline(request.maintFeePenaltyDeadline())
                .maintFeeOrderDate(request.maintFeeOrderDate())
                .maintFeePaymentDate(request.maintFeePaymentDate())
                .build();

        if (StringUtils.hasText(maintFeeSeq)) {
            result = maintenanceFeeMapper.getDuplicateMaintenanceCnt(officeSeq, appSeq, maintFeeSeq);
        }

        if (result > 0) {
            maintenanceFeeVO.setMaintenanceFeeSeq(maintFeeSeq);
            maintenanceFeeVO.setUpdateUser(userSeq);
            // 수정 로직
            result = maintenanceFeeMapper.updateMaintenanceFee(maintenanceFeeVO);

            if (result <= 0) {
                throw new RuntimeException("Failed to update maintenance fee");
            }
        } else {
            maintenanceFeeVO.setCreateUser(userSeq);

            // 등록 로직
            result = maintenanceFeeMapper.insertMaintenanceFee(maintenanceFeeVO);

            if (result <= 0) {
                throw new RuntimeException("Failed to insert maintenance fee");
            }
        }

        // duedate 로직 들어갈 차례.
        List<DueDateVO> dueDates = getDueDateList(maintenanceFeeVO, officeSeq);

        if (!dueDates.isEmpty()) {
            dueDateService.saveAllDueDates(dueDates);
        }

        // AI Vector Sync
        ragService.syncVectorData(officeSeq, "MAINTENANCE_FEE", maintenanceFeeVO.getMaintenanceFeeSeq(),"유지비", this.getMaintenanceFeeDetail(appSeq, maintenanceFeeVO.getMaintenanceFeeSeq()));

    }

    @Override
    public BaseSearchResponse<MaintenanceFeeResponse.MaintenanceFeeList> getMaintFeeListByAppSeq(BaseSearchRequest request) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int totalCnt = maintenanceFeeMapper.getMaintenanceFeeCnt(officeSeq, request.getTblSeq());

        if (totalCnt > 0) {
            List<MaintenanceFeeVO> listVO = maintenanceFeeMapper.getMaintenanceFeeList(officeSeq, request.getTblSeq());

            List<MaintenanceFeeResponse.MaintenanceFeeList> res = listVO.stream()
                    .map(maintFeeVO -> MaintenanceFeeResponse.MaintenanceFeeList.builder()
                            .maintenanceFeeSeq(maintFeeVO.getMaintenanceFeeSeq())
                            .nextPaymentInstallment(maintFeeVO.getNextPaymentInstallment())
                            .maintFeeDeadline(formatMinusHoursString8(maintFeeVO.getMaintFeeDeadline()))
                            .maintFeePenaltyDeadline(formatMinusHoursString8(maintFeeVO.getMaintFeePenaltyDeadline()))
                            .maintFeeOrderDate(formatMinusHoursString8(maintFeeVO.getMaintFeeOrderDate()))
                            .maintFeePaymentDate(formatMinusHoursString8(maintFeeVO.getMaintFeePaymentDate()))
                            .note(maintFeeVO.getNote())
                            .build())
                    .toList();

            return BaseSearchResponse.of(res, totalCnt, request.getPage(), request.getPageSize());
        }

        return new BaseSearchResponse<>();
    }

    @Override
    public MaintenanceFeeResponse.MaintenanceFeeDetail getMaintenanceFeeDetail(String appSeq, String maintenanceFeeSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        MaintenanceFeeVO maintFeeVO = maintenanceFeeMapper.getMaintenanceFee(maintenanceFeeSeq, officeSeq, appSeq);

        return MaintenanceFeeResponse.MaintenanceFeeDetail.builder()
                .appSeq(maintFeeVO.getAppSeq())
                .maintenanceFeeSeq(maintFeeVO.getMaintenanceFeeSeq())
                .nextPaymentInstallment(maintFeeVO.getNextPaymentInstallment())
                .maintFeeDeadline(formatMinusHoursString8(maintFeeVO.getMaintFeeDeadline()))
                .maintFeePenaltyDeadline(formatMinusHoursString8(maintFeeVO.getMaintFeePenaltyDeadline()))
                .maintFeeOrderDate(formatMinusHoursString8(maintFeeVO.getMaintFeeOrderDate()))
                .maintFeePaymentDate(formatMinusHoursString8(maintFeeVO.getMaintFeePaymentDate()))
                .note(maintFeeVO.getNote())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteMaintenanceFee(String appSeq, String maintenanceFeeSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = 0;

        result = maintenanceFeeMapper.softDeleteMaintenanceFee(maintenanceFeeSeq, officeSeq, appSeq, userSeq);

        if (result <= 0) {
            throw new RuntimeException(">>> maintenance fee delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiSoftDeleteMaintenanceFee(String appSeq, List<String> maintenanceFeeSeqList) {
        if (maintenanceFeeSeqList == null || maintenanceFeeSeqList.isEmpty()) {
            throw new RuntimeException("maintenanceFeeSeq is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = maintenanceFeeMapper.softDeleteMaintenanceFeeByList(maintenanceFeeSeqList, officeSeq, appSeq, userSeq);

        if (result != maintenanceFeeSeqList.size()) {
            throw new RuntimeException(">>> maintenance fee list soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteMaintenanceFee(String appSeq, String maintenanceFeeSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = 0;

        result = maintenanceFeeMapper.hardDeleteMaintenanceFee(maintenanceFeeSeq, officeSeq, appSeq);

        if (result <= 0) {
            throw new RuntimeException(">>> maintenance fee hard delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiHardDeleteMaintenanceFee(String appSeq, List<String> maintenanceFeeSeqList) {
        if (maintenanceFeeSeqList == null || maintenanceFeeSeqList.isEmpty()) {
            throw new RuntimeException("maintenanceFeeSeq is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = maintenanceFeeMapper.hardDeleteMaintenanceFeeByList(maintenanceFeeSeqList, officeSeq, appSeq);

        if (result != maintenanceFeeSeqList.size()) {
            throw new RuntimeException(">>> maintenance fee list hard delete failed.");
        }
    }

    public List<DueDateVO> getDueDateList(MaintenanceFeeVO maintFeeVO, String officeSeq) {

        String maintFeeSeq = maintFeeVO.getMaintenanceFeeSeq();

        List<DueDateVO> duedateVOList = new ArrayList<>();

        addDueDateIfPresent(duedateVOList, "maintFeeDeadline", maintFeeVO.getMaintFeeDeadline(), maintFeeSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "maintFeePenaltyDeadline", maintFeeVO.getMaintFeePenaltyDeadline(), maintFeeSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "maintFeeOrderDate", maintFeeVO.getMaintFeeOrderDate(), maintFeeSeq, officeSeq);
        addDueDateIfPresent(duedateVOList, "maintFeePaymentDate", maintFeeVO.getMaintFeePaymentDate(), maintFeeSeq, officeSeq);

        return duedateVOList;
    }

    // =================================================================
    // 기일 추가 (Null Safe & 날짜 파싱 통합)
    // =================================================================
    public void addDueDateIfPresent(List<DueDateVO> list, String code, String dateStr, String tblSeq, String officeSeq) {
        // 날짜 문자열이 존재할 때만 파싱해서 리스트에 추가
        list.add(DueDateVO.builder()
                .duedateCategoryCode(code)
                .duedateDate(parseToOffsetDateTime(dateStr))
                .officeSeq(officeSeq)
                .tblSeq(tblSeq)
                .build());
    }


}
