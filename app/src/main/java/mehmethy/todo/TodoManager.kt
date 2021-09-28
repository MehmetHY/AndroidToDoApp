package mehmethy.todo

import mehmethy.todo.widget.TodoWidget

class TodoManager {
    private var _activeTodoWidget: TodoWidget? = null
    var activeTodoWidget: TodoWidget?
        get() = _activeTodoWidget
        set(value) {
            _activeTodoWidget = value
        }

    var activeTodoGroup: TodoGroup? = null

    companion object {
        var activeRecipeList: List<TodoRecipe>? = null
        var activeTodoGroupId: Long? = null
    }
}
