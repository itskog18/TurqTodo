package com.example.turqtodo.lists.data

data class TaskData (
    var taskName: String?,
    var taskId: String,
    var completed: Boolean
) {
    constructor(): this("","",false)
    companion object Factory {
        fun createTask(): TaskData = TaskData()
    }
}
