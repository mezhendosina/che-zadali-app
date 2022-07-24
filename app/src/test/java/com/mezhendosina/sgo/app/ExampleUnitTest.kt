package com.mezhendosina.sgo.app

import android.app.Instrumentation
import com.mezhendosina.sgo.data.GradesFromHtml
import com.mezhendosina.sgo.data.Requests
import com.mezhendosina.sgo.data.SettingsLoginData
import io.noties.markwon.Markwon
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun testExtractGradeOptions() {
        val f = File("D:/Programming/gradesOptions.html").readText()
        println(GradesFromHtml().getOptions(f))

    }

    @Test
    fun testExtractGrades(){
        val f = File("D:/Programming/extractGrades.html").readText()
        println(GradesFromHtml().extractGrades(f))
    }
}