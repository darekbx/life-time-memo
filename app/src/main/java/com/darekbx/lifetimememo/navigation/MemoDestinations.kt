package com.darekbx.lifetimememo.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface MemoDestination {
    val route: String
    val label: String
}

object LogIn : MemoDestination {
    override val route = "login"
    override val label = "Login"
}

object Memos : MemoDestination {
    override val route = "memos/null"
    override val label = "Memos"
    const val rawRoute = "memos"
    const val parentIdArg = "parent_id"
    const val routeWithArgs = "$rawRoute/{${parentIdArg}}"
    val arguments = listOf(
        navArgument(parentIdArg) { type = NavType.StringType }
    )
}

object Settings : MemoDestination {
    override val route = "settings"
    override val label = "Settings"
}

object Categories : MemoDestination {
    override val route = "categories"
    override val label = "Categories"
}

object Memo : MemoDestination {
    override val route = "memo"
    override val label = "Memo"
    const val memoIdArg = "memo_id"
    const val parentIdArg = "parent_id"
    val routeWithArgs = "${route}/{$memoIdArg}/{$parentIdArg}"
    val arguments = listOf(
        navArgument(memoIdArg) { type = NavType.StringType },
        navArgument(parentIdArg) { type = NavType.StringType }
    )
}

object Container : MemoDestination {
    override val route = "container"
    override val label = "Container"
    const val parentIdArg = "parent_id"
    val routeWithArgs = "${route}/{$parentIdArg}"
    val arguments = listOf(
        navArgument(parentIdArg) { type = NavType.StringType }
    )
}

val appTabRowScreens = listOf(Memos, Settings)
