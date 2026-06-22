package kr.co.mindpro.ipms.domain.participant.service.impl;

import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.participant.repository.db1.ParticipantMapper;
import kr.co.mindpro.ipms.domain.participant.service.ParticipantService;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantMergeVo;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;



/**
 * 관계자 비즈니스 로직 구현체
 */
@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantMapper participantMapper;

    /**
     * [Detail 화면 요약용]
     * 화면 상단 요약 바에 표시할 대표자(main_yn='Y')들만 Map으로 가공하여 반환.
     * 타 도메인 로직에서 역할별 대표자 성명을 즉시 참조할 때 사용.
     */


    @Override
    @Transactional(readOnly = true)
    public List<ParticipantVO> getParticipantListByWork(String tblSeq, String officeSeq) {
        // 2. 이름 유지된 매퍼를 통해 전체 리스트 그대로 반환
        return participantMapper.findParticipantsByWork(tblSeq, officeSeq);
    }

    /**
     * [특정 역할 상세 리스트 조회]
     * 요약 화면에서 '더보기' 클릭 시, 특정 코드(예: APP)에 해당하는 모든 관계자 명단을 조회.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipantVO> getParticipantsByCode(String tblSeq, String officeSeq, String participantCode) {
        // 특정 코드에 해당하는 전체 명단(대표+일반)을 반환
        return participantMapper.findParticipantsByCode(tblSeq, officeSeq, participantCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllParticipants(List<ParticipantVO> list) {
        // 1. 기초 검증
        if (list == null || list.isEmpty()) return;

        // 2. 기준 정보 추출 (리스트의 첫 번째 객체에서 tblSeq, officeSeq 등을 가져옴)
        ParticipantVO firstVo = list.get(0);
        String tblSeq = firstVo.getTblSeq();
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 유저 정보 결정 (전달받은 정보가 없으면 시큐리티 세션에서 추출)
        String userId = SecurityUtil.getUserInfoSeq();

        // 3. 기존 데이터 논리 삭제 실행
        // DueDate 방식처럼 무조건 삭제를 먼저 호출하여 초기화합니다.
        int deletedRows = participantMapper.updateDeleteStatusByWork(tblSeq, officeSeq);

        // 4. 삭제 처리 후(신규 포함 deletedRows >= 0) 보정 후 인서트
        if (deletedRows >= 0) {
            list.forEach(vo -> {
                // 데이터 강제 매칭 및 보정 (3단 보정)
                vo.setTblSeq(tblSeq);
                vo.setOfficeSeq(officeSeq);
                vo.setCreateUser(userId);
                vo.setUpdateUser(userId);
                vo.setDelYn("Y");

                // 관계자 전용 필드 보정
                vo.setMainYn(vo.getMainYn() == null ? "N" : vo.getMainYn());
                if (vo.getShareRatio() == null || vo.getShareRatio() == 0) {
                    vo.setShareRatio(100);
                }

                // 최종 저장
                participantMapper.insertParticipant(vo);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertParticipant(ParticipantVO vo) {
        participantMapper.insertParticipant(vo);
    }



    @Override
    @Transactional(readOnly = true) // 단순 조회이므로 readOnly 설정
    public List<String> getIdsByFilters(String officeSeq, String targetJob , List<SearchParamVO> list) {

        // 1. 리스트가 비어있으면 즉시 빈 리스트 반환 (리소스 절약)
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 2. Map으로 변환하는 오버헤드 없이 VO 리스트를 직접 전달
        // MyBatis는 객체의 필드명(participantCode, userInfoSeq 등)을 직접 참조할 수 있음
        return participantMapper.findTblSeqsByFilters(officeSeq, targetJob , list);
    }
}

