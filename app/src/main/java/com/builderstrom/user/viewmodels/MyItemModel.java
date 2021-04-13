package com.builderstrom.user.viewmodels;

public class MyItemModel {
    private String users;
    private String docId;
    private String issueId;
    private String refId;
    private String completedFor;
    private int isCompleted;
    private String projectId;


    public MyItemModel(String users, String docId, String issueId, String refId, String completedFor,
                       int isCompleted, String projectId) {
        this.users = users;
        this.docId = docId;
        this.issueId = issueId;
        this.refId = refId;
        this.completedFor = completedFor;
        this.isCompleted = isCompleted;
        this.projectId = projectId;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCompletedFor() {
        return completedFor;
    }

    public void setCompletedFor(String completedFor) {
        this.completedFor = completedFor;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
