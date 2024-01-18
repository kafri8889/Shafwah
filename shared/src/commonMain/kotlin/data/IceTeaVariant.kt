package data

import ICE_TEA_VARIANT_CAFFE_LATTE
import ICE_TEA_VARIANT_CHOCOLATE_MILK
import ICE_TEA_VARIANT_COOKIES
import ICE_TEA_VARIANT_GREEN_TEA
import ICE_TEA_VARIANT_LEMON
import ICE_TEA_VARIANT_LYCHEE
import ICE_TEA_VARIANT_MANGO
import ICE_TEA_VARIANT_MILK
import ICE_TEA_VARIANT_ORIGINAL
import ICE_TEA_VARIANT_PEACH
import ICE_TEA_VARIANT_STRAWBERRY
import ICE_TEA_VARIANT_TARO
import ICE_TEA_VARIANT_YAKULT

enum class IceTeaVariant {
    Original,
    Lemon,
    Lychee,
    Mango,
    Strawberry,
    Taro,
    Milk,
    Yakult,
    Peach,
    ChocolateMilk,
    Cookies,
    CaffeLatte,
    GreenTea;

    val displayName: String
        get() = when (this) {
            Original -> "Original"
            Lemon -> "Lemon Tea"
            Lychee -> "Lychee Tea"
            Mango -> "Mango Tea"
            Strawberry -> "Strawberry"
            Taro -> "Taro"
            Milk -> "Milk Tea"
            Yakult -> "Yakult Tea"
            Peach -> "Peach Tea"
            ChocolateMilk -> "Chocolate Milk Tea"
            Cookies -> "Cookies Milk Tea"
            CaffeLatte -> "Caffe Latte Tea"
            GreenTea -> "Green Tea"
        }

    val price: Double
        get() = when (this) {
            Original -> ICE_TEA_VARIANT_ORIGINAL
            Lemon -> ICE_TEA_VARIANT_LEMON
            Lychee -> ICE_TEA_VARIANT_LYCHEE
            Mango -> ICE_TEA_VARIANT_MANGO
            Strawberry -> ICE_TEA_VARIANT_STRAWBERRY
            Taro -> ICE_TEA_VARIANT_TARO
            Milk -> ICE_TEA_VARIANT_MILK
            Yakult -> ICE_TEA_VARIANT_YAKULT
            Peach -> ICE_TEA_VARIANT_PEACH
            ChocolateMilk -> ICE_TEA_VARIANT_CHOCOLATE_MILK
            Cookies -> ICE_TEA_VARIANT_COOKIES
            CaffeLatte -> ICE_TEA_VARIANT_CAFFE_LATTE
            GreenTea -> ICE_TEA_VARIANT_GREEN_TEA
        }

    val drawableRes: String
        get() = when (this) {
            Original -> "original_ice_tea.jpg"
            Lemon -> "lemon_ice_tea.jpg"
            Lychee -> "lychee_ice_tea.jpg"
            Mango -> "mango_ice_tea.jpg"
            Strawberry -> "strawberry_ice_tea.jpg"
            Taro -> "taro_ice_tea.jpg"
            Milk -> "milktea_ice_tea.jpg"
            Yakult -> "yakult_ice_tea.jpg"
            Peach -> "peach_ice_tea.jpg"
            ChocolateMilk -> "chocho_milktea_ice_tea.jpg"
            Cookies -> "cookies_ice_tea.jpg"
            CaffeLatte -> "caffe_latte_ice_tea.jpg"
            GreenTea -> "greentea_ice_tea.jpg"
        }
}
