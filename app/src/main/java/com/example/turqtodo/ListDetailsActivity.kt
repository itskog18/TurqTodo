package com.example.turqtodo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.turqtodo.databinding.ActivityListDetailsBinding
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.lists.data.TaskData
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_details.*

class ListDetailsActivity : AppCompatActivity(), TaskUpdateAndDelete {

    private lateinit var binding: ActivityListDetailsBinding

    private lateinit var database: DatabaseReference
    private val task: MutableList<TaskData> = mutableListOf<TaskData>()
    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null
    private var openListID:String? = ""
    private var tasksPath:String = ""
    private var listName:String = ""
    private var maxListProgress:Int = 0
    private var currentListProgress:Int = 0
    private var maxProgressPath:String = ""
    private var currentProgressPath:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addNewTask = findViewById<View>(R.id.addNewTaskButton) as FloatingActionButton

        database = FirebaseDatabase.getInstance().reference
        addNewTask.setOnClickListener { view ->
            val newTaskPopup = AlertDialog.Builder(this)
            val newTaskName = EditText(this)
            newTaskPopup.setTitle("Enter name of new task: ")
            newTaskPopup.setMessage("Write a name for your new task")
            newTaskPopup.setView(newTaskName)
            newTaskPopup.setPositiveButton("Add") { dialog, i ->
                val newTaskData = TaskData.createTask()
                newTaskData.taskName = newTaskName.text.toString()

                val taskDataForDb = database.child(tasksPath).push()
                newTaskData.taskId = taskDataForDb.key.toString()
                task.add(newTaskData)
                taskDataForDb.setValue(newTaskData)

                dialog.dismiss()
                Toast.makeText(this,"Task saved!", Toast.LENGTH_SHORT).show()
            }
            newTaskPopup.show()
            taskAdapter.notifyDataSetChanged()
        }

    }

        private fun addTaskToOverview() : Task<DataSnapshot> {
            return database.child(tasksPath).get().addOnSuccessListener { it ->
                if (it.exists()) {
                    task.clear()
                }

                val tasks = it.children.iterator()

                if(tasks.hasNext()) {
                    val tasksIterator = tasks.iterator()

                    while(tasksIterator.hasNext()) {
                        val currentTask = tasksIterator.next()
                        val taskItemData = TaskData.createTask()

                        taskItemData.taskId = currentTask.key.toString()
                        taskItemData.taskName = currentTask.child("taskName").value as String?
                        taskItemData.completed = currentTask.child("completed").value as Boolean

                        task.add(taskItemData)
                    }
                }
            }
        }

        override fun modifyTask(taskID: String, isCompleted: Boolean, position: Int) {
            val taskReference = database.child(tasksPath).child(taskID)
            taskReference.child("completed").setValue(isCompleted)
            task[position].completed = isCompleted
            taskProgressBar.max = task.count()
            database.child(maxProgressPath).setValue(taskProgressBar.max)
            taskProgressBar.progress = getProgressAmount().count()
            database.child(currentProgressPath).setValue(taskProgressBar.progress)
            taskAdapter.notifyDataSetChanged()
        }

        override fun onTaskDelete(taskID: String, position: Int) {
            val taskReference = database.child(tasksPath).child(taskID)
            taskReference.removeValue()
            task.removeAt(position)
            taskAdapter.notifyDataSetChanged()
            Toast.makeText(applicationContext,"Task deleted!", Toast.LENGTH_SHORT).show()
        }

        private fun getProgressAmount(): List<TaskData> {
            return task.filter {
                it.completed
            }
        }

    override fun onResume() {
        super.onResume()

        val listTitle = findViewById<View>(R.id.listTitle) as TextView
        taskViewItem = findViewById<ListView>(R.id.tasksOverview_taskView)

        openListID = intent.getStringExtra("listClickedID")
        tasksPath = "listsOverview/$openListID/listOfTasks"
        maxProgressPath = "listsOverview/$openListID/maxProgress"
        currentProgressPath = "listsOverview/$openListID/currentProgress"

        listName = intent.getStringExtra("listClickedName").toString()
        listTitle.text = listName
        maxListProgress = intent.getIntExtra("listClickedMaxProgress", task.count())
        taskProgressBar.max = maxListProgress
        currentListProgress = intent.getIntExtra("listClickedCurrentProgress",
                getProgressAmount().count())
        taskProgressBar.progress = currentListProgress

        taskAdapter = TaskAdapter(this, task)
        taskViewItem!!.adapter = taskAdapter

        addTaskToOverview().addOnCompleteListener {
            if(it.isComplete) {
                taskAdapter.notifyDataSetChanged()
            }
        }

    }

}
