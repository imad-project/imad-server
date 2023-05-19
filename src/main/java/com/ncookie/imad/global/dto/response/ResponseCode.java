package com.ncookie.imad.global.dto.response;

public enum ResponseCode {

    // Common
    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405,  " Invalid Input Value"),
    ENTITY_NOT_FOUND(400,  " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),

    // 유저
    SIGNUP_SUCCESS(201, "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(200, "정상적으로 로그인 되었습니다."),
    USER_INFO_GET_SUCCESS(200, "정상적으로 회원 정보를 조회했습니다."),
    USER_INFO_UPDATE_SUCCESS(201, "정상적으로 회원 정보를 수정했습니다"),
    USER_MODIFY_PASSWORD_SUCCESS(201, "정상적으로 비밀번호를 수정했습니다"),
    USER_DELETE_SUCCESS(204, "회원 탈퇴가 정상적으로 처리되었습니다."),

    LOGIN_FAILURE(400, "로그인 정보를 다시 확인해주세요."),
    SIGNUP_EMAIL_DUPLICATED(400, "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATED(400, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(404,  "해당 계정을 찾을 수 없습니다."),
    USER_MODIFY_PASSWORD_FAILURE(404,  "기존 비밀번호를 다시 확인해주세요."),


    HANDLE_ACCESS_DENIED(403, "로그인이 필요합니다."),
    INVALID_INPUT_USERNAME(400, "닉네임을 3자 이상 입력하세요"),
    NOTEQUAL_INPUT_PASSWORD(400,  "비밀번호가 일치하지 않습니다"),
    INVALID_PASSWORD(400,  "비밀번호를 4자 이상 입력하세요"),
    INVALID_USERNAME(400,  "알파벳 대소문자와 숫자로만 입력하세요"),
    NOT_AUTHORIZED(403, "작성자만 수정 및 삭제를 할 수 있습니다."),
    USERNAME_DUPLICATION(400, "이미 등록된 아이디입니다."),

    // 게시글
    NOTFOUND_POST(404, "해당 게시글이 존재하지 않습니다."),
    CONVERTING_FAILED(400, "파일 변환에 실패했습니다.");

    private final String message;
    private final int status;

    ResponseCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
    public int getStatus() {
        return status;
    }
    }
