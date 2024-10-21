package com.varabyte.kobweb.compose.foundation.layout

import com.varabyte.kobweb.compose.style.toClassNames
import com.varabyte.truthish.assertThat
import org.jetbrains.compose.web.css.*
import kotlin.test.Test


class ArrangementTest {
    @Test
    fun arrangementSpacedByEqualityWorksAsExpected() {
        val spacedBy10 = Arrangement.spacedBy(10.px)
        val spacedBy20 = Arrangement.spacedBy(20.px)
        val horizSpacedBy10Start = Arrangement.spacedBy(10.px, Alignment.Start)
        val horizSpacedBy20Start = Arrangement.spacedBy(20.px, Alignment.Start)
        val horizSpacedBy10End = Arrangement.spacedBy(10.px, Alignment.End)
        val horizSpacedBy20End = Arrangement.spacedBy(20.px, Alignment.End)
        val vertSpacedBy10Top = Arrangement.spacedBy(10.px, Alignment.Top)
        val vertSpacedBy20Top = Arrangement.spacedBy(20.px, Alignment.Top)
        val vertSpacedBy10Bottom = Arrangement.spacedBy(10.px, Alignment.Bottom)
        val vertSpacedBy20Bottom = Arrangement.spacedBy(20.px, Alignment.Bottom)

        assertThat(spacedBy10).isEqualTo(Arrangement.spacedBy(10.px))
        assertThat(spacedBy20).isEqualTo(Arrangement.spacedBy(20.px))
        assertThat(horizSpacedBy10Start).isEqualTo(Arrangement.spacedBy(10.px, Alignment.Start))
        assertThat(horizSpacedBy20Start).isEqualTo(Arrangement.spacedBy(20.px, Alignment.Start))
        assertThat(horizSpacedBy10End).isEqualTo(Arrangement.spacedBy(10.px, Alignment.End))
        assertThat(horizSpacedBy20End).isEqualTo(Arrangement.spacedBy(20.px, Alignment.End))
        assertThat(vertSpacedBy10Top).isEqualTo(Arrangement.spacedBy(10.px, Alignment.Top))
        assertThat(vertSpacedBy20Top).isEqualTo(Arrangement.spacedBy(20.px, Alignment.Top))
        assertThat(vertSpacedBy10Bottom).isEqualTo(Arrangement.spacedBy(10.px, Alignment.Bottom))
        assertThat(vertSpacedBy20Bottom).isEqualTo(Arrangement.spacedBy(20.px, Alignment.Bottom))

        assertThat(spacedBy10).isNotEqualTo(spacedBy20)
        assertThat(spacedBy10).isNotEqualTo(horizSpacedBy10Start)
        assertThat(spacedBy10).isNotEqualTo(vertSpacedBy10Top)

        assertThat(horizSpacedBy10Start).isNotEqualTo(horizSpacedBy20Start)
        assertThat(horizSpacedBy10Start).isNotEqualTo(horizSpacedBy10End)
        assertThat(horizSpacedBy10Start).isNotEqualTo(vertSpacedBy10Top)
    }

    @Test
    fun arrangementClassNamesMatchExpected() {

        assertThat(Arrangement.spacedBy(10.px).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-start")
        assertThat(Arrangement.spacedBy(20.px).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-start")

        assertThat(Arrangement.spacedBy(10.px, Alignment.Start).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-start")
        assertThat(Arrangement.spacedBy(20.px, Alignment.End).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-end")

        assertThat(Arrangement.spacedBy(10.px, Alignment.Top).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-top")
        assertThat(Arrangement.spacedBy(20.px, Alignment.Bottom).toClassNames())
            .containsExactly("kobweb-arrange-spaced-by", "kobweb-arrange-bottom")
    }
}
