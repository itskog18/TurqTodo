package com.example.turqtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}

class todoList {
    //needs list-of-tasks:listOf(task)
    val title: String = "placeholder"
    //needs progress-bar:booleanTracker
    //needs add-task:func
    //needs del-task:func
}

class task {
    //needs name:string
    //needs ifCompleted:boolean(checkbox)
}