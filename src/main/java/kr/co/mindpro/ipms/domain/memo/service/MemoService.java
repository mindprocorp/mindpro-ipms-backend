package kr.co.mindpro.ipms.domain.memo.service;

import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.memo.dto.request.MemoRequest;
import kr.co.mindpro.ipms.domain.memo.dto.response.MemoResponse;
import kr.co.mindpro.ipms.domain.memo.vo.MemoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : InvoiceService.java
 * @since    : 2026. 01. 07.
 */
public interface MemoService {

    void saveAllMemos(List<MemoVO> list);
    BaseSearchResponse<MemoResponse.MemoDetail> getMemoListByWork(String tblSeq);
    MemoResponse.MemoDetail registerMemo(MemoRequest.MemoDetail memo, List<MultipartFile> files);
    MemoResponse.MemoDetail getMemoDetail(String memoSeq);

    void softDeleteMemo(String tblSeq, String memoSeq);

    void softDeleteMemoByList(String tblSeq, List<String> memoSeqList);

    void hardDeleteMemo(String tblSeq, String memoSeq);

    void hardDeleteMemoByList(String tblSeq, List<String> memoSeqList);
}