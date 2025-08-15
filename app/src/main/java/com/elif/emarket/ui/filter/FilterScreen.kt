package com.elif.emarket.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elif.emarket.R

@Composable
fun FilterScreen(
    initialFilter: FilterData,
    availableBrands: List<String>,
    availableModels: List<String>,
    onFilterChanged: (FilterData) -> Unit,
    onDismiss: () -> Unit
) {
    var currentFilter by remember { mutableStateOf(initialFilter) }
    var brandSearchText by remember { mutableStateOf("") }
    var modelSearchText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable { }, color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.filter),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                HorizontalDivider(
                    Modifier, DividerDefaults.Thickness, color = Color.Gray.copy(alpha = 0.3f)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Sort By Section
                    Text(
                        text = stringResource(R.string.sort_by),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    SortOption.values().forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentFilter = currentFilter.copy(sortBy = option)
                                }, verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = currentFilter.sortBy == option, onClick = {
                                    currentFilter = currentFilter.copy(sortBy = option)
                                }, colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Blue
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = when (option) {
                                    SortOption.OLD_TO_NEW -> stringResource(R.string.old_to_new)
                                    SortOption.NEW_TO_OLD -> stringResource(R.string.new_to_old)
                                    SortOption.PRICE_HIGH_TO_LOW -> stringResource(R.string.price_high_to_low)
                                    SortOption.PRICE_LOW_TO_HIGH -> stringResource(R.string.price_low_to_high)
                                }, fontSize = 16.sp, color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Brand Section
                    Text(
                        text = stringResource(R.string.brand),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Brand Search
                    OutlinedTextField(
                        value = brandSearchText,
                        onValueChange = { brandSearchText = it },
                        placeholder = { Text(stringResource(R.string.search)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    // Brand Checkboxes
                    val filteredBrands = availableBrands.filter {
                        it.contains(brandSearchText, ignoreCase = true)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        filteredBrands.forEach { brand ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentFilter = if (brand in currentFilter.selectedBrands) {
                                            currentFilter.copy(selectedBrands = currentFilter.selectedBrands - brand)
                                        } else {
                                            currentFilter.copy(selectedBrands = currentFilter.selectedBrands + brand)
                                        }
                                    }, verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = brand in currentFilter.selectedBrands,
                                    onCheckedChange = {
                                        currentFilter = if (brand in currentFilter.selectedBrands) {
                                            currentFilter.copy(selectedBrands = currentFilter.selectedBrands - brand)
                                        } else {
                                            currentFilter.copy(selectedBrands = currentFilter.selectedBrands + brand)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.Blue
                                    )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = brand, fontSize = 16.sp, color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Model Section
                    Text(
                        text = stringResource(R.string.model),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Model Search
                    OutlinedTextField(
                        value = modelSearchText,
                        onValueChange = { modelSearchText = it },
                        placeholder = { Text(stringResource(R.string.search)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    // Model Checkboxes
                    val filteredModels = availableModels.filter {
                        it.contains(modelSearchText, ignoreCase = true)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        filteredModels.forEach { model ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentFilter = if (model in currentFilter.selectedModels) {
                                            currentFilter.copy(selectedModels = currentFilter.selectedModels - model)
                                        } else {
                                            currentFilter.copy(selectedModels = currentFilter.selectedModels + model)
                                        }
                                    }, verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = model in currentFilter.selectedModels,
                                    onCheckedChange = {
                                        currentFilter = if (model in currentFilter.selectedModels) {
                                            currentFilter.copy(selectedModels = currentFilter.selectedModels - model)
                                        } else {
                                            currentFilter.copy(selectedModels = currentFilter.selectedModels + model)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.Blue
                                    )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = model, fontSize = 16.sp, color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Button(
                    onClick = {
                        onFilterChanged(currentFilter)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.filter),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FilterScreenPreview() {
    val initialFilter = FilterData(
        sortBy = SortOption.OLD_TO_NEW,
        selectedBrands = setOf("Apple", "Samsung"),
        selectedModels = setOf("iPhone 13", "Galaxy S21")
    )

    FilterScreen(
        initialFilter = initialFilter,
        onFilterChanged = {},
        onDismiss = {},
        availableBrands = listOf("Apple", "Samsung", "Xiaomi", "OnePlus", "Google"),
        availableModels = listOf("iPhone 13", "Galaxy S21", "Mi 11", "OnePlus 9", "Pixel 6")
    )
}