package com.example.turqtodo.lists.data

class Task {
    var taskName: String? = null
    var taskId: String? = null
    val isCompleted: Boolean = false

    companion object Factory {
        fun createTask(): Task = Task()
    }
}
