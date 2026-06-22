package kr.co.mindpro.ipms.domain.retain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.domain.retain.service.RetainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : ParticipantController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "관계자 API", description = "관계자 CRUD API")
@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class RetainController {

    private final RetainService participantService;

}