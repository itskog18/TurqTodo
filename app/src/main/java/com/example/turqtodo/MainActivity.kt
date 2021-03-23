package com.example.turqtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turqtodo.lists.list.Task
import com.example.turqtodo.lists.data.TodoList
import com.example.turqtodo.lists.TodoListCollectionAdapter
import com.example.turqtodo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var todoListCollection:MutableList<TodoList> = mutableListOf(
            TodoList()
    )

    private var taskCollection:MutableList<Task> = mutableListOf(
            Task("dishes", 0, 0),
            Task("vacuum",0,0),
            Task("trash",0,0)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.todoListListing.layoutManager = LinearLayoutManager(this)
        binding.todoListListing.adapter = TodoListCollectionAdapter(todoListCollection, this::onTodoListclicked)
    }

    private fun onTodoListclicked(todoList: TodoList):Unit {
        // show detailed list view, containing that lists tasks
    }


}
