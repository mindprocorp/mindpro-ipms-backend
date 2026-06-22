package kr.co.mindpro.ipms.domain.locarno.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : LocarnoResponse.java
 * @since : 2026. 2. 9.
 */
public class LocarnoResponse {

    @Builder
    public record Detail(

            String appSeq,

            String classNo,

            String subClassNo,

            String locarnoGroupId,

            String goodsSummaryKo,

            String goodsSummaryEn,

            int goodsCount
    ) {}

    /** 그룹 내 개별 항목 (수정 모달 초기 데이터용) */
    @Builder
    public record GroupItem(

            String classNo,

            String subClassNo,

            String goodsSeq,

            String locarnoGroupId,

            String locarnoNameKo,

            String locarnoNameEn,

            int goodsCount
    ) {}
}
