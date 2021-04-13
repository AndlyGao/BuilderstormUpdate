package com.builderstrom.user.views.viewInterfaces;

import java.util.List;

public interface TaskReturnInterface {
    void updateTaskList(List<String> taskList);
    void cancelTaskList(List<String> strings);

}
