package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class PojoToDoListResp extends BaseApiModel {

    private ToDoRespModel data = null;

    public ToDoRespModel getData() {
        return data;
    }


    public class ToDoRespModel {

        private List<PojoToDo> todos = null;
        private List<ToDoType> todosTotals = null;

        public List<PojoToDo> getToDos() {
            return todos;
        }

        public List<ToDoType> getToDoTotals() {
            return todosTotals;
        }
    }
}
