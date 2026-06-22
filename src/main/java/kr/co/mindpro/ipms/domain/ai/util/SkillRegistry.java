package kr.co.mindpro.ipms.domain.ai.util;

import kr.co.mindpro.ipms.domain.ai.service.SchemaFieldMappingService;
import kr.co.mindpro.ipms.domain.ai.tool.*;
import kr.co.mindpro.ipms.domain.ai.skill.BusinessSpecialistSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * [AutoGen Pattern] 도구들을 논리적 그룹(Skill)으로 묶어 관리하고, 의도에 따라 선별적으로 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class SkillRegistry {

    private final SearchSystemDictionaryTool searchSystemDictionaryTool;
    private final SearchRealtimeDatabaseRecordTool searchRealtimeDatabaseRecordTool;
    private final SearchDatabaseListTool searchDatabaseListTool;
    private final SearchDocumentMasterTool searchDocumentMasterTool;
    private final SearchVectorDatabaseTool searchVectorDatabaseTool;
    private final KiprisKeywordSearchTool kiprisKeywordSearchTool;
    private final KiprisPatentAdminHistoryTool kiprisPatentAdminHistoryTool;
    private final KiprisTrademarkHistoryTool kiprisTrademarkHistoryTool;
    private final KiprisLegalStatusTool kiprisLegalStatusTool;
    private final KiprisExtensionDetailTool kiprisExtensionDetailTool;
    private final KiprisTrademarkFlashTool kiprisTrademarkFlashTool;
    private final KiprisTrademarkClassHistoryTool kiprisTrademarkClassHistoryTool;
    private final KiprisPatentStatusST27Tool kiprisPatentStatusST27Tool;
    private final KiprisDesignStatusST87Tool kiprisDesignStatusST87Tool;
    private final BusinessSpecialistSkill businessSpecialistSkill;
    private final ReasoningDateDifferenceTool reasoningDateDifferenceTool;
    private final ReasoningFutureDateTool reasoningFutureDateTool;
    private final ReasoningBoardConfigTool reasoningBoardConfigTool;
    private final SecurityOfficeAccessTool securityOfficeAccessTool;
    private final SecurityAdminCheckTool securityAdminCheckTool;
    private final SecurityPermissionSearchTool securityPermissionSearchTool;
    private final SearchFrontRouteTool searchFrontRouteTool;
    private final HttpRequestTool httpRequestTool;
    private final AiFeedbackTool aiFeedbackTool;
    private final SchemaFieldMappingService schemaFieldMappingService;

    /**
     * 의도(Intent)에 따라 필터링된 도구 목록을 반환합니다.
     */
    public Object[] getToolsForIntent(String intent) {
        List<Object> tools = new ArrayList<>();

        // 공통 필수 도구 (보안, 피드백 관련) - 모든 인텐트에 포함
        tools.add(securityAdminCheckTool);
        tools.add(securityOfficeAccessTool);
        tools.add(securityPermissionSearchTool);
        tools.add(aiFeedbackTool);

        switch (intent.toUpperCase()) {
            case "SEARCH" -> {
                tools.add(searchVectorDatabaseTool);
                tools.add(searchDatabaseListTool);
                tools.add(searchRealtimeDatabaseRecordTool);
                tools.add(searchSystemDictionaryTool);
                tools.add(searchDocumentMasterTool);
                tools.add(searchFrontRouteTool);
                tools.add(httpRequestTool);
                tools.add(schemaFieldMappingService); // [Mapping Tools Included]
            }
            case "BUSINESS" -> {
                tools.add(businessSpecialistSkill);
                tools.add(searchFrontRouteTool);
                tools.add(httpRequestTool);
            }
            case "KIPRIS" -> {
                tools.addAll(List.of(kiprisKeywordSearchTool, kiprisPatentAdminHistoryTool, kiprisTrademarkHistoryTool,
                        kiprisLegalStatusTool, kiprisExtensionDetailTool, kiprisTrademarkFlashTool,
                        kiprisTrademarkClassHistoryTool, kiprisPatentStatusST27Tool, kiprisDesignStatusST87Tool));
            }
            case "GENERAL" -> {
                tools.add(reasoningDateDifferenceTool);
                tools.add(reasoningFutureDateTool);
                tools.add(reasoningBoardConfigTool);
            }
            default -> {
                // 전체 로드 (안전 장치)
                return getAllTools();
            }
        }
        return tools.toArray();
    }

    public Object[] getAllTools() {
        return new Object[]{
                searchSystemDictionaryTool, searchRealtimeDatabaseRecordTool, searchDatabaseListTool,
                searchDocumentMasterTool, searchVectorDatabaseTool, kiprisKeywordSearchTool,
                kiprisPatentAdminHistoryTool, kiprisTrademarkHistoryTool, kiprisLegalStatusTool,
                kiprisExtensionDetailTool, kiprisTrademarkFlashTool, kiprisTrademarkClassHistoryTool,
                kiprisPatentStatusST27Tool, kiprisDesignStatusST87Tool, businessSpecialistSkill,
                reasoningDateDifferenceTool, reasoningFutureDateTool,
                reasoningBoardConfigTool, securityOfficeAccessTool, securityAdminCheckTool,
                securityPermissionSearchTool, searchFrontRouteTool, httpRequestTool,
                aiFeedbackTool, schemaFieldMappingService
        };
    }
}
