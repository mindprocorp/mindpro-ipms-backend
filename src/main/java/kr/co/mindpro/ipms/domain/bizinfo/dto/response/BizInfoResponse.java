package kr.co.mindpro.ipms.domain.bizinfo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BizInfoResponse {
    @Schema(description = "사업자 정보 상세 응답")
    @Builder
    public record BizInfoDetail(
            @Schema(description = "사업자 정보 일련번호", example = "BIZ202600001")
            String bizInfoSeq,
            @Schema(description = "사무소 일련번호", example = "OFFICE001")
            String officeSeq,
            @Schema(description = "법인 코드", example = "CORP001")
            String corpCode,
            @Schema(description = "사업장 상호", example = "(주)마인드프로")
            String bizCorpName,
            @Schema(description = "사업자 등록번호", example = "123-45-67890")
            String bizRegNo,
            @Schema(description = "종사업장번호", example = "123-45-67890")
            String bizWorkplaceNo,
            @Schema(description = "대표자 성명", example = "홍길동")
            String ceoName,
            @Schema(description = "사업장 주소", example = "서울특별시 강남구 ...")
            String bizAddr,
            @Schema(description = "사업장 상세 주소", example = "101호")
            String bizAddrDetail,
            @Schema(description = "우편번호", example = "06000")
            String bizPostNo,
            @Schema(description = "전화번호", example = "02-123-4567")
            String bizTelNo,
            @Schema(description = "팩스번호", example = "02-123-4568")
            String bizFaxNo,
            @Schema(description = "업태", example = "서비스")
            String bizType,
            @Schema(description = "종목", example = "소프트웨어 개발")
            String bizKind,
            @Schema(description = "등록 감면 코드", example = "RED01")
            String regDiscountCode,
            @Schema(description = "연차 감면 코드", example = "YRED01")
            String yearDiscountCode,
            @Schema(description = "감면 종료일", example = "2026-12-31")
            String discountClosingDate,
            @Schema(description = "사업자담당자", example = "이지금")
                    String bizContactName,

            @Schema(description = "사업자부서", example = "경리부")
            String bizDeptName,

            @Schema(description = "사업자이메일", example = "acc@mindpro.co.kr")
            String bizEmail

    ) {}
    @Schema(description = "사업자 정보 목록용 응답 (핵심 정보 위주)")
    @Builder
    public record BizInfoList(
            List<BizInfoDetail> bizInfoList,
            int totalCount
    ) {}

}
