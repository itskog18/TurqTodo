package com.example.turqtodo.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turqtodo.lists.data.TodoList
import com.example.turqtodo.databinding.TodolistLayoutBinding
import java.nio.file.Files.size


class TodoListCollectionAdapter(private val todoLists:MutableList<TodoList>, private val onTodoListClicked:(TodoList) -> Unit) : RecyclerView.Adapter<TodoListCollectionAdapter.ViewHolder>() {
    class ViewHolder(val binding:TodolistLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(todoList: TodoList) {
            //binding.listName.text = todoList.listName
            //binding.progressBar.progressBar = todoList.amountOfTasksCompleted
        }
    }

    override fun getItemCount(): Int = todoLists.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(TodolistLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoList = todoLists[position]
        holder.bind(todoList)
    }
}