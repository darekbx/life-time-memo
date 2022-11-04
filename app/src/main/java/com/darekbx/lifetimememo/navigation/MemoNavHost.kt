package com.darekbx.lifetimememo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.darekbx.lifetimememo.screens.category.ui.CategoriesScreen
import com.darekbx.lifetimememo.screens.container.ui.ContainerScreen
import com.darekbx.lifetimememo.screens.login.ui.LoginScreen
import com.darekbx.lifetimememo.screens.memo.ui.MemoScreen
import com.darekbx.lifetimememo.screens.memos.ui.MemosScreen
import com.darekbx.lifetimememo.screens.settings.ui.SettingsScreen

@Composable
fun MemoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LogIn.route,
        modifier = modifier
    ) {
        composable(route = LogIn.route) {
            LoginScreen(
                authorized = { navController.navigateSingleTopTo("${Memos.rawRoute}/null") }
            )
        }

        composable(
            route = Memos.routeWithArgs,
            arguments = Memos.arguments
        ) { navBackStackEntry ->
            val parentId = navBackStackEntry.arguments?.getString(Memos.parentIdArg)
            MemosScreen(
                parentId = parentId,
                onMemoClick = { memoId -> navController.navigateSingleTopTo("${Memo.route}/$memoId") },
                onContainerClick = { containerId -> navController.navigate("${Memos.rawRoute}/$containerId") },
                onAddMemoClick = { id -> navController.navigateSingleTopTo("${Memo.route}/null/$id") },
                onAddContainerClick = { id -> navController.navigateSingleTopTo("${Container.route}/$id")  }
            )
        }

        composable(route = Settings.route) {
            SettingsScreen(
                openCategories = { navController.navigateSingleTopTo(Categories.route) }
            )
        }

        composable(route = Categories.route) {
            CategoriesScreen()
        }

        composable(
            route = Memo.routeWithArgs,
            arguments = Memo.arguments
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                val memoId = args.getString(Memo.memoIdArg)
                val parentId = args.getString(Memo.parentIdArg)
                MemoScreen(memoId = memoId, parentId = parentId) {
                    navController.navigateSingleTopTo("${Memos.rawRoute}/$parentId")
                    navController.navigateSingleTopTo("${Memos.rawRoute}/$parentId")
                }
            }
        }

        composable(
            route = Container.routeWithArgs,
            arguments = Container.arguments
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                val parentId = args.getString(Container.parentIdArg)
                ContainerScreen(parentId = parentId) {
                    navController.navigateSingleTopTo("${Memos.rawRoute}/$parentId")
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }