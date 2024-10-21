@file:Suppress("FunctionName", "DeprecatedCallableAddReplaceWith", "DEPRECATION")

package com.varabyte.kobweb.material3.components.style

import androidx.compose.runtime.*
import com.varabyte.kobweb.browser.util.titleCamelCaseToKebabCase
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.material3.components.util.internal.CacheByPropertyNameDelegate
import com.varabyte.kobweb.material3.style.ComponentKind
import com.varabyte.kobweb.material3.style.CssStyle
import com.varabyte.kobweb.material3.style.CssStyleBaseScope
import com.varabyte.kobweb.material3.style.CssStyleScope
import com.varabyte.kobweb.material3.style.CssStyleVariant
import com.varabyte.kobweb.material3.style.GeneralKind
import com.varabyte.kobweb.material3.style.RestrictedKind
import com.varabyte.kobweb.material3.style.SimpleCssStyle
import com.varabyte.kobweb.material3.style.combine
import com.varabyte.kobweb.material3.style.toAttrs
import com.varabyte.kobweb.material3.style.toModifier
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*

/**
 * A class which allows a user to define styles that get added to the page's stylesheet, instead of inline styles.
 *
 * This is important because some functionality is only available when defined in the stylesheet, e.g. link colors,
 * media queries, and pseudo classes.
 *
 * If defining a style for a custom widget, you should call the [toModifier] method to apply it:
 *
 * ```
 * val CustomStyle by ComponentStyle { ... }
 *
 * @Composable
 * fun CustomWidget(..., variant: ComponentVariant? = null, ...) {
 *   val modifier = CustomStyle.toModifier(variant).then(...)
 *   // ^ This modifier is now set with your registered styles.
 * }
 * ```
 *
 * @param name The name of the style, which will be used as the CSS class name.
 * @param extraModifiers Additional modifiers that can be tacked onto this component style, convenient for including
 *   non-style attributes whenever this style is applied.
 * @param prefix An optional prefix to prepend in front of the style name, as a helpful tool for reducing the chance of
 *   style name collisions. (Note: unless you are a library author, it's not expected you'll set this.) Why not just put
 *   the prefix directly in the name itself? We allow separating it out since you can use delegation to create a style,
 *   at which point the name will be derived from the style's property name. In contrast, a prefix will be manually
 *   chosen. For a concrete example, `val ButtonStyle by ComponentStyle(prefix = "silk")` creates the full name
 *   `"silk-button"`). Also, when creating a variant by delegation, it is useful to know the non-prefixed name of the
 *   style it is based on when creating a name for it.
 */
