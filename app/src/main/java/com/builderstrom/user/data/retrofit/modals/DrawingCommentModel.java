package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrawingCommentModel {


    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Comment> comments = null;

    public Boolean getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Boolean responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public class Comment {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("drawing_id")
        @Expose
        private String drawingId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("comment_date")
        @Expose
        private String commentDate;
        @SerializedName("commentor_name")
        @Expose
        private String commentorName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDrawingId() {
            return drawingId;
        }


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommentDate() {
            return commentDate;
        }


        public String getCommentorName() {
            return commentorName;
        }

    }
}
