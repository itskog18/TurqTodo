package com.example.turqtodo

interface TaskUpdateAndDelete {
    fun modifyTask(taskID: String, isCompleted: Boolean)
    fun onTaskDelete(taskID: String)
}