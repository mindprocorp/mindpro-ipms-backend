package kr.co.mindpro.ipms.domain.locarno.service.impl;

import java.util.List;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.locarno.dto.request.LocarnoRequest;
import kr.co.mindpro.ipms.domain.locarno.dto.response.LocarnoResponse;
import kr.co.mindpro.ipms.domain.locarno.vo.AppLocarnoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.domain.locarno.repository.db1.LocarnoMapper;
import kr.co.mindpro.ipms.domain.locarno.service.LocarnoService;
import kr.co.mindpro.ipms.domain.locarno.vo.LocarnoVO;
import lombok.RequiredArgsConstructor;

import static kr.co.mindpro.ipms.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static kr.co.mindpro.ipms.common.util.IdGenerator.generateTSID;

/**
 * [Service Implementation] 로카르노 관리 서비스 구현체
 *
 * @author	 : intst
 * @fileName	 : LocarnoServiceImpl.java
 * @since	 : 2026. 2. 4.
 */
@Service
@RequiredArgsConstructor
public class LocarnoServiceImpl implements LocarnoService {
	private final LocarnoMapper locarnoMapper;
	
    /**
     * 로카르노 목록 조회
     * 요청받은 LocarnoDetail의 검색 조건을 Mapper로 전달하여 목록을 반환합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocarnoVO> getLocarnoList() {
        // MyBatis Mapper를 호출하여 DB 데이터 조회
        return locarnoMapper.selectLocarnoList(null);
    }
    
    /**
     * 로카르노 목록 버전별 조회 (추가)
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocarnoVO> getLocarnoListByVersion(String locarnoVersion) {
        // 특정 버전을 파라미터로 전달하여 MyBatis Mapper 호출
        return locarnoMapper.selectLocarnoList(locarnoVersion);
    }
    
    /**
     * 로카르노 소분류 목록 조회
     * 특정 물품류(classNo)에 속한 소분류 목록을 조회합니다. (버전 선택 가능)
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocarnoVO> getLocarnoSubclassList(String classNo, String locarnoVersion) {
        // Mapper의 신규 메소드 호출
        return locarnoMapper.selectLocarnoSubclassList(classNo, locarnoVersion);
    }
    
    /**
     * 로카르노 물품 목록 조회
     * 특정 물품류(classNo) 및 소분류(subclassNo)에 속한 물품 목록을 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocarnoVO> getLocarnoGoodsList(String classNo, String subclassNo, String locarnoVersion) {
        // Mapper의 물품 조회 메소드 호출
        return locarnoMapper.selectLocarnoGoodsList(classNo, subclassNo, locarnoVersion);
    }    

	@Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllLocarno(LocarnoRequest.SaveAllLocarno request) {

        if (request == null || request.locarnoList() == null || request.locarnoList().isEmpty()) {
            throw new BusinessException("locarnoList cannot be null or empty", INTERNAL_SERVER_ERROR);
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String appSeq = request.locarnoList().get(0).appSeq();

        String locarnoGroupId = request.locarnoGroupId();

        if (locarnoGroupId != null && !locarnoGroupId.isEmpty()) {
            int existCount = locarnoMapper.getDuplicateLocarnoCnt(officeSeq, appSeq, locarnoGroupId);

            if (existCount > 0) {
                // 기존 데이터 소프트 삭제
                int deleteResult = locarnoMapper.softDeleteLocarnoGroup(officeSeq, appSeq, locarnoGroupId, userSeq);

                if (deleteResult <= 0) {
                    throw new RuntimeException("locarno group soft delete failed.");
                }
            } else {
                throw new BusinessException("존재하지 않거나 이미 삭제된 로카르노 그룹입니다.", ErrorCode.INVALID_INPUT_VALUE);
            }
        } else {
            // 그룹 아이디가 없으면 신규 채번
            locarnoGroupId = generateTSID("LOCARNO");
        }

        for (LocarnoRequest.SaveLocarno locarnoReq : request.locarnoList()) {
            AppLocarnoVO locarnoVO = AppLocarnoVO.builder()
                    .officeSeq(officeSeq)
                    .appSeq(locarnoReq.appSeq())
                    .classNo(locarnoReq.classNo())
                    .subClassNo(locarnoReq.subClassNo())
                    .goodsSeq(locarnoReq.goodsSeq())
                    .locarnoGroupId(locarnoGroupId)
                    .locarnoNameKo(locarnoReq.locarnoNameKo())
                    .locarnoNameEn(locarnoReq.locarnoNameEn())
                    .goodsCount(locarnoReq.goodsCount())

                    .createUser(userSeq)
                    .delYn("N")
                    .build();

            int result = locarnoMapper.insertLocarno(locarnoVO);

            if (result <= 0) {
                throw new BusinessException("상품류 등록에 실패하였습니다.", INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public BaseSearchResponse<LocarnoResponse.Detail> getLocarnoListByAppSeq(String appSeq) {

        String officeSeq = SecurityUtil.getOfficeSeq();

        List<AppLocarnoVO> list = locarnoMapper.getLocarnoList(officeSeq, appSeq);

        List<LocarnoResponse.Detail> res = list.stream()
                .map(vo -> LocarnoResponse.Detail.builder()
                        .appSeq(vo.getAppSeq())
                        .classNo(vo.getClassNo())
                        .subClassNo(vo.getSubClassNo())
                        .locarnoGroupId(vo.getLocarnoGroupId())
                        .goodsSummaryKo(vo.getGoodsSummaryKo())
                        .goodsSummaryEn(vo.getGoodsSummaryEn())
                        .goodsCount(vo.getGoodsCount())
                        .build()
                ).toList();

        return BaseSearchResponse.of(res, 1, 99);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocarnoResponse.GroupItem> getLocarnoGroupDetail(String appSeq, String locarnoGroupId) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<AppLocarnoVO> items = locarnoMapper.selectLocarnoByGroup(officeSeq, appSeq, locarnoGroupId);

        return items.stream()
                .map(vo -> LocarnoResponse.GroupItem.builder()
                        .classNo(vo.getClassNo())
                        .subClassNo(vo.getSubClassNo())
                        .goodsSeq(vo.getGoodsSeq())
                        .locarnoGroupId(vo.getLocarnoGroupId())
                        .locarnoNameKo(vo.getLocarnoNameKo())
                        .locarnoNameEn(vo.getLocarnoNameEn())
                        .goodsCount(vo.getGoodsCount())
                        .build())
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteLocarnoGroup(String appSeq, String locarnoGroupId) {
        String userSeq = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = locarnoMapper.softDeleteLocarnoGroup(officeSeq, appSeq, locarnoGroupId, userSeq);

        if (deleteResult <= 0) {
            throw new RuntimeException("locarno group soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteLocarnoGroupByList(String appSeq, List<String> locarnoGroupIdList) {
        if (locarnoGroupIdList == null || locarnoGroupIdList.isEmpty()) {
            throw new RuntimeException("locarnoGroupIdList is empty");
        }

        String userSeq = SecurityUtil.getUserInfoSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = locarnoMapper.softDeleteLocarnoGroupByList(officeSeq, appSeq, locarnoGroupIdList, userSeq);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (deleteResult <= 0) {
            throw new RuntimeException("locarno group list soft delete failed.");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteLocarnoGroup(String appSeq, String locarnoGroupId) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = locarnoMapper.hardDeleteLocarnoGroup(officeSeq, appSeq, locarnoGroupId);

        if (deleteResult <= 0) {
            throw new RuntimeException("locarno group soft delete failed.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteLocarnoGroupByList(String appSeq, List<String> locarnoGroupIdList) {
        if (locarnoGroupIdList == null || locarnoGroupIdList.isEmpty()) {
            throw new RuntimeException("locarnoGroupIdList is empty");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int deleteResult = locarnoMapper.hardDeleteLocarnoGroupByList(officeSeq, appSeq, locarnoGroupIdList);

        // 테이블 1대 다 로 구성되어 있어서 리스트에 4개가 들어와도 지워진 개수는 4개가 아닐 수 잇어서 사이즈로 비교하지 않게 적용.
        if (deleteResult <= 0) {
            throw new RuntimeException("locarno group list soft delete failed.");
        }
    }
}
