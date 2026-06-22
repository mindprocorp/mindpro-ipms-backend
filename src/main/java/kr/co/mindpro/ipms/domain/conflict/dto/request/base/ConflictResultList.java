package kr.co.mindpro.ipms.domain.conflict.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictMergeVO;
import kr.co.mindpro.ipms.domain.conflict.vo.ConflictResultViewVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConflictResultList {

    @Schema(    description = "분쟁 결과 리스트",   example = "[{ \"conflictResultSeq\": \"CONFRES20260000001\", \"judgmentResult\": \"인용\" }]"    )
    List<ConflictResultViewVO> conflictResultList;

    public ConflictResultList(ConflictMergeVO mergeVO) {
        if (mergeVO != null && mergeVO.getConflictResultList() != null) {
            // VO에 담긴 결과 리스트를 할당
            this.conflictResultList = mergeVO.getConflictResultList();
        } else {
            this.conflictResultList = new ArrayList<>();
        }
    }
}