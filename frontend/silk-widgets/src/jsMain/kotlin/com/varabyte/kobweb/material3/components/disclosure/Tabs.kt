package com.varabyte.kobweb.material3.components.disclosure

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.functions.calc
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.material3.style.ComponentKind
import com.varabyte.kobweb.material3.style.CssStyle
import com.varabyte.kobweb.material3.style.base
import com.varabyte.kobweb.material3.style.selectors.active
import com.varabyte.kobweb.material3.style.selectors.ariaDisabled
import com.varabyte.kobweb.material3.style.selectors.hover
import com.varabyte.kobweb.material3.style.selectors.not
import com.varabyte.kobweb.material3.style.vars.animation.TransitionDurationVars
import com.varabyte.kobweb.material3.style.vars.color.BorderColorVar
import org.jetbrains.compose.web.css.*

object TabVars {
    val Color by StyleVariable<CSSColorValue>(prefix = "silk")
    val BorderColor by StyleVariable(prefix = "silk", defaultFallback = BorderColorVar.value())
    val BackgroundColor by StyleVariable<CSSColorValue>(prefix = "silk")
    val DisabledBackgroundColor by StyleVariable<CSSColorValue>(prefix = "silk")
    val HoverBackgroundColor by StyleVariable<CSSColorValue>(prefix = "silk")
    val PressedBackgroundColor by StyleVariable<CSSColorValue>(prefix = "silk")
    val BorderThickness by StyleVariable<CSSLengthNumericValue>(prefix = "silk", defaultFallback = 2.px)
    val ColorTransitionDuration by StyleVariable(
        prefix = "silk",
        defaultFallback = TransitionDurationVars.Normal.value()
    )
}

sealed interface TabsKind : ComponentKind {
    sealed interface TabRow : ComponentKind
    sealed interface Tab : ComponentKind
    sealed interface Panel : ComponentKind
}

val TabsStyle = CssStyle<TabsKind> {}

// TODO: should this take a variant? currently it's used without one
val TabsTabRowStyle = CssStyle.base<TabsKind.TabRow> {
    Modifier
        .fillMaxWidth()
        .borderBottom(TabVars.BorderThickness.value(), LineStyle.Solid, TabVars.BorderColor.value())
}
val TabsTabStyle = CssStyle<TabsKind.Tab>(extraModifier = { Modifier.tabIndex(0) }) {
    base {
        Modifier
            .cursor(Cursor.Pointer)
            .transition(
                Transition.group(
                    listOf("background-color", "color", "border-color"), TabVars.ColorTransitionDuration.value()
                )
            )
            .backgroundColor(TabVars.BackgroundColor.value())
            .color(TabVars.Color.value())
            .userSelect(UserSelect.None)
            .padding(0.5.cssRem)
            .margin(bottom = calc { -TabVars.BorderThickness.value() })
            .borderBottom(TabVars.BorderThickness.value(), LineStyle.Solid, TabVars.BorderColor.value())
    }

    ariaDisabled {
        Modifier.backgroundColor(TabVars.DisabledBackgroundColor.value()).cursor(Cursor.NotAllowed)
    }

    (hover + not(ariaDisabled)) {
        Modifier.backgroundColor(TabVars.HoverBackgroundColor.value())
    }

    (active + not(ariaDisabled)) {
        Modifier.backgroundColor(TabVars.PressedBackgroundColor.value())
    }
}

val TabsPanelStyle = CssStyle.base<TabsKind.Panel> {
    Modifier.padding(1.cssRem).fillMaxWidth().flexGrow(1).overflow { y(Overflow.Auto) }
}

