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
import com.darekbx.lifetimememo.screens.search.ui.SearchScreen
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
                authorized = {
                    navController.navigate(Memos.route) {
                        popUpTo(0 /* clear root */)
                    }
                }
            )
        }

        composable(route = Search.route) {
            SearchScreen()
        }

        composable(
            route = Memos.routeWithArgs,
            arguments = Memos.arguments
        ) { navBackStackEntry ->
            val parentId = navBackStackEntry.arguments?.getString(Memos.parentIdArg)
            with(navController) {
                MemosScreen(
                    parentId = parentId,
                    onMemoClick = { memoId -> navigateSingleTopTo("${Memo.route}?${Memo.memoIdArg}=$memoId") },
                    onContainerClick = { containerId -> navigate("${Memos.route}?${Memos.parentIdArg}=$containerId") },
                    onAddMemoClick = { parentId ->
                        if (parentId == null) {
                            navigate(Memo.route)
                        } else {
                            navigate("${Memo.route}?${Memo.parentIdArg}=$parentId")
                        }
                    },
                    onAddContainerClick = { parentId ->
                        if (parentId == null) {
                            navigate(Container.route)
                        } else {
                            navigate(Container.parentIdRouteFormat.format(parentId))
                        }
                    }
                )
            }
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
                    navController.popBackStack()
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
                    navController.popBackStack()
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