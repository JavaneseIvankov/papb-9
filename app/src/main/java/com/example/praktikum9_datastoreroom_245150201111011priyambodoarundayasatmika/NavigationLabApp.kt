package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.local.DatabaseProvider
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.preferences.UserPreferencesRepository
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.remote.RetrofitClient
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.remote.StudentRemoteDataSource
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.DefaultStudentRepository
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.FavoriteRepository
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.detail.DetailViewModel
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.favorites.FavoritesViewModel
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.home.HomeViewModel
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen.AboutScreen
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen.DetailScreen
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen.FavoritesScreen
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen.HomeScreen
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.screen.ProfileScreen

@Composable
fun NavigationLabApp() {
    val context = LocalContext.current

    val remoteDataSource = remember {
        StudentRemoteDataSource(RetrofitClient.apiService)
    }
    val repository = remember {
        DefaultStudentRepository(remoteDataSource)
    }
    val preferencesRepository = remember {
        UserPreferencesRepository(context.applicationContext)
    }
    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context.applicationContext).favoriteStudentDao()
        )
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository, preferencesRepository)
            )
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = homeUiState,
                searchQuery = searchQuery,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onToggleSort = homeViewModel::toggleSortAscending,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onRetry = homeViewModel::retry,
                onRefresh = homeViewModel::refresh,
                onOpenFavorites = {
                    navController.navigate(Routes.FAVORITES)
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }

            DetailScreen(
                uiState = detailUiState,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onBack = { navController.popBackStack() },
                onRetry = { detailViewModel.loadStudent(studentId) },
                onOpenProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository)
            )
            val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()

            FavoritesScreen(
                uiState = favoritesUiState,
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
