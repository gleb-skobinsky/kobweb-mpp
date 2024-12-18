package com.varabyte.kobweb.material3.components.display

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.JsRow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.material3.components.icons.ExclaimIcon
import com.varabyte.kobweb.material3.components.icons.InfoIcon
import com.varabyte.kobweb.material3.components.icons.LightbulbIcon
import com.varabyte.kobweb.material3.components.icons.QuestionIcon
import com.varabyte.kobweb.material3.components.icons.QuoteIcon
import com.varabyte.kobweb.material3.components.icons.StopIcon
import com.varabyte.kobweb.material3.components.icons.WarningIcon
import com.varabyte.kobweb.material3.components.text.SpanText
import com.varabyte.kobweb.material3.style.ComponentKind
import com.varabyte.kobweb.material3.style.CssStyle
import com.varabyte.kobweb.material3.style.CssStyleScope
import com.varabyte.kobweb.material3.style.CssStyleVariant
import com.varabyte.kobweb.material3.style.addVariant
import com.varabyte.kobweb.material3.style.selectors.descendants
import com.varabyte.kobweb.material3.style.toModifier
import com.varabyte.kobweb.material3.theme.colors.ColorMode
import com.varabyte.kobweb.material3.theme.colors.palette.callout
import com.varabyte.kobweb.material3.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

object CalloutVars {
    val Color by StyleVariable<CSSColorValue>(prefix = "silk")
    val BackgroundColor by StyleVariable<CSSColorValue>(prefix = "silk")
    val TitleFontSize by StyleVariable<CSSLengthNumericValue>(prefix = "silk")
    val TitleGap by StyleVariable<CSSLengthNumericValue>(prefix = "silk", defaultFallback = 0.5.cssRem)
}

class CalloutType(
    val icon: @Composable () -> Unit,
    val label: String,
    provideColor: (ColorMode) -> CSSColorValue,
    provideBackgroundColor: (ColorMode) -> CSSColorValue,
) : CssStyle.Restricted.Base(init = {
    Modifier
        .setVariable(CalloutVars.Color, provideColor(colorMode))
        .setVariable(CalloutVars.BackgroundColor, provideBackgroundColor(colorMode))
}) {
    constructor(
        icon: @Composable () -> Unit,
        label: String,
        color: Color
    ) : this(
        icon,
        label,
        { color }
    )

    constructor(
        icon: @Composable () -> Unit,
        label: String,
        provideColor: (ColorMode) -> Color
    ) : this(
        icon,
        label,
        { provideColor(it) },
        { provideColor(it).toRgb().copyf(alpha = if (it.isLight) 0.15f else 0.2f) }
    )

    companion object {
        /**
         * Calls attention to something that the user should be extra careful about using.
         */
        val CAUTION = CalloutType(
            { StopIcon() },
            "Caution",
            provideColor = { it.toPalette().callout.caution },
        )

        /**
         * Important context that the user should be aware of.
         */
        val IMPORTANT = CalloutType(
            { ExclaimIcon() },
            "Important",
            provideColor = { it.toPalette().callout.important },
        )

        /**
         * Neutral information that the user should notice, even when skimming.
         */
        val NOTE = CalloutType(
            { InfoIcon() },
            "Note",
            provideColor = { it.toPalette().callout.note },
        )

        /**
         * A question posed whose answer is left as an exercise to the reader.
         */
        val QUESTION = CalloutType(
            { QuestionIcon() },
            "Question",
            provideColor = { it.toPalette().callout.question },
        )

        /**
         * A direct quote.
         */
        val QUOTE = CalloutType(
            { QuoteIcon() },
            "Quote",
            provideColor = { it.toPalette().callout.quote },
        )

        /**
         * Advice that the user may find useful.
         */
        val TIP = CalloutType(
            { LightbulbIcon() },
            "Tip",
            provideColor = { it.toPalette().callout.tip },
        )

        /**
         * Information that a user should be aware of to prevent errors.
         */
        val WARNING = CalloutType(
            { WarningIcon() },
            "Warning",
            provideColor = { it.toPalette().callout.warning },
        )
    }
}

sealed interface CalloutKind : ComponentKind

