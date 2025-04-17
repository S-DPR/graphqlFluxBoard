package com.example.graphqlfluxboard.common.validation;

public class DTOValidationMessage {
    public static final String NOT_BLANK_POST_ID = "postID는 필수입니다.";
    public static final String NOT_BLANK_COMMENT_ID = "commentID는 필수입니다.";
    public static final String NOT_BLANK_REPLY_ID = "replyID는 필수입니다.";
    public static final String NOT_BLANK_USER_ID = "userID는 필수입니다.";
    public static final String NOT_BLANK_PASSWORD = "비밀번호는 필수입니다.";

    public static final String COMMENT_SIZE_LIMIT = "댓글은 1자 이상 1000자 이하로 작성해야 합니다.";
    public static final String TITLE_SIZE_LIMIT = "제목은 1자 이상 100자 이하로 작성해야 합니다.";
    public static final String CONTENT_SIZE_LIMIT = "내용은 1자 이상 50000자 이하로 작성해야 합니다.";
    public static final String USERNAME_SIZE_LIMIT = "유저이름은 2자 이상 10자 이하로 작성해야 합니다.";
    public static final String PASSWORD_SIZE_LIMIT = "비밀번호는 4자 이상으로 작성해야 합니다.";
    public static final String PAGE_SIZE_LIMIT = "페이지는 1 이상으로 작성해야 합니다.";
    public static final String POST_SIZE_PER_PAGE_LIMIT = "페이지당 게시물 수는 1 이상으로 작성해야 합니다.";

    public static final String NOT_NULL_FILTER_TYPE = "필터 타입이 null일 수 없습니다.";
    public static final String NOT_NULL_KEYWORD = "키워드가 null일 수 없습니다.";

    public static final String POST_SORT_FIELD_ERROR = "정렬 필드가 적절하지 않습니다.";
    public static final String SORT_ORDER_ERROR = "정렬 순서가 적절하지 않습니다.";
}
