package com.varabyte.kobweb.material3.style

import com.varabyte.kobweb.compose.attributes.ComparableAttrsScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.material3.style.breakpoint.Breakpoint
import com.varabyte.kobweb.material3.theme.breakpoint.toMinWidthQuery
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.w3c.dom.Element

@Deprecated(
    "The name `StyleModifiers` has been renamed to `StyleScope`.",
    replaceWith = ReplaceWith("StyleScope", "com.varabyte.kobweb.silk.style.StyleScope")
)
typealias StyleModifiers = StyleScope

/**
 * Class used as the receiver to a callback, allowing the user to define various state-dependent styles (defined via
 * [Modifier]s).
 *
 * See also [CssStyleScope] which extends this class to provide additional functionality for defining `CssStyle` blocks.
 */
abstract class StyleScope {
    private val _cssModifiers = mutableListOf<CssModifier>()
    internal val cssModifiers: List<CssModifier> = _cssModifiers

    /** Define base styles for this component. This will always be applied first. */
    fun base(createModifier: () -> Modifier) {
        _cssModifiers.add(CssModifier(createModifier()))
    }

    /**
     * Add a CSS rule that is applied to this component class, passing in a [suffix] (which represents a pseudo-class
     * or pseudo-element) and a [mediaQuery] entry if the style should be defined within a media rule.
     *
     * CSS rules will always be applied in the order they were registered in.
     *
     * See also:
     *   https://developer.mozilla.org/en-US/docs/Web/CSS/@media
     *   https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-classes
     *   https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-elements
     */
    fun cssRule(mediaQuery: CSSMediaQuery?, suffix: String?, createModifier: () -> Modifier) {
        _cssModifiers.add(CssModifier(createModifier(), mediaQuery, suffix))
    }

    fun cssRule(suffix: String, createModifier: () -> Modifier) {
        _cssModifiers.add(CssModifier(createModifier(), null, suffix))
    }

    fun cssRule(mediaQuery: CSSMediaQuery, createModifier: () -> Modifier) {
        _cssModifiers.add(CssModifier(createModifier(), mediaQuery))
    }

    /**
     * Convenience function for associating a modifier directly against a breakpoint enum.
     *
     * For example, you can call
     *
     * ```
     * Breakpoint.MD { Modifier.color(...) }
     * ```
     *
     * which is identical to:
     *
     * ```
     * cssRule(CSSMediaQuery.MediaFeature("min-width", ...)) { Modifier.color(...) }
     * ```
     *
     * Note: This probably would have been an extension method except Kotlin doesn't support multiple receivers yet
     * (here, we'd need to access both "Breakpoint" and "StyleScope")
     */
    operator fun Breakpoint.invoke(createModifier: () -> Modifier) {
        cssRule(this.toMinWidthQuery(), createModifier)
    }
}

/**
 * Convenience method for tying a general CSS rule to a breakpoint.
 *
 * This is equivalent to:
 *
 * ```
 * cssRule(breakpoint.toMinWidthQuery(), suffix, createModifier)
 * ```
 *
 * but when people use a breakpoint, like Breakpoint.MD, they almost always want to use the min-width query.
 */
fun StyleScope.cssRule(breakpoint: Breakpoint, suffix: String?, createModifier: () -> Modifier) {
    cssRule(breakpoint.toMinWidthQuery(), suffix, createModifier)
}

// The
// For example, ".myclass:hover" separates ".myclass" from ":hover".
// See: https://www.w3schools.com/cssref/css_selectors.php and https://www.w3schools.com/cssref/trysel.php
private val selectorSeparators = setOf(' ', '>', '+', '~', '.', ':', ',', '[')

/**
 * Represents a [Modifier] entry that is tied to a css rule, e.g. the modifier for ".myclass:hover" for example.
 */
internal class CssModifier(
    val modifier: Modifier,
    val mediaQuery: CSSMediaQuery? = null,
    suffix: String? = null,
) {
    // People might use e.g. "h1" as a suffix, but it has to be " h1" (leading space) to avoid running into the previous
    // part of the selector (e.g. ".myclass h1", not ".myclassh1"). Let's detect this ourselves and add the space, since
    // we understand the user's intentions (and forgetting the space is really hard to debug).
    val suffix: String? =
        suffix?.takeIf { it.isNotBlank() }?.let { if (it.first() !in selectorSeparators ) " $it" else it }

    internal fun mergeWith(other: CssModifier): CssModifier {
        check(this !== other && mediaQuery == other.mediaQuery && suffix == other.suffix)
        return CssModifier(modifier.then(other.modifier), mediaQuery, suffix)
    }

    companion object {
        // We use this key to represent the base CSS rule, which is always applied first
        internal val BaseKey = Key(null, null)
    }

    data class Key(val mediaQuery: String?, val suffix: String?)

    /**
     * A key useful for storing this entry into a map.
     *
     * If two [CssModifier] instances have the same key, that means they would evaluate to the same CSS rule. Although
     * we don't expect this to happen in practice, if it does, then both selectors can be merged. We can also use this
     * key to test a light and dark version of the same component style to see if this particular selector is the same
     * or not across the two.
     */
    // Note: We have to convert mediaQuery toString for now because CSSMediaQuery.MediaFeature is not itself defined
    // correctly for equality checking (for some reason, they don't define the hashcode)
    val key get() = Key(mediaQuery?.toString(), suffix)
}

internal fun CssModifier.assertNoAttributes(selectorName: String, extraContext: String) {
    val attrsScope = ComparableAttrsScope<Element>()
    this.modifier.toAttrs<AttrsScope<Element>>().invoke(attrsScope)

    if (attrsScope.attributes.isEmpty()) return

    error(buildString {
        appendLine("Style block declarations cannot contain Modifiers that specify attributes. Only style modifiers are allowed here.")
        appendLine()
        appendLine("Details:")

        append("\tCSS rule: ")
        append("\"$selectorName")
        if (mediaQuery != null) append(mediaQuery)
        if (suffix != null) append(suffix)
        appendLine("\"")
        appendLine("\tAttribute(s): ${attrsScope.attributes.keys.joinToString(", ") { "\"$it\"" }}")
        appendLine()
        appendLine(extraContext)
    })
}
