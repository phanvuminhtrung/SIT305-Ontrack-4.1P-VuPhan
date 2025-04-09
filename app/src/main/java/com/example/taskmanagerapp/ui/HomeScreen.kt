package com.example.taskmanagerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onSeeTasksClick: () -> Unit,
    onAddTaskClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSeeTasksClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("See All Tasks")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddTaskClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add New Task")
            }
        }
    }
}
