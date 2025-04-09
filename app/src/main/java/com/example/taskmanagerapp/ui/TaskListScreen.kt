package com.example.taskmanagerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagerapp.data.Task
import com.example.taskmanagerapp.viewmodel.TaskViewModel
import androidx.compose.foundation.clickable

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
                    Text("No tasks yet. Tap + to add one!", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(taskList) { task ->
                    TaskItem(task = task, onClick = { onTaskClick(task.id) })
                }
            }
        }
    }
}

//fun TaskListScreen(viewModel: TaskViewModel, onAddClick: () -> Unit) {
//    val taskList by viewModel.tasks.collectAsState()
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = onAddClick) {
//                Icon(Icons.Default.Add, contentDescription = "Add Task")
//            }
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//        ) {
//            if (taskList.isEmpty()) {
//                item {
//                    Text(
//                        text = "No tasks yet. Tap + to add one!",
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            } else {
//                items(taskList) { task: Task ->
//                    TaskItem(task = task, onDelete = { viewModel.deleteTask(task) })
//                }
//            }
//        }
//    }
//}

@Composable
//fun TaskItem(task: Task, onDelete: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(task.title, style = MaterialTheme.typography.titleLarge)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(task.description, style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text("Due: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
//            Spacer(modifier = Modifier.height(8.dp))
//            IconButton(onClick = onDelete) {
//                Icon(Icons.Default.Delete, contentDescription = "Delete")
//            }
//        }
//    }
//}
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = task.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
