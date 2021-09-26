package mehmethy.todo.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import mehmethy.todo.TodoManager
import mehmethy.todo.dialogs.TodoEditDialog


class TodoWidget(private val context: Context, parent: LinearLayout, private val todoManager: TodoManager) {
    private val bg: LinearLayout
    private val stateButton: ImageButton
    private var todoState = TodoState.IN_PROGRESS
    private val titleButton: Button
    private val descriptionButton: ImageButton
    private var description = ""

    init {
        bg = buildContainer()
        stateButton = buildStateButton()
        descriptionButton = buildNoteButton()
        titleButton = buildTitleButton()

        bg.addView(stateButton)
        bg.addView(descriptionButton)
        bg.addView(titleButton)
        parent.addView(bg)
    }

    fun getTitleText() = titleButton?.text.toString()
    fun getDescriptionText() = description

    fun handleEditConfirm(title: String, _description: String) {
        if (title.isNullOrBlank()) {
            return
        }
        titleButton.text = title
        description = _description
    }

    fun activate() {
        if (todoManager.activeTodoWidget == this) {
            return
        }
        bg.isSelected = true
        todoManager.activeTodoWidget?.deactivate()
        todoManager.activeTodoWidget = this
    }

    private fun deactivate() {
        bg.isSelected = false
        todoManager.activeTodoWidget = null
    }

    private fun changeState() {
        when (todoState) {
            TodoState.IN_PROGRESS -> {
                val id = mehmethy.todo.R.drawable.completed_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.COMPLETED
            }
            TodoState.COMPLETED -> {
                val id = mehmethy.todo.R.drawable.not_started_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.NOT_STARTED
            }
            TodoState.NOT_STARTED -> {
                val id = mehmethy.todo.R.drawable.progress_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.IN_PROGRESS
            }
        }
    }

    private fun buildContainer(): LinearLayout {
        val params = LinearLayout.LayoutParams(ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT))
        params.setMargins(16, 4, 16, 4)
        val layout = LinearLayout(context)
        layout.layoutParams = params
        layout.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_bg)
        layout.setPadding(8, 8, 8, 8)
        return layout
    }

    private fun buildStateButton(): ImageButton {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(0, 0, 8, 0)
        val button = ImageButton(context)
        button.layoutParams = params
        val id = mehmethy.todo.R.drawable.progress_icon
        val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
        button.setImageDrawable(drawable)
        button.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_button)
        button.setPadding(16, 16, 16, 16)
        button.setOnClickListener {
            activate()
            changeState()
        }
        return button
    }

    private fun buildNoteButton(): ImageButton {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(0, 0, 8, 0)
        val button = ImageButton(context)
        button.layoutParams = params
        val id = mehmethy.todo.R.drawable.note_icon
        val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
        button.setImageDrawable(drawable)
        button.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_button)
        button.setPadding(16, 16, 16, 16)
        button.setOnClickListener { activate() }
        return button
    }

    private fun buildTitleButton(): Button {
        val button = Button(context)
        button.text = context.getString(mehmethy.todo.R.string.todo_title_label)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.0f
        button.layoutParams = params
        button.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_button)
        button.textAlignment = View.TEXT_ALIGNMENT_CENTER
        button.setOnClickListener { activate() }
        button.setOnLongClickListener {
            TodoEditDialog(context, ::handleEditConfirm, getTitleText(), getDescriptionText()).show()
            true
        }
        return button
    }
}
