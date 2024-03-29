package uicomponent

import CurrencyUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.IceTeaVariant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun IceTeaOrderItem(
    count: Int,
    variant: IceTeaVariant,
    modifier: Modifier = Modifier
) = CoreOrderItem(
    count = count,
    name = variant.displayName,
    price = variant.price,
    modifier = modifier,
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
fun DimsumOrderItem(
    count: Int,
    name: String,
    price: Double,
    imageRes: String,
    modifier: Modifier = Modifier
) = CoreOrderItem(
    count = count,
    name = name,
    price = price,
    modifier = modifier,
    image = {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
        )
    }
)

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun CoreOrderItem(
    count: Int,
    name: String,
    price: Double,
    modifier: Modifier = Modifier,
    image: @Composable BoxScope.() -> Unit
) {

    BoxWithConstraints {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10))
            ) {
                image()
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .height(this@BoxWithConstraints.maxHeight)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "x$count",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        text = CurrencyUtil.toRupiah(price.toInt() * count),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
