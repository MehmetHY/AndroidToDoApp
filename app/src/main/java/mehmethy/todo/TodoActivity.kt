package mehmethy.todo

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import mehmethy.todo.data.DataBaseHelper
import mehmethy.todo.data.DataBaseInfo
import mehmethy.todo.dialogs.TodoEditDialog
import mehmethy.todo.widget.TodoState
import mehmethy.todo.widget.TodoWidget

class TodoActivity : AppCompatActivity() {
    private val todoContainer: LinearLayout by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.todoContainer) }
    private val todoList: MutableList<TodoWidget> = mutableListOf()
    private val todoManager = TodoManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val dataBaseHelper = DataBaseHelper(this)
            val db = dataBaseHelper.writableDatabase
            val cv = ContentValues()
            cv.put(DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME, TodoManager.activeTodoGroupId)
            cv.put(DataBaseInfo.TODO_COLUMN_TITLE_NAME, getString(R.string.todo_default_title))
            cv.put(DataBaseInfo.TODO_COLUMN_DESCRIPTION_NAME, "")
            cv.put(DataBaseInfo.TODO_COLUMN_STATE_NAME, 0)
            val newId = db.insert(DataBaseInfo.TODO_TABLE_NAME, null, cv)
            val widget = TodoWidget(
                this,
                todoContainer,
                todoManager,
                newId,
                TodoManager.activeTodoGroupId!!,
                getString(R.string.todo_default_title)
            )
            widget.activate()
            todoList.add(widget)
            TodoEditDialog(this, widget::handleEditConfirm, widget.getTitleText()).show()
        }

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            val dataBaseHelper = DataBaseHelper(this)
            val db = dataBaseHelper.writableDatabase
            db.delete(
                DataBaseInfo.TODO_TABLE_NAME,
                "${DataBaseInfo.TODO_COLUMN_ID_NAME} = ${todoManager.activeTodoWidget?.getId()}",
                null
            )
            todoContainer.removeView(todoManager.activeTodoWidget?.getView())
        }

        populateContainer(todoContainer, todoManager)
    }

    private fun populateContainer(container: LinearLayout, todoManager: TodoManager) {
        val dataBaseHelper = DataBaseHelper(this)
        val db = dataBaseHelper.readableDatabase
        val queryString =
            "SELECT * FROM ${DataBaseInfo.TODO_TABLE_NAME} WHERE ${DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME} = ${TodoManager.activeTodoGroupId}"
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val groupId = TodoManager.activeTodoGroupId
                val title = cursor.getString(2)
                val description = cursor.getString(3)
                val state =
                    when (cursor.getInt(4)) {
                        1 -> TodoState.COMPLETED
                        2 -> TodoState.NOT_STARTED
                        else -> TodoState.IN_PROGRESS
                    }
                TodoWidget(
                    this,
                    container,
                    todoManager,
                    id,
                    groupId!!,
                    title,
                    description,
                    state
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
    }
}