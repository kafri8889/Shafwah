package ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.IceTeaVariant
import uicomponent.IceTeaVariantItem

@Composable
fun HomeScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = IceTeaVariant.entries
        ) { variant ->
            IceTeaVariantItem(
                variant = variant,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
