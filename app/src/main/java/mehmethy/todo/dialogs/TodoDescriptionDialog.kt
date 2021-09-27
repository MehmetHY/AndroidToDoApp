package mehmethy.todo.dialogs

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog

class TodoDescriptionDialog(context: Context, private val title: String = "", private val description: String = "") : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mehmethy.todo.R.layout.dialog_todo_description)

        val titleView: TextView? = findViewById(mehmethy.todo.R.id.todo_description_dialog_title)
        titleView?.text = title

        val descriptionView: TextView? = findViewById(mehmethy.todo.R.id.todo_description_dialog_text)
        descriptionView?.text = description

        val closeButton: Button? = findViewById(mehmethy.todo.R.id.todo_description_close)
        closeButton?.setOnClickListener {
            this.dismiss()
        }
    }
}