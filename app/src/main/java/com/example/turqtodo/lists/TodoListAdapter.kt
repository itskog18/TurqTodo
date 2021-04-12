package com.example.turqtodo.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.turqtodo.*
import com.example.turqtodo.lists.data.TodoList

/*
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
*/

class TodoListAdapter(context: Context, todoList: MutableList<TodoList>) : BaseAdapter() {

    private val inflater:LayoutInflater = LayoutInflater.from(context)
    private val currentTodoList = todoList
    private val progressAndDelete: ListProgressAndDelete = context as ListProgressAndDelete

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val uniqueID: String = currentTodoList[position].listId as String
        val listName = currentTodoList[position].listName as String
        val currentProgress = currentTodoList[position].currentProgress as Int
        val maxProgress = currentTodoList[position].maxProgress as Int

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
        view.setOnClickListener {
            //MainActivity.onTodoListClicked(it)
        }
        viewHolder.isDeleted.setOnClickListener {
            progressAndDelete.onListDelete(uniqueID)
        }
        viewHolder.toOpen.setOnClickListener {
            progressAndDelete.onListOpen(uniqueID, listName, currentProgress, maxProgress)
        }

        return view
    }

    private class ListViewHolder(row: View?) {
        val textLabel: TextView=row!!.findViewById(R.id.list_textView) as TextView
        //val currentProgress: ProgressBar = row!!.findViewById(R.id.progressBar) as ProgressBar
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