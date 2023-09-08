package com.ncookie.imad.global.dto.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

    // Common
    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405,  " Invalid Input Value"),
    ENTITY_NOT_FOUND(400,  " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),

    // spring security 예외
    UNAUTHORIZED_REQUEST(401, "잘못된 요청이거나 올바르지 않은 인증정보입니다."),
    TOKEN_EXPIRED(401, "토큰의 기한이 만료되었습니다."),
    INVALID_REQUEST(403, "잘못된 접근입니다."),

    // 유저
    SIGNUP_SUCCESS(201, "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(200, "정상적으로 로그인 되었습니다."),
    USER_INFO_GET_SUCCESS(200, "정상적으로 회원 정보를 조회했습니다."),
    USER_INFO_VALIDATION(200, "정상적으로 중복 검사를 수행했습니다."),
    USER_INFO_UPDATE_SUCCESS(200, "정상적으로 회원 정보를 수정했습니다."),
    USER_MODIFY_PASSWORD_SUCCESS(200, "정상적으로 비밀번호를 수정했습니다."),
    USER_DELETE_SUCCESS(204, "회원 탈퇴가 정상적으로 처리되었습니다."),

    LOGIN_FAILURE(400, "로그인 정보를 다시 확인해주세요."),
    SIGNUP_EMAIL_DUPLICATED(400, "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATED(400, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(404,  "해당 계정을 찾을 수 없습니다."),
    USER_MODIFY_PASSWORD_FAILURE(404,  "기존 비밀번호를 다시 확인해주세요."),

    // 작품 관련
    CONTENTS_SEARCH_SUCCESS(200, "성공적으로 작품 검색을 완료했습니다."),
    CONTENTS_SEARCH_WRONG_TYPE(400, "잘못된 검색 타입입니다."),
    CONTENTS_PARSING_CERTIFICATION_ERROR(500, "데이터 파싱 중 문제가 발생했습니다."),

    CONTENTS_GET_DETAILS_SUCCESS(200, "작품 상세 정보 조회를 완료했습니다."),
    CONTENTS_GET_DETAILS_WRONG_TYPE(400, "잘못된 작품 타입입니다. (TV / MOVIE만 가능)"),
    CONTENTS_NOT_EXIST_TMDB_ID(404, "유효하지 않은 id이거나 TMDB API 서버에 문제가 발생했습니다."),
    CONTENTS_ID_NOT_FOUND(404, "DB에 등록되지 않았거나 유효하지 않은 작품 ID 입니다."),
    
    // 리뷰 관련
    REVIEW_GET_DETAILS_SUCCESS(200, "정상적으로 리뷰 정보를 조회했습니다."),
    REVIEW_POST_DETAILS_SUCCESS(201, "정상적으로 리뷰 정보를 등록했습니다."),
    REVIEW_PATCH_DETAILS_SUCCESS(200, "정상적으로 리뷰 정보를 수정했습니다."),
    REVIEW_DELETE_DETAILS_SUCCESS(204, "정상적으로 리뷰 정보를 삭제했습니다."),
    REVIEW_GET_LIST_SUCCESS(200, "정상적으로 리뷰 정보 리스트를 조회했습니다."),

    REVIEW_NOT_FOUND(404, "해당 ID의 리뷰를 찾을 수 없습니다."),
    REVIEW_MODIFY_NO_PERMISSION(403, "해당 리뷰를 수정할 권한이 없습니다. 로그인 정보를 다시 확인해주세요."),

    REVIEW_LIKE_STATUS_MODIFY_SUCCESS(200, "정상적으로 리뷰 좋아요/싫어요 상태를 수정했습니다."),
    REVIEW_LIKE_STATUS_MODIFY_FAILED(400, "리뷰 좋아요/싫어요 상태를 수정하는데 실패했습니다."),
    REVIEW_LIKE_STATUS_INVALID(404, "유효하지 않은 like status입니다. 다시 확인해주세요."),

    // 기타
    HANDLE_ACCESS_DENIED(403, "로그인이 필요합니다."),
    INVALID_INPUT_USERNAME(400, "닉네임을 3자 이상 입력하세요."),
    NOTEQUAL_INPUT_PASSWORD(400,  "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(400,  "비밀번호를 4자 이상 입력하세요."),
    INVALID_USERNAME(400,  "알파벳 대소문자와 숫자로만 입력하세요."),
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

}
