package com.example.turqtodo.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.turqtodo.*
import com.example.turqtodo.lists.data.TodoList

class TodoListAdapter(context: Context, todoList: MutableList<TodoList>) : BaseAdapter() {

    private val inflater:LayoutInflater = LayoutInflater.from(context)
    private val currentTodoList = todoList
    private val progressAndDelete: ListProgressAndDelete = context as ListProgressAndDelete

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val uniqueID: String = currentTodoList[position].listId
        val listName = currentTodoList[position].listName
        val maxProgress = currentTodoList.elementAt(position).maxProgress
        val currentProgress = currentTodoList[position].currentProgress

        val view: View
        val viewHolder: ListViewHolder

        if(convertView == null) {
            view=inflater.inflate(R.layout.row_listslayout, parent, false)
            viewHolder=ListViewHolder(view)
            view.tag=viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.textLabel.text = listName
        viewHolder.progressBar.max = maxProgress
        viewHolder.progressBar.progress = currentProgress

        view.setOnClickListener {
        }
        viewHolder.isDeleted.setOnClickListener {
            progressAndDelete.onListDelete(uniqueID)
        }
        viewHolder.toOpen.setOnClickListener {
            progressAndDelete.onListOpen(uniqueID, listName, maxProgress, currentProgress)
        }

        return view
    }

    private class ListViewHolder(row: View?) {
        val textLabel: TextView=row!!.findViewById(R.id.list_textView) as TextView
        val progressBar: ProgressBar = row!!.findViewById(R.id.listProgressBar) as ProgressBar
        val isDeleted: ImageButton = row!!.findViewById(R.id.removeList) as ImageButton
        val toOpen: Button = row!!.findViewById(R.id.openList) as Button
    }

    override fun getItem(position: Int): Any {
        return currentTodoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return currentTodoList.count()
    }

}