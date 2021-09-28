package mehmethy.todo

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import mehmethy.todo.data.DataBaseHelper
import mehmethy.todo.data.DataBaseInfo
import mehmethy.todo.dialogs.TodoGroupEditDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todoGroupList: LinearLayout? = findViewById(R.id.todo_main_menu_list)
        val todoManager = TodoManager()

        val loadButton = findViewById<Button>(R.id.todo_main_menu_load)
        loadButton.setOnClickListener {
            if (todoManager.activeTodoGroup != null) {
                val intent = Intent(this, TodoActivity::class.java)
                TodoManager.activeTodoGroupId = todoManager.activeTodoGroup?.getId()
                startActivity(intent)
            }
        }

        val newButton: Button? = findViewById(R.id.todo_main_menu_new)
        newButton?.setOnClickListener {
            val dataBaseHelper = DataBaseHelper(this)
            val db = dataBaseHelper.writableDatabase
            val cv = ContentValues()
            cv.put(DataBaseInfo.TODO_GROUP_COLUMN_TITLE_NAME, getString(R.string.todo_default_title))
            val id = db.insert(DataBaseInfo.TODO_GROUP_TABLE_NAME, null, cv)
            val newTodoGroup = TodoGroup(this, todoManager, id)
            todoGroupList?.addView(newTodoGroup.getView())
            TodoGroupEditDialog(this, newTodoGroup::handleTodoEditDialog).show()
        }

        val deleteButton: Button? = findViewById(R.id.todo_main_menu_delete)
        deleteButton?.setOnClickListener {
            if (todoManager.activeTodoGroup != null) {
                val dataBaseHelper = DataBaseHelper(this)
                val db = dataBaseHelper.writableDatabase
                db.delete(DataBaseInfo.TODO_TABLE_NAME, "${DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME} = ${todoManager.activeTodoGroup?.getId()}", null)
                db.delete(DataBaseInfo.TODO_GROUP_TABLE_NAME, "${DataBaseInfo.TODO_GROUP_COLUMN_ID_NAME} = ${todoManager.activeTodoGroup?.getId()}", null)
                todoGroupList?.removeView(todoManager.activeTodoGroup?.getView())
            }
        }

        populateTodoGroupList(todoGroupList, todoManager)
    }

    private fun populateTodoGroupList(list: LinearLayout?, todoManager: TodoManager) {
        val dataBaseHelper = DataBaseHelper(this)
        val db = dataBaseHelper.readableDatabase
        val todoGroupQueryString = "SELECT * FROM ${DataBaseInfo.TODO_GROUP_TABLE_NAME}"
        val todoGroupCursor = db.rawQuery(todoGroupQueryString, null)
        if (todoGroupCursor.moveToFirst()) {
            do {
                val todoGroupId = todoGroupCursor.getLong(0)
                val todoGroupTitle = todoGroupCursor.getString(1)
                val todoGroup = TodoGroup(this, todoManager, todoGroupId, todoGroupTitle)
                list?.addView(todoGroup.getView())
            } while (todoGroupCursor.moveToNext())
            todoGroupCursor.close()
        }
    }

}