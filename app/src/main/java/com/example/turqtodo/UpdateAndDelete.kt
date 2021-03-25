package com.example.turqtodo

interface UpdateAndDelete {

    fun modifyList(listID: String)
    fun onListDelete(listID: String)

    fun modifyTask(taskID: String, isCompleted: Boolean)
    fun onTaskDelete(taskID: String)
}