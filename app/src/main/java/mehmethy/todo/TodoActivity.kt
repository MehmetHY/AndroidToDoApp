package mehmethy.todo

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
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

        for (recipe in TodoManager.activeRecipeList!!) {
            val widget = TodoWidget(this, todoContainer, todoManager)
            widget.setTitle(recipe.title)
            widget.description = recipe.description
            widget.setState(recipe.state)
            todoList.add(widget)
        }

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val widget = TodoWidget(this, todoContainer, todoManager)
            widget.activate()
            todoList.add(widget)
            TodoEditDialog(this, widget::handleEditConfirm, widget.getTitleText()).show()
            val dataBaseHelper = DataBaseHelper(this)
            val db = dataBaseHelper.writableDatabase
            val cv = ContentValues()
            cv.put(DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME, todoManager.activeTodoGroup?.)
            cv.put(DataBaseInfo.todo_column)
            cv.put(DataBaseInfo.todo_column)
            cv.put(DataBaseInfo.todo_column)
            db.insert(DataBaseInfo.TODO_TABLE_NAME, null, )
        }

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            todoContainer.removeView(todoManager.activeTodoWidget?.getView())
        }
    }
}