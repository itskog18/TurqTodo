package com.example.turqtodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.turqtodo.lists.data.Task
import com.example.turqtodo.lists.data.TodoList
import com.example.turqtodo.lists.TodoListAdapter
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class ListHolder {
    companion object {
        var ClickedList:TodoList? = null
    }
}

class MainActivity : AppCompatActivity(), ListProgressAndDelete {

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
    lateinit var listAdapter: TodoListAdapter
    private var listViewItem: ListView? = null

    /*
    //might be for the sub_list fragment view.
    var taskList: MutableList<Task>? = null
    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)


        val addNewList = findViewById<View>(R.id.addNewListButton) as FloatingActionButton

        listViewItem = findViewById(R.id.listsOverview_listView) as ListView
        //binding.listsOverviewListView.layoutManager = LinearLayoutManager(this)
        //binding.listsOverviewListView.adapter as TodoListAdapter(todoList<TodoList>(), this::onListClicked)

        database = FirebaseDatabase.getInstance().reference
        addNewList.setOnClickListener {view ->
            val newListPopup = AlertDialog.Builder(this)
            val newListName = EditText(this)
            newListPopup.setMessage("Write a name for your new list")
            newListPopup.setTitle("Enter name of new list: ")
            newListPopup.setView(newListName)
            newListPopup.setPositiveButton("Add") {dialog, i ->
                val newListData = TodoList.createList()
                newListData.listName = newListName.text.toString()

                val listDataForDb = database.child("listsOverview").push()
                newListData.listId = listDataForDb.key
                listDataForDb.setValue(newListData)

                dialog.dismiss()
                Toast.makeText(this,"List saved!",Toast.LENGTH_SHORT).show()
            }
            newListPopup.show()
        }

        listsOverview_listView.setOnClickListener {view ->
            //onTodoListClicked()
        }

        toDoList = mutableListOf<TodoList>()
        listAdapter = TodoListAdapter(this, toDoList!!)
        listViewItem!!.adapter=listAdapter
        database.addValueEventListener(object : ValueEventListener {
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
                                    // Changed from: .getValue() to just: .value
                val map = currentList.value as HashMap<String, Any>

                listItemData.listId = currentList.key
                                        // Changed from: map.get["listName"] to just: map["listName"]
                listItemData.listName = map["listName"] as String?
                toDoList!!.add(listItemData)
            }
        }

        listAdapter.notifyDataSetChanged()

    }

    fun onTodoListClicked(todoList: TodoList):Unit {
        // show detailed list view, containing that lists tasks
        ListHolder.ClickedList = todoList
        val clickedListID = todoList.listId
        val intent = Intent(this, ListDetailsActivity::class.java)
        intent.putExtra("listClickedID", clickedListID)
        startActivity(intent)
    }

    override fun onListDelete(listID: String) {
        val listReference = database.child("listsOverview").child(listID)
        listReference.removeValue()
        listAdapter.notifyDataSetChanged()
    }

    override fun onListProgress(listID: String, currentProgress: Int) {

        // alt dette er feil,
            // add så den teller gjennom en liste probs via en get() eller noe for å få tak i lista med bare ID,
            // eller send inn lista over tasks,
            // men det hadde sikkert skapt trøbbel med at du da ikke kan sende currentProgress tilbake til riktig listeID like lett.
        // Men denne metoden burde funke for akkurat dette.

        val listToCount = listOf(2, 8, 5, 7, 9, 6, 10)
        val progress: (Int) -> Boolean = {it % 2 == 0}
        val result = listToCount.count(progress)
    }


}
