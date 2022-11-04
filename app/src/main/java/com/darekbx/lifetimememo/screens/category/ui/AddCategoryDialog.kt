@file:OptIn(ExperimentalMaterial3Api::class)

package com.darekbx.expenses.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(modifier = Modifier.padding(vertical = 8.dp), text = "Add new category") },
        text = {
            Spacer(Modifier.height(50.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
            )
        },
        confirmButton = { Button( onClick = { onAdd(name) }) { Text("Add") } },
        dismissButton = { Button(onClick = { onDismiss() }) { Text("Cancel") } }
    )
}