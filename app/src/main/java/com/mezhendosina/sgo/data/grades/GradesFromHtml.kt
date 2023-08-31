/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.data.grades

import com.mezhendosina.sgo.app.utils.getEmojiLesson
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.GradeOptions
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.InputTag
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.SelectTag
import org.jsoup.Jsoup

class GradesFromHtml {

    private lateinit var reportType: List<SelectTag>
    private lateinit var PCLID: InputTag
    private lateinit var sid: InputTag
    private lateinit var termid: List<SelectTag>

    fun getOptions(html: String): GradeOptions {
        val jsoup = Jsoup.parse(html)

        val formControls = jsoup.getElementsByClass("col-md-8 col-lg-5 col-sm-8")

        formControls.forEach { formControl ->
            val selectTags = formControl.getElementsByTag("select")
            val inputTags = formControl.getElementsByTag("input")

            selectTags.forEach { selectTag ->
                val optionsList = mutableListOf<SelectTag>()
                val optionsItem = selectTag.getElementsByTag("option")

                val selectedItem = selectTag.getElementsByAttribute("selected")

                optionsItem.forEach { optionItem ->
                    val selected = selectedItem.first() == optionItem
                    optionsList.add(SelectTag(selected, optionItem.text(), optionItem.`val`()))
                }

                when (selectTag.attr("name")) {
                    "ReportType" -> reportType = optionsList
                    "TERMID" -> termid = optionsList
                }
            }

            inputTags.forEach { inputTag ->
                when (inputTag.attr("name")) {
                    "PCLID" -> PCLID = InputTag("PCLID", inputTag.attr("value"))
                    "SID" -> sid = InputTag("SID", inputTag.attr("value"))
                }
            }
        }
        return GradeOptions(PCLID, reportType, sid, termid)
    }

    fun extractGrades(html: String): List<GradesItem> {
        val jsoup = Jsoup.parse(html)

        val grades = mutableListOf<GradesItem>()

        val getTableTag = jsoup.getElementsByClass("table-print").first()

        val trTags = getTableTag?.getElementsByTag("tr")
            ?.drop(2)
            ?.dropLast(1)
        trTags?.forEach { trTag ->
            val tdTags = trTag.getElementsByTag("td")

            val lessonName = tdTags[0].text()
            val avg = tdTags.last()?.text()
            val fiveGrade = tdTags[1].text().toGradeInt()
            val fourGrade = tdTags[2].text().toGradeInt()
            val threeGrade = tdTags[3].text().toGradeInt()
            val twoGrade = tdTags[4].text().toGradeInt()
// TODO            val oneGrade = if()tdTags[5].text().toInt()

            grades.add(
                GradesItem(
                    lessonName,
                    fiveGrade,
                    fourGrade,
                    threeGrade,
                    twoGrade,
                    null,
                    avg
                )
            )
        }
        return grades
    }

    private fun String.toGradeInt(): Int? = if (this.isNotEmpty()) this.toInt() else null
}
