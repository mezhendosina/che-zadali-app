package com.che.zadali.sgo_app.data

import com.che.zadali.sgo_app.data.announcements.AnnouncementsData
import com.che.zadali.sgo_app.data.diary.Diary
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.data.schools.SchoolsList
import com.che.zadali.sgo_app.data.typesId.TypesId
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*


suspend fun sendCheckLogin(data: LoginData): Output {
//    val client = HttpClient(CIO) {
//        install(JsonFeature) {
//            serializer = GsonSerializer()
//        }
//    }
//    val s = client.post<Output>("https://che-zadali-server.herokuapp.com/check_login") {
//        contentType(ContentType.Application.Json)
//        body = data
//    }
//    client.close()
//    return s
    return Output("Ok")
}

fun diaryRequest(loginData: LoginData? = null): Diary {
    val a =
        """{"weekStart":"2022-01-17T00:00:00","weekEnd":"2022-01-23T00:00:00","weekDays":[{"date":"2022-01-17T00:00:00","lessons":[{"classmeetingId":190808464,"day":"2022-01-17T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Математика профиль","assignments":[{"id":218777731,"typeId":3,"assignmentName":"сборник Мальцева 8 тест 11 часть (кроме №4 и 11)","weight":0,"dueDate":"2022-01-17T00:00:00","classMeetingId":190808464}],"isEaLesson":false},{"classmeetingId":191128416,"day":"2022-01-17T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"История","assignments":[{"id":216878523,"typeId":3,"assignmentName":"Страны Востока в п.п.20 века: политическое, экономическое, социальное развитие","weight":0,"dueDate":"2022-01-17T00:00:00","classMeetingId":191128416}],"isEaLesson":false},{"classmeetingId":191434444,"day":"2022-01-17T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Индивидуальный проект","assignments":[{"id":216878387,"typeId":3,"assignmentName":"проект","weight":0,"dueDate":"2022-01-17T00:00:00","classMeetingId":191434444}],"isEaLesson":false}]},{"date":"2022-01-18T00:00:00","lessons":[{"classmeetingId":191129083,"day":"2022-01-18T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Родной язык","assignments":[{"id":216878700,"typeId":3,"assignmentName":"-","weight":0,"dueDate":"2022-01-18T00:00:00","classMeetingId":191129083}],"isEaLesson":false},{"classmeetingId":191871044,"day":"2022-01-18T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Обществознание базовый","assignments":[{"id":218570528,"typeId":3,"assignmentName":"п 26 Семейные правоотношения","weight":0,"dueDate":"2022-01-18T00:00:00","classMeetingId":191871044}],"isEaLesson":false},{"classmeetingId":190808498,"day":"2022-01-18T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Математика профиль","isEaLesson":false},{"classmeetingId":190808531,"day":"2022-01-18T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Математика профиль","isEaLesson":false},{"classmeetingId":194655235,"day":"2022-01-18T00:00:00","number":8,"relay":2,"room":null,"startTime":"16:00","endTime":"16:40","subjectName":"\"Человек и общество\" 10 класс","isEaLesson":true},{"classmeetingId":192287923,"day":"2022-01-18T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Физика профиль","assignments":[{"id":218808326,"typeId":3,"assignmentName":"решить задачи  (см прик файл) на оценку 3 задачи 1-2, на оценку 4 задачи 3-4, на оценку 5 задачи 5-6","weight":0,"dueDate":"2022-01-18T00:00:00","classMeetingId":192287923},{"id":218808342,"typeId":10,"assignmentName":"Уравнение Клапейрона—Менделеева (§ 53).","weight":0,"dueDate":"2022-01-18T00:00:00","classMeetingId":192287923}],"isEaLesson":false},{"classmeetingId":191434478,"day":"2022-01-18T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Индивидуальный проект","isEaLesson":false}]},{"date":"2022-01-19T00:00:00","lessons":[{"classmeetingId":191431731,"day":"2022-01-19T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Иностранный язык","isEaLesson":false},{"classmeetingId":190808563,"day":"2022-01-19T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Математика профиль","isEaLesson":false},{"classmeetingId":191128449,"day":"2022-01-19T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"История","assignments":[{"id":218573432,"typeId":3,"assignmentName":"повторить тему: Первая Мировая война по плану","weight":0,"dueDate":"2022-01-19T00:00:00","classMeetingId":191128449}],"isEaLesson":false},{"classmeetingId":191431763,"day":"2022-01-19T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Иностранный язык","isEaLesson":false},{"classmeetingId":191129872,"day":"2022-01-19T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Русский язык базовый","assignments":[{"id":216878718,"typeId":3,"assignmentName":"-","weight":0,"dueDate":"2022-01-19T00:00:00","classMeetingId":191129872}],"isEaLesson":false},{"classmeetingId":192287955,"day":"2022-01-19T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Физика профиль","isEaLesson":false}]},{"date":"2022-01-20T00:00:00","lessons":[{"classmeetingId":192287988,"day":"2022-01-20T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физика профиль","isEaLesson":false},{"classmeetingId":192288022,"day":"2022-01-20T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","isEaLesson":false},{"classmeetingId":191128566,"day":"2022-01-20T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Литература","assignments":[{"id":218864428,"typeId":3,"assignmentName":"анализ стихотворения \"Как птичка, раннею зарёй\" Ф.И.Тютчева","weight":0,"dueDate":"2022-01-20T00:00:00","classMeetingId":191128566}],"isEaLesson":false},{"classmeetingId":191128600,"day":"2022-01-20T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","isEaLesson":false},{"classmeetingId":191432177,"day":"2022-01-20T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Физическая культура","isEaLesson":false},{"classmeetingId":191871077,"day":"2022-01-20T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Обществознание базовый","isEaLesson":false}]},{"date":"2022-01-21T00:00:00","lessons":[{"classmeetingId":191432212,"day":"2022-01-21T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физическая культура","isEaLesson":false},{"classmeetingId":192288057,"day":"2022-01-21T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","isEaLesson":false},{"classmeetingId":191128864,"day":"2022-01-21T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Основы безопасности жизнедеятельности","assignments":[{"id":218794696,"typeId":3,"assignmentName":"Здоровый образ жизни и его составляющие.","weight":0,"dueDate":"2022-01-21T00:00:00","classMeetingId":191128864}],"isEaLesson":false},{"classmeetingId":191128090,"day":"2022-01-21T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Информатика профиль","assignments":[{"id":218875414,"typeId":3,"assignmentName":"https://inf-ege.sdamgia.ru/test?id=9960522","weight":0,"dueDate":"2022-01-21T00:00:00","classMeetingId":191128090}],"isEaLesson":false},{"classmeetingId":191128125,"day":"2022-01-21T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","assignments":[{"id":218875420,"typeId":3,"assignmentName":"https://inf-ege.sdamgia.ru/test?id=9960522","weight":0,"dueDate":"2022-01-21T00:00:00","classMeetingId":191128125}],"isEaLesson":false},{"classmeetingId":190808597,"day":"2022-01-21T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Математика профиль","isEaLesson":false},{"classmeetingId":190808632,"day":"2022-01-21T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Математика профиль","isEaLesson":false},{"classmeetingId":191432247,"day":"2022-01-21T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Физическая культура","isEaLesson":false}]},{"date":"2022-01-22T00:00:00","lessons":[{"classmeetingId":194655272,"day":"2022-01-22T00:00:00","number":7,"relay":2,"room":null,"startTime":"14:20","endTime":"15:00","subjectName":"\"Человек и общество\" 10 класс","isEaLesson":true},{"classmeetingId":191431797,"day":"2022-01-22T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Иностранный язык","isEaLesson":false},{"classmeetingId":191128635,"day":"2022-01-22T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","isEaLesson":false},{"classmeetingId":191128160,"day":"2022-01-22T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","assignments":[{"id":218875498,"typeId":3,"assignmentName":"https://inf-ege.sdamgia.ru/test?id=9960527","weight":0,"dueDate":"2022-01-22T00:00:00","classMeetingId":191128160}],"isEaLesson":false},{"classmeetingId":191128195,"day":"2022-01-22T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Информатика профиль","assignments":[{"id":218875502,"typeId":3,"assignmentName":"https://inf-ege.sdamgia.ru/test?id=9960527","weight":0,"dueDate":"2022-01-22T00:00:00","classMeetingId":191128195}],"isEaLesson":false}]}],"laAssigns":[],"termName":"2 триместр","className":"10Б"}"""
    return Gson().fromJson(a, Diary::class.java)
//    val client = HttpClient(CIO) {
//        install(JsonFeature) {
//            serializer = GsonSerializer()
//        }
//    }
//    val s = client.post<Diary>("https://che-zadali-server.herokuapp.com/homework"){
//        contentType(ContentType.Application.Json)
//        body = data
//    }
//    client.close()
//    return s
}

