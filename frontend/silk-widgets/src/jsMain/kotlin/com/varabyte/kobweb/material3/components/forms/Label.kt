package com.varabyte.kobweb.material3.components.forms

import androidx.compose.runtime.*
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.browser.dom.descendantsBfs
import com.varabyte.kobweb.browser.dom.generateUniqueId
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLMeterElement
import org.w3c.dom.HTMLProgressElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import org.jetbrains.compose.web.dom.Label as JbLabel

// See: https://html.spec.whatwg.org/multipage/forms.html#category-label
private fun HTMLElement.isLabelable(): Boolean {
    return when (this) {
        is HTMLButtonElement,
        is HTMLMeterElement,
        is HTMLProgressElement,
        is HTMLTextAreaElement,
        is HTMLSelectElement -> true

        is HTMLInputElement -> type != "hidden"
        else -> false
    }
}

private fun HTMLElement.findFirstLabelable(): HTMLElement? {
    return this.descendantsBfs.firstOrNull { it.isLabelable() }
}

/**
 * Create a label that should be associated with some target form element.
 *
 * Labels are used to associate a text caption with some HTML form control, such as a text input or checkbox. When
 * connected up correctly, a11y tools will be able to associate the label with the control, and clicking on the label
 * will focus the control.
 *
 * ```
 * Row {
 *   Label(target = ElementTarget.NextSibling, label = "Name")
 *   TextInput(...)
 * }
 * ```
 *
 * If the target element does not have an ID already set on it, this widget will create a random ID for it, as having
 * one is required to associate a label with it.
 *
 * Note that HTML provides an alternate pattern for labels, where you can create one as a parent container for some text
 * and a child labelable element, at which point they can be connected without requiring an ID. If you want to use that
 * pattern in your own code, you can do that using the default [Compose HTML Label][JbLabel] widget instead.
 *
 * See also: https://html.spec.whatwg.org/multipage/forms.html#the-label-element
 *
 * @param target An element finder that returns the element that this label should be associated with. Note that if the
 *   element returned is not itself labelable, its children will be recursively searched (in a breadth-first manner) for
 *   one that is. This is useful as Silk provides various widgets which expose an opaque container type with a child
 *   from element wrapped within it.
 */
@Composable
fun Label(
    target: ElementTarget,
    label: String,
    modifier: Modifier = Modifier,
    ref: ElementRefScope<HTMLLabelElement>? = null,
) {
    JbLabel(attrs = modifier.toAttrs {
        ref { labelElement ->
            target.invoke(labelElement)?.findFirstLabelable()
                ?.let { labelTargetElement ->
                    if (labelTargetElement.id.isEmpty()) labelTargetElement.id =
                        document.generateUniqueId(prefix = "for-label-")
                    labelElement.htmlFor = labelTargetElement.id
                }
            onDispose { }
        }
    }) {
        registerRefScope(ref)
        Text(label)
    }
}
