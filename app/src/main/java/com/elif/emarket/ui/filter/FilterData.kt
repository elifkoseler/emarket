package com.elif.emarket.ui.filter

// FilterData.kt
data class FilterData(
    val sortBy: SortOption = SortOption.OLD_TO_NEW,
    val selectedBrands: Set<String> = emptySet(),
    val selectedModels: Set<String> = emptySet()
)

enum class SortOption {
    OLD_TO_NEW,
    NEW_TO_OLD,
    PRICE_HIGH_TO_LOW,
    PRICE_LOW_TO_HIGH
}
