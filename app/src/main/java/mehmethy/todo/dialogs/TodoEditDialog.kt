package mehmethy.todo.dialogs

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog

class TodoEditDialog(
    context: Context,
    private val confirmCallback: (String, String) -> Unit,
    private var initTitle: String = "",
    private var initDescription: String = ""
) : AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mehmethy.todo.R.layout.dialog_todo_edit)

        val title_input: EditText? = findViewById(mehmethy.todo.R.id.todo_edit_title_input)
        title_input?.setText(initTitle)
        title_input?.setOnFocusChangeListener { view, b -> title_input?.selectAll() }

        val description_input: EditText? =
            findViewById(mehmethy.todo.R.id.todo_edit_description_input)
        description_input?.setText(initDescription)

        val okButton: Button? = findViewById(mehmethy.todo.R.id.todo_edit_ok_button)
        okButton?.setOnClickListener {
            confirmCallback(title_input?.text.toString(), description_input?.text.toString())
            this.hide()
        }

        setOnShowListener {
            title_input?.requestFocus()
        }
    }
}