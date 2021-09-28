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
import mehmethy.todo.widget.TodoState

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
                TodoManager.activeRecipeList = todoManager.activeTodoGroup?.todoRecipeList
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
            TodoGroupEditDialog(this, newTodoGroup::handleTodoEditDialog)
        }

        val deleteButton: Button? = findViewById(R.id.todo_main_menu_delete)
        deleteButton?.setOnClickListener {
            if (todoManager.activeTodoGroup != null) {
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
                val todoQueryString = "SELECT * FROM ${DataBaseInfo.TODO_TABLE_NAME} WHERE ${DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME} = $todoGroupId"
                val todoCursor = db.rawQuery(todoQueryString, null)
                val todoRecipeList = mutableListOf<TodoRecipe>()
                if (todoCursor.moveToFirst()) {
                    do {
                        val todoTitle = todoCursor.getString(2)
                        val todoDescription = todoCursor.getString(3)
                        val state =
                            when (todoCursor.getInt(4)) {
                                1 -> TodoState.COMPLETED
                                2 -> TodoState.NOT_STARTED
                                else -> TodoState.IN_PROGRESS
                            }
                        val todoRecipe = TodoRecipe(todoTitle, todoDescription, state)
                        todoRecipeList.add(todoRecipe)
                    } while (todoCursor.moveToNext())
                    todoCursor.close()
                }
                val todoGroup = TodoGroup(this, todoManager, todoGroupId, todoGroupTitle, todoRecipeList)
                list?.addView(todoGroup.getView())
            } while (todoGroupCursor.moveToNext())
            todoGroupCursor.close()
        }
    }

}