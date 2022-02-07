package com.che.zadali.sgo_app.data

import android.content.Context
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
//    val s = client.post("") {
//        contentType(ContentType.Application.Json)
//        body = data
//    }
//    client.close()
//    return s
    return Output("Ok", true)
}

fun diaryRequest(loginData: LoginData? = null): Diary {
    val a =
        """{"weekStart":"2022-01-24T00:00:00","weekEnd":"2022-01-30T00:00:00","weekDays":[{"date":"2022-01-24T00:00:00","lessons":[{"classmeetingId":190808465,"day":"2022-01-24T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Математика профиль","assignments":[{"id":219897992,"typeId":3,"assignmentName":"§21; №21.1; 21.2; 21.9; записать формулы в справочник с примерами","weight":0,"dueDate":"2022-01-24T00:00:00","classMeetingId":190808465}],"isEaLesson":false},{"classmeetingId":191128417,"day":"2022-01-24T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"История","assignments":[{"id":219948211,"typeId":3,"assignmentName":"Повторить тему: Февральская и Октябрьская революции в России - понятия, даты","weight":0,"dueDate":"2022-01-24T00:00:00","classMeetingId":191128417},{"id":220285747,"typeId":20,"assignmentName":"Экономика нэпа","weight":0,"dueDate":"2022-01-24T00:00:00","classMeetingId":191128417,"mark":{"assignmentId":220285747,"studentId":472262,"mark":3,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191434445,"day":"2022-01-24T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Индивидуальный проект","isEaLesson":false}]},{"date":"2022-01-25T00:00:00","lessons":[{"classmeetingId":191129084,"day":"2022-01-25T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Родной язык","assignments":[{"id":219216429,"typeId":3,"assignmentName":"выучить теорию Н и НН (см.файл)","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":191129084},{"id":220397219,"typeId":10,"assignmentName":"Морфология и синтаксис родного (русского) языка","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":191129084,"mark":{"assignmentId":220397219,"studentId":472262,"mark":3,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191871045,"day":"2022-01-25T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Обществознание базовый","assignments":[{"id":220324461,"typeId":3,"assignmentName":"записи в тетради","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":191871045},{"id":220333976,"typeId":20,"assignmentName":"Семья как социальный институт.","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":191871045,"mark":{"assignmentId":220333976,"studentId":472262,"mark":4,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":190808499,"day":"2022-01-25T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Математика профиль","assignments":[{"id":220098966,"typeId":3,"assignmentName":"задание из прикрепленного файла","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":190808499},{"id":220372561,"typeId":10,"assignmentName":"Формулы двойного аргумента","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":190808499,"mark":{"assignmentId":220372561,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":190808532,"day":"2022-01-25T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Математика профиль","assignments":[{"id":220404729,"typeId":3,"assignmentName":"заполнить справочник","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":190808532}],"isEaLesson":false},{"classmeetingId":194655236,"day":"2022-01-25T00:00:00","number":8,"relay":2,"room":null,"startTime":"16:00","endTime":"16:40","subjectName":"\"Человек и общество\" 10 класс","isEaLesson":true},{"classmeetingId":192287924,"day":"2022-01-25T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Физика профиль","assignments":[{"id":220256235,"typeId":3,"assignmentName":"стр 272 задачаи 1, 2. Учить лекцию (см тетрадь)","weight":0,"dueDate":"2022-01-25T00:00:00","classMeetingId":192287924}],"isEaLesson":false},{"classmeetingId":191434479,"day":"2022-01-25T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Индивидуальный проект","isEaLesson":false}]},{"date":"2022-01-26T00:00:00","lessons":[{"classmeetingId":191431732,"day":"2022-01-26T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Иностранный язык","assignments":[{"id":219473785,"typeId":3,"assignmentName":"\tвыучить всю лексику в тетради (описание внешности)","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":191431732}],"isEaLesson":false},{"classmeetingId":190808564,"day":"2022-01-26T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Математика профиль","assignments":[{"id":220375417,"typeId":3,"assignmentName":"№21.5; 21.13; 21.14; записать формулы понижения степени из учебника в справочник","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":190808564}],"isEaLesson":false},{"classmeetingId":191128450,"day":"2022-01-26T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"История","assignments":[{"id":220244406,"typeId":3,"assignmentName":"повторить темы: Гражданская война, Военный коммунизм, НЭП, Образование СССР  - понятия, даты","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":191128450}],"isEaLesson":false},{"classmeetingId":191431764,"day":"2022-01-26T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Иностранный язык","assignments":[{"id":220351746,"typeId":3,"assignmentName":"\tSb p.47 Speak Out (составить план-шаблон)","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":191431764}],"isEaLesson":false},{"classmeetingId":191129873,"day":"2022-01-26T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Русский язык базовый","assignments":[{"id":219419455,"typeId":3,"assignmentName":"работа № 26980407 на решу.ЕГЭ","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":191129873}],"isEaLesson":false},{"classmeetingId":192287956,"day":"2022-01-26T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Физика профиль","assignments":[{"id":220389931,"typeId":3,"assignmentName":"стр 272 задачи 3,4. Учить лекцию (см тетрадь)","weight":0,"dueDate":"2022-01-26T00:00:00","classMeetingId":192287956}],"isEaLesson":false}]},{"date":"2022-01-27T00:00:00","lessons":[{"classmeetingId":192287989,"day":"2022-01-27T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физика профиль","assignments":[{"id":220618537,"typeId":3,"assignmentName":"См дз на 26.01","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":192287989}],"isEaLesson":false},{"classmeetingId":192288023,"day":"2022-01-27T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","assignments":[{"id":220618705,"typeId":3,"assignmentName":"Задание прежнее","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":192288023}],"isEaLesson":false},{"classmeetingId":191128567,"day":"2022-01-27T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Литература","assignments":[{"id":219971816,"typeId":3,"assignmentName":"заполнить таблицу в тетради (см.файл)","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":191128567,"mark":{"assignmentId":219971816,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false},"textAnswer":{"answer":"https://docs.google.com/document/d/15mtQAHkLOWnT95DerrOU-hTt_v3q6BXBaAlPEoAThPU/edit?usp=sharing","answerDate":"2022-01-26T18:41:32.85"}}],"isEaLesson":false},{"classmeetingId":191128601,"day":"2022-01-27T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","assignments":[{"id":220024738,"typeId":3,"assignmentName":"«Я пришел к тебе с приветом…» (1843),  « ПЧЕЛЫ»,  «Сияла ночь. Луной был полон сад…» Анализ и один стих наизусть","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":191128601,"mark":{"assignmentId":220024738,"studentId":472262,"mark":3,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191432178,"day":"2022-01-27T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Физическая культура","assignments":[{"id":220842122,"typeId":3,"assignmentName":"Ходьба на лыжах","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":191432178},{"id":220842449,"typeId":10,"assignmentName":"Прохождение дистанции 3 км изученными ходами.","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":191432178,"mark":{"assignmentId":220842449,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191871078,"day":"2022-01-27T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Обществознание базовый","assignments":[{"id":220374866,"typeId":3,"assignmentName":"с 276-277 до Гражданского процесса","weight":0,"dueDate":"2022-01-27T00:00:00","classMeetingId":191871078}],"isEaLesson":false}]},{"date":"2022-01-28T00:00:00","lessons":[{"classmeetingId":191432213,"day":"2022-01-28T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физическая культура","assignments":[{"id":220842171,"typeId":3,"assignmentName":"Ходьба на лыжах","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191432213},{"id":220918057,"typeId":10,"assignmentName":"Совершенствование техники перехода с одновременных ходов на попеременные.","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191432213,"mark":{"assignmentId":220918057,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":192288058,"day":"2022-01-28T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","assignments":[{"id":220726667,"typeId":3,"assignmentName":"стр 2292-293, 265-267 УЧИТЬ ФОРМУЛЫ!!!","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":192288058},{"id":220909817,"typeId":10,"assignmentName":"Работа газа при расширении и сжатии (§ 56).","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":192288058,"mark":{"assignmentId":220909817,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191128865,"day":"2022-01-28T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Основы безопасности жизнедеятельности","assignments":[{"id":219892208,"typeId":3,"assignmentName":"Здоровый образ жизни и его составляющие.","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191128865}],"isEaLesson":false},{"classmeetingId":191128091,"day":"2022-01-28T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Информатика профиль","assignments":[{"id":218875539,"typeId":3,"assignmentName":"повторить разобранные задания","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191128091}],"isEaLesson":false},{"classmeetingId":191128126,"day":"2022-01-28T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","assignments":[{"id":218875551,"typeId":3,"assignmentName":"выучить формулы","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191128126}],"isEaLesson":false},{"classmeetingId":190808598,"day":"2022-01-28T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Математика профиль","assignments":[{"id":220536494,"typeId":3,"assignmentName":"§21; №21.16; 21.24-21.26(б)","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":190808598},{"id":220994637,"typeId":10,"assignmentName":"А: Урок обобщения и систематизации знаний по теме: \"Решение тригонометрических уравнений\".","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":190808598,"mark":{"assignmentId":220994637,"studentId":472262,"mark":4,"resultScore":null,"dutyMark":false}},{"id":221048408,"typeId":53,"assignmentName":"А: Урок обобщения и систематизации знаний по теме: \"Решение тригонометрических уравнений\".","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":190808598,"mark":{"assignmentId":221048408,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":190808633,"day":"2022-01-28T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Математика профиль","assignments":[{"id":220680481,"typeId":3,"assignmentName":"сборник Мальцева  тесты 8 и 14 задание №12","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":190808633}],"isEaLesson":false},{"classmeetingId":191432248,"day":"2022-01-28T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Физическая культура","assignments":[{"id":220842304,"typeId":3,"assignmentName":"Ходьба на лыжах","weight":0,"dueDate":"2022-01-28T00:00:00","classMeetingId":191432248}],"isEaLesson":false}]},{"date":"2022-01-29T00:00:00","lessons":[{"classmeetingId":194655273,"day":"2022-01-29T00:00:00","number":7,"relay":2,"room":null,"startTime":"14:20","endTime":"15:00","subjectName":"\"Человек и общество\" 10 класс","isEaLesson":true},{"classmeetingId":191431798,"day":"2022-01-29T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Иностранный язык","assignments":[{"id":220513190,"typeId":3,"assignmentName":"Sb p.47 Speak Out (составить план-шаблон)","weight":0,"dueDate":"2022-01-29T00:00:00","classMeetingId":191431798}],"isEaLesson":false},{"classmeetingId":191128636,"day":"2022-01-29T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","assignments":[{"id":220024702,"typeId":3,"assignmentName":"один стих из списка наизусть (см.файл)","weight":0,"dueDate":"2022-01-29T00:00:00","classMeetingId":191128636,"mark":{"assignmentId":220024702,"studentId":472262,"mark":4,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191128161,"day":"2022-01-29T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","assignments":[{"id":220955236,"typeId":3,"assignmentName":"дорешать в тетради","weight":0,"dueDate":"2022-01-29T00:00:00","classMeetingId":191128161}],"isEaLesson":false},{"classmeetingId":191128196,"day":"2022-01-29T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Информатика профиль","assignments":[{"id":220955246,"typeId":3,"assignmentName":"дорешать в тетради","weight":0,"dueDate":"2022-01-29T00:00:00","classMeetingId":191128196}],"isEaLesson":false}]}],"laAssigns":[],"termName":"2 триместр","className":"10Б"}"""
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

suspend fun changePassword(newPassword: String, context: Context): Int {
    SettingsPrefs(context).changePassword(newPassword)
    return 200
}