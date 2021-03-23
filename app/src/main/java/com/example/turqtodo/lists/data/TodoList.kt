package com.example.turqtodo.lists.data

import com.example.turqtodo.lists.list.task

class TodoList {
    private var placeholderListName = "placeholderListName"
    val listOfTasks: List<task> = listOf<task>()
    val listName: String = placeholderListName
    val listId: Int = 0
    val currentProgress = listOf(task())
    val amountOfTasksCompleted = currentProgress.sumBy{it.isCompleted}
    //needs add-task:func
    //needs del-task:func
}