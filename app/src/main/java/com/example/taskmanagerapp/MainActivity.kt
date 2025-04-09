package com.example.taskmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagerapp.data.*
import com.example.taskmanagerapp.ui.AddEditTaskScreen
import com.example.taskmanagerapp.ui.TaskDetailScreen
import com.example.taskmanagerapp.ui.HomeScreen
import com.example.taskmanagerapp.viewmodel.TaskViewModel
import com.example.taskmanagerapp.viewmodel.TaskViewModelFactory
import com.example.taskmanagerapp.ui.theme.TaskManagerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val context = LocalContext.current
    val db = remember { TaskDatabase.getDatabase(context) }
    val repository = remember { TaskRepository(db.taskDao()) }
    val viewModel = viewModel<TaskViewModel>(factory = TaskViewModelFactory(repository))

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onSeeTasksClick = { navController.navigate("task_list") },
                onAddTaskClick = { navController.navigate("add_task") }
            )
        }

        composable("task_list") {
            TaskListScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("add_task") },
                onTaskClick = { taskId ->
                    navController.navigate("detail/$taskId")
                }
            )
        }

        composable("add_task") {
            AddEditTaskScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val task = viewModel.tasks.collectAsState().value.find { it.id == taskId }

            task?.let {
                TaskDetailScreen(
                    task = it,
                    onBack = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    onUpdate = { updated ->
                        viewModel.updateTask(updated)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    onDelete = {
                        viewModel.deleteTask(it)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddClick: () -> Unit,
    onTaskClick: (Int) -> Unit
) {
    val taskList by viewModel.tasks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (taskList.isEmpty()) {
                item {
                    Text(
                        "No tasks yet. Tap + to add one!",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(taskList) { task ->
                    TaskItem(
                        task = task,
                        onClick = { onTaskClick(task.id) },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Due: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskManagerAppTheme {
        Text("Preview")
    }
}