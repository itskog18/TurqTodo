package com.example.turqtodo.lists.data

data class TodoList (
    var listName: String,
    var listId: String,
    var maxProgress: Int,
    var currentProgress: Int
) {
    constructor(): this("","",0,0)
    companion object Factory {
        fun createList(): TodoList = TodoList()
    }
}