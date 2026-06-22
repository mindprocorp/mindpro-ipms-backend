//package kr.co.mindpro.ipms.common.util;
//
//
//
//
//import kr.co.mindpro.ipms.domain.searchcondition.vo.BaseSearchRequest;
//import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchFieldVO;
//import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchQueryParamVO;
//import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import java.time.*;
//import java.util.List;
//import java.util.Map;
//
//
//
//public class SearchFilterUtil {
//
//    private SearchFilterUtil() {} // 인스턴스화 방지
//
//    /**
//     * UI 검색 요청을 분석하여 테이블 및 필터 타입별로 분류된 결과 객체를 반환합니다.
//     */
//    public static SearchQueryParamVO buildSearchParams(BaseSearchRequest request, List<SearchFieldVO> fieldList) {
//        // 1. 결과 VO 생성 (SuperBuilder 사용 시 .builder().build() 호출)
//        SearchQueryParamVO result = SearchQueryParamVO.builder().build();
//
//        // 2. searchcondition 처리 (상단 검색)
//        if (!ObjectUtils.isEmpty(request.getSearchCondition())) {
//            for (Map<String, String> condMap : request.getSearchCondition()) {
//                for (Map.Entry<String, String> entry : condMap.entrySet()) {
//                    handleStandardFilter(entry.getKey(), entry.getValue(), fieldList, result, false);
//                }
//            }
//        }
//
//        // 3. textFilters 처리 (그리드 텍스트)
//        if (!ObjectUtils.isEmpty(request.getTextFilters())) {
//            for (Object obj : request.getTextFilters()) {
//                String type = null;
//                String value = null;
//
//                if (obj instanceof BaseSearchRequest.TextFilter tf) {
//                    type = tf.getType();
//                    value = tf.getValue();
//                } else if (obj instanceof Map<?, ?> tfMap) {
//                    type = String.valueOf(tfMap.get("type"));
//                    value = String.valueOf(tfMap.get("value"));
//                }
//
//                // ★ 이 부분이 빠져있었습니다! 추출한 값을 처리하도록 호출해야 합니다.
//                if (StringUtils.hasText(type) && StringUtils.hasText(value)) {
//                    handleStandardFilter(type, value, fieldList, result, true);
//                }
//            }
//        }
//
//        // 4. dateFilters 처리 (그리드 날짜)
//        if (!ObjectUtils.isEmpty(request.getDateFilters())) {
//            for (Object obj : request.getDateFilters()) {
//                if (obj instanceof BaseSearchRequest.DateFilter df) {
//                    // ★ DTO 객체인 경우 (가장 권장되는 방식)
//                    handleDateFilterFromObj(df, fieldList, result);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * [내부 헬퍼] 텍스트 및 일반 옵션 필터를 찾아서 분류함에 담음
//     */
//    private static void handleStandardFilter(String key, String val, List<SearchFieldVO> fieldList, SearchQueryParamVO result, boolean isTextFilter) {
//        if (!StringUtils.hasText(val) || "null".equals(val)) return;
//
//        for (SearchFieldVO f : fieldList) {
//            // fieldKey(petitionerType)와 맞거나, dbColumn(petitioner_type)과 맞으면 진행
//            if (key.equals(f.getFieldKey()) || key.equals(f.getDbColumn())) {
//
//                // PARTI나 DUEDATE인 경우, key(필드명)를 그대로 코드값으로 사용
//                String finalCodeVal = ("PARTI".equals(f.getTargetTable()) || "DUEDATE".equals(f.getTargetTable()))
//                        ? f.getFieldKey() : f.getSearchCodeValue();
//
//                SearchParamVO vo = SearchParamVO.builder()
//                        .codeCol(f.getSearchCodeColumn())
//                        .codeVal(finalCodeVal)
//                        .dataCol(f.getDbColumn())
//                        .value(val)
//                        .isTextFilter(isTextFilter)
//                        .build();
//
//                dispatchFilter(f.getTargetTable(), vo, result, isTextFilter);
//                break;
//            }
//        }
//    }
//
//    /**
//     * [내부 헬퍼] 날짜 필터를 처리하여 분류함에 담음
//     */
//    private static void handleDateFilter(Map<?, ?> dfMap, List<SearchFieldVO> fieldList, SearchQueryParamVO result) {
//        String type = String.valueOf(dfMap.get("type"));
//        String startStr = String.valueOf(dfMap.get("startDate"));
//        String endStr = String.valueOf(dfMap.get("endDate"));
//
//        if (StringUtils.hasText(startStr) || StringUtils.hasText(endStr)) {
//            for (SearchFieldVO f : fieldList) {
//                if (type.equals(f.getFieldKey())) {
//
//                    // 1. OffsetDateTime으로 변환 (DataConvertUtil 내부에서 처리)
//                    OffsetDateTime startDt = DataConvertUtil.parseToOffsetDateTime(startStr);
//                    OffsetDateTime endDt = DataConvertUtil.parseToOffsetDateTime(endStr);
//
//                    // 2. 시간대 보정 (종료일 23:59:59 처리)
//                    if (startDt != null) {
//                        startDt = startDt.with(LocalTime.MIN); // 00:00:00
//                    }
//
//                    if (endDt != null) {
//                        // 문자열 길이가 8~10자리(날짜만 있는 경우)라면 하루 전체를 포함하도록 보정
//                        if (endStr.replaceAll("[^0-9]", "").length() <= 8) {
//                            endDt = endDt.with(LocalTime.MAX); // 23:59:59.999
//                        }
//                    }
//
//                    SearchParamVO vo = SearchParamVO.builder()
//                            .codeCol(f.getSearchCodeColumn())
//                            .codeVal(f.getSearchCodeValue())
//                            .dataCol(f.getDbColumn())
//                            .startDate(startDt)
//                            .endDate(endDt)
//                            .build();
//
//                    dispatchFilter(f.getTargetTable(), vo, result, false);
//                    break;
//                }
//            }
//        }
//    }
//
//
//    private static void handleDateFilterFromObj(BaseSearchRequest.DateFilter df, List<SearchFieldVO> fieldList, SearchQueryParamVO result) {
//        Object rawStart = df.getStartDate();
//        Object rawEnd = df.getEndDate();
//
//        if (rawStart != null || rawEnd != null) {
//            for (SearchFieldVO f : fieldList) {
//                if (df.getType().equals(f.getFieldKey())) {
//
//                    OffsetDateTime startDt = (rawStart instanceof String s) ? DataConvertUtil.parseToOffsetDateTime(s) : (OffsetDateTime) rawStart;
//                    OffsetDateTime endDt = (rawEnd instanceof String s) ? DataConvertUtil.parseToOffsetDateTime(s) : (OffsetDateTime) rawEnd;
//
//                    if (startDt != null) startDt = startDt.with(LocalTime.MIN);
//                    if (endDt != null) endDt = endDt.with(LocalTime.MAX);
//
//                    // 핵심 매핑 로직
//                    SearchParamVO vo = SearchParamVO.builder()
//                            .codeCol(f.getSearchCodeColumn()) // "duedate_category_code"
//                            .codeVal(f.getFieldKey())         // "abandonDate" (또는 "appDate" 등 UI 타입명)
//                            .dataCol(f.getSearchCodeValue())  // "duedate_date" (날짜 컬럼명)
//                            .startDate(startDt)
//                            .endDate(endDt)
//                            .build();
//
//                    dispatchFilter(f.getTargetTable(), vo, result, false);
//                    break;
//                }
//            }
//        }
//    }
//    /**
//     * [분류 전담 메소드] 테이블 코드에 따라 적절한 리스트로 분배
//     * 새로운 테이블이 추가되면 이 메소드만 수정하면 됨
//     */
//    private static void dispatchFilter(String targetTable, SearchParamVO vo, SearchQueryParamVO result, boolean isTextFilter) {
//        if ("MST".equals(targetTable)) {
//            if (isTextFilter) result.getMstLikeFilters().add(vo);
//            else result.getMstEqFilters().add(vo);
//        } else if ("PARTI".equals(targetTable)) {
//            result.getPSearchList().add(vo);
//        } else if ("DUEDATE".equals(targetTable)) {
//            result.getDSearchList().add(vo);
//        }
//        // else if ("NEW_TBL".equals(targetTable)) { ... }
//    }
//
//}
