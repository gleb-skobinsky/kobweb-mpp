package com.varabyte.kobweb.compose.ui

import androidx.compose.runtime.*

actual typealias Modifier = JsModifier

actual typealias ModifierElement = JsModifier.Element

@Suppress("ModifierFactoryExtensionFunction")
@Stable
interface JsModifier {
    fun <R> foldIn(initial: R, operation: (R, Element) -> R): R
    fun <R> foldOut(initial: R, operation: (Element, R) -> R): R
    fun any(predicate: (Element) -> Boolean): Boolean
    fun all(predicate: (Element) -> Boolean): Boolean

    infix fun then(other: Modifier): Modifier =
        if (other === Modifier) this else CombinedModifier(this, other)

    interface Element : Modifier {
        override fun <R> foldIn(initial: R, operation: (R, Element) -> R): R =
            operation(initial, this)

        override fun <R> foldOut(initial: R, operation: (Element, R) -> R): R =
            operation(this, initial)

        override fun any(predicate: (Element) -> Boolean): Boolean = predicate(this)

        override fun all(predicate: (Element) -> Boolean): Boolean = predicate(this)
    }

    companion object : Modifier {
        override fun <R> foldIn(initial: R, operation: (R, Element) -> R): R = initial
        override fun <R> foldOut(initial: R, operation: (Element, R) -> R): R = initial
        override fun any(predicate: (Element) -> Boolean): Boolean = false
        override fun all(predicate: (Element) -> Boolean): Boolean = true
        override infix fun then(other: Modifier): Modifier = other
        override fun toString() = "Modifier"
    }
}

class CombinedModifier(
    internal val outer: Modifier,
    internal val inner: Modifier
) : Modifier {
    override fun <R> foldIn(initial: R, operation: (R, ModifierElement) -> R): R =
        inner.foldIn(outer.foldIn(initial, operation), operation)

    override fun <R> foldOut(initial: R, operation: (ModifierElement, R) -> R): R =
        outer.foldOut(inner.foldOut(initial, operation), operation)

    override fun any(predicate: (ModifierElement) -> Boolean): Boolean =
        outer.any(predicate) || inner.any(predicate)

    override fun all(predicate: (ModifierElement) -> Boolean): Boolean =
        outer.all(predicate) && inner.all(predicate)

    override fun equals(other: Any?): Boolean =
        other is CombinedModifier && outer == other.outer && inner == other.inner

    override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

    override fun toString() = "[" + foldIn("") { acc, element ->
        if (acc.isEmpty()) element.toString() else "$acc, $element"
    } + "]"
}


/**
 * Like [then] but the [other] modifier is only applied if the condition is true.
 */
fun Modifier.thenIf(condition: Boolean, other: Modifier): Modifier {
    return this.thenIf(condition) { other }
}

/**
 * Like [thenIf] but with an inverted condition.
 */
fun Modifier.thenUnless(condition: Boolean, other: Modifier): Modifier {
    return this.thenUnless(condition) { other }
}

/**
 * Like the version of [thenIf] which takes in a modifier directly, but it produces that modifier lazily.
 *
 * This is occasionally useful if you have a Modifier that is expensive to create, e.g. it takes some complicated
 * parameters you need to allocate which is a waste if the condition is false.
 */
inline fun Modifier.thenIf(condition: Boolean, lazyProduce: () -> Modifier): Modifier {
    return this.then(if (condition) lazyProduce() else Modifier)
}

inline fun <T> Modifier.thenIfNotNull(value: T?, consume: (T) -> Modifier): Modifier {
    return this.thenIf(value != null) { consume(value!!) }
}

/**
 * Like the version of [thenUnless] which takes in a modifier directly, but it produces that modifier lazily.
 *
 * This is occasionally useful if you have a Modifier that is expensive to create, e.g. it takes some complicated
 * parameters you need to allocate which is a waste if the condition is true.
 */
inline fun Modifier.thenUnless(condition: Boolean, lazyProduce: () -> Modifier): Modifier {
    return this.thenIf(!condition, lazyProduce)
}