val CalloutStyle = CssStyle<CalloutKind> {
    base {
        Modifier
            .textAlign(TextAlign.Start)
            .marginBlock(both = 1.em)
    }

    cssRule(" >.callout-title") {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .fontWeight(FontWeight.Medium)
            .fontSize(CalloutVars.TitleFontSize.value())
            .gap(CalloutVars.TitleGap.value())
    }
}

// Markdown generates blockquote content wrapped in a <p> tag, which adds weird spacing that we don't want.
private fun CssStyleScope.markdownParagraphHack() {
    cssRule(" >.callout-body>p:last-child") {
        Modifier.marginBlock { end(0.px) }
    }
}

// Style from https://github.com/orgs/community/discussions/16925
val LeftBorderedCalloutVariant = CalloutStyle.addVariant {
    base {
        Modifier
            .borderLeft(0.25.em, LineStyle.Solid, CalloutVars.Color.value())
            .padding(0.5.cssRem, 1.cssRem)
    }

    cssRule(" >.callout-title") {
        Modifier
            .color(CalloutVars.Color.value())
            .margin { bottom(1.cssRem) }
    }

    markdownParagraphHack()
}

// Style from https://just-the-docs.com/docs/ui-components/callouts/
val LeftBorderedFilledCalloutVariant = CalloutStyle.addVariant {
    base {
        Modifier
            .borderLeft(0.25.em, LineStyle.Solid, CalloutVars.Color.value())
            .backgroundColor(CalloutVars.BackgroundColor.value())
            .borderRadius(4.px)
            .padding(0.8.cssRem)
            .boxShadow(
                BoxShadow.of(0.px, 1.px, 2.px, color = Colors.Black.copyf(alpha = 0.12f)),
                BoxShadow.of(0.px, 3.px, 10.px, color = Colors.Black.copyf(alpha = 0.08f)),
            )
    }

    cssRule(" >.callout-title") {
        Modifier
            .color(CalloutVars.Color.value())
            .margin { bottom(0.25.cssRem) }
    }

    markdownParagraphHack()
}


// Style from https://squidfunk.github.io/mkdocs-material/reference/admonitions/
val OutlinedCalloutVariant = CalloutStyle.addVariant {
    base {
        Modifier
            .border(1.px, LineStyle.Solid, CalloutVars.Color.value())
            .borderRadius(0.2.cssRem)
    }

    cssRule(" >.callout-title") {
        Modifier
            .backgroundColor(CalloutVars.BackgroundColor.value())
            .padding(0.5.cssRem, 0.75.cssRem)
    }

    cssRule(" .callout-icon") {
        Modifier.color(CalloutVars.Color.value())
    }

    cssRule(" >.callout-body") {
        Modifier.padding(0.5.cssRem, 0.75.cssRem)
    }

    markdownParagraphHack()
}

/**
 * A variant which makes it so any links inside the callout match the callout's color.
 *
 * Note that this variant isn't expected to be used on its own; it should be combined with another base variant.
 * For example, `LeftBorderedCalloutVariant.then(MatchingLinkCalloutVariant)`.
 */
val MatchingLinkCalloutVariant = CalloutStyle.addVariant {
    descendants(":any-link") {
        Modifier.color(CalloutVars.Color.value())
    }
}

object CalloutDefaults {
    val Variant = LeftBorderedCalloutVariant
}

@Composable
fun Callout(
    type: CalloutType,
    text: String,
    modifier: Modifier = Modifier,
    variant: CssStyleVariant<CalloutKind>? = CalloutDefaults.Variant,
    label: String? = null,
    ref: ElementRefScope<HTMLElement>? = null,
) {
    Callout(type, modifier, variant, label, ref) {
        Text(text)
    }
}

@Composable
fun Callout(
    type: CalloutType,
    modifier: Modifier = Modifier,
    variant: CssStyleVariant<CalloutKind>? = CalloutDefaults.Variant,
    label: String? = null,
    ref: ElementRefScope<HTMLElement>? = null,
    content: @Composable () -> Unit,
) {
    Div(CalloutStyle.toModifier(variant).then(type.toModifier()).then(modifier).toAttrs()) {
        registerRefScope(ref)
        JsRow(Modifier.classNames("callout-title")) {
            Box(Modifier.classNames("callout-icon"), contentAlignment = Alignment.Center) {
                type.icon()
            }
            SpanText(label ?: type.label, Modifier.classNames("callout-label"))
        }
        Div(Modifier.classNames("callout-body").toAttrs()) {
            content()
        }
    }
}
