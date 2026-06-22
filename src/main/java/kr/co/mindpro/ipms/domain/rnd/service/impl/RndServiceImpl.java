package kr.co.mindpro.ipms.domain.rnd.service.impl;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.rnd.dto.request.RndRequest;
import kr.co.mindpro.ipms.domain.rnd.dto.response.RndResponse;
import kr.co.mindpro.ipms.domain.rnd.repository.db1.RndMapper;
import kr.co.mindpro.ipms.domain.rnd.service.RndService;
import kr.co.mindpro.ipms.domain.rnd.vo.RndVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.co.mindpro.ipms.common.util.DataConvertUtil.parseToOffsetDateTime;

import kr.co.mindpro.ipms.domain.ai.service.RagService;

/**
 * @author : seokho
 * @fileName : RndServiceImpl.java
 * @since : 2026. 2. 5.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RndServiceImpl implements RndService {

    private final RndMapper rndMapper;
    private final RagService ragService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRnd(RndRequest.RnbRequestDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        String rndSeq = request.rndSeq();

        int result = 0;

        RndVO vo = RndRequest.RnbRequestDetail.setRndVO(request);

        vo.setOfficeSeq(officeSeq);
        vo.setRndSeq(rndSeq);

        if (StringUtils.hasText(rndSeq)) {
            result = rndMapper.getDuplicateRndCnt(vo);
        }

        if (result > 0) {
            vo.setUpdateUser(userSeq);

            result = rndMapper.updateRnd(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] 연구과제 수정 실패! (Return: 0)");
                throw new RuntimeException("연구과제 수정에 실패했습니다.");
            }
        } else {
            vo.setCreateUser(userSeq);

            result = rndMapper.insertRnd(vo);

            if (result <= 0) {
                log.error(">>> [ERROR] 연구과제 등록 실패! (Return: 0)");
                throw new RuntimeException("연구과제 등록에 실패했습니다.");
            }
        }
        
        // AI Vector Sync
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "RND", vo.getRndSeq(),"연구과제", request);
    }

    @Override
    public BaseSearchResponse<RndResponse.RndResponseDetail> getRndList(String appSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        List<RndVO> list = rndMapper.getRndList(officeSeq, appSeq);

        int count = rndMapper.getRndCount(officeSeq, appSeq);

        List<RndResponse.RndResponseDetail> resList = list.stream()
                .map(RndResponse.RndResponseDetail::of)
                .toList();

        return BaseSearchResponse.of(resList, count, 1, 99);
    }

    @Override
    public RndResponse.RndResponseDetail getRndDetail(String appSeq, String rndSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        RndVO vo = rndMapper.getRndDetail(officeSeq, appSeq, rndSeq);

        if (vo == null) {
            throw new RuntimeException("요청하신 연구과제 데이터가 존재하지 않습니다.");
        }

        return RndResponse.RndResponseDetail.of(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteRnd(String appSeq, String rndSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = rndMapper.softDeleteRnd(officeSeq, appSeq, rndSeq, userSeq);

        if (result <= 0 ) {
            log.error(">>> [ERROR] 연구과제 삭제 실패! (Return: 0)");
            throw new RuntimeException("연구과제 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteRndByList(String appSeq, List<String> rndSeqList) {
        if (rndSeqList == null || rndSeqList.isEmpty()) {
            throw new RuntimeException("연구과제 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        int result = rndMapper.softDeleteRndList(officeSeq, appSeq, rndSeqList, userSeq);

        if (result != rndSeqList.size()) {
            log.error(">>> [ERROR] 연구과제 다건 논리 삭제 실패! (Expected: {}, Actual: {})", rndSeqList.size(), result);
            throw new RuntimeException("연구과제 다건 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteRnd(String appSeq, String rndSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = rndMapper.hardDeleteRnd(officeSeq, appSeq, rndSeq);

        if (result <= 0) {
            log.error(">>> [ERROR] 연구과제 단건 물리 삭제 실패! (Return: 0)");
            throw new RuntimeException("연구과제 단건 물리 삭제에 실패했습니다.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteRndByList(String appSeq, List<String> rndSeqList) {
        if (rndSeqList == null || rndSeqList.isEmpty()) {
            throw new RuntimeException("연구과제 시퀀스 목록이 비어 있습니다.");
        }

        String officeSeq = SecurityUtil.getOfficeSeq();

        int result = rndMapper.hardDeleteRndList(officeSeq, appSeq, rndSeqList);

        if (result != rndSeqList.size()) {
            log.error(">>> [ERROR] 연구과제 다건 물리 삭제 실패! (Expected: {}, Actual: {})", rndSeqList.size(), result);
            throw new RuntimeException("연구과제 다건 물리 삭제에 실패했습니다.");
        }
    }
}
