package com.varabyte.kobweb.material3.init

import com.varabyte.kobweb.material3.theme.colors.ColorMode

/**
 * Configuration values which are frozen at initialization time and accessed globally within Silk after that point.
 */
interface SilkConfig {
    companion object {
        val Instance: SilkConfig get() = MutableSilkConfigInstance
    }

    val initialColorMode: ColorMode
}

class MutableSilkConfig : SilkConfig {
    override var initialColorMode: ColorMode = ColorMode.LIGHT
}

internal var MutableSilkConfigInstance: MutableSilkConfig = MutableSilkConfig()
