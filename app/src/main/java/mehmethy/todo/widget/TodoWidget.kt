package mehmethy.todo.widget

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import mehmethy.todo.TodoManager
import mehmethy.todo.data.DataBaseHelper
import mehmethy.todo.data.DataBaseInfo
import mehmethy.todo.dialogs.TodoDescriptionDialog
import mehmethy.todo.dialogs.TodoEditDialog


class TodoWidget(
    private val context: Context,
    parent: LinearLayout,
    private val todoManager: TodoManager,
    private val id: Long,
    private val groupId: Long,
    private var _title: String,
    private var description: String = "",
    _state: TodoState = TodoState.IN_PROGRESS
) {
    private val bg: LinearLayout
    private val stateButton: ImageButton
    private var todoState = TodoState.IN_PROGRESS
    private val titleButton: Button
    private val descriptionButton: ImageButton

    init {
        bg = buildContainer()
        stateButton = buildStateButton()
        descriptionButton = buildNoteButton()
        titleButton = buildTitleButton()

        bg.addView(stateButton)
        bg.addView(descriptionButton)
        bg.addView(titleButton)

        parent.addView(bg)

        if (_title.isBlank()) {
            _title = context.getString(mehmethy.todo.R.string.todo_default_title)
        }
        titleButton.text = _title
        setState(_state)
    }

    fun getView(): LinearLayout = bg

    fun getTitleText() = titleButton.text.toString()
    private fun getDescriptionText() = description

    private fun setState(state: TodoState) {
        val dataBaseHelper = DataBaseHelper(context)
        val db = dataBaseHelper.writableDatabase
        val cv = ContentValues()
        val stateLong: Long =
            when (state) {
                TodoState.IN_PROGRESS -> 0
                TodoState.COMPLETED -> 1
                TodoState.NOT_STARTED -> 2
            }
        cv.put(DataBaseInfo.TODO_COLUMN_STATE_NAME, stateLong)
        db.update(DataBaseInfo.TODO_TABLE_NAME, cv, "${DataBaseInfo.TODO_COLUMN_ID_NAME} = $id", null)
        when (state) {

            TodoState.IN_PROGRESS -> {
                val id = mehmethy.todo.R.drawable.progress_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.IN_PROGRESS
            }
            TodoState.COMPLETED -> {
                val id = mehmethy.todo.R.drawable.completed_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.COMPLETED
            }
            TodoState.NOT_STARTED -> {
                val id = mehmethy.todo.R.drawable.not_started_icon
                val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
                stateButton.setImageDrawable(drawable)
                todoState = TodoState.NOT_STARTED
            }
        }
    }

    fun handleEditConfirm(title: String, _description: String) {
        if (title.isBlank()) {
            return
        }
        titleButton.text = title
        description = _description
        val dataBaseHelper = DataBaseHelper(context)
        val db = dataBaseHelper.writableDatabase
        val cv = ContentValues()
        cv.put(DataBaseInfo.TODO_COLUMN_TITLE_NAME, title)
        cv.put(DataBaseInfo.TODO_COLUMN_DESCRIPTION_NAME, description)
        db.update(DataBaseInfo.TODO_TABLE_NAME, cv, "${DataBaseInfo.TODO_COLUMN_ID_NAME} = $id", null)
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
                setState(TodoState.COMPLETED)
            }
            TodoState.COMPLETED -> {
                setState(TodoState.NOT_STARTED)
            }
            TodoState.NOT_STARTED -> {
                setState(TodoState.IN_PROGRESS)
            }
        }
    }

    private fun buildContainer(): LinearLayout {
        val params = LinearLayout.LayoutParams(
            ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
        )
        params.setMargins(16, 4, 16, 4)
        val layout = LinearLayout(context)
        layout.layoutParams = params
        layout.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_bg)
        layout.setPadding(8, 8, 8, 8)
        return layout
    }

    private fun buildStateButton(): ImageButton {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
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
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 0, 8, 0)
        val button = ImageButton(context)
        button.layoutParams = params
        val id = mehmethy.todo.R.drawable.note_icon
        val drawable: Drawable? = AppCompatResources.getDrawable(context, id)
        button.setImageDrawable(drawable)
        button.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_button)
        button.setPadding(16, 16, 16, 16)
        button.setOnClickListener {
            activate()
            TodoDescriptionDialog(context, titleButton.text.toString(), getDescriptionText()).show()
        }
        return button
    }

    private fun buildTitleButton(): Button {
        val button = Button(context)
        button.text = context.getString(mehmethy.todo.R.string.todo_title_label)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.weight = 1.0f
        button.layoutParams = params
        button.setBackgroundResource(mehmethy.todo.R.drawable.todo_widget_button)
        button.textAlignment = View.TEXT_ALIGNMENT_CENTER
        button.setOnClickListener { activate() }
        button.setOnLongClickListener {
            TodoEditDialog(
                context,
                ::handleEditConfirm,
                getTitleText(),
                getDescriptionText()
            ).show()
            true
        }
        return button
    }

    fun getId() = id
    fun getGroupId() = groupId
}
