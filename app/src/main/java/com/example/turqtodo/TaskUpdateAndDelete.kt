package com.example.turqtodo

interface TaskUpdateAndDelete {
    fun modifyTask(taskID: String, isCompleted: Boolean, position: Int)
    fun onTaskDelete(taskID: String, position: Int)
}