package kr.co.mindpro.ipms.domain.bizinfo.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.bizinfo.dto.request.BizInfoRequest;
import kr.co.mindpro.ipms.domain.bizinfo.dto.response.BizInfoResponse;
import kr.co.mindpro.ipms.domain.bizinfo.repository.db1.BizInfoMapper;
import kr.co.mindpro.ipms.domain.bizinfo.service.BizInfoService;
import kr.co.mindpro.ipms.domain.bizinfo.vo.BizInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import kr.co.mindpro.ipms.domain.ai.service.RagService;

/**
 * 청구서 비즈니스 로직 구현체
 *
 * @author   : min
 * @fileName : DuedateServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class BizInfoServiceImpl implements BizInfoService {
    private final BizInfoMapper bizInfoMapper;
    private final RagService ragService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizInfoResponse.BizInfoDetail saveBizInfo(BizInfoRequest.BizInfoDetail request) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userId = SecurityUtil.getUserInfoSeq();



        // 3. BizInfoVO 빌더 생성 (Record 필드 접근)
        BizInfoVO vo = BizInfoVO.builder()
                .officeSeq(officeSeq)
                .corpCode(request.corpCode())
                .bizCorpName(request.bizCorpName())
                .bizRegNo(request.bizRegNo())
                .bizWorkplaceNo(request.bizWorkplaceNo())
                .ceoName(request.ceoName())
                .bizAddr(request.bizAddr())
                .bizAddrDetail(request.bizAddrDetail())
                .bizPostNo(request.bizPostNo())
                .bizTelNo(request.bizTelNo())
                .bizFaxNo(request.bizFaxNo())
                .bizType(request.bizType())
                .bizKind(request.bizKind())
                .bizContactName(request.bizContactName())
                .bizDeptName(request.bizDeptName())
                .bizEmail(request.bizEmail())
                .regDiscountCode(request.regDiscountCode())
                .yearDiscountCode(request.yearDiscountCode())
                .discountClosingDate(request.discountClosingDate())
                //.bizRegFile(request.bizRegFile())
                .createUser(userId)
                .build();

          bizInfoMapper.insertBizInfo(vo);


        BizInfoResponse.BizInfoDetail detail = convertToDetail(vo);
        ragService.syncVectorData(kr.co.mindpro.ipms.common.util.SecurityUtil.getOfficeSeq(), "BIZ_INFO", detail.bizInfoSeq(),"사업자정보", detail);
        return detail;
    }

    @Override
    @Transactional(readOnly = true)
    public BizInfoResponse.BizInfoList getBizInfoList( BaseSearchRequest request) {
        List<BizInfoVO> voList = bizInfoMapper.findAllByOffice(SecurityUtil.getOfficeSeq(),  request.getOffSet(), request.getPageSize());

        // 1. 상세 목록 빌드로 생성
        List<BizInfoResponse.BizInfoDetail> detailList = voList.stream()
                .map(this::convertToDetail)
                .collect(Collectors.toList());

        // 2. 결과 래퍼 빌드로 생성
        return BizInfoResponse.BizInfoList.builder()
                .bizInfoList(detailList)
                .totalCount(detailList.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BizInfoResponse.BizInfoDetail getBizInfoDetail(String bizInfoSeq) {
        BizInfoVO vo = bizInfoMapper.findById(bizInfoSeq, SecurityUtil.getOfficeSeq());
        return convertToDetail(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBizInfo(String bizInfoSeq) {
        bizInfoMapper.deleteBizInfo(bizInfoSeq, SecurityUtil.getOfficeSeq(), SecurityUtil.getUserInfoSeq());
    }

    // VO를 Response Record로 변환하는 공통 메서드
    private BizInfoResponse.BizInfoDetail convertToDetail(BizInfoVO vo) {
        return BizInfoResponse.BizInfoDetail.builder()
                .bizInfoSeq(vo.getBizInfoSeq())
                .officeSeq(vo.getOfficeSeq())
                .corpCode(vo.getCorpCode())
                .bizCorpName(vo.getBizCorpName())
                .bizRegNo(vo.getBizRegNo())
                .bizWorkplaceNo(vo.getBizWorkplaceNo())
                .ceoName(vo.getCeoName())
                .bizAddr(vo.getBizAddr())
                .bizAddrDetail(vo.getBizAddrDetail())
                .bizPostNo(vo.getBizPostNo())
                .bizTelNo(vo.getBizTelNo())
                .bizFaxNo(vo.getBizFaxNo())
                .bizType(vo.getBizType())
                .bizKind(vo.getBizKind())
                .regDiscountCode(vo.getRegDiscountCode())
                .yearDiscountCode(vo.getYearDiscountCode())
                .discountClosingDate(vo.getDiscountClosingDate())
                .bizContactName(vo.getBizContactName())
                .bizDeptName(vo.getBizDeptName())
                .bizEmail(vo.getBizEmail())
                //.bizRegFile(vo.getBizRegFile())
                .build();
    }
}