package com.example.turqtodo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.turqtodo.databinding.ActivityListDetailsBinding
import com.example.turqtodo.lists.TaskAdapter
import com.example.turqtodo.lists.data.TaskData
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_details.*

class TaskHolder {
    companion object {
        var ClickedTask: TaskData? = null
    }
}

class ListDetailsActivity : AppCompatActivity(), TaskUpdateAndDelete {

    private lateinit var binding: ActivityListDetailsBinding

    private lateinit var database: DatabaseReference
    private val task: MutableList<TaskData> = mutableListOf<TaskData>()
    lateinit var taskAdapter: TaskAdapter
    private var taskViewItem: ListView? = null
    var openListID:String? = ""
    var taskPath:String = ""
    var listName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listTitle = findViewById<View>(R.id.listTitle) as TextView
        val addNewTask = findViewById<View>(R.id.addNewTaskButton) as FloatingActionButton

        taskViewItem = findViewById<ListView>(R.id.tasksOverview_taskView)

        openListID = intent.getStringExtra("listClickedID")
        Toast.makeText(applicationContext, "ID: $openListID, now open", Toast.LENGTH_LONG).show()
        taskPath = "listsOverview/$openListID/listOfTasks"

        listName = intent.getStringExtra("listClickedName").toString()
        Toast.makeText(applicationContext, "MY NAME IS $listName", Toast.LENGTH_LONG).show()
        listTitle.text = listName

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

                val taskDataForDb = database.child(taskPath).push()
                newTaskData.taskId = taskDataForDb.key
                task.add(newTaskData)
                taskDataForDb.setValue(newTaskData)

                dialog.dismiss()
                Toast.makeText(this,"Task saved!", Toast.LENGTH_SHORT).show()
            }
            newTaskPopup.show()
            taskAdapter.notifyDataSetChanged()
        }

        taskAdapter = TaskAdapter(this, task)
        taskViewItem!!.adapter = taskAdapter
        addTaskToOverview().addOnCompleteListener {
            if(it.isComplete) {
                taskAdapter.notifyDataSetChanged()
            }
        }

        //val dbReference = FirebaseDatabase.getInstance().getReference(taskPath)
        //val taskReference = dbReference.child(openListID.toString()).child("listOfTasks")
        /*
        database.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "There was no task added", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                task.clear()
                Toast.makeText(applicationContext, "TaskData was changed!", Toast.LENGTH_SHORT).show()
                addTaskToOverview()
            }
        })
         */

    }

/*
        private fun addTaskToOverview(snapshot: DataSnapshot) {
            val tasks = snapshot.children.iterator()

            if(tasks.hasNext()) {
                val taskIndexedValue = tasks.next()
                val tasksIterator = taskIndexedValue.children.iterator()

                while(tasksIterator.hasNext()) {
                    val currentTask = tasksIterator.next()
                    val taskItemData = Task.createTask()
                    val map = currentTask.value as HashMap<String, Any>

                    taskItemData.taskId = currentTask.key
                    taskItemData.taskName = map["taskName"] as String?
                    taskItemData.isCompleted = map["isCompleted"] as Boolean
                    task.add(taskItemData)
                }
            }
            taskAdapter.notifyDataSetChanged()
        }
*/

        private fun addTaskToOverview() : Task<DataSnapshot> {
            return database.child(taskPath).get().addOnSuccessListener { it ->
                if (it.exists()) {
                    //val taskStatus = database.child(taskPath).child("isCompleted")
                        task.clear()
                    //database.child(taskPath).child("isCompleted").setValue(taskStatus)
                }

                it.children.mapNotNullTo(task) {
                    it.getValue<TaskData>(TaskData::class.java)
                }
            }
        }

        override fun modifyTask(taskID: String, isCompleted: Boolean, position: Int) {
            val taskReference = database.child(taskPath).child(taskID)
            taskReference.child("isCompleted").setValue(isCompleted)
            task[position].isCompleted = isCompleted
            taskProgressBar.max = task.count()
            taskProgressBar.progress = getProgressAmount().count()
            taskAdapter.notifyDataSetChanged()
        }

        override fun onTaskDelete(taskID: String, position: Int) {
            val taskReference = database.child(taskPath).child(taskID)
            taskReference.removeValue()
            task.removeAt(position)
            taskAdapter.notifyDataSetChanged()
            Toast.makeText(applicationContext,"Task deleted!", Toast.LENGTH_SHORT).show()
        }

    fun getProgressAmount(): List<TaskData> {
        return task.filter {
            it.isCompleted
        }
    }

}