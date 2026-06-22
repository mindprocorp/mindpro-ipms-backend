package kr.co.mindpro.ipms.domain.duedate.service.impl;


import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.code.dto.request.CodeRequest;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeDtlResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeMstResponse;
import kr.co.mindpro.ipms.domain.code.dto.response.CodeResponse;
import kr.co.mindpro.ipms.domain.code.repository.db1.CodeMapper;
import kr.co.mindpro.ipms.domain.code.service.CodeService;
import kr.co.mindpro.ipms.domain.code.vo.CodeMstVO;
import kr.co.mindpro.ipms.domain.conflict.vo.DateSearchVO;
import kr.co.mindpro.ipms.domain.conflict.vo.ParticipantSearchVO;
import kr.co.mindpro.ipms.domain.duedate.dto.request.DueDateSaveRequest;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateDetailResponse;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateResponse;
import kr.co.mindpro.ipms.domain.duedate.repository.db1.DueDateMapper;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateManageVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateMapVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.duedate.vo.ProgressHistoryVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 기일 비즈니스 로직 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DueDateServiceImpl implements DueDateService {


    private final DueDateMapper dueDateMapper;
    private final CodeService  codeService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllDueDates(List<DueDateVO> list) {
        if (list == null || list.isEmpty()) return;

        // 1. 기준 정보 추출
        DueDateVO firstVo = list.get(0);
        String tblSeq = firstVo.getTblSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();



        DueDateMapVO mapVO = DueDateMapVO.builder()
                .tblCode(firstVo.getTblSeq().substring(0, 6))
                .officeSeq(officeSeq)
                .tblSeq(firstVo.getTblSeq())
                .createUser(userId)
                .delYn("N")
                .build();

        // 기존 데이터가 있으면 duedateSeq 값을 가져옴.
        String duedateSeq = dueDateMapper.getDuedateSeqByWork(firstVo.getTblSeq(), officeSeq);

        // duedateSeq 값이 비어있으면 새로 기일데이터를 인서트하는 로직.
        if (duedateSeq == null || duedateSeq.isEmpty()) {
            dueDateMapper.insertDueDateMapp(mapVO);
        } else {        // duedateSeq 값이 존재하면 기존 데이터를 softDelete하고 새로 인서트하는 로직.
            log.info(">>> duedateMapp 테이블에 중복된 데이터가 존재합니다. softDeleteAndInsertDueDate 실행");

            for (DueDateVO vo : list) {
                vo.setTblSeq(tblSeq);
                vo.setOfficeSeq(officeSeq);
                vo.setCreateUser(userId);

                int result = dueDateMapper.softDeleteAndInsertDueDate(vo);

                // 기존 데이터가 없는 경우 (새로운 기일 정보 입력 시) 새로운 기일 인서트.
                if (result <= 0) {
                    log.info(">>> softDeleteAndInsertDueDate {} : duedateMst 테이블에 인서트가 되지 않았습니다.", vo.getDuedateCategoryCode());

                    vo.setDuedateSeq(duedateSeq);
                    vo.setDelYn("N");

                    dueDateMapper.insertDueDate(vo);
                }
            }

            return;
        }

// 생성된 mapSeq 가져오기
        //String duedateSeq = mapVO.getDuedateSeq();

        for (DueDateVO vo : list) {
            vo.setDuedateSeq(mapVO.getDuedateSeq());
            vo.setTblSeq(tblSeq);
            vo.setOfficeSeq(officeSeq);
            vo.setDelYn("N");
            vo.setCreateUser(userId);

            dueDateMapper.insertDueDate(vo);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<DueDateVO> getDueDateListByWork(String tblSeq, String officeSeq) {
        // 1. 매퍼에서 가져온 리스트를 그대로 반환합니다.
        return dueDateMapper.findAllByWork(tblSeq, officeSeq);
    }

        @Override
        @Transactional(readOnly = true)
        public List<DueDateVO> getUpcomingDueDates(String officeSeq) {
            // 날짜 세팅 없이 officeSeq만 담아서 전달
            DueDateVO searchVO = DueDateVO.builder()
                    .officeSeq(officeSeq)
                    .build();

            return dueDateMapper.findUpcomingDueDates(officeSeq);
        }


    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<DueDateResponse.DueDateDetail> getDueDateList(BaseSearchRequest request) {
        // 1. 사무소 코드 설정
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        if (request.getSearchCondition() == null) {
            request.setSearchCondition(new java.util.ArrayList<>());
        }
        if (request.getDateFilters() == null) {
            request.setDateFilters(new java.util.ArrayList<>());
        }
        // --- [A] 마감일 종류(DUE_CATE) 상세 코드 주입 ---
        boolean hasDueType =
                request.getSearchCondition().stream().anyMatch(c -> "dueTypeCode".equals(c.get("codeName")));

        if (!hasDueType) {
            if (request.getSearchCondition() == null) request.setSearchCondition(new java.util.ArrayList<>());
            List<CodeDtlResponse> dtlCodes = codeService.getCodeDtlList("DUE_TYPE");

            for (CodeDtlResponse vo : dtlCodes) {
                java.util.Map<String, String> cond = new java.util.HashMap<>();
                cond.put("codeName", "dueTypeCode");
                cond.put("codeValue", vo.dtlCd()); // Record의 dtlCd() 호출
                cond.put("andOrNOT", "OR");
                request.getSearchCondition().add(cond);
            }
        }

        // --- [B] 날짜 필터 처리 (프리셋 -> 실제 YYYYMMDD 변환) ---
        if (request.getDateFilters() == null || request.getDateFilters().isEmpty()) {
            if (request.getDateFilters() == null) request.setDateFilters(new java.util.ArrayList<>());
            request.getDateFilters().add(new java.util.HashMap<>(java.util.Map.of(
                    "dateCode", "month3", "andOrNOT", "AND"
            )));
        }

        LocalDate now = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (java.util.Map<String, String> filter : request.getDateFilters()) {
            String code = filter.get("dateCode");
            if (code == null) continue;
            switch (code) {
                case "week2":   filter.putAll(java.util.Map.of("startDate", now.format(fmt), "endDate", now.plusWeeks(2).format(fmt), "dateCode", "deadline")); break;
                case "month1":  filter.putAll(java.util.Map.of("startDate", now.format(fmt), "endDate", now.plusMonths(1).format(fmt), "dateCode", "deadline")); break;
                case "month3":  filter.putAll(java.util.Map.of("startDate", now.format(fmt), "endDate", now.plusMonths(3).format(fmt), "dateCode", "deadline")); break;
                case "month6":  filter.putAll(java.util.Map.of("startDate", now.format(fmt), "endDate", now.plusMonths(6).format(fmt), "dateCode", "deadline")); break;
                case "year1":   filter.putAll(java.util.Map.of("startDate", now.format(fmt), "endDate", now.plusYears(1).format(fmt), "dateCode", "deadline")); break;
                case "overdue": filter.putAll(java.util.Map.of("startDate", "19000101", "endDate", now.minusDays(1).format(fmt), "dateCode", "deadline")); break;
                default:        filter.put("dateCode", "deadline"); break;
            }
        }

        // 2. Mapper 실행
        List<DueDateManageVO> list = dueDateMapper.findDueDateList(request);
        int totalCount = dueDateMapper.findDueDateListCount(request);

        // 3. VO -> DTO (DueDateDetail) 변환
        List<DueDateResponse.DueDateDetail> dtoList = list.stream()
                .map(vo -> DueDateResponse.DueDateDetail.builder()
                        .caseCategory(CommonRecordResponse.CodeInfo.builder().code(vo.getCaseCategoryCode()).codeName(vo.getCaseCategoryName()).build())
                        .dueDate(CommonRecordResponse.CodeInfo.builder().code(vo.getDueDateCode()).codeName(vo.getDueDateName()).build())
                        .country(CommonRecordResponse.CodeInfo.builder().code(vo.getCountryCode()).codeName(vo.getCountryName()).build())
                        .rightType(CommonRecordResponse.CodeInfo.builder().code(vo.getRightTypeCode()).codeName(vo.getRightTypeName()).build())
                        .titleKo(vo.getTitleKo()).titleEn(vo.getTitleEn())
                        .appRoute(CommonRecordResponse.CodeInfo.builder().code(vo.getAppRouteCode()).codeName(vo.getAppRouteName()).build())
                        .deadline(vo.getDeadline())
                        .dueType(CommonRecordResponse.CodeInfo.builder().code(vo.getDueTypeCode()).codeName(vo.getDueTypeName()).build())
                        .appSeq(vo.getAppSeq()).appNo(vo.getAppNo()).appDate(vo.getAppDate())
                        .regNo(vo.getRegNo()).regDate(vo.getRegDate())
                        .ourRef(vo.getOurRef()).yourRef(vo.getYourRef()).applicantRefNo(vo.getApplicantRefNo())
                        .applicant(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getApplicantSeq()).userName(vo.getApplicantName()).build())
                        .client(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getClientSeq()).userName(vo.getClientName()).build())
                        .inventor(vo.getInventorName()).deptName(vo.getDeptName())
                        .adminMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAdminMgrSeq()).userName(vo.getAdminMgrName()).build())
                        .caseMgr(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getCaseMgrSeq()).userName(vo.getCaseMgrName()).build())
                        .attorney(CommonRecordResponse.PersonInfo.builder().userSeq(vo.getAttorneySeq()).userName(vo.getAttorneyName()).build())
                        .duedateSeq(vo.getDuedateSeq())
                        .tblSeq(vo.getTblSeq())
                        .duedateCompleteYn(vo.getDuedateCompleteYn())
                        .build())
                .toList();

        return BaseSearchResponse.of(dtoList, totalCount, request.getPage(), request.getPageSize());
    }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void registerDueDate(DueDateVO vo) {
            String userId = (vo.getCreateUser() != null && !vo.getCreateUser().isEmpty())
                    ? vo.getCreateUser()
                    : SecurityUtil.getUserInfoSeq();

            // 기존 매핑 확인 (중복 key 방지)
            String existingDuedateSeq = dueDateMapper.getDuedateSeqByWork(vo.getTblSeq(), vo.getOfficeSeq());

            if (existingDuedateSeq != null && !existingDuedateSeq.isEmpty()) {
                // 기존 매핑이 있으면 soft delete 후 재등록
                vo.setDuedateSeq(existingDuedateSeq);
                vo.setCreateUser(userId);
                vo.setDelYn("N");
                int result = dueDateMapper.softDeleteAndInsertDueDate(vo);
                if (result <= 0) {
                    dueDateMapper.insertDueDate(vo);
                }
            } else {
                DueDateMapVO mapVO = DueDateMapVO.builder()
                        .tblCode(vo.getTblSeq().substring(0, 6))
                        .officeSeq(vo.getOfficeSeq())
                        .tblSeq(vo.getTblSeq())
                        .delYn("N")
                        .createUser(userId)
                        .build();

                dueDateMapper.insertDueDateMapp(mapVO);
                vo.setDuedateSeq(mapVO.getDuedateSeq());
                vo.setDelYn("N");
                vo.setCreateUser(userId);
                dueDateMapper.insertDueDate(vo);
            }
        }

    @Override
    @Transactional(readOnly = true) // 단순 조회이므로 readOnly 추천
    public List<String> getIdsByDateFilters(String officeSeq, String targetJob, List<SearchParamVO> dateFilters) {

        if (ObjectUtils.isEmpty(dateFilters)) {
            return Collections.emptyList();
        }

        // Mapper 호출 (officeSeq와 필터 리스트 전달)
        return dueDateMapper.findTblSeqsByDateFilters(officeSeq, targetJob, dateFilters);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDueDateMappByTblSeq(String tblSeq, String officeSeq) {
        dueDateMapper.deleteDueDateMappByTblSeq(tblSeq, officeSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCompleteYn(String duedateSeq, String completeYn, String dueTypeCategoryCode) {
        String userId = SecurityUtil.getUserInfoSeq();
        dueDateMapper.updateCompleteYn(duedateSeq, completeYn, userId, dueTypeCategoryCode);
    }

    /// 접발송내역

    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<DueDateResponse.ProgressHistoryDetail> getProgressHistoryList(BaseSearchRequest request) {
        // 1. DB 목록 조회 (VO 리스트)
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());;
        List<ProgressHistoryVO> dbList = dueDateMapper.selectProgressHistoryList(request);
        int totalCount = 0;
        // 2. 총 개수 조회
         totalCount = dueDateMapper.selectProgressHistoryCount(request);

        // 3. VO -> DTO 변환 (전부 String 매핑)
        List<DueDateResponse.ProgressHistoryDetail> dtoList = dbList.stream()
                .map(vo -> DueDateResponse.ProgressHistoryDetail.builder()
                        .appSeq(vo.getAppSeq())
                        .conflictSeq(vo.getConflictSeq())
                        .caseCategoryName(vo.getCaseCategoryName())
                        .progressTypeName(vo.getProgressTypeName())
                        .rightType(CommonRecordResponse.CodeInfo.builder()
                                .code(vo.getRightTypeCode())
                                .codeName(vo.getRightTypeName())
                                .build())
                        .ourRef(vo.getOurRef())
                        .appDate(vo.getAppDate())
                        .appNo(vo.getAppNo())
                        .regDate(vo.getRegDate())
                        .regNo(vo.getRegNo())
                        .productClass(vo.getProductClass())
                        .eventDate(vo.getEventDate())
                        .content(vo.getContent())
                        .applicantName(vo.getApplicantName())
                        .titleKo(vo.getTitleKo())
                        .build()
                ).collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, dtoList.size() , request.getPage(), request.getPageSize());
    }

}

