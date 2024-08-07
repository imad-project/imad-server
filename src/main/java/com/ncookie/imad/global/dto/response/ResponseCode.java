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
    INVALID_CONTENT_TYPE(400, "잘못된 Content-Type 유형입니다."),
    RESOURCE_URL_GET_SUCCESS(200, "정상적으로 리소스의 URL을 조회했습니다."),
    FILE_MAX_UPLOAD_SIZE_EXCEEDED(400, "파일의 용량이 너무 큽니다."),

    // Spring Security
    UNAUTHORIZED_REQUEST(401, "잘못된 요청이거나 올바르지 않은 인증정보입니다."),
    INVALID_REQUEST(403, "잘못된 접근입니다."),

    TOKEN_EXPIRED(401, "토큰의 기한이 만료되었습니다."),
    TOKEN_INVALID(401, "유효하지 않은 토큰입니다."),
    TOKEN_REISSUE_SUCCESS(201, "정상적으로 토큰을 재발급했습니다"),

    // AWS
    LOADBALANCER_HEALTH_CHECK_SUCCESS(200, "AWS 로드밸런서 health 체크 성공"),

    // 유저
    SIGNUP_SUCCESS(201, "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(200, "정상적으로 로그인 되었습니다."),
    USER_INFO_GET_SUCCESS(200, "정상적으로 회원 정보를 조회했습니다."),
    USER_INFO_VALIDATION(200, "정상적으로 중복 검사를 수행했습니다."),
    USER_INFO_UPDATE_SUCCESS(200, "정상적으로 회원 정보를 수정했습니다."),
    USER_PROFILE_IMAGE_MODIFY_SUCCESS(200, "정상적으로 프로필 이미지를 수정했습니다."),
    USER_MODIFY_PASSWORD_SUCCESS(200, "정상적으로 비밀번호를 수정했습니다."),
    USER_DELETE_SUCCESS(204, "회원 탈퇴가 정상적으로 처리되었습니다."),

    LOGIN_FAILURE(400, "로그인 정보를 다시 확인해주세요."),
    OAUTH2_APPLE_TOKEN_INVALID(400, "유효하지 않은 토큰입니다."),
    OAUTH2_APPLE_ID_TOKEN_INVALID(400, "유효하지 않은 ID 토큰입니다."),
    SIGNUP_EMAIL_DUPLICATED(400, "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATED(400, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(404,  "해당 계정을 찾을 수 없습니다."),
    USER_MODIFY_PASSWORD_FAILURE(404,  "기존 비밀번호를 다시 확인해주세요."),

    // 작품
    CONTENTS_SEARCH_SUCCESS(200, "성공적으로 작품 검색을 완료했습니다."),
    CONTENTS_SEARCH_WRONG_TYPE(400, "잘못된 검색 타입입니다."),
    CONTENTS_PARSING_CERTIFICATION_ERROR(500, "데이터 파싱 중 문제가 발생했습니다."),

    CONTENTS_GET_DETAILS_SUCCESS(200, "작품 상세 정보 조회를 완료했습니다."),
    CONTENTS_GET_DETAILS_WRONG_TYPE(400, "잘못된 작품 타입입니다. (TV / MOVIE만 가능)"),
    CONTENTS_NOT_EXIST_TMDB_ID(404, "유효하지 않은 id이거나 TMDB API 서버에 문제가 발생했습니다."),
    CONTENTS_ID_NOT_FOUND(404, "DB에 등록되지 않았거나 유효하지 않은 작품 ID 입니다."),
    
    // 리뷰
    REVIEW_GET_DETAILS_SUCCESS(200, "정상적으로 리뷰 정보를 조회했습니다."),
    REVIEW_ADD_DETAILS_SUCCESS(201, "정상적으로 리뷰 정보를 등록했습니다."),
    REVIEW_MODIFY_DETAILS_SUCCESS(200, "정상적으로 리뷰 정보를 수정했습니다."),
    REVIEW_DELETE_DETAILS_SUCCESS(204, "정상적으로 리뷰 정보를 삭제했습니다."),
    REVIEW_GET_LIST_SUCCESS(200, "정상적으로 리뷰 정보 리스트를 조회했습니다."),

    REVIEW_ALREADY_REGISTERED(409, "한 작품에 대해 한 가지 리뷰만 작성할 수 있습니다."),
    REVIEW_NOT_FOUND(404, "해당 ID의 리뷰를 찾을 수 없습니다."),
    REVIEW_NO_PERMISSION(403, "해당 리뷰를 수정 또는 삭제할 권한이 없습니다. 로그인 정보를 다시 확인해주세요."),

    REVIEW_LIKE_STATUS_MODIFY_SUCCESS(200, "정상적으로 리뷰 좋아요/싫어요 상태를 수정했습니다."),
    REVIEW_LIKE_STATUS_MODIFY_FAILED(400, "리뷰 좋아요/싫어요 상태를 수정하는데 실패했습니다."),

    // 게시글
    POSTING_GET_DETAILS_SUCCESS(200, "정상적으로 게시글을 조회했습니다."),
    POSTING_GET_LIST_SUCCESS(200, "정상적으로 게시글 리스트를 조회했습니다."),
    POSTING_ADD_DETAILS_SUCCESS(201, "정상적으로 게시글을 추가했습니다."),
    POSTING_MODIFY_DETAILS_SUCCESS(200, "정상적으로 게시글을 수정했습니다."),
    POSTING_DELETE_DETAILS_SUCCESS(204, "정상적으로 게시글을 삭제했습니다."),
    POSTING_LIKE_STATUS_MODIFY_SUCCESS(200, "정상적으로 게시글 좋아요/싫어요 상태를 수정했습니다."),

    POSTING_NOT_FOUND(404, "해당 ID의 게시글을 찾을 수 없습니다."),
    POSTING_NO_PERMISSION(403, "해당 게시글을 수정 또는 삭제할 권한이 없습니다. 로그인 정보를 다시 확인해주세요."),
    POSTING_WRONG_SEARCH_TYPE(400, "옳바르지 않은 검색 타입입니다."),
    
    // 댓글
    COMMENT_GET_SUCCESS(200, "정상적으로 댓글을 조회했습니다."),
    COMMENT_GET_LIST_SUCCESS(200, "정상적으로 댓글 리스트를 조회했습니다."),
    COMMENT_ADD_SUCCESS(201, "정상적으로 댓글을 추가했습니다."),
    COMMENT_MODIFY_SUCCESS(200, "정상적으로 댓글을 수정했습니다."),
    COMMENT_DELETE_SUCCESS(200, "정상적으로 댓글을 삭제했습니다."),
    COMMENT_LIKE_STATUS_MODIFY_SUCCESS(200, "정상적으로 댓글 좋아요/싫어요 상태를 수정했습니다."),

    COMMENT_NO_PERMISSION(403, "해당 댓글을 수정 또는 삭제할 권한이 없습니다. 로그인 정보를 다시 확인해주세요."),
    COMMENT_NOT_FOUND(404, "해당 ID의 댓글을 찾을 수 없습니다."),
    COMMENT_ALREADY_REMOVED(410, "이미 삭제된 댓글입니다."),
    COMMENT_LIST_WRONG_TYPE(400, "옳바르지 않은 댓글 타입입니다."),

    // 좋아요/싫어요
    LIKE_STATUS_INVALID(404, "유효하지 않은 like status입니다. 다시 확인해주세요."),

    // 프로필
    PROFILE_GET_INFO_SUCCESS(200, "정상적으로 프로필 정보를 조회했습니다."),

    BOOKMARK_ADD_SUCCESS(201, "정상적으로 작품 북마크를 등록했습니다."),
    BOOKMARK_ALREADY_EXIST(400, "이미 등록된 작품 북마크입니다."),
    BOOKMARK_DELETE_SUCCESS(200, "정상적으로 작품 북마크를 삭제했습니다."),
    BOOKMARK_WRONG_ID(400, "해당 ID의 작품 북마크를 찾을 수 없습니다."),

    SCRAP_ADD_SUCCESS(201, "정상적으로 게시글 스크랩을 등록했습니다."),
    SCRAP_ALREADY_EXIST(400, "이미 등록된 게시글 스크랩입니다."),
    SCRAP_DELETE_SUCCESS(200, "정상적으로 게시글 스크랩을 삭제했습니다."),
    SCRAP_WRONG_ID(400, "해당 ID의 게시글 스크랩을 찾을 수 없습니다."),

    PROFILE_GET_WRITTEN_POSTING_LIST_SUCCESS(200, "정상적으로 작성한 게시글 리스트를 조회했습니다."),
    PROFILE_GET_LIKED_POSTING_LIST_SUCCESS(200, "정상적으로 좋아요/싫어요를 등록한 게시글 리스트를 조회했습니다."),
    PROFILE_GET_WRITTEN_REVIEW_LIST_SUCCESS(200, "정상적으로 작성한 리뷰 리스트를 조회했습니다."),
    PROFILE_GET_LIKED_REVIEW_LIST_SUCCESS(200, "정상적으로 좋아요/싫어요를 등록한 리뷰 리스트를 조회했습니다."),
    
    // 랭킹
    RANKING_GET_SUCCESS(200, "정상적으로 랭킹 정보를 조회했습니다."),
    RANKING_NOTICE_RANKING_UPDATE_TIME(200, "현재 랭킹 데이터 갱신 중입니다."),
    RANKING_GET_NO_DATA(404, "현재 랭킹 정보가 존재하지 않습니다."),
    RANKING_WRONG_CONTENTS_TYPE(400, "옳바르지 않은 ContentsType 입니다."),
    RANKING_WRONG_PERIOD(400, "옯바르지 않은 랭킹 기간입니다."),

    POPULAR_REVIEW_GET_SUCCESS(200, "정상적으로 오늘의 리뷰를 조회했습니다."),
    POPULAR_POSTING_GET_SUCCESS(200, "정상적으로 오늘의 게시글을 조회했습니다."),

    // 추천
    RECOMMEND_GET_SUCCESS(200, "정상적으로 작품 추천 데이터를 조회했습니다."),

    // 신고
    REPORT_REQUEST_SUCCESS(201, "정상적으로 신고 접수가 완료되었습니다."),

    REPORT_CANNOT_SELF_REPORT(400, "자신이 작성한 컨텐츠나 계정은 신고할 수 없습니다."),
    REPORT_NOT_FOUND_USER(404, "신고하려는 유저 계정이 존재하지 않습니다."),
    REPORT_NOT_FOUND_CONTENTS(404, "신고하려는 컨텐츠가 존재하지 않습니다."),
    REPORT_ALREADY_REPORTED(400, "이미 신고된 계정 또는 컨텐츠입니다."),

    // Utils
    WRONG_SORT_STRING(400, "옳바르지 않은 정렬 기준입니다.");

    private final String message;
    private final int status;

    ResponseCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

}
