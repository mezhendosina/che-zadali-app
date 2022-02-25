package com.che.zadali.sgoapp.data.services

import com.che.zadali.sgo_app.data.announcements.AnnouncementsData
import com.che.zadali.sgoapp.data.layout.announcements.AnnouncementsDataItem
import com.google.gson.Gson
import kotlinx.coroutines.*

typealias AnnouncementsActionListener = (announcements: List<AnnouncementsDataItem>) -> Unit

class AnnouncementsService {
    private var announcements = mutableListOf<AnnouncementsDataItem>()


    private val listeners = mutableSetOf<AnnouncementsActionListener>()


    fun loadAnnouncements() {
        CoroutineScope(Dispatchers.IO).launch {
            val a = async {
                val c =
                    """[{"description":"<p>Консультации и работа с проектами:</p>\n<p>Учитель: Овсянникова Т.В.</p>\n<p>каждую пятницу в 12.50.</p>\n<p>кабинет 204.</p>\n<p>&amp;#160;</p>","postDate":"2022-01-19T10:07:31.93","deleteDate":null,"author":{"id":525007,"fio":"Овсянникова Татьяна Владимировна","nickName":"Овсянникова Татьяна Владимировна"},"em":null,"recipientInfo":null,"attachments":[],"id":1107587,"name":"Индивидуальные проекты по информатике, 7 класс"},{"description":"<p>Уважаемые обучающиеся 7-х классов и их родители, ежегодно ученики 7-х классов Челябинской области принимают участие в Региональном исследовании качества образования &amp;#171;Индивидуальный проект&amp;#187;. В рамках исследования каждый ученик должен написать проект по одной из предложенных тем. Обучающимся предлагается на выбор четыре типа проектов: социальный, исследовательский, информационно-познавательный, творческий, по каждому предлагается контрольно-измерительные материалы (КИМ). В КИМ можно познакомиться со всеми требованиями, к созданию проекта.</p>\n<p>Вашему вниманию в приложении представлены все КИМ и темы проектов, к 17 января необходимо выбрать тему проекта.</p>\n<p>Желаю успехов!</p>","postDate":"2022-01-12T12:50:31.857","deleteDate":null,"author":{"id":40054,"fio":"Нафикова Елена Владимировна","nickName":"Нафикова Елена Владимировна"},"em":null,"recipientInfo":null,"attachments":[{"id":20750386,"name":"Темы проектов.docx","originalFileName":"Темы проектов.docx","description":null},{"id":20750387,"name":"1_КИМ ИП 7_2021_социальный.pdf","originalFileName":"1_КИМ ИП 7_2021_социальный.pdf","description":null},{"id":20750389,"name":"2_КИМ ИП 7_2021_исследовательский.pdf","originalFileName":"2_КИМ ИП 7_2021_исследовательский.pdf","description":null},{"id":20750392,"name":"3_КИМ ИП 7_2021_информационно-познавательный.pdf","originalFileName":"3_КИМ ИП 7_2021_информационно-познавательный.pdf","description":null},{"id":20750393,"name":"4_КИМ ИП 7_2021_творческий.pdf","originalFileName":"4_КИМ ИП 7_2021_творческий.pdf","description":null}],"id":1105795,"name":"Индивидуальный проект 7 класс"}]"""
                return@async Gson().fromJson(c, AnnouncementsData::class.java)
            }
            withContext(Dispatchers.Main) {
                announcements = a.await().toMutableList()
                notifyChanges()
            }
        }
    }

    fun addListener(listener: AnnouncementsActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: AnnouncementsActionListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(announcements) }
    }
}