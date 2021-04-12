package com.example.turqtodo.lists.data

class TodoList {
    var listName: String? = null
    var listId: String? = null
    var currentProgress: Int = 0
    var maxProgress: Int = 0

    // fix the function under (amountOfTasksCompleted) at a later time
    //val amountOfTasksCompleted = currentProgress.sumBy{it.isCompleted}

    companion object Factory {
        fun createList(): TodoList = TodoList()
    }
}