package ru.vsu.cs.iachnyi_m_a.gigaguide.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.datastore.DataStoreManager
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.CreateSightScreenObject
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.CreateTourScreenObject
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.EditSightScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.EditTourScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.SightReviewScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.SightTourListScreenObject
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation.TourReviewScreenClass
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.AuthRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.MapRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.MomentRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.TourRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.UserRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.GigaGuideAdminTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.CurrentLoginState
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.CreateSightScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.CreateTourScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.EditSightScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.EditTourScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.LoginScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.SearchScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.CreateSightScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.CreateTourScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.EditSightScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.EditTourScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.LoginScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.SearchScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var dataStoreManager = DataStoreManager(this)
        var authRepository = AuthRepository()
        var sightRepository = SightRepository()
        var tourRepository = TourRepository()
        var userRepository = UserRepository()
        var momentRepository = MomentRepository()
        var mapRepository = MapRepository()

        var loginScreenViewModel = LoginScreenViewModel(dataStoreManager, authRepository)
        var searchScreenViewModel = SearchScreenViewModel(sightRepository, tourRepository)
        var createSightScreenViewModel = CreateSightScreenViewModel(sightRepository, this)
        var editSightScreenViewModel =
            EditSightScreenViewModel(sightRepository, momentRepository, mapRepository, this)
        var createTourScreenViewModel = CreateTourScreenViewModel(tourRepository, sightRepository, this)
        var editTourScreenViewModel = EditTourScreenViewModel(sightRepository, tourRepository, this)

        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                if (dataStoreManager.getJWT() != null) {
                    var creds =
                        ServerUtils.executeNetworkCall { userRepository.getUserData(dataStoreManager.getJWT()!!) }
                    if (creds != null) CurrentLoginState.logged = true
                }
            }
            var navController = rememberNavController()
            GigaGuideAdminTheme {
                if (CurrentLoginState.logged) {
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController, startDestination = SightTourListScreenObject,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                        popEnterTransition = { EnterTransition.None },
                        popExitTransition = { ExitTransition.None },
                    ) {
                        composable<SightTourListScreenObject> {
                            SearchScreen(searchScreenViewModel, navController)
                        }
                        composable<EditSightScreenClass> {
                            EditSightScreen(
                                it.toRoute<
                                        EditSightScreenClass>().sightId.toInt(),
                                editSightScreenViewModel,
                                navController
                            )
                        }
                        composable<CreateSightScreenObject> {
                            CreateSightScreen(createSightScreenViewModel, navController)
                        }
                        composable<EditTourScreenClass> {
                            EditTourScreen(it.toRoute<EditTourScreenClass>().tourId.toInt(), editTourScreenViewModel, navController)
                        }
                        composable<SightReviewScreenClass> {

                        }
                        composable<TourReviewScreenClass> {

                        }
                        composable<CreateTourScreenObject> {
                            CreateTourScreen(navController = navController, createTourScreenViewModel = createTourScreenViewModel)
                        }
                    }
                } else {
                    LoginScreen(loginScreenViewModel)
                }
            }
        }
    }
}