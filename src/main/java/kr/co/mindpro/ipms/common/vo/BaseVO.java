package kr.co.mindpro.ipms.common.vo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 모든 VO에서 공통으로 사용하는 컬럼 정의
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO {
    private String createUser;          	// 등록자
    private OffsetDateTime createAt; 		// 등록 일시
    private String updateUser;          	// 수정자
    private OffsetDateTime updateAt; 		// 수정 일시
    private String delYn;
    private String note;
}