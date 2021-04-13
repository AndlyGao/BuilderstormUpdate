package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class RfiComment {
    private String name = "";
    private String lastname = "";
    private String id = "";
    private String rfi_id = "";
    private String comment_by = "";
    private String comment_by_email = "";
    private String comment = "";
    private String commented_on = "";
    private String is_previous_answer = "";
    private List<PojoAttachment> commentfiles = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRfi_id() {
        return rfi_id;
    }

    public void setRfi_id(String rfi_id) {
        this.rfi_id = rfi_id;
    }

    public String getComment_by() {
        return comment_by;
    }

    public void setComment_by(String comment_by) {
        this.comment_by = comment_by;
    }

    public String getComment_by_email() {
        return comment_by_email;
    }

    public void setComment_by_email(String comment_by_email) {
        this.comment_by_email = comment_by_email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommented_on() {
        return commented_on;
    }

    public void setCommented_on(String commented_on) {
        this.commented_on = commented_on;
    }

    public String getIs_previous_answer() {
        return is_previous_answer;
    }

    public void setIs_previous_answer(String is_previous_answer) {
        this.is_previous_answer = is_previous_answer;
    }

    public List<PojoAttachment> getCommentfiles() {
        return commentfiles;
    }

    public void setCommentfiles(List<PojoAttachment> commentfiles) {
        this.commentfiles = commentfiles;
    }

  /*  public class CommentAttachment {

        private String url;
        private String original_name;
        private String file_name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOriginal_name() {
            return original_name;
        }

        public void setOriginal_name(String original_name) {
            this.original_name = original_name;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }
    }*/
}
