package kr.co.mindpro.ipms.domain.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * 청구서 테이블 매핑 객체
 * DB 테이블 화면과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : InvoiceMergeVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class InvoiceBankingVO extends BaseVO {

    @Schema(description = "청구서 일련번호", example = "INV20260000001", format = "SEQ")
    private String invoiceSeq;

    @Schema(description = "입금 내역 일련번호", example = "BNK20260000001", format = "SEQ")
    private String bankingSeq;

    @Schema(description = "관계자 일련번호", example = "PAR20260000001", format = "SEQ")
    private String participantSeq;

    @Schema(description = "사용자 일련번호", example = "USR20260000001", format = "SEQ")
    private String userMstSeq;

    @Schema(description = "사무소 일련번호", example = "OFFICE20260000001")
    private String officeSeq;

    @Schema(description = "입출금 구분 코드", example = "BNK01")
    private String bankingCategory;

    @Schema(description = "입출금 구분 명칭", example = "입금")
    private String bankingCategoryName;

    @Schema(description = "상세 종류", example = "해외대리인송금")
    private String bankingKind;

    @Schema(description = "입금 확인 일시")
    private OffsetDateTime depositCheckDate;

    @Schema(description = "입금/송금 발송 일시")
    private OffsetDateTime depositSendDate;

    @Schema(description = "송금 수수료", example = "5000")
    private String depositFee;

    @Schema(description = "입금/송금 방법 코드", example = "WAY01")
    private String depositWay;

    @Schema(description = "입금/송금 방법 명칭", example = "무통장입금")
    private String depositWayName;

    @Schema(description = "입금/송금 금액 (원화)", example = "1500000")
    private Integer depositAmount;

    @Schema(description = "화폐 단위 코드", example = "USD")
    private String currencyUnit;

    @Schema(description = "화폐 단위 명칭", example = "미국 달러")
    private String currencyUnitName;

    @Schema(description = "환율", example = "1350.20")
    private BigDecimal exchangeRatio;

    @Schema(description = "외화 금액", example = "1110.50")
    private BigDecimal exchangeAmount;

    @Schema(description = "입금/송금자 명", example = "홍길동")
    private String depositName;

    @Schema(description = "은행명", example = "신한은행")
    private String depositBank;

    @Schema(description = "계좌번호", example = "110-123-456789")
    private String depositAccountNo;

    // ─── 선수금 ────────────────────────────────────────
    @Schema(description = "선수금 입금번호", example = "PRE20260000001")
    private String prepaymentDepositNo;

    @Schema(description = "일반선수금 잔액")
    private Long generalPrepaymentBalance;

    @Schema(description = "일반선수금 사용액")
    private Long generalPrepaymentUsedAmount;

    @Schema(description = "지정선수금 잔액")
    private Long designatedPrepaymentBalance;

    @Schema(description = "지정선수금 사용액")
    private Long designatedPrepaymentUsedAmount;
}