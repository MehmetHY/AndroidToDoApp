package mehmethy.todo

import android.content.ContentValues
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import mehmethy.todo.data.DataBaseHelper
import mehmethy.todo.data.DataBaseInfo
import mehmethy.todo.dialogs.TodoGroupEditDialog

class TodoGroup(private val context: Context, private val todoManager: TodoManager, private val id: Long, private var title: String = "", val todoRecipeList: MutableList<TodoRecipe> = mutableListOf()) {
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
            TodoGroupEditDialog(context, this::handleTodoEditDialog, title).show()
            true
        }
        activate()
    }

    fun handleTodoEditDialog(text: String) {
        if (text.isBlank()) {
            return
        }
        title = text
        view.text = title
        val dataBaseHelper = DataBaseHelper(context)
        val db = dataBaseHelper.writableDatabase
        val cv = ContentValues()
        cv.put(DataBaseInfo.TODO_GROUP_COLUMN_TITLE_NAME, title)
        db.update(DataBaseInfo.TODO_GROUP_TABLE_NAME, cv, "${DataBaseInfo.TODO_GROUP_COLUMN_ID_NAME} = $id", null)
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

    fun getId(): Long {
        return id
    }

    fun getView(): TextView = view
}