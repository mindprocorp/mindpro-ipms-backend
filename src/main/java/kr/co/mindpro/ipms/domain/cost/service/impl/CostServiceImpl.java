package kr.co.mindpro.ipms.domain.cost.service.impl;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.cost.dto.request.AnnuityYearRequest;
import kr.co.mindpro.ipms.domain.cost.dto.request.CostSaveRequest;
import kr.co.mindpro.ipms.domain.cost.dto.response.AnnuityYearResponse;
import kr.co.mindpro.ipms.domain.cost.dto.response.CostDetailResponse;
import kr.co.mindpro.ipms.domain.cost.repository.db1.CostMapper;
import kr.co.mindpro.ipms.domain.cost.service.CostService;
import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.mindpro.ipms.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.formatMinusHoursString8;
import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

/**
 * 청구서 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : InvoiceServiceImpl.java
 * @since    : 2026. 01. 07.
 */
/**
 * 비용 비즈니스 로직 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {

    private final CostMapper costMapper;
    private final DueDateService dueDateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllCosts(String tblSeq, List<CostVO> costList) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();

        if (costList == null) costList = new java.util.ArrayList<>();

        log.info(">>> [COST SAVE] Start saving costs for tblSeq: {}, total items: {}", tblSeq, costList.size());

        // 1. 기존 매핑 정보 및 현재 본체(Active Mst) 조회
        List<CostVO> existingCosts = costMapper.findAllByWork(tblSeq, officeSeq);

        // 2. 카테고리별 매핑 Map 구성 (대소문자 무시 및 공백 제거)
        java.util.Map<String, CostVO> existingMap = existingCosts.stream()
                .filter(vo -> vo.getCostCategoryCode() != null)
                .collect(java.util.stream.Collectors.toMap(
                        vo -> vo.getCostCategoryCode().trim().toUpperCase(), 
                        vo -> vo, 
                        (a, b) -> a));

        java.util.Set<String> processedCategories = new java.util.HashSet<>();

        // 3. 신규 리스트 순회
        for (CostVO newVo : costList) {
            if (newVo.getCostCategoryCode() == null) continue;
            
            String rawCategory = newVo.getCostCategoryCode().trim();
            String upperCategory = rawCategory.toUpperCase();
            processedCategories.add(upperCategory);

            // 공통 정보 세팅
            newVo.setTblSeq(tblSeq);
            newVo.setOfficeSeq(officeSeq);
            newVo.setCreateUser(userId);
            newVo.setUpdateUser(userId);
            newVo.setDelYn("N");

            if (existingMap.containsKey(upperCategory)) {
                // [기존 매핑 존재] -> 변경 여부 체크 및 버전업
                CostVO oldVo = existingMap.get(upperCategory);
                
                boolean isChanged = !org.springframework.util.ObjectUtils.nullSafeEquals(oldVo.getKrwAmount(), newVo.getKrwAmount())
                                 || !org.springframework.util.ObjectUtils.nullSafeEquals(oldVo.getNote(), newVo.getNote());

                if (isChanged) {
                    log.info(">>> [COST UPDATE] Category: {}, New Amount: {}", rawCategory, newVo.getKrwAmount());
                    // 1) 기존 Mst Soft Delete
                    costMapper.softDeleteCost(officeSeq, oldVo.getCostSeq(), userId);
                    
                    // 2) 신규 Mst Insert (기존 mapping_cost_seq 재사용, <selectKey>로 costSeq 자동 채번)
                    newVo.setMappingCostSeq(oldVo.getMappingCostSeq());
                    costMapper.insertCostMst(newVo);
                }
            } else {
                // [신규 항목] mapp 먼저 → mst 나중
                // mapp.cost_seq NOT NULL 이므로 mst용 costSeq를 먼저 채번하여 mapp에도 기록
                log.info(">>> [COST NEW] Category: {}, Amount: {}", rawCategory, newVo.getKrwAmount());
                newVo.setCostSeq(costMapper.getNextCostSeq());
                costMapper.insertCostMapp(newVo); // <selectKey>: mappingCostSeq 자동 채번, cost_seq = 위에서 채번
                costMapper.insertCostMst(newVo);  // <selectKey>: costSeq 자동 채번
            }
        }

        // 4. 삭제 처리 (기존에는 있었으나 신규 리스트에 없는 항목)
        for (CostVO oldVo : existingCosts) {
            String upperDbCategory = oldVo.getCostCategoryCode().trim().toUpperCase();
            if (!processedCategories.contains(upperDbCategory)) {
                log.info(">>> [COST DELETE] Category: {}, costSeq: {}", oldVo.getCostCategoryCode(), oldVo.getCostSeq());
                costMapper.softDeleteCost(officeSeq, oldVo.getCostSeq(), userId);
                costMapper.softDeleteCostMapp(officeSeq, tblSeq, oldVo.getMappingCostSeq(), userId);
            }
        }
    }

    /**
     * [저장] 필요 시 개별 호출을 위한 단건 등록 메서드
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCost(CostVO vo) {

        int result = 0;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        vo.setOfficeSeq(officeSeq);
        vo.setCostCategoryCode("annuityYear"); // 연차 관리
        vo.setCreateUser(userSeq);
        vo.setUpdateUser(userSeq);
        vo.setDelYn("N");

        result = costMapper.insertCostMst(vo);

        if (result <= 0) {
            log.error(">>> [ERROR] 비용 마스터 정보 저장 실패! (Return: 0) : saveCost");
            throw new RuntimeException("비용 마스터 정보 저장에 실패했습니다.");
        }

        log.info(">>> [INFO] 비용 마스터 정보 등록에 성공하였습니다. - costSeq: {}", vo.getCostSeq());

        result = costMapper.insertCostMapp(vo);

        if (result <= 0) {
            log.error(">>> [ERROR] 비용 멥퍼 테이블 정보 저장 실패! (Return: 0) : saveCost");
            throw new RuntimeException("비용 맵퍼 테이블 정보 저장에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAnnuityYear(AnnuityYearRequest.AnnuityYearTabRequest request) {
        int result;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq   = SecurityUtil.getUserInfoSeq();
        String costSeq   = request.costSeq();

        CostVO costVO = new CostVO();
        costVO.setOfficeSeq(officeSeq);
        costVO.setTblSeq(request.tblSeq());
        costVO.setCostSeq(costSeq);
        costVO.setCostCategoryCode("annuityYear");
        costVO.setRemittanceCount(request.remittanceCount());
        costVO.setCostFee(request.costFee());
        costVO.setDiscountRatio(request.discountRatio());
        costVO.setNote(request.note());

        // costSeq DB 존재 여부로 UPDATE/INSERT 분기 (saveAllCosts 방식과 동일)
        result = costMapper.getDuplicateAnnuityYearCnt(costVO);

        if (result > 0) {
            // ── [수정] costSeq가 DB에 존재 → mst 업데이트 ──────────────────────────
            costVO.setUpdateUser(userSeq);
            result = costMapper.updateCostMst(costVO);
            if (result <= 0) {
                log.error(">>> [ERROR] 비용 마스터 정보 수정 실패! : saveAnnuityYear");
                throw new RuntimeException("비용 마스터 정보 수정에 실패했습니다.");
            }
            log.info(">>> [INFO] 비용 마스터 수정 성공 - costSeq: {}", costVO.getCostSeq());

        } else {
            // ── [신규] mapp 공유 여부 확인 후 INSERT (saveAllCosts 신규 항목 분기와 동일) ──
            costVO.setCreateUser(userSeq);
            costVO.setDelYn("N");

            // 같은 (tblSeq, 'annuityYear') mapp이 이미 있는지 확인
            String existingMappingCostSeq = costMapper.findMappingCostSeqByTblSeqAndCategory(
                    officeSeq, request.tblSeq(), "annuityYear");

            if (org.springframework.util.StringUtils.hasText(existingMappingCostSeq)) {
                // 기존 mapp 재사용 → mst만 신규 등록 (<selectKey>로 costSeq 자동 채번)
                costVO.setMappingCostSeq(existingMappingCostSeq);
                log.info(">>> [INFO] 기존 mapp 재사용 - mappingCostSeq: {}", existingMappingCostSeq);
            } else {
                // 최초 등록 → mapp 먼저 생성 후 mst 등록
                // mapp.cost_seq NOT NULL 이므로 mst용 costSeq를 먼저 채번하여 mapp에도 기록
                costVO.setCostSeq(costMapper.getNextCostSeq());
                result = costMapper.insertCostMapp(costVO); // <selectKey>: mappingCostSeq 자동 채번
                if (result <= 0) {
                    log.error(">>> [ERROR] 비용 맵퍼 정보 저장 실패! : saveAnnuityYear");
                    throw new RuntimeException("비용 맵퍼 정보 저장에 실패했습니다.");
                }
                log.info(">>> [INFO] 신규 mapp 등록 - mappingCostSeq: {}", costVO.getMappingCostSeq());
            }

            // mst 등록 (<selectKey>: costSeq 자동 채번, mappingCostSeq는 위에서 세팅 완료)
            result = costMapper.insertCostMst(costVO); // <selectKey>: costSeq 자동 채번
            if (result <= 0) {
                log.error(">>> [ERROR] 비용 마스터 정보 저장 실패! : saveAnnuityYear");
                throw new RuntimeException("비용 마스터 정보 저장에 실패했습니다.");
            }
            log.info(">>> [INFO] 비용 마스터 등록 성공 - costSeq: {}", costVO.getCostSeq());
        }

        // 기일 정보 저장
        List<DueDateVO> dueDateVOList = new ArrayList<>();
        dueDateVOList.add(DueDateVO.builder()
                .duedateCategoryCode("costRemittanceDate")
                .duedateDate(parseToOffsetDateTime(request.costRemittanceDate()))
                .officeSeq(officeSeq)
                .tblSeq(costVO.getCostSeq())
                .build());
        dueDateService.saveAllDueDates(dueDateVOList);
    }

    /**
     * [저장] 상표 - 갱신관리 정보 등록 메서드
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRenewalMng(CostSaveRequest.TrademarkRenewalRequest request) {
        int result;

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq   = SecurityUtil.getUserInfoSeq();
        String costSeq   = request.costSeq();

        CostVO costVO = CostVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(request.appSeq())
                .costSeq(costSeq)
                .costCategoryCode("renewalMng")
                .paymentDiv(request.paymentDiv())
                .appNo(request.appNo())
                .krwAmount(request.krwAmount())
                .remittanceCount(request.remittanceCount())
                .note(request.note())
                .build();

        // costSeq DB 존재 여부로 UPDATE/INSERT 분기 (saveAllCosts 방식과 동일)
        result = costMapper.getDuplicateRenewalMngCnt(costVO);

        if (result > 0) {
            // ── [수정] costSeq가 DB에 존재 → mst 업데이트 ──────────────────────────
            costVO.setUpdateUser(userSeq);
            result = costMapper.updateCostMst(costVO);
            if (result <= 0) {
                log.error(">>> [ERROR] 비용 마스터 정보 수정 실패! : saveRenewalMng");
                throw new RuntimeException("비용 마스터 정보 수정에 실패했습니다.");
            }
            log.info(">>> [INFO] 비용 마스터 수정 성공 - costSeq: {}", costVO.getCostSeq());

        } else {
            // ── [신규] mapp 공유 여부 확인 후 INSERT (saveAllCosts 신규 항목 분기와 동일) ──
            costVO.setCreateUser(userSeq);
            costVO.setDelYn("N");

            // 같은 (tblSeq, 'renewalMng') mapp이 이미 있는지 확인
            String existingMappingCostSeq = costMapper.findMappingCostSeqByTblSeqAndCategory(
                    officeSeq, request.appSeq(), "renewalMng");

            if (org.springframework.util.StringUtils.hasText(existingMappingCostSeq)) {
                // 기존 mapp 재사용 → mst만 신규 등록 (<selectKey>로 costSeq 자동 채번)
                costVO.setMappingCostSeq(existingMappingCostSeq);
                log.info(">>> [INFO] 기존 mapp 재사용 - mappingCostSeq: {}", existingMappingCostSeq);
            } else {
                // 최초 등록 → mapp 먼저 생성 후 mst 등록
                // mapp.cost_seq NOT NULL 이므로 mst용 costSeq를 먼저 채번하여 mapp에도 기록
                costVO.setCostSeq(costMapper.getNextCostSeq());
                result = costMapper.insertCostMapp(costVO); // <selectKey>: mappingCostSeq 자동 채번
                if (result <= 0) {
                    log.error(">>> [ERROR] 비용 맵퍼 정보 저장 실패! : saveRenewalMng");
                    throw new RuntimeException("비용 맵퍼 정보 저장에 실패했습니다.");
                }
                log.info(">>> [INFO] 신규 mapp 등록 - mappingCostSeq: {}", costVO.getMappingCostSeq());
            }

            // mst 등록 (<selectKey>: costSeq 자동 채번, mappingCostSeq는 위에서 세팅 완료)
            result = costMapper.insertCostMst(costVO); // <selectKey>: costSeq 자동 채번
            if (result <= 0) {
                log.error(">>> [ERROR] 비용 마스터 정보 저장 실패! : saveRenewalMng");
                throw new RuntimeException("비용 마스터 정보 저장에 실패했습니다.");
            }
            log.info(">>> [INFO] 비용 마스터 등록 성공 - costSeq: {}", costVO.getCostSeq());
        }

        // 기일 정보 저장
        List<DueDateVO> dueDateVOList = new ArrayList<>();
        dueDateVOList.add(DueDateVO.builder()
                .duedateCategoryCode("costRemittanceDate")
                .duedateDate(parseToOffsetDateTime(request.costRemittanceDate()))
                .officeSeq(officeSeq)
                .tblSeq(costVO.getCostSeq())
                .build());
        dueDateVOList.add(DueDateVO.builder()
                .duedateCategoryCode("requestDate")
                .duedateDate(parseToOffsetDateTime(request.requestDate()))
                .officeSeq(officeSeq)
                .tblSeq(costVO.getCostSeq())
                .build());
        dueDateService.saveAllDueDates(dueDateVOList);
    }

    /**
     * [조회] 특정 업무키(tblSeq)로 매핑된 비용 리스트 반환
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<CostVO> getCostListByWork(String tblSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 인터페이스 명칭 변경에 맞춰 findAllByWork 호출
        List<CostVO> listVO = costMapper.findAllByWork(tblSeq, officeSeq);

        return BaseSearchResponse.of(listVO, 1, 99);
    }

    @Override
    public AnnuityYearResponse.AnnuityYearDetailResponse getAnnuityYearDetail(String tblSeq, String costSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        CostVO costVO = costMapper.getAnnuityYearDetail(tblSeq, officeSeq, costSeq);

        if (costVO != null) {
            AnnuityYearResponse.AnnuityYearDetailResponse res = AnnuityYearResponse.AnnuityYearDetailResponse.builder()
                    .tblSeq(costVO.getTblSeq())
                    .costSeq(costVO.getCostSeq())
                    .remittanceCount(java.util.Optional.ofNullable(costVO.getRemittanceCount()).orElse(0))
                    .costRemittanceDate(costVO.getCostRemittanceDate() != null ? formatMinusHoursString8(String.valueOf(costVO.getCostRemittanceDate())) : "")
                    .costFee(java.util.Optional.ofNullable(costVO.getCostFee()).orElse(0))
                    .discountRatio(java.util.Optional.ofNullable(costVO.getDiscountRatio()).orElse(0))
                    .note(costVO.getNote())
                    .createAt(formatMinusHoursString8(costVO.getCreateAt()))
                    .createUser(costVO.getCreateUser())
                    .updateAt(formatMinusHoursString8(costVO.getUpdateAt()))
                    .updateUser(costVO.getUpdateUser())
                    .delYn(costVO.getDelYn())
                    .build();

            return res;
        } else {
            throw new BusinessException("존재하지 않는 데이터입니다.", INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * [조회] 특정 검색 조건에 관한 비용 리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<CostVO> getCostList(CostVO searchVO) {
        return costMapper.findCostList(searchVO);
    }

    /**
     * [조회] 출원 연차관리 탭 리스트 조회
     * */
    @Override
    public BaseSearchResponse<AnnuityYearResponse.AnnuityYearDetailResponse> getAnnuityYearListByWork(String tblSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 인터페이스 명칭 변경에 맞춰 findAllByWork 호출
        List<CostVO> listVO = costMapper.findAllByWork(tblSeq, officeSeq);

        List<AnnuityYearResponse.AnnuityYearDetailResponse> resVOList = listVO.stream()
                .map(vo -> AnnuityYearResponse.AnnuityYearDetailResponse.builder()
                        .tblSeq(vo.getTblSeq())
                        .costSeq(vo.getCostSeq())
                        .remittanceCount(java.util.Optional.ofNullable(vo.getRemittanceCount()).orElse(0))
                        .costRemittanceDate(vo.getCostRemittanceDate() != null ? formatMinusHoursString8(String.valueOf(vo.getCostRemittanceDate())) : "")
                        .costFee(java.util.Optional.ofNullable(vo.getCostFee()).orElse(0))
                        .discountRatio(java.util.Optional.ofNullable(vo.getDiscountRatio()).orElse(0))
                        .note(vo.getNote())
                        .createAt(formatMinusHoursString8(vo.getCreateAt()))
                        .createUser(vo.getCreateUser())
                        .updateAt(formatMinusHoursString8(vo.getUpdateAt()))
                        .updateUser(vo.getUpdateUser())
                        .delYn(vo.getDelYn())
                        .build())
                .toList();

        return BaseSearchResponse.of(resVOList, 1, 99);
    }

    @Override
    public BaseSearchResponse<CostDetailResponse.TrademarkRenewalResponse> getRenewalMngList(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<CostVO> listVO = costMapper.findAllByWork(appSeq, officeSeq);

        List<CostDetailResponse.TrademarkRenewalResponse> resVOList = listVO.stream()
                .map(vo -> CostDetailResponse.TrademarkRenewalResponse.builder()
                        .appSeq(vo.getTblSeq())
                        .costSeq(vo.getCostSeq())
                        .remittanceCount(java.util.Optional.ofNullable(vo.getRemittanceCount()).orElse(0))
                        .paymentDiv(vo.getPaymentDiv())
                        .requestDate(vo.getCostDate() != null ? formatMinusHoursString8(vo.getCostDate()) : "")
                        .appNo(vo.getAppNo())
                        .costRemittanceDate(vo.getCostRemittanceDate() != null ? formatMinusHoursString8(vo.getCostRemittanceDate()) : "")
                        .krwAmount(java.util.Optional.ofNullable(vo.getKrwAmount()).orElse(0L))
                        .note(vo.getNote())
                        .build()
                )
                .toList();

        return BaseSearchResponse.of(resVOList, 1, 99);
    }

    @Override
    public CostDetailResponse.TrademarkRenewalResponse getRenewalMngDetail(String tblSeq, String costSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        CostVO costVO = costMapper.getRenewalMngDetail(tblSeq, officeSeq, costSeq);

        if (costVO != null) {
            CostDetailResponse.TrademarkRenewalResponse res = CostDetailResponse.TrademarkRenewalResponse.builder()
                    .appSeq(costVO.getTblSeq())
                    .costSeq(costVO.getCostSeq())
                    .remittanceCount(costVO.getRemittanceCount())
                    .paymentDiv(costVO.getPaymentDiv())
                    .requestDate(costVO.getCostDate() != null ? formatMinusHoursString8(costVO.getCostDate()) : "")
                    .appNo(costVO.getAppNo())
                    .costRemittanceDate(costVO.getCostRemittanceDate() != null ? formatMinusHoursString8(costVO.getCostRemittanceDate()) : "")
                    .krwAmount(costVO.getKrwAmount())
                    .note(costVO.getNote())
                    .build();


            return res;
        } else {
            throw  new BusinessException("존재하지 않는 데이터입니다.", INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteCostWithCostMapp(String tblSeq, String costSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq   = SecurityUtil.getUserInfoSeq();

        // mapp은 같은 (tblSeq, category) 아래 여러 mst가 공유하므로 mst만 삭제
        int result = costMapper.softDeleteCost(officeSeq, costSeq, userSeq);
        if (result <= 0) {
            throw new RuntimeException("Cost deletion failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteCostWithCostMappByList(String tblSeq, List<String> targetSeqList) {
        if (targetSeqList == null || targetSeqList.isEmpty()) {
            throw new RuntimeException("Target sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq   = SecurityUtil.getUserInfoSeq();

        // mapp은 공유 리소스이므로 mst 목록만 삭제
        int result = costMapper.softDeleteCostByList(officeSeq, userSeq, targetSeqList);
        if (result <= 0) {
            throw new RuntimeException("Cost list deletion failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteCostWithCostMapp(String tblSeq, String costSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result;

        result = costMapper.hardDeleteCost(officeSeq, tblSeq, costSeq);

        if (result <= 0) {
            throw new RuntimeException("Cost hard deletion failed");
        }

        result = costMapper.hardDeleteCostMapp(officeSeq, tblSeq, costSeq);

        if (result <= 0) {
            throw new RuntimeException("Cost Mapp hard deletion failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteCostWithCostMappByList(String tblSeq, List<String> targetSeqList) {
        if (targetSeqList == null || targetSeqList.isEmpty()) {
            throw new RuntimeException("Target sequence list is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result;

        result = costMapper.hardDeleteCostByList(officeSeq, targetSeqList);

        if (result <= 0) {
            throw new RuntimeException("Cost list hard deletion failed");
        }

        result = costMapper.hardDeleteCostMappByList(officeSeq, tblSeq, targetSeqList);

        if (result != targetSeqList.size()) {
            throw new RuntimeException("Cost list Mapp hard deletion failed");
        }
    }
}