package com.example.turqtodo

interface ListProgressAndDelete {
    fun onListDelete(listID: String)
    fun onListProgress(listID: String, currentProgress: Int)
    fun onListOpen(listID: String)
}