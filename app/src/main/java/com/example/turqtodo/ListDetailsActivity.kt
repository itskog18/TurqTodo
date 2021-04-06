package com.example.turqtodo

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.turqtodo.databinding.ActivityListDetailsBinding
import com.example.turqtodo.databinding.ActivityMainBinding
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.lists.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class TaskHolder {
    companion object {
        var ClickedTask:Task? = null
    }
}

class ListDetailsActivity : AppCompatActivity(), TaskUpdateAndDelete {

    private lateinit var binding: ActivityListDetailsBinding

    lateinit var database: DatabaseReference

    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityListDetailsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_list_details)


        val openListID = intent.getStringExtra("listClickedID")

        val addNewTask = findViewById<View>(R.id.addNewTaskButton) as FloatingActionButton

        taskViewItem = findViewById(R.id.tasksOverview_taskView) as ListView

        database = FirebaseDatabase.getInstance().reference
        addNewTask.setOnClickListener { view ->
            val newTaskPopup = AlertDialog.Builder(this)
            val newTaskName = EditText(this)
            newTaskPopup.setMessage("Write a name for your new task")
            newTaskPopup.setTitle("Enter name of new task: ")
            newTaskPopup.setView(newTaskName)
            newTaskPopup.setPositiveButton("Add") { dialog, i ->
                val newTaskData = Task.createTask()
                newTaskData.taskName = newTaskName.text.toString()

                val taskDataForDb = database.child("tasksOverview").push()
                newTaskData.taskId = taskDataForDb.key
                taskDataForDb.setValue(newTaskData)



                dialog.dismiss()
                Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()
            }
            newTaskPopup.show()
        }

        val dbReference = FirebaseDatabase.getInstance().getReference("listsOverview")
        val listReference = dbReference.child(openListID.toString())

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