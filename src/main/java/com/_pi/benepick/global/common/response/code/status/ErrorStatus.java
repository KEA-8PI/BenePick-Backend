package com._pi.benepick.global.common.response.code.status;


import com._pi.benepick.global.common.response.code.BaseErrorCode;
import com._pi.benepick.global.common.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * 에러 응답 코드
 * 도메인별로 에러 코드와 원인을 추가해주세요
 */
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    //goods 및 category 관련
    _GOODS_NOT_FOUND(HttpStatus.BAD_REQUEST, "GOODS_001", "존재하지 않는 상품입니다."),
    _GOODS_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "GOODS_002", "카테고리를 가지고 있는 않는 상품입니다.."),
    _CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "GOODS_003", "존재하지 않는 카테고리입니다."),
    _FILE_INPUT_DISABLED(HttpStatus.BAD_REQUEST, "GOODS_004", "파일 추가가 불가능합니다."),
    _RAFFLES_NOT_FOUND(HttpStatus.NOT_FOUND, "RAFFLES_001", "존재하지 않는 응모입니다."),
    _RAFFLES_NOT_COMPLETED(HttpStatus.BAD_GATEWAY, "RAFFLES_002", "종료되지 않은 응모입니다."),



    //member 관련
    _MEMBERS_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBERS_001","존재하지 않는 사원입니다."),
    _ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST,"MEMBERS_002","이미 존재하는 사원입니다."),
    _MEMBER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "MEMBER_003", "비밀번호가 일치하지 않습니다."),
    _PASSWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"MEMBERS_004","기존 비밀번호와 동일합니다."),
    _PASSWORD_DISABLED(HttpStatus.BAD_REQUEST,"MEMBERS_005","비밀번호 유효성 검사 실패"),

    //wishlist 관련
    _WISHLIST_NOT_FOUND(HttpStatus.NOT_FOUND,"WISHLIST_001","존재하지 않는 위시리스트입니다."),

  
    //auth 관련
    _FILE_OUTPUT_DISABLED(HttpStatus.INTERNAL_SERVER_ERROR, "DRAWS_001", "파일 출력이 불가능합니다."),
    _NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "DRAWS_002", "Hash Algorithm 을 사용할 수 없습니다.");








    private final HttpStatus httpStatus;        // HTTP 상태 코드
    private final String code;                  // 내부적인 에러 코드. 도메인명 + 숫자로 구성. 숫자는 HTTP 상태코드 100의자리 참고
    private final String message;               // FE에 전달할 오류 메세지

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}
