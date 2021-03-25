package com.example.turqtodo.lists

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.turqtodo.R
import com.example.turqtodo.lists.data.Task

class TaskAdapter(context: Context, Task: MutableList<Task>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val currentTask = Task

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val uniqueID: String = currentTask.get(position).taskId as String
        val taskName = currentTask.get(position).taskName as String
        val isCompleted: Boolean = currentTask.get(position).isCompleted as Boolean

        val view: View
        val viewHolder: TaskViewHolder

        if(convertView == null) {
            view=inflater.inflate(R.layout.row_taskslayout, parent, false)
            viewHolder = TaskViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as TaskViewHolder
        }

        viewHolder.textLabel.text = taskName
        viewHolder.isCompleted.isChecked = isCompleted

        return view
    }

    private class TaskViewHolder(row: View?) {
        val textLabel: TextView = row!!.findViewById(R.id.task_textView) as TextView
        val isCompleted: CheckBox = row!!.findViewById(R.id.checkBox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.removeTask) as ImageButton
    }

    override fun getItem(position: Int): Any {
        return currentTask.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return currentTask.size
    }
}