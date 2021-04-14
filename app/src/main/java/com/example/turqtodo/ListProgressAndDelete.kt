package com.example.turqtodo

interface ListProgressAndDelete {
    fun onListDelete(listID: String)
    fun onListOpen(listID: String, listName: String, maxProgress: Int, currentProgress: Int)
}