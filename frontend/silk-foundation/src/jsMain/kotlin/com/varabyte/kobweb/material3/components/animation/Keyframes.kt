@file:Suppress("DEPRECATION")

package com.varabyte.kobweb.material3.components.animation

import androidx.compose.runtime.*
import com.varabyte.kobweb.browser.util.titleCamelCaseToKebabCase
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.CSSAnimation
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.material3.components.style.ComponentStyle
import com.varabyte.kobweb.material3.components.util.internal.CacheByPropertyNameDelegate
import com.varabyte.kobweb.material3.init.SilkStylesheet
import com.varabyte.kobweb.material3.style.animation.KeyframesBuilder
import com.varabyte.kobweb.material3.theme.colors.ColorMode
import com.varabyte.kobweb.material3.theme.colors.suffixedWith
import org.jetbrains.compose.web.css.*

/**
 * Define a set of keyframes that can later be references in animations.
 *
 * For example,
 *
 * ```
 * val BounceKeyframes = Keyframes("bounce") {
 *   from { Modifier.translateX((-50).percent) }
 *   to { Modifier.translateX((50).percent) }
 * }
 *
 * // Later
 * Div(
 *   Modifier
 *     .size(100.px).backgroundColor(Colors.Red)
 *     .animation(BounceKeyframes.toAnimation(
 *       duration = 2.s,
 *       timingFunction = AnimationTimingFunction.EaseIn,
 *       direction = AnimationDirection.Alternate,
 *       iterationCount = AnimationIterationCount.Infinite
 *     ))
 *     .toAttrs()
 * )
 * ```
 *
 * Note: You should prefer to create keyframes using the [Keyframes] delegate method to avoid needing to duplicate the
 * property name, e.g.
 *
 * ```
 * val BounceKeyframes by Keyframes {
 *   from { Modifier.translateX((-50).percent) }
 *   to { Modifier.translateX((50).percent) }
 * }
 * ```
 *
 * If you are not using Kobweb, e.g. if you're using these widgets as a standalone library, you will have to use an
 * `@InitSilk` block to register your keyframes:
 *
 * ```
 * val BounceKeyframes = Keyframes("bounce") { ... }
 * @InitSilk
 * fun initSilk(ctx: InitSilkContext) {
 *   ctx.stylesheet.registerKeyframes(BounceKeyframes)
 * }
 * ```
 *
 * Otherwise, the Kobweb Gradle plugin will do this for you.
 */
@Deprecated("Use `com.varabyte.kobweb.silk.style.animation.Keyframes` instead. Use `@CssName` and `@CssPrefix` to specify a custom name or prefix if necessary.")
class Keyframes(name: String, prefix: String? = null, internal val init: KeyframesBuilder.() -> Unit) {
    val name = prefix?.let { "$it-$name" } ?: name

    companion object {
        internal fun isColorModeAgnostic(build: KeyframesBuilder.() -> Unit): Boolean {
            // A user can use colorMode checks to change the keyframes builder, either by completely changing what sort
            // of keyframes show up across the light version and the dark version, or (more commonly) keeping the same
            // keyframes but changing some color values in the styles.
            return listOf(ColorMode.LIGHT, ColorMode.DARK)
                .map { colorMode -> KeyframesBuilder(colorMode).apply(build) }
                .distinct().count() == 1
        }
    }

    // Note: Need to postpone checking this value, because color modes aren't ready until after a certain point in
    // Silk's initialization.
    val usesColorMode by lazy { !isColorModeAgnostic(init) }
}

/**
 * A delegate provider class which allows you to create a [Keyframes] instance via the `by` keyword.
 */
class KeyframesProvider internal constructor(
    private val prefix: String?,
    private val init: KeyframesBuilder.() -> Unit
) : CacheByPropertyNameDelegate<Keyframes>() {
    override fun create(propertyName: String): Keyframes {
        val name = propertyName
            .removeSuffix("Anim")
            .removeSuffix("Animation")
            .removeSuffix("Keyframes")
            .titleCamelCaseToKebabCase()
        return Keyframes(name, prefix, init)
    }
}

