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
    _GOODS_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "GOODS_004", "상품의 이름이 50자를 초과합니다."),
    _COMPLETED_GOODS(HttpStatus.BAD_REQUEST, "GOODS_005", "응모가 종료된 상품입니다."),

    //file 관련
    _FILE_INPUT_DISABLED(HttpStatus.BAD_REQUEST, "FILE_001", "파일 추가가 불가능합니다."),
    _INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "FILE_002", "파일 형식이 올바르지 않습니다."),
    _INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "FILE_003", "기간이 올바르지 않습니다."),
    _INVALID_PRICE(HttpStatus.BAD_REQUEST, "FILE_004", "정가와 할인가가 올바르지 않습니다."),

    //raffle 관련
    _RAFFLES_NOT_FOUND(HttpStatus.NOT_FOUND, "RAFFLES_001", "존재하지 않는 응모입니다."),
    _RAFFLES_NOT_COMPLETED(HttpStatus.BAD_GATEWAY, "RAFFLES_002", "종료되지 않은 응모입니다."),
    _RAFFLES_POINT_TOO_MUCH(HttpStatus.BAD_REQUEST, "RAFFLES_003", "응모하기에 포인트가 너무 많습니다."),
    _RAFFLES_POINT_TOO_LESS(HttpStatus.BAD_REQUEST, "RAFFLES_004", "포인트는 음수가 될 수 없습니다."),
    _RAFFLES_CANNOT_APPLY(HttpStatus.BAD_REQUEST, "RAFFLES_005", "응모 가능한 상태가 아닙니다."),

    //member 관련
    _MEMBERS_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBERS_001","존재하지 않는 사원입니다."),
    _ALREADY_EXIST_MEMBER(HttpStatus.BAD_REQUEST,"MEMBERS_002","이미 존재하는 사원입니다."),
    _MEMBER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "MEMBER_003", "비밀번호가 일치하지 않습니다."),
    _PASSWORD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"MEMBERS_004","기존 비밀번호와 동일합니다."),
    _PASSWORD_DISABLED(HttpStatus.BAD_REQUEST,"MEMBERS_005","비밀번호 유효성 검사 실패"),
    _ACCESS_DENIED_FOR_MEMBER(HttpStatus.BAD_REQUEST,"MEMBERS_006","사원이 접근할 수 없는 요청입니다."),
  
    //auth 관련
    _FILE_OUTPUT_DISABLED(HttpStatus.INTERNAL_SERVER_ERROR, "DRAWS_001", "파일 출력이 불가능합니다."),
    _NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "DRAWS_002", "Hash Algorithm 을 사용할 수 없습니다."),
    _CONFIRM_REQUIRE_WINNER(HttpStatus.BAD_REQUEST, "DRAWS_003", "확정은 당첨자에 한해서만 변경 가능합니다."),

    //wishlist 관련
    _WISHLIST_NOT_FOUND(HttpStatus.NOT_FOUND,"WISHLIST_001","존재하지 않는 위시리스트입니다."),
    _WISHLIST_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"WISHLIST_002","이미 존재하는 위시리스트입니다."),

    //auth 관련
    _INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_001", "유효하지 않은 토큰입니다.");



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
