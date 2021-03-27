package com.example.turqtodo

interface UpdateAndDelete {

    fun onListDelete(listID: String)

    fun modifyTask(taskID: String, isCompleted: Boolean)
    fun onTaskDelete(taskID: String)
}