@Deprecated("Please call `SilkTheme.registerKeyframes` instead.")
fun SilkStylesheet.registerKeyframes(keyframes: Keyframes) = registerKeyframes(keyframes.name, keyframes.init)

/**
 * Construct a [Keyframes] instance where the name comes from the variable name.
 *
 * For example,
 *
 * ```
 * val Bounce by Keyframes { ... }
 * ```
 *
 * creates a keyframe entry into the site stylesheet (provided by Silk) with the name "bounce".
 *
 * Title camel case gets converted to snake case, so if the variable was called "AnimBounce", the final name added to
 * the style sheet would be "anim-bounce"
 *
 * Note: You can always construct a [Keyframes] object directly if you need to control the name, e.g.
 *
 * ```
 * // Renamed "Bounce" to "LegacyBounce" but don't want to break some old code.
 * val LegacyBounce = Keyframes("bounce") { ... }
 * ```
 */
@Suppress("FunctionName") // name chosen to look like a constructor intentionally
@Deprecated("Use `com.varabyte.kobweb.silk.style.animation.Keyframes` instead. Use `@CssName` and `@CssPrefix` to specify a custom name or prefix if necessary.")
fun Keyframes(prefix: String? = null, init: KeyframesBuilder.() -> Unit) = KeyframesProvider(prefix, init)

/**
 * A convenience method to convert this [Keyframes] instance into an object that can be passed into [Modifier.animation].
 *
 * This version of the method is [Composable] because it's aware of the site's current color mode.
 *
 * @see ColorMode.currentState
 */
@Composable
fun Keyframes.toAnimation(
    duration: CSSTimeNumericValue? = null,
    timingFunction: AnimationTimingFunction? = null,
    delay: CSSTimeNumericValue? = null,
    iterationCount: AnimationIterationCount? = null,
    direction: AnimationDirection? = null,
    fillMode: AnimationFillMode? = null,
    playState: AnimationPlayState? = null
): CSSAnimation {
    val colorMode = if (this.usesColorMode) ColorMode.current else null
    return toAnimation(colorMode, duration, timingFunction, delay, iterationCount, direction, fillMode, playState)
}

/**
 * A convenience method to convert this [Keyframes] instance into an object that can be passed into [Modifier.animation].
 *
 * This version of the method is not [Composable] and requires the user pass in a [ColorMode] explicitly, especially to
 * distinguish it from the other [toAnimation] method.
 *
 * If you defined a [Keyframes] that uses references the site's color mode, it is an error if you pass in [colorMode] is
 * null. Alternately, if the [Keyframes] doesn't reference the site's color mode in its definition, then whatever color
 * mode is passed in is ignored.
 *
 * It can be useful to call this method from within a [ComponentStyle]. For example:
 *
 * ```
 * val MyAnimatedStyle = CssStyle {
 *   after {
 *     Modifier.animation(AnimOut.toAnimation(colorMode, ...))
 *   }
 * }
 * ```
 */
fun Keyframes.toAnimation(
    colorMode: ColorMode?,
    duration: CSSTimeNumericValue? = null,
    timingFunction: AnimationTimingFunction? = null,
    delay: CSSTimeNumericValue? = null,
    iterationCount: AnimationIterationCount? = null,
    direction: AnimationDirection? = null,
    fillMode: AnimationFillMode? = null,
    playState: AnimationPlayState? = null,
): CSSAnimation {
    @Suppress("NAME_SHADOWING")
    val colorMode = if (this.usesColorMode) {
        colorMode ?: error("Animation $name depends on the site's color mode but no color mode was specified.")
    } else {
        null
    }

    val finalName = if (colorMode != null) {
        this.name.suffixedWith(colorMode)
    } else {
        this.name
    }

    return CSSAnimation(
        finalName,
        duration,
        timingFunction,
        delay,
        iterationCount,
        direction,
        fillMode,
        playState
    )
}
