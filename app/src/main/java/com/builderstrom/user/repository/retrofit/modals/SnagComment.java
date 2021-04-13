package com.builderstrom.user.repository.retrofit.modals;

public class SnagComment {
    private String id;
    private String defect_id;
    private String user_id;
    private String comment;
    private String comment_date;
    private String comment_by;
    private String commentor_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefect_id() {
        return defect_id;
    }

    public void setDefect_id(String defect_id) {
        this.defect_id = defect_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_by() {
        return comment_by;
    }

    public void setComment_by(String comment_by) {
        this.comment_by = comment_by;
    }

    public String getCommentor_id() {
        return commentor_id;
    }

    public void setCommentor_id(String commentor_id) {
        this.commentor_id = commentor_id;
    }
}
