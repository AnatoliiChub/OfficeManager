package com.chub.officemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.ui.screens.OfficeItemsScreen
import com.chub.officemanager.ui.theme.OfficeManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfficeManagerTheme {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = OfficeDestinations.OFFICE_ITEMS_ROUTE,

                    ) {
                    composable(
                        OfficeDestinations.OFFICE_ITEMS_ROUTE
                    ) { entry ->
                        OfficeItemsScreen(
                            items = listOf(
                                OfficeItem(1, "35", "A desk for working", "Desk"),
                                OfficeItem(2, "32", "A chair for sitting", "Chair"),
                                OfficeItem(3, "10w LED", "A lamp for lighting", "Lamp"),
                                OfficeItem(4, "Dell 24HL", "A monitor for viewing", "Monitor"),
                                OfficeItem(5, "Logitech 121", "A keyboard for typing", "Keyboard"),
                                OfficeItem(6, "Logitech 121", "A mouse for clicking", "Mouse"),
                                OfficeItem(7, "Max", "A person for working", "Employee"),
                                OfficeItem(8, "Dexter", "A person for working", "Employee"),
                                OfficeItem(9, "John", "A person for working", "Employee"),
                            )
                        )
                    }
//                    composable(
//                        OfficeDestinations.ADD_EDIT_OFFICE_ITEM_ROUTE,
//                        arguments = listOf(navArgument(ITEM_ID) { type = NavType.StringType })
//                    ) { entry ->
//
//                    }
                }
            }
        }
    }
}