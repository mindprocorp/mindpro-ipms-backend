package kr.co.mindpro.ipms.domain.history.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.history.dto.response.HistoryResponse;
import kr.co.mindpro.ipms.domain.history.vo.ModifiedHistVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMapper {
    void insertModifiedHist(ModifiedHistVO vo);

    List<HistoryResponse.HistorySearchListDetail> selectHistoryList(@Param("request") BaseSearchRequest request);

    int selectHistoryListTotalCount(@Param("request") BaseSearchRequest request);

    ModifiedHistVO selectModifiedHistDetail(
            @Param("modifiedHistSeq") String modifiedHistSeq,
            @Param("officeSeq") String officeSeq
    );

    List<ModifiedHistVO> selectModifiedHistListByTblSeq(@Param("request") BaseSearchRequest request);

    void deleteModifiedHist(
            @Param("modifiedHistSeq") String modifiedHistSeq,
            @Param("officeSeq") String officeSeq
    );

    void deleteModifiedHistList(
            @Param("ids") List<String> ids,
            @Param("officeSeq") String officeSeq
    );
}
