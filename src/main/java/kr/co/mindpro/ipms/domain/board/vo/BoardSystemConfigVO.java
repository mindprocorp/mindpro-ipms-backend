package kr.co.mindpro.ipms.domain.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 게시판 시스템 통합 설정 VO
 * 전역 용량, 본문 크기, 휴지통 보관 기간 등을 관리합니다.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "게시판 시스템 통합 설정 정보")
public class BoardSystemConfigVO extends BaseVO {

    @Schema(description = "사무소 시퀀스 (PK)")
    private String officeSeq;

    @Schema(description = "첨부파일 최대 용량 (MB)")
    private Integer maxFileSize;

    @Schema(description = "게시글 본문 최대 용량 (MB)")
    private Integer maxBodySize;

    @Schema(description = "휴지통 보관 기한 (일, 'INF'는 무제한)")
    private String trashRetentionDays;

    @Schema(description = "게시판 마스터의 설정 변경 허용 여부")
    private String allowMasterChangeYn;

    @Schema(description = "메인 레이아웃 형태 (LIST, FEED)")
    private String mainLayoutType;

    @Schema(description = "최근 게시글 노출 개수")
    private Integer recentPostCount;

    @Schema(description = "새 글 아이콘 노출 기간 (시간)")
    private Integer newBadgeDuration;

    @Schema(description = "공지사항 최상단 고정 여부 (Y, N)")
    private String showPinOnTopYn;
}
