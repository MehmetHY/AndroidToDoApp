package mehmethy.todo.dialogs

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog

class TodoGroupEditDialog(context: Context, private val confirmCallback: (String) -> Unit) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mehmethy.todo.R.layout.dialog_todo_group_edit)
        val inputText: EditText? = findViewById(mehmethy.todo.R.id.todoGroupNameInput)
        val okButton: Button? = findViewById(mehmethy.todo.R.id.todoGroupConfirmButton)
        okButton?.setOnClickListener {
            confirmCallback(inputText?.text.toString())
            this.dismiss()
        }
    }
}