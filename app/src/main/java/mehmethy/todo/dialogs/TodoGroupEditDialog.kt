package mehmethy.todo.dialogs

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog

class TodoGroupEditDialog(context: Context, private val confirmCallback: (String) -> Unit, private val defaultText: String = "") : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mehmethy.todo.R.layout.dialog_todo_group_edit)

        val inputText: EditText? = findViewById(mehmethy.todo.R.id.todoGroupNameInput)
        inputText?.setText(defaultText)
        inputText?.selectAll()

        val okButton: Button? = findViewById(mehmethy.todo.R.id.todoGroupConfirmButton)
        okButton?.setOnClickListener {
            confirmCallback(inputText?.text.toString())
            this.dismiss()
        }

        val cancelButton: Button? = findViewById(mehmethy.todo.R.id.todoGroupCancelButton)
        cancelButton?.setOnClickListener {
            this.cancel()
        }
    }
}