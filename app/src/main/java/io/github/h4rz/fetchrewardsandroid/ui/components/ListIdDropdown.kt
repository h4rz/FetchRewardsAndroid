package io.github.h4rz.fetchrewardsandroid.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListIdDropdown(
    availableListIds: List<Int>,
    selectedListId: Int?,
    onListIdSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (selectedListId != null) "List ID $selectedListId" else "All items",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Default "All Items" option
            DropdownMenuItem(
                text = { Text("All Items") },
                onClick = {
                    onListIdSelected(null)
                    expanded = false
                }
            )

            // Individual listId options
            availableListIds.forEach { listId ->
                DropdownMenuItem(
                    text = { Text("List ID: $listId") },
                    onClick = {
                        onListIdSelected(listId)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListIdDropdownPreview() {
    var selectedListId by remember { mutableStateOf<Int?>(null) }
    val availableListIds = listOf(1, 2, 3, 4, 5)

    ListIdDropdown(
        availableListIds = availableListIds,
        selectedListId = selectedListId,
        onListIdSelected = { selectedListId = it }
    )
}