package mehmethy.todo

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class TodoActivity : AppCompatActivity() {
    private val todoContainer: LinearLayout by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.todoContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener { createTodo() }
    }

    private fun createTodo() {
        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layout.orientation = LinearLayout.HORIZONTAL

        val params = layout.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(16, 4, 16, 4)

        layout.layoutParams = params

        val stateButton = Button(this)
        stateButton.setText("Cross")

        layout.addView(stateButton)
        todoContainer.addView(layout)
    }
}