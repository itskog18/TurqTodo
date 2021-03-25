package com.example.turqtodo.lists.data

import com.example.turqtodo.lists.data.Task

class TodoList {
    val listOfTasks: List<Task> = listOf<Task>()
    var listName: String? = null
    var listId: String? = null
    private val currentProgress = listOf(Task())

    // fix the function under (amountOfTasksCompleted) at a later time
    //val amountOfTasksCompleted = currentProgress.sumBy{it.isCompleted}

    //needs add-task:func
    //needs del-task:func

    companion object Factory{
        fun createList(): TodoList = TodoList()
    }
}