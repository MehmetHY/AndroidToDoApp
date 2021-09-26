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

        val titleInput: EditText = findViewById(mehmethy.todo.R.id.todo_edit_title_input)!!
        titleInput.setText(initTitle)
        titleInput.setOnFocusChangeListener { _, _ -> titleInput.selectAll() }

        val descriptionInput: EditText? =
            findViewById(mehmethy.todo.R.id.todo_edit_description_input)
        descriptionInput?.setText(initDescription)

        val okButton: Button? = findViewById(mehmethy.todo.R.id.todo_edit_ok_button)
        okButton?.setOnClickListener {
            confirmCallback(titleInput.text.toString(), descriptionInput?.text.toString())
            this.hide()
        }

        setOnShowListener {
            titleInput.requestFocus()
        }
    }
}