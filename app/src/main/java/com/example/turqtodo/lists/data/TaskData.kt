package com.example.turqtodo.lists.data

class TaskData {
    var taskName: String? = null
    var taskId: String? = null
    var isCompleted: Boolean = false

    companion object Factory {
        fun createTask(): TaskData = TaskData()
    }
}
