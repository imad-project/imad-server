package com.ncookie.imad.domain.posting.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDetailsResponse {
    private Long commentId;

    // 유저 정보
    private Long userId;                    // 유저 ID
    private String userNickname;            // 닉네임
    private int userProfileImage;           // 프로필 이미지

    // 댓글 정보
    private Long parentId;                  // 댓글 부모 ID. 이 댓글이 최상위라면 null 값이 들어감
    private String content;                 // 댓글 내용
    /**
     * 댓글 삭제 여부
     * 해당 필드가 true이면 이 댓글은 삭제된 데이터이므로 content는 null이고 클라이언트에서 삭제되었음이 표시되어야 함
     * Ex) "이 댓글은 삭제되었습니다."
     */
    private boolean isRemoved;

    private int likeStatus;                 // 1이면 좋아요, -1이면 싫어요, 0이면 아무 상태도 아님

    private LocalDateTime createdAt;        // 댓글 작성 날짜
    private LocalDateTime modifiedAt;       // 댓글 수정 날짜


    public static CommentDetailsResponse toDTO(Comment comment) {
        UserAccount user = comment.getUserAccount();
        Long parentId = comment.getParent() != null ? comment.getParent().getCommentId() : null;

        return CommentDetailsResponse.builder()
                .commentId(comment.getCommentId())

                .userId(user.getId())
                .userNickname(user.getNickname())
                .userProfileImage(user.getProfileImage())

                .parentId(parentId)
                .content(comment.getContent())
                .isRemoved(comment.isRemoved())

                .createdAt(comment.getCreatedDate())
                .modifiedAt(comment.getModifiedDate())

                .build();
    }
}
