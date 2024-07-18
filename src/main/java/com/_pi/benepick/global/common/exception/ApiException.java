package com._pi.benepick.global.common.exception;

import com._pi.benepick.global.common.response.code.ErrorReasonDTO;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;

public class ApiException extends RuntimeException{
    private final ErrorStatus errorStatus;

    public ApiException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.errorStatus.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.errorStatus.getReasonHttpStatus();
    }

}
