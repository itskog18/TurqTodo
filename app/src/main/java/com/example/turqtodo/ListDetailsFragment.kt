package com.example.turqtodo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.lists.TodoListAdapter
import com.example.turqtodo.lists.data.Task
import com.example.turqtodo.lists.data.TodoList

/*
class ListDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context != null) {
            val listResources = context.resources
            // add the different resources, maybe, like taskName, isDone.

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.todolist_layout, container, false)
        val listActivity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksOverview_taskView)
        taskList = mutableListOf<Task>()
        taskAdapter = TaskAdapter(listActivity, taskList!!)
        taskViewItem!!.adapter=taskAdapter
        recyclerView.layoutManager = GridLayoutManager(listActivity, 2)
        recyclerView.adapter = taskAdapter(listActivity)

        return view
    }

    var taskList: MutableList<Task>? = null
    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null



}
*/