package kr.co.mindpro.ipms.domain.priorresearch.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [Controller] 선행기술조사 API
 *
 * @author	 : min
 * @fileName : priorResearchController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "선행기술조사 API", description = "선행기술조사 CRUD API")
@RestController
@RequestMapping("/api/priorResearch")
@RequiredArgsConstructor
public class PriorResearchController {

    private final PaperService paperService;

}