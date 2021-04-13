package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PojoToDo implements Parcelable {
    private Integer id;
    private Integer column_id;
    private Integer template_task;
    private Integer status;
    private String title = "";
    private String description = "";
    private String due_date = "";
    private String completed_on = "";
    private String added_on = "";
    private Integer project_id;
    private Integer category;
    private List<User> to_users = null;
    private List<User> cc_users = null;
    private String strToUsers = "";
    private String strCcUsers = "";
    private String category_title = "";
    private String project_title = "";
    private String addedBy = "";
    private Integer udays;
    private String due_tooltip = "";
    private String todo_status_color = "";
    private boolean isSelected = false;
    private List<Attachment> attachments = null;
    private boolean isSynced = false;
    private String imgae_Id = "";

    public PojoToDo() {
    }

    public Integer getTemplateTask() {
        return template_task;
    }

    public void setTemplateTask(Integer template_task) {
        this.template_task = template_task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return due_date;
    }

    public void setDueDate(String due_date) {
        this.due_date = due_date;
    }

    public String getCompletedOn() {
        return completed_on;
    }

    public void setCompletedOn(String completed_on) {
        this.completed_on = completed_on;
    }

    public String getAddedOn() {
        return added_on;
    }

    public void setAddedOn(String added_on) {
        this.added_on = added_on;
    }

    public Integer getProjectId() {
        return project_id;
    }

    public void setProjectId(Integer project_id) {
        this.project_id = project_id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<User> getToUsers() {
        return to_users;
    }

    public void setToUsers(List<User> to_users) {
        this.to_users = to_users;
    }

    public List<User> getCcUsers() {
        return cc_users;
    }

    public void setCcUsers(List<User> cc_users) {
        this.cc_users = cc_users;
    }

    public String getCategoryTitle() {
        return category_title;
    }

    public void setCategoryTitle(String category_title) {
        this.category_title = category_title;
    }

    public String getProjectTitle() {
        return project_title;
    }

    public void setProjectTitle(String project_title) {
        this.project_title = project_title;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Integer getUdays() {
        return udays;
    }

    public void setUdays(Integer udays) {
        this.udays = udays;
    }

    public String getDueToolTip() {
        return due_tooltip;
    }

    public void setDueToolTip(String due_tooltip) {
        this.due_tooltip = due_tooltip;
    }

    public String getTodoStatusColor() {
        return todo_status_color;
    }

    public void setTodoStatusColor(String todo_status_color) {
        this.todo_status_color = todo_status_color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrToUsers() {
        return strToUsers;
    }

    public void setStrToUsers(String strToUsers) {
        this.strToUsers = strToUsers;
    }

    public String getStrCcUsers() {
        return strCcUsers;
    }

    public void setStrCcUsers(String strCcUsers) {
        this.strCcUsers = strCcUsers;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImgae_Id() {
        return imgae_Id;
    }

    public void setImgae_Id(String imgae_Id) {
        this.imgae_Id = imgae_Id;
    }

    public Integer getColumn_id() {
        return column_id;
    }

    public void setColumn_id(Integer column_id) {
        this.column_id = column_id;
    }

    protected PojoToDo(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        column_id = in.readByte() == 0x00 ? null : in.readInt();
        template_task = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readByte() == 0x00 ? null : in.readInt();
        title = in.readString();
        description = in.readString();
        due_date = in.readString();
        completed_on = in.readString();
        added_on = in.readString();
        project_id = in.readByte() == 0x00 ? null : in.readInt();
        category = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            to_users = new ArrayList<User>();
            in.readList(to_users, User.class.getClassLoader());
        } else {
            to_users = null;
        }
        if (in.readByte() == 0x01) {
            cc_users = new ArrayList<User>();
            in.readList(cc_users, User.class.getClassLoader());
        } else {
            cc_users = null;
        }
        strToUsers = in.readString();
        strCcUsers = in.readString();
        category_title = in.readString();
        project_title = in.readString();
        addedBy = in.readString();
        udays = in.readByte() == 0x00 ? null : in.readInt();
        due_tooltip = in.readString();
        todo_status_color = in.readString();
        isSelected = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            attachments = new ArrayList<Attachment>();
            in.readList(attachments, Attachment.class.getClassLoader());
        } else {
            attachments = null;
        }
        isSynced = in.readByte() != 0x00;
        imgae_Id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (column_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(column_id);
        }
        if (template_task == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(template_task);
        }
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(due_date);
        dest.writeString(completed_on);
        dest.writeString(added_on);
        if (project_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(project_id);
        }
        if (category == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(category);
        }
        if (to_users == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(to_users);
        }
        if (cc_users == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cc_users);
        }
        dest.writeString(strToUsers);
        dest.writeString(strCcUsers);
        dest.writeString(category_title);
        dest.writeString(project_title);
        dest.writeString(addedBy);
        if (udays == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(udays);
        }
        dest.writeString(due_tooltip);
        dest.writeString(todo_status_color);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        if (attachments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(attachments);
        }
        dest.writeByte((byte) (isSynced ? 0x01 : 0x00));
        dest.writeString(imgae_Id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PojoToDo> CREATOR = new Parcelable.Creator<PojoToDo>() {
        @Override
        public PojoToDo createFromParcel(Parcel in) {
            return new PojoToDo(in);
        }

        @Override
        public PojoToDo[] newArray(int size) {
            return new PojoToDo[size];
        }
    };
}