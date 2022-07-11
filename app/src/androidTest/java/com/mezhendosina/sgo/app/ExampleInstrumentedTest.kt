package com.mezhendosina.sgo.app

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val a =
            "<p>Муниципальный центр онлайн-обучения &amp;#171;Челябинская Электронная Школа&amp;#187; организует набор обучающихся на 2022/2023 учебный год в 6-8 классы и в 10 класс универсального профиля для следующих категорий:&amp;#160;</p>\\n<ul>\\n<li>обучающиеся, проживающие заграницей (жители Челябинска);&amp;#160;</li>\\n<li>обучающиеся с высоким уровнем учебных академических достижений (индивидуальный образовательный маршрут);&amp;#160;</li>\\n<li>обучающиеся с высоким уровнем учебных творческих достижений (индивидуальный образовательный маршрут);&amp;#160;</li>\\n<li>обучающиеся с высоким уровнем учебных спортивных достижений (индивидуальный образовательный маршрут);&amp;#160;</li>\\n<li>обучающиеся, находящиеся по состоянию здоровья временно на домашнем обучении;&amp;#160;</li>\\n<li>обучающиеся на семейной форме обучения.&amp;#160;</li>\\n</ul>\\n<p>Консультацию по вопросам обучения в ЧЭШ родители/законные представители<br/>могут получить у директора МАОУ &amp;#171;Лицей № 77 г. Челябинска&amp;#187;<br/><em><strong>Саблиной Марии Александровны по т. 253-38-64.</strong></em></p>\n"

        val markwon = Markwon.builder(appContext).usePlugin(HtmlPlugin.create()).build()
        println(markwon.parse(a))
    }
}