@Deprecated("You probably want to use `CssStyle` instead. Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
class ComponentStyle(
    name: String,
    internal val extraModifiers: @Composable () -> Modifier,
    val prefix: String? = null,
    internal val init: CssStyleScope.() -> Unit,
) {
    init {
        require(name.isNotEmpty()) { "ComponentStyle name must not be empty" }
    }

    internal val nameWithoutPrefix = name
    val name = prefix?.let { "$it-$name" } ?: name
    internal val cssStyle = SimpleCssStyle(".${this.name}", init, extraModifiers, layer = null)

    constructor(
        name: String,
        extraModifiers: Modifier = Modifier,
        prefix: String? = null,
        init: CssStyleScope.() -> Unit
    )
        : this(name, { extraModifiers }, prefix, init)

    companion object // for extensions

    /**
     * Create a new variant that builds on top of this style.
     *
     *  @param extraModifiers Additional modifiers that can be tacked onto this component style, convenient for
     *    including non-style attributes that should always get applied whenever this variant style is applied.
     */
    fun addVariant(
        name: String,
        extraModifiers: Modifier = Modifier,
        init: CssStyleScope.() -> Unit
    ): ComponentVariant {
        return addVariant(name, { extraModifiers }, init)
    }

    /**
     * Create a new variant that builds on top of this style.
     *
     *  @param extraModifiers Additional modifiers that can be tacked onto this component style, convenient for
     *    including non-style attributes that should always get applied whenever this variant style is applied.
     */
    fun addVariant(
        name: String,
        extraModifiers: @Composable () -> Modifier,
        init: CssStyleScope.() -> Unit
    ): ComponentVariant {
        val fullName = "${this.name}-$name"
        return SimpleComponentVariant(
            fullName,
            SimpleCssStyle(".$fullName", init, extraModifiers, layer = null),
            baseStyle = this
        )
    }
}

/**
 * Convenience method when you only care about registering the base style, which can help avoid a few extra lines.
 *
 * So this:
 *
 * ```
 * CssStyle.base {
 *   Modifier.fontSize(48.px)
 * }
 * ```
 *
 * replaces this:
 *
 * ```
 * ComponentStyle {
 *   base {
 *     Modifier.fontSize(48.px)
 *   }
 * }
 * ```
 *
 * You may still wish to construct a [ComponentStyle] directly instead if you expect that at some point in the future
 * you'll want to add additional, non-base styles.
 */
@Deprecated("You probably want to use `CssStyle.base` instead (and use `@CssName` to specify your custom name). Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle.Companion.base(
    className: String,
    extraModifiers: Modifier = Modifier,
    init: CssStyleBaseScope.() -> Modifier
): ComponentStyle {
    return base(className, { extraModifiers }, init)
}

@Deprecated("You probably want to use `CssStyle.base` instead (and use `@CssName` to specify your custom name). Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle.Companion.base(
    className: String,
    extraModifiers: @Composable () -> Modifier,
    init: CssStyleBaseScope.() -> Modifier
): ComponentStyle {
    return ComponentStyle(className, extraModifiers) {
        base {
            CssStyleBaseScope(colorMode).let(init)
        }
    }
}

/**
 * A delegate provider class which allows you to create a [ComponentStyle] via the `by` keyword.
 */
class ComponentStyleProvider internal constructor(
    private val extraModifiers: @Composable () -> Modifier,
    private val prefix: String? = null,
    private val init: CssStyleScope.() -> Unit,
) : CacheByPropertyNameDelegate<ComponentStyle>() {
    override fun create(propertyName: String): ComponentStyle {
        // e.g. "TitleTextStyle" to "title-text"
        val name = propertyName.removeSuffix("Style").titleCamelCaseToKebabCase()
        return ComponentStyle(name, extraModifiers, prefix, init)
    }
}

@Deprecated("You probably want to use `CssStyle` instead. Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle(
    extraModifiers: Modifier = Modifier,
    prefix: String? = null,
    init: CssStyleScope.() -> Unit
) =
    ComponentStyle({ extraModifiers }, prefix, init)

@Deprecated("You probably want to use `CssStyle` instead. Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle(
    extraModifiers: @Composable () -> Modifier,
    prefix: String? = null,
    init: CssStyleScope.() -> Unit
) = ComponentStyleProvider(extraModifiers, prefix, init)

@Deprecated("You probably want to use `CssStyle.base` instead. Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle.Companion.base(
    extraModifiers: Modifier = Modifier,
    prefix: String? = null,
    init: CssStyleBaseScope.() -> Modifier
) = base({ extraModifiers }, prefix, init)

@Deprecated("You probably want to use `CssStyle.base` instead. Please see https://github.com/varabyte/kobweb/blob/main/docs/css-style.md#migration for more guidance.")
fun ComponentStyle.Companion.base(
    extraModifiers: @Composable () -> Modifier,
    prefix: String? = null,
    init: CssStyleBaseScope.() -> Modifier
) = ComponentStyleProvider(extraModifiers, prefix, init = { base { CssStyleBaseScope(colorMode).let(init) } })

/**
 * Convert a user's component style into a [Modifier].
 *
 * @param variants 0 or more variants that can potentially extend the base style. Although it may seem odd at first that
 *   nullable values are accepted here, that's because Silk widgets all default their `variant` parameter to null, so
 *   it's easier to just accept null here rather than require users to branch based on whether the variant is null or
 *   not.
 */
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
@Composable
fun ComponentStyle.toModifier(vararg variants: ComponentVariant?): Modifier {
    return cssStyle.toModifier()
        .then(variants.toList().combine()?.toModifier() ?: Modifier)
}

/**
 * Convert a user's component style into an [AttrsScope] builder.
 *
 * This is useful if you need to convert a style into something directly consumable by a Compose HTML widget.
 */
@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toAttrs`.")
fun <A : AttrsScope<*>> ComponentStyle.toAttrs(
    vararg variant: ComponentVariant?,
    finalHandler: (A.() -> Unit)? = null
): A.() -> Unit {
    return this.toModifier(*variant).toAttrs(finalHandler)
}

/**
 * A convenience method for chaining a collection of styles into a single modifier.
 *
 * This can be useful as sometimes you might break up many css rules across multiple styles for re-usability, and it's
 * much easier to type `listOf(Style1, Style2, Style3).toModifier()` than
 * `Style1.toModifier().then(Style2.toModifier())...`
 */
@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
fun Iterable<ComponentStyle>.toModifier(): Modifier {
    return fold<_, Modifier>(Modifier) { acc, style -> acc.then(style.toModifier()) }
}

/**
 * A convenience method for chaining a collection of styles into a single [AttrsScope] builder.
 */
@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toAttrs`.")
fun <A : AttrsScope<*>> Iterable<ComponentStyle>.toAttrs(finalHandler: (A.() -> Unit)? = null): A.() -> Unit {
    return this.toModifier().toAttrs(finalHandler)
}

// region Deprecated

// The following methods are only provided so that a user's old code will still compile.
// For example, `LinkStyle` used to be a `silk.components.style.ComponentStyle`, but now it's a
// `silk.style.component.CssStyle<T>`. If the user has `LinkStyle.toModifier()` in their code, it should continue
// to compile!

@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
fun <K : ComponentKind> CssStyle<K>.toModifier(vararg variants: CssStyleVariant<K>?): Modifier {
    return toModifier()
        .then(variants.toList().combine()?.toModifier() ?: Modifier)
}

@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
fun CssStyle<GeneralKind>.toModifier(): Modifier {
    return toModifier()
}

@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
fun CssStyle<RestrictedKind>.toModifier(): Modifier {
    return toModifier()
}

@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toModifier`.")
fun Iterable<CssStyle<GeneralKind>>.toModifier(): Modifier {
    return toModifier()
}

@Composable
@Deprecated("Please change the import for this extension method to `com.varabyte.kobweb.silk.style.toAttrs`.")
fun <A : AttrsScope<*>> Iterable<CssStyle<GeneralKind>>.toAttrs(finalHandler: (A.() -> Unit)? = null): A.() -> Unit {
    return toAttrs(finalHandler)
}

// endregion