suspend fun schoolsRequest(): List<SchoolItem> {

    val res = withContext(Dispatchers.IO) {
        val b = async {
            val client = HttpClient(CIO) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }
            val s: HttpResponse =
                client.get("https://mezhendosina.pythonanywhere.com/schools")
            if (s.status != HttpStatusCode.OK) {
                val a: SchoolsList =
                    client.get("https://che-zadali-server.herokuapp.com/schools")
                client.close()
                return@async a
            } else {
                val a: SchoolsList =
                    Gson().fromJson(s.receive<String>(), SchoolsList::class.java)
                client.close()
                return@async a
            }
        }
        return@withContext b.await()
    }
    return res.schoolItems
}

suspend fun getAssignmentsId(typeId: Int): TypesId {
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
    val s = client.get<TypesId>("https://sgo.edu-74.ru/webapi/grade/assignment/types?all=true")
    client.close()
    return s
}


fun getAnnouncements(): AnnouncementsData {
    val a =
        """[{"description":"<p>Уважаемые обучающиеся 7-х классов и их родители, ежегодно ученики 7-х классов Челябинской области принимают участие в Региональном исследовании качества образования &amp;#171;Индивидуальный проект&amp;#187;. В рамках исследования каждый ученик должен написать проект по одной из предложенных тем. Обучающимся предлагается на выбор четыре типа проектов: социальный, исследовательский, информационно-познавательный, творческий, по каждому предлагается контрольно-измерительные материалы (КИМ). В КИМ можно познакомиться со всеми требованиями, к созданию проекта.</p>\n<p>Вашему вниманию в приложении представлены все КИМ и темы проектов, к 17 января необходимо выбрать тему проекта.</p>\n<p>Желаю успехов!</p>","postDate":"2022-01-12T12:50:31.857","deleteDate":null,"author":{"id":40054,"fio":"Нафикова Елена Владимировна","nickName":"Нафикова Елена Владимировна"},"em":null,"recipientInfo":null,"attachments":[{"id":20750386,"name":"Темы проектов.docx","originalFileName":"Темы проектов.docx","description":null},{"id":20750387,"name":"1_КИМ ИП 7_2021_социальный.pdf","originalFileName":"1_КИМ ИП 7_2021_социальный.pdf","description":null},{"id":20750389,"name":"2_КИМ ИП 7_2021_исследовательский.pdf","originalFileName":"2_КИМ ИП 7_2021_исследовательский.pdf","description":null},{"id":20750392,"name":"3_КИМ ИП 7_2021_информационно-познавательный.pdf","originalFileName":"3_КИМ ИП 7_2021_информационно-познавательный.pdf","description":null},{"id":20750393,"name":"4_КИМ ИП 7_2021_творческий.pdf","originalFileName":"4_КИМ ИП 7_2021_творческий.pdf","description":null}],"id":1105795,"name":"Индивидуальный проект 7 класс"}]"""
    return Gson().fromJson(a, AnnouncementsData::class.java)
}