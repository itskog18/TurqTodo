package com.example.turqtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turqtodo.lists.data.Task
import com.example.turqtodo.lists.data.TodoList
import com.example.turqtodo.lists.TodoListCollectionAdapter
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), UpdateAndDelete {

    private lateinit var binding: ActivityMainBinding

    private var todoListCollection:MutableList<TodoList> = mutableListOf(
            TodoList()
    )

    /*
    private var taskCollection:MutableList<Task> = mutableListOf(
            Task("dishes", 0, 0),
            Task("vacuum",0,0),
            Task("trash",0,0)
    )
    */

    lateinit var database: DatabaseReference

    var toDoList: MutableList<TodoList>? = null
    lateinit var listAdapter: TodoListCollectionAdapter
    private var listViewItem: ListView? = null

    //might be for the sub_list fragment view.
    var taskList: MutableList<Task>? = null
    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        /*
        binding.todoListListing.layoutManager = LinearLayoutManager(this)
        binding.todoListListing.adapter = TodoListCollectionAdapter(todoListCollection, this::onTodoListClicked)
        */

        val addNewList = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton

        listViewItem = findViewById(R.id.allListView) as ListView

        database = FirebaseDatabase.getInstance().reference
        addNewList.setOnClickListener{view ->
            val newListPopup = AlertDialog.Builder(this)
            val newListName = EditText(this)
            newListPopup.setMessage("Write a name for your new list")
            newListPopup.setTitle("Enter name of new list: ")
            newListPopup.setView(newListName)
            newListPopup.setPositiveButton("Add"){dialog, i ->
                val newListData = TodoList.createList()
                newListData.listName = newListName.text.toString()

                val listDataForDb = database.child("list").push()
                newListData.listId = listDataForDb.key
                listDataForDb.setValue(newListData)

                dialog.dismiss()
                Toast.makeText(this,"List saved!",Toast.LENGTH_SHORT).show()
            }
            newListPopup.show()
        }

        toDoList = mutableListOf<TodoList>()
        listAdapter = TodoListCollectionAdapter(this, toDoList!!)
        listViewItem!!.adapter=listAdapter
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"There was no list added", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                toDoList!!.clear()
                addListToOverview(snapshot)
            }

        })

    }

    private fun addListToOverview(snapshot: DataSnapshot) {
        val lists=snapshot.children.iterator()

        if(lists.hasNext()) {
            val todoListIndexedValue = lists.next()
            val listsIterator = todoListIndexedValue.children.iterator()

            while(listsIterator.hasNext()) {
                val currentList = listsIterator.next()
                val listItemData = TodoList.createList()
                val map = currentList.getValue() as HashMap<String, Any>

                listItemData.listId = currentList.key
                listItemData.listName = map.get("listName") as String?
                toDoList!!.add(listItemData)
            }
        }

        listAdapter.notifyDataSetChanged()

    }

    private fun onTodoListClicked(todoList: TodoList):Unit {
        // show detailed list view, containing that lists tasks
    }

    override fun modifyList(listID: String) {
        val listReference = database.child("list").child(listID)
    }

    override fun onListDelete(listID: String) {
        val listReference = database.child("list").child(listID)
        listReference.removeValue()
        listAdapter.notifyDataSetChanged()
    }

    override fun modifyTask(taskID: String, isCompleted: Boolean) {
        val taskReference = database.child("task").child(taskID)
        taskReference.child("isCompleted").setValue(isCompleted)
    }

    override fun onTaskDelete(taskID: String) {
        val taskReference = database.child("task").child(taskID)
        taskReference.removeValue()
        taskAdapter.notifyDataSetChanged()
    }


}
