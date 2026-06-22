package kr.co.mindpro.ipms.domain.dispatch.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.dispatch.dto.request.DispatchRequest;
import kr.co.mindpro.ipms.domain.dispatch.dto.response.DispatchResponse;
import kr.co.mindpro.ipms.domain.dispatch.repository.db1.DispatchMapper;
import kr.co.mindpro.ipms.domain.dispatch.service.DispatchService;
import kr.co.mindpro.ipms.domain.dispatch.vo.DispatchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchMapper dispatchMapper;

    @Override
    public BaseSearchResponse<DispatchResponse.DispatchDetail> getDispatchList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());

        List<DispatchVO> list = dispatchMapper.findList(request);
        long count = dispatchMapper.countList(request);

        List<DispatchResponse.DispatchDetail> responseList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return BaseSearchResponse.<DispatchResponse.DispatchDetail>builder()
                .list(responseList)
                .totalCount((int) count)
                .build();
    }

    @Override
    @Transactional
    public DispatchResponse.DispatchDetail saveDispatch(DispatchRequest.DispatchDetail request) {
        DispatchVO vo = new DispatchVO();
        vo.setDispatchSeq(request.getDispatchSeq());
        vo.setOfficeSeq(SecurityUtil.getOfficeSeq());
        vo.setCategory(request.getCategory());
        vo.setDocType(request.getDocType());
        vo.setDispatchDate(request.getDispatchDate());
        vo.setClient(request.getClient());
        vo.setManager(request.getManager());
        vo.setDocContent(request.getDocContent());
        vo.setMethod(request.getMethod());
        vo.setSendDate(request.getSendDate());
        vo.setRegNo(request.getRegNo());
        vo.setAckYn(request.getAckYn());
        vo.setPostAddr(request.getPostAddr());
        vo.setNote(request.getNote());
        vo.setCreateUser(SecurityUtil.getUserInfoSeq());
        vo.setUpdateUser(SecurityUtil.getUserInfoSeq());

        if (vo.getDispatchSeq() == null || vo.getDispatchSeq().isEmpty()) {
            dispatchMapper.insert(vo);
        } else {
            dispatchMapper.update(vo);
        }

        return convertToResponse(vo);
    }

    @Override
    @Transactional
    public void deleteDispatch(String dispatchSeq) {
        dispatchMapper.delete(dispatchSeq, SecurityUtil.getUserInfoSeq());
    }

    @Override
    @Transactional
    public void deleteDispatchList(List<String> ids) {
        for (String id : ids) {
            dispatchMapper.delete(id, SecurityUtil.getUserInfoSeq());
        }
    }

    private DispatchResponse.DispatchDetail convertToResponse(DispatchVO vo) {
        return DispatchResponse.DispatchDetail.builder()
                .dispatchSeq(vo.getDispatchSeq())
                .category(vo.getCategory())
                .docType(vo.getDocType())
                .dispatchDate(vo.getDispatchDate())
                .client(vo.getClient())
                .manager(vo.getManager())
                .docContent(vo.getDocContent())
                .method(vo.getMethod())
                .sendDate(vo.getSendDate())
                .regNo(vo.getRegNo())
                .ackYn(vo.getAckYn())
                .postAddr(vo.getPostAddr())
                .note(vo.getNote())
                .uploadDate(vo.getCreateAt())
                .uploadUserName(vo.getUploadUserName())
                .build();
    }
}
