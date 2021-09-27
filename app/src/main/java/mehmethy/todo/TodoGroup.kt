package mehmethy.todo

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class TodoGroup(private val context: Context, var title: String = "", val todoList: MutableList<TodoRecipe> = mutableListOf()) {
    private val view: Button

    init {
        if (title == "") {
            title = context.getString(R.string.todo_default_title)
        }
        view = Button(ContextThemeWrapper(context, R.style.TodoCommon_UIButton))
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 4, 0, 4)
        view.layoutParams = params
        view.text = title
        view.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        view.setPadding(24, 8, 24, 8)
        view.setOnClickListener {
            activate()
        }
    }

    fun activate() {

    }

    fun deactivate() {

    }

    fun getView(): Button = view
}