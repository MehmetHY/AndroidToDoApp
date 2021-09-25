package mehmethy.todo.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout


private var _activeTodoWidget: TodoWidget? = null
var activeTodoWidget: TodoWidget?
    get() = _activeTodoWidget
    set(value) {
        _activeTodoWidget = value
    }

class TodoWidget(private val context: Context, parent: LinearLayout) {
    init {
        val container = buildContainer()
        val stateButton = buildStateButton()
        val noteButton = buildNoteButton()
        val titleButton = buildTitleButton()

        container.addView(stateButton)
        container.addView(noteButton)
        container.addView(titleButton)
        parent.addView(container)
    }

    private fun buildContainer(): LinearLayout {
        val params = LinearLayout.LayoutParams(ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT))
        params.setMargins(16, 4, 16, 4)
        val layout = LinearLayout(context)
        layout.layoutParams = params
        return layout
    }

    private fun buildStateButton(): Button {
        val button = Button(context)
        button.text = "State"
        return button
    }

    private fun buildNoteButton(): Button {
        val button = Button(context)
        button.text = "Note"
        return button
    }

    private fun buildTitleButton(): Button {
        val button = Button(context)
        button.text = "Title"
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.0f
        button.layoutParams = params
        return button
    }
}
