package com.task.management.workflow.calendar.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import java.lang.reflect.Modifier
import java.util.Calendar
import androidx.compose.ui.unit.dp

import android.app.DatePickerDialog

@Composable
fun AddEventDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duedate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    val updateDate = {
        duedate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
    }

    val today = Calendar.getInstance()

    // Mostrar el DatePickerDialog cuando se pulsa el botón
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedYear = year
            selectedMonth = month
            selectedDay = dayOfMonth
            updateDate()
        },
        selectedYear,
        selectedMonth,
        selectedDay
    ).apply {
        datePicker.minDate = today.timeInMillis
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Añadir Evento") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
                // Botón para mostrar el DatePicker
                Button(onClick = { datePickerDialog.show() }) {
                    Text("Seleccionar fecha")
                }

                // Muestra la fecha seleccionada en formato yyyy-MM-dd
                Text(text = "Fecha seleccionada: $duedate")
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(title, description, duedate)
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}