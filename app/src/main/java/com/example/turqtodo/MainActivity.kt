package com.example.turqtodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.turqtodo.lists.data.TodoList
import com.example.turqtodo.lists.TodoListAdapter
import com.example.turqtodo.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), ListProgressAndDelete {

    private lateinit var binding: ActivityMainBinding

    private lateinit var database: DatabaseReference
    val toDoList: MutableList<TodoList> = mutableListOf<TodoList>()
    lateinit var listAdapter: TodoListAdapter
    private var listViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addNewList = findViewById<View>(R.id.addNewListButton) as FloatingActionButton

        listViewItem = findViewById<View>(R.id.listsOverview_listView) as ListView
        database = FirebaseDatabase.getInstance().reference
        listAdapter = TodoListAdapter(this, toDoList)
        listViewItem!!.adapter = listAdapter

        addNewList.setOnClickListener {view ->
            val newListPopup = AlertDialog.Builder(this)
            val newListName = EditText(this)
            newListPopup.setTitle("Enter name of new list: ")
            newListPopup.setMessage("Write a name for your new list")
            newListPopup.setView(newListName)
            newListPopup.setPositiveButton("Add") {dialog, i ->
                val newListData = TodoList.createList()
                newListData.listName = newListName.text.toString()

                val listDataForDb = database.child("listsOverview").push()
                newListData.listId = listDataForDb.key.toString()
                listDataForDb.setValue(newListData)

                dialog.dismiss()
                Toast.makeText(this,"List saved!",Toast.LENGTH_SHORT).show()
            }
            newListPopup.show()
        }

    }

    private fun addListToOverview(snapshot: DataSnapshot) {
        val lists = snapshot.children.iterator()

        if(lists.hasNext()) {
            val todoListIndexedValue = lists.next()
            val listsIterator = todoListIndexedValue.children.iterator()

            while(listsIterator.hasNext()) {
                val currentList = listsIterator.next()
                val listItemData = TodoList.createList()
                val map = currentList.value as HashMap<String, Any>

                listItemData.listId = currentList.key.toString()
                listItemData.listName = map["listName"] as String
                listItemData.maxProgress = map["maxProgress"].toString().toInt()
                listItemData.currentProgress = map["currentProgress"].toString().toInt()

                toDoList.add(listItemData)
            }
        }
        listAdapter.notifyDataSetChanged()
    }

    override fun onListDelete(listID: String) {
        val listReference = database.child("listsOverview").child(listID)
        listReference.removeValue()
        listAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"List deleted!", Toast.LENGTH_SHORT).show()
    }

    override fun onListOpen(listID: String, listName: String, maxProgress: Int, currentProgress: Int) {
        val intent = Intent(this, ListDetailsActivity::class.java)
        intent.putExtra("listClickedID", listID)
        intent.putExtra("listClickedName", listName)
        intent.putExtra("listClickedMaxProgress", maxProgress)
        intent.putExtra("listClickedCurrentProgress", currentProgress)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        listViewItem = findViewById<View>(R.id.listsOverview_listView) as ListView
        database = FirebaseDatabase.getInstance().reference
        listAdapter = TodoListAdapter(this, toDoList)
        listViewItem!!.adapter = listAdapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"There was no list added", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                toDoList.clear()
                addListToOverview(snapshot)
            }

        })
    }

}
