package com.task.management.workflow.calendar.presentation

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.task.management.workflow.calendar.domain.EventPackage
import java.time.LocalDate
import java.util.Calendar

@Composable
fun EditEventDialog(
    event: EventPackage,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description) }
    var duedate by remember { mutableStateOf(event.dueDate) }


    val calendar = Calendar.getInstance()

    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    val updateDate = {
        duedate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
    }

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
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Evento") },
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
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}