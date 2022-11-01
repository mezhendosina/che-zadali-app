package com.mezhendosina.sgo.app

import com.mezhendosina.sgo.data.grades.GradesFromHtml
import org.junit.Test
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
    fun testExtractGrades() {
        val f = File("D:/Programming/extractGrades.html").readText()
        println(GradesFromHtml().extractGrades(f))
    }
//
//    @Test
//    fun testGradesCalculator() {
//        println(
//            GradesCalculator(
//                GradesItem(
//                    "123",
//                    4,
//                    7,
//                    0,
//                    0,
//                    0,
//                    "3,43"
//                )
//            ).calculateGrade(
//                GradesCalculator.FIVE_GRADE,
//                4.5f
//            )
//        )
//    }

}