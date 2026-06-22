package kr.co.mindpro.ipms.common.util;


import kr.co.mindpro.ipms.domain.cost.vo.CostVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;

import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DataConvertUtil {

    private DataConvertUtil() {} // 인스턴스화 방지

    private static final DateTimeFormatter DATE_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * [관계자 추출]
     * @param vo 소스 VO (ConflictMergeVO)
     * @param tblSeq 저장될 마스터 키 (CFT... 또는 APP...)
     * @param officeSeq 사무소 키
     * @param targetGroup 추출 대상 그룹 ("CFT" 또는 "APP")
     */
    public static List<ParticipantVO> extractParticipants(Object vo, String tblSeq, String officeSeq, String targetGroup) {
        List<ParticipantVO> list = new ArrayList<>();
        if (vo == null) return list;

        for (Field field : vo.getClass().getDeclaredFields()) {
            CommonMapping mapping = field.getAnnotation(CommonMapping.class);

            // 타입이 PERSON이고, 지정한 targetGroup과 어노테이션의 group이 일치할 때만
            if (mapping != null && "PERSON".equals(mapping.type()) && targetGroup.equals(mapping.group())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(vo);

                    if (value instanceof String && !((String) value).isEmpty()) {
                        ParticipantVO pVo = new ParticipantVO();
                        pVo.setTblSeq(tblSeq);
                        pVo.setOfficeSeq(officeSeq);
                        pVo.setWorkCategory(targetGroup); // VO에 그룹 정보(APP/CFT) 주입

                        // code가 없으면 자바 필드명을 코드로 사용
                        String participantCode = mapping.code().isEmpty() ? field.getName() : mapping.code();
                        pVo.setParticipantCode(participantCode);

                        pVo.setUserInfoSeq((String) value);
                        pVo.setMainYn("Y");

                        list.add(pVo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * [기일 추출]
     * @param targetGroup 추출 대상 그룹 ("CFT" 또는 "APP")
     */
    public static List<DueDateVO> extractDueDates(Object vo, String tblSeq, String officeSeq, String targetGroup) {
        List<DueDateVO> list = new ArrayList<>();
        if (vo == null) return list;

        for (Field field : vo.getClass().getDeclaredFields()) {
            CommonMapping mapping = field.getAnnotation(CommonMapping.class);

            // 타입이 DATE이고, 지정한 targetGroup과 어노테이션의 group이 일치할 때만
            if (mapping != null && "DATE".equals(mapping.type()) && targetGroup.equals(mapping.group())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(vo);

                    if (value instanceof String && !((String) value).isEmpty()) {
                        OffsetDateTime odt = parseToOffsetDateTime((String) value);
                        if (odt != null) {
                            DueDateVO dVo = new DueDateVO();
                            dVo.setTblSeq(tblSeq);
                            dVo.setOfficeSeq(officeSeq);
                            dVo.setWorkCategory(targetGroup); // VO에 그룹 정보(APP/CFT) 주입

                            String categoryCode = mapping.code().isEmpty() ? field.getName() : mapping.code();
                            dVo.setDuedateCategoryCode(categoryCode);
                            dVo.setDuedateDate(odt);
                            dVo.setNote(mapping.description());

                            list.add(dVo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static List<CostVO> extractCosts(Object vo, String tblSeq, String officeSeq, String targetGroup) {
        List<CostVO> list = new ArrayList<>();
        if (vo == null) return list;

        for (Field field : vo.getClass().getDeclaredFields()) {
            CommonMapping mapping = field.getAnnotation(CommonMapping.class);

            // 타입이 COST이고, 지정한 targetGroup(workType)과 어노테이션의 group이 일치할 때
            if (mapping != null && "COST".equals(mapping.type()) && targetGroup.equals(mapping.group())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(vo);

                    // 금액이 있고 문자열 "null"이 아닌 경우에만 추출
                    if (value instanceof String valStr && !valStr.isEmpty() && !"null".equals(valStr)) {
                        // 콤마 제거 후 Long 변환
                        long amount = parseLongSafe(valStr);

                        CostVO cVo = new CostVO();
                        cVo.setTblSeq(tblSeq);
                        cVo.setOfficeSeq(officeSeq);

                        // Mapping 정보에서 코드와 카테고리 추출
                        String costCode = mapping.code().isEmpty() ? field.getName() : mapping.code();
                        cVo.setCostCategoryCode(costCode);

                        cVo.setKrwAmount(amount);
                        cVo.setNote(mapping.description()); // 어노테이션의 설명을 비고로 활용
                        cVo.setCostDate(OffsetDateTime.now()); // 발생일은 현재 시간 기본값
                        cVo.setCostRemittanceYn("N"); // 기본 미송금 처리

                        list.add(cVo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 금액 문자열에서 콤마를 제거하고 Long으로 안전하게 변환
     * 소수점이 포함된 경우(예: "1111.0")도 처리할 수 있도록 Double로 먼저 파싱
     */
    private static long parseLongSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0L;
        try {
            // 콤마 제거 및 공백 제거
            String cleanVal = val.replaceAll(",", "").trim();
            if (cleanVal.isEmpty()) return 0L;
            
            // 소수점이 있을 수 있으므로 Double로 파싱 후 Long으로 변환
            return (long) Double.parseDouble(cleanVal);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static void injectData(Object targetVo, List<?> sourceList, String targetGroup) {
        if (targetVo == null || sourceList == null || sourceList.isEmpty()) return;

        for (Object item : sourceList) {
            String key = "";
            String value = "";
            String nameValue = ""; // 이름 값을 담을 변수 추가
            // --- [날짜 ] ---
            if (item instanceof ParticipantVO pVo) {
                key = pVo.getParticipantCode();
                value = pVo.getUserInfoSeq();    // ID (시퀀스)
                nameValue = pVo.getUserNameKo(); // 성명
            }
            // --- [날짜 ] ---
            else if (item instanceof DueDateVO dVo) {
                key = dVo.getDuedateCategoryCode();
                Object dateObj = dVo.getDuedateDate();
                if (dateObj instanceof java.time.temporal.TemporalAccessor temporal) {
                    value = formatMinusHoursString8(temporal.toString());
                } else if (dateObj instanceof String s) {
                    value = formatMinusHoursString8(s);
                }
            // --- [비용 ] ---
            }else if (item instanceof CostVO cVo) {
                key = cVo.getCostCategoryCode();
                value = String.valueOf(cVo.getKrwAmount()); // Long -> String 변환
            }else{
                //todo
            }

            if (key == null || key.isEmpty() || value == null || value.isEmpty()) continue;

            for (java.lang.reflect.Field field : targetVo.getClass().getDeclaredFields()) {
                CommonMapping mapping = field.getAnnotation(CommonMapping.class);
                if (mapping != null && targetGroup.equals(mapping.group())) {
                    String mappingCode = (mapping.code() == null || mapping.code().isEmpty()) ? field.getName() : mapping.code();

                    if (key.equals(mappingCode)) {
                        try {
                            field.setAccessible(true);
                            // 1. 현재 필드에 값(ID 또는 날짜) 주입
                            field.set(targetVo, value);

                            // 2. 관계자(PERSON)인 경우 "필드명 + Name" 필드를 찾아 이름 주입
                            if ("PERSON".equals(mapping.type()) && !nameValue.isEmpty()) {
                                try {
                                    String nameFieldName = field.getName() + "Name";
                                    java.lang.reflect.Field nameField = targetVo.getClass().getDeclaredField(nameFieldName);
                                    nameField.setAccessible(true);
                                    nameField.set(targetVo, nameValue);
                                } catch (NoSuchFieldException e) {
                                    // "Name" 필드가 없는 경우는 무시하고 넘어감
                                }
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    /**
     * 날짜 문자열(8자리/14자리)을 OffsetDateTime으로 변환
     */
    public static OffsetDateTime parseToOffsetDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        String pureNumbers = dateStr.replaceAll("[^0-9]", "");
        int len = pureNumbers.length();
        try {
            if (len >= 8 && len < 14) {
                LocalDate date = LocalDate.parse(pureNumbers.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
                return OffsetDateTime.of(date, LocalTime.MIN, ZoneOffset.of("+09:00"));
            } else if (len >= 14) {
                LocalDateTime dateTime = LocalDateTime.parse(pureNumbers.substring(0, 14), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                return dateTime.atOffset(ZoneOffset.of("+09:00"));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    public static String formatMinusHoursString8(Object dateInput) {
        if (dateInput == null) return "";

        try {
            OffsetDateTime odt = null;

            // 1. 입력 타입에 따른 분기 처리
            if (dateInput instanceof OffsetDateTime) {
                odt = (OffsetDateTime) dateInput;
            } else if (dateInput instanceof String dateStr) {
                if (dateStr.isEmpty()) return "";
                // 문자열일 경우 파싱 시도
                odt = OffsetDateTime.parse(dateStr);
            }

            // 2. 공통 변환 로직 (Asia/Seoul 보정 및 포맷팅)
            if (odt != null) {
                return odt.atZoneSameInstant(java.time.ZoneId.of("Asia/Seoul"))
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            }

        } catch (Exception e) {
            // 파싱 실패 시 (문자열인 경우) 기존처럼 숫자만 8자리 추출
            if (dateInput instanceof String dateStr) {
                String numeric = dateStr.replaceAll("[^0-9]", "");
                return numeric.substring(0, Math.min(numeric.length(), 8));
            }
        }

        return "";
    }

    public static String formatDateTimeString12(Object dateInput) {
        if (dateInput == null) return "";

        try {
            OffsetDateTime odt = null;

            if (dateInput instanceof OffsetDateTime) {
                odt = (OffsetDateTime) dateInput;
            } else if (dateInput instanceof String dateStr) {
                if (dateStr.isEmpty()) return "";
                odt = OffsetDateTime.parse(dateStr);
            }

            if (odt != null) {
                return odt.atZoneSameInstant(java.time.ZoneId.of("Asia/Seoul"))
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }

        } catch (Exception e) {
            if (dateInput instanceof String dateStr) {
                String numeric = dateStr.replaceAll("[^0-9]", "");
                return numeric.substring(0, Math.min(numeric.length(), 12));
            }
        }

        return "";
    }

    // 유틸리티 메소드 - 필요 시 별도 Util 클래스로 분리
    // 리스트 사이즈 반환 (Null이면 0)
    public static int getListSize(List<?> list) {
        return list == null ? 0 : list.size();
    }

    // 리스트를 콤마 구분 문자열로 변환 (예: [US, JP] -> "US,JP")
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(",", list);
    }

    public static Integer parseIntSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 콤마로 구분된 문자열을 List<String>으로 변환
     * 예: "KR, US, JP" -> ["KR", "US", "JP"]
     */
    public static List<String> stringToList(String content) {
        if (content == null || content.trim().isEmpty()) {
            return null;
        }
        // 콤마(,)를 기준으로 자르고 앞뒤 공백 제거
        List<String> list = new ArrayList<>();
        for (String s : content.split(",")) {
            if (!s.trim().isEmpty()) {
                list.add(s.trim());
            }
        }
        return list;
    }

    /**
     * offsetDateTime을 "Asia/Seoul" 시간대로 보정하여 "yyyy-MM-dd HH:mm:ss" 형태의 스트링으로 변환해주는 메소드
     * */
    public static String formatOffsetDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return "";
        }
        // Asia/Seoul 시간대로 변환 후 포맷팅
        return offsetDateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DATE_AT_FORMATTER);
    }



}