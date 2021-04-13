package com.builderstrom.user.data.retrofit.modals;

public class ToDoType {
    private String name = "";
    private Integer total;

    public ToDoType(String name) {
        this.name = name;
        this.total = 0;
    }

    public ToDoType(String name, Integer total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public Integer getTotal() {
        return total;
    }
}
