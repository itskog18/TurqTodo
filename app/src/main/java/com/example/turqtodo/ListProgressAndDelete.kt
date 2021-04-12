package com.example.turqtodo

interface ListProgressAndDelete {
    fun onListDelete(listID: String)
    fun onListOpen(listID: String, listName: String, currentProgress: Int, maxProgress: Int)
}