package mehmethy.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todoGroupList: LinearLayout? = findViewById(R.id.todo_main_menu_list)
        val todoManager = TodoManager()

        val loadButton = findViewById<Button>(R.id.todo_main_menu_load)
        loadButton.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }

        val newButton: Button? = findViewById(R.id.todo_main_menu_new)
        newButton?.setOnClickListener {
            val button = TodoGroup(this, todoManager, "test")
            todoGroupList?.addView(button.getView())
        }

        val deleteButton: Button? = findViewById(R.id.todo_main_menu_delete)
        deleteButton?.setOnClickListener {
            if (todoManager.activeTodoGroup != null) {
                todoGroupList?.removeView(todoManager.activeTodoGroup?.getView())
            }
        }
    }
}