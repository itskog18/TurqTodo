package com.example.turqtodo.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.turqtodo.R
import com.example.turqtodo.TaskUpdateAndDelete
import com.example.turqtodo.lists.data.TaskData
import kotlinx.android.synthetic.main.row_taskslayout.view.*

class TaskAdapter(context: Context, taskDataList: MutableList<TaskData>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val currentTask = taskDataList
    private val updateAndDelete: TaskUpdateAndDelete = context as TaskUpdateAndDelete

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val uniqueID: String = currentTask[position].taskId as String
        val taskName = currentTask[position].taskName as String?
        val isCompleted: Boolean = currentTask[position].isCompleted

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
        viewHolder.isCompleted.setOnClickListener {
            updateAndDelete.modifyTask(uniqueID, it?.checkBox?.isChecked!!, position)
        }
        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onTaskDelete(uniqueID, position)
        }
        return view
    }

    private class TaskViewHolder(row: View?) {
        val textLabel: TextView = row!!.findViewById(R.id.task_textView) as TextView
        val isCompleted: CheckBox = row!!.findViewById(R.id.checkBox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.removeTask) as ImageButton
    }

    override fun getItem(position: Int): Any {
        return currentTask[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return currentTask.size
    }
}