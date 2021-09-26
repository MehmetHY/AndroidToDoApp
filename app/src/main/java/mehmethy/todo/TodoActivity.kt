package mehmethy.todo

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import mehmethy.todo.dialogs.TodoEditDialog
import mehmethy.todo.widget.TodoWidget

class TodoActivity : AppCompatActivity() {
    private val todoContainer: LinearLayout by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.todoContainer) }
    private val todoList: MutableList<TodoWidget> = mutableListOf()
    private val todoManager = TodoManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val root: ConstraintLayout = findViewById(R.id.root)

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val widget = TodoWidget(this, todoContainer, todoManager)
            widget.activate()
            todoList.add(widget)
        }

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            TodoEditDialog(this).show()
        }
    }
}