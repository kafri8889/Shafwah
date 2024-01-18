package common

import Platform
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import getPlatform

val LocalPlatform: ProvidableCompositionLocal<Platform> = compositionLocalOf { getPlatform() }
