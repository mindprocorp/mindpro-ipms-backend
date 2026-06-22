package kr.co.mindpro.ipms.domain.locarno.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * [Request DTO] 로카르노 관리 요청 객체
 *
 * @author	 : intst
 * @fileName	 : LocarnoRequest.java
 * @since	 : 2026. 2. 4.
 */
@Data
public class LocarnoRequest {

    @Schema(description = "로카르노 일괄 저장 요청 객체")
    public record LocarnoListRequest(
            @Schema(description = "저장 대상 로카르노 리스트")
            List<LocarnoDetail> locarnoList
    ) {}

    @Schema(description = "로카르노 상세 정보 데이터")
    public record LocarnoDetail(
            @Schema(description = "분류 번호", example = "001")
            String classNo,

            @Schema(description = "로카르노 버전", example = "14")
            String locarnoVersion,

            @Schema(description = "카테고리 구분", example = "C")
            String categoryGb,

            @Schema(description = "분류 명칭 (국문)", example = "식료품")
            String classNmKo,

            @Schema(description = "분류 명칭 (영문)", example = "FOODSTUFFS")
            String classNmEn,

            @Schema(description = "분류 설명 (국문)", example = "식료품에 대한 상세 설명")
            String classDescKo,

            @Schema(description = "분류 설명 (영문)", example = "Detailed description of foodstuffs")
            String classDescEn,

            @Schema(description = "삭제 여부 (Y/N)", allowableValues = {"Y", "N"}, example = "N")
            String delYn,

            @Schema(description = "검색 키워드 (검색 시 사용)", example = "식료")
            String searchKeyword
    ) {}

    public record SaveAllLocarno(

            // 등록할 로카르노 리스트
            List<SaveLocarno> locarnoList,

            // 로카르노 그룹 아이디(수정 시 필요함)
            String locarnoGroupId
    ) {}

    public record SaveLocarno(

            // 출원 시퀀스
            String appSeq,

            // 물품류
            String classNo,

            // 물품군
            String subClassNo,

            // 굿즈 시퀀스
            String goodsSeq,

            // 로카르노 국문명
            String locarnoNameKo,

            // 로카르노 영문명
            String locarnoNameEn,

            @Schema(description = "굿즈 수", example = "10")
            int goodsCount
    ) {}
}
