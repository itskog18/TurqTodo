package com.example.turqtodo.lists.data

class TodoList {
    val listOfTaskData: List<TaskData> = listOf<TaskData>()
    var listName: String? = null
    var listId: String? = null
    var currentProgress: Int = 0

    // fix the function under (amountOfTasksCompleted) at a later time
    //val amountOfTasksCompleted = currentProgress.sumBy{it.isCompleted}

    companion object Factory {
        fun createList(): TodoList = TodoList()
    }
}