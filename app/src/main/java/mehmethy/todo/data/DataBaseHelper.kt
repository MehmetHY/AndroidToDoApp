package mehmethy.todo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


object DataBaseInfo {
    const val NAME = "tododb"
    const val VERSION = 1

    const val TODO_GROUP_TABLE_NAME = "todo_group"
    const val TODO_GROUP_COLUMN_ID_NAME = "id"
    const val TODO_GROUP_COLUMN_TITLE_NAME = "title"

    const val TODO_TABLE_NAME = "todo"
    const val TODO_COLUMN_ID_NAME = "id"
    const val TODO_COLUMN_GROUP_ID_NAME = "groupId"
    const val TODO_COLUMN_TITLE_NAME = "title"
    const val TODO_COLUMN_DESCRIPTION_NAME = "description"
    const val TODO_COLUMN_STATE_NAME = "state"

}

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseInfo.NAME, null, DataBaseInfo.VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTodoGroupTableStatement =
            "CREATE TABLE ${DataBaseInfo.TODO_GROUP_TABLE_NAME} (" +
                    "${DataBaseInfo.TODO_GROUP_COLUMN_ID_NAME} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${DataBaseInfo.TODO_GROUP_COLUMN_TITLE_NAME} TEXT NOT NULL" +
                    ");"
        val createTodoTableStatement =
            "CREATE TABLE ${DataBaseInfo.TODO_TABLE_NAME} (" +
                    "${DataBaseInfo.TODO_COLUMN_ID_NAME} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${DataBaseInfo.TODO_COLUMN_GROUP_ID_NAME} INTEGER NOT NULL," +
                    "${DataBaseInfo.TODO_COLUMN_TITLE_NAME} TEXT NOT NULL," +
                    "${DataBaseInfo.TODO_COLUMN_DESCRIPTION_NAME} TEXT," +
                    "${DataBaseInfo.TODO_COLUMN_STATE_NAME} INTEGER NOT NULL" +
                    ");"
        db?.execSQL(createTodoGroupTableStatement)
        db?.execSQL(createTodoTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}