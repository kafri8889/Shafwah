package uicomponent

import CurrencyUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.IceTeaVariant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun IceTeaProductItem(
    count: Int,
    variant: IceTeaVariant,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) = ProductItemCore(
    count = count,
    price = variant.price,
    modifier = modifier,
    onValueChange = onValueChange,
    image = {
        Image(
            painter = painterResource(variant.drawableRes),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
        )
    }
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DimsumProductItem(
    count: Int,
    price: Double,
    imageRes: String,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) = ProductItemCore(
    count = count,
    price = price,
    modifier = modifier,
    onValueChange = onValueChange,
    image = {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .matchParentSize()
        )
    }
)

@Composable
private fun ProductItemCore(
    count: Int,
    price: Double,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit,
    image: @Composable BoxScope.() -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f/1f)
        ) {
            image()
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = CurrencyUtil.toRupiah(price.toInt()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        CountButton(
            count = count,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(0.64f)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CountButton(
    count: Int,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.32f))
            .padding(4.dp)
    ) {
        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.White
            ),
            onClick = {
                onValueChange(count - 1)
            },
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
        }

        BasicTextField(
            value = count.toString(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { newCount ->
                onValueChange(
                    newCount
                        .filter { it.isDigit() }
                        .toIntOrNull() ?: 0
                )
            },
            modifier = Modifier
                .weight(1f)
        )

        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                onValueChange(count + 1)
            },
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}
