package com.example.taskmanagerapp.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.taskmanagerapp.data.Task
import com.example.taskmanagerapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var dueDate by remember { mutableStateOf("") }

    var isTitleError by remember { mutableStateOf(false) }
    var isDescriptionError by remember { mutableStateOf(false) }
    var isDueDateError by remember { mutableStateOf(false) }

    // Formatter for due date
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) }
    val calendar = remember { Calendar.getInstance() }

    // Date picker dialog
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                dueDate = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isTitleError = false
                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = isTitleError
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    isDescriptionError = false
                },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                isError = isDescriptionError
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dueDate,
                onValueChange = { },
                label = { Text("Due Date (dd/MM/yyyy)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                readOnly = true,
                isError = isDueDateError
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    isTitleError = title.text.isBlank()
                    isDescriptionError = description.text.isBlank()
                    isDueDateError = dueDate.isBlank()

                    if (!isTitleError && !isDescriptionError && !isDueDateError) {
                        viewModel.addTask(
                            Task(
                                title = title.text,
                                description = description.text,
                                dueDate = dueDate
                            )
                        )
                        onNavigateBack()
                    }
                }) {
                    Text("Save Task")
                }
            }
        }
    }
}