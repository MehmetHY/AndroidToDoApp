package mehmethy.todo

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import mehmethy.todo.dialogs.TodoGroupEditDialog

class TodoGroup(context: Context, private val todoManager: TodoManager, private var title: String = "", val todoList: MutableList<TodoRecipe> = mutableListOf()) {
    private val view: TextView

    init {
        if (title == "") {
            title = context.getString(R.string.todo_default_title)
        }
        view = TextView(ContextThemeWrapper(context, R.style.TodoCommon_UIButton))
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 4, 0, 4)
        view.layoutParams = params
        view.text = title
        view.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        view.setPadding(24, 8, 24, 8)
        view.setOnClickListener {
            activate()
        }
        view.setOnLongClickListener {
            activate()
            TodoGroupEditDialog(context, this::handleTodoEditDialog).show()
            true
        }
        activate()
        TodoGroupEditDialog(context, this::handleTodoEditDialog).show()
    }

    fun handleTodoEditDialog(text: String) {
        if (text.isBlank()) {
            return
        }
        title = text
        view.text = title
    }

    private fun activate() {
        if (todoManager.activeTodoGroup == this) {
            return
        }
        if (todoManager.activeTodoGroup != null) {
            todoManager.activeTodoGroup?.deactivate()
        }
        todoManager.activeTodoGroup = this
        view.isSelected = true
    }

    private fun deactivate() {
        view.isSelected = false
    }

    fun getView(): TextView = view
}