package com.varabyte.kobweb.material3.init

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.material3.components.document.TocBorderedVariant
import com.varabyte.kobweb.material3.components.document.TocStyle
import com.varabyte.kobweb.material3.components.graphics.FitWidthImageVariant
import com.varabyte.kobweb.material3.components.graphics.ImageStyle
import com.varabyte.kobweb.material3.components.navigation.AlwaysUnderlinedLinkVariant
import com.varabyte.kobweb.material3.components.navigation.LinkStyle
import com.varabyte.kobweb.material3.components.navigation.LinkVars
import com.varabyte.kobweb.material3.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.material3.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.material3.theme.colors.palette.link
import com.varabyte.kobweb.material3.theme.colors.palette.toPalette
import com.varabyte.kobweb.material3.theme.modifyStyleBase

// Note: This expects to be called after `initSilkWidgets` is called first.
fun initSilkWidgetsKobweb(ctx: InitSilkContext) {
    val mutableTheme = ctx.theme

    mutableTheme.palettes.apply {
        light.apply {
            link.set(
                default = Colors.Blue,
                visited = Colors.Purple,
            )
        }
        dark.apply {
            link.set(
                default = Colors.Cyan,
                visited = Colors.Violet,
            )
        }
    }

    mutableTheme.modifyStyleBase(SilkColorsStyle) {
        val palette = colorMode.toPalette()
        Modifier
            .setVariable(LinkVars.DefaultColor, palette.link.default)
            .setVariable(LinkVars.VisitedColor, palette.link.visited)
    }

    // TODO: Automate the creation of this list (with a Gradle task?)

    mutableTheme.registerStyle("silk-image", ImageStyle)
    mutableTheme.registerVariant("-fit-width", FitWidthImageVariant)

    mutableTheme.registerStyle("silk-link", LinkStyle)
    mutableTheme.registerVariant("-uncolored", UncoloredLinkVariant)
    mutableTheme.registerVariant("-undecorated", UndecoratedLinkVariant)
    mutableTheme.registerVariant("-always-underlined", AlwaysUnderlinedLinkVariant)

    mutableTheme.registerStyle("silk-toc", TocStyle)
    mutableTheme.registerVariant("-bordered", TocBorderedVariant)
}
