package com.che.zadali.sgo_app.data

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
        """ {"weekStart":"2022-01-10T00:00:00","weekEnd":"2022-01-16T00:00:00","weekDays":[{"date":"2022-01-13T00:00:00","lessons":[{"classmeetingId":192287987,"day":"2022-01-13T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физика профиль","assignments":[{"id":216833251,"typeId":3,"assignmentName":"повторить формулы МКТ","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":192287987},{"id":216833258,"typeId":10,"assignmentName":"Основное уравнение молекулярно-кинетической теории (§ 52).","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":192287987,"mark":{"assignmentId":216833258,"studentId":472262,"mark":5,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":192288021,"day":"2022-01-13T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","assignments":[{"id":216833316,"typeId":3,"assignmentName":"задание прежнее","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":192288021}],"isEaLesson":false},{"classmeetingId":191128565,"day":"2022-01-13T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Литература","assignments":[{"id":216878548,"typeId":3,"assignmentName":"-","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":191128565},{"id":218553105,"typeId":68,"assignmentName":"Споры критиков вокруг романа. Подготовка к домашнему сочинению.","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":191128565,"mark":{"assignmentId":218553105,"studentId":472262,"mark":4,"resultScore":null,"dutyMark":false}}],"isEaLesson":false},{"classmeetingId":191128599,"day":"2022-01-13T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","assignments":[{"id":216878557,"typeId":3,"assignmentName":"-","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":191128599}],"isEaLesson":false},{"classmeetingId":191432176,"day":"2022-01-13T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Физическая культура","assignments":[{"id":216878799,"typeId":3,"assignmentName":"-","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":191432176}],"isEaLesson":false},{"classmeetingId":191871076,"day":"2022-01-13T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Обществознание базовый","assignments":[{"id":216213348,"typeId":3,"assignmentName":"п 26 Семейные правоотношения","weight":0,"dueDate":"2022-01-13T00:00:00","classMeetingId":191871076}],"isEaLesson":false}]},{"date":"2022-01-14T00:00:00","lessons":[{"classmeetingId":191432211,"day":"2022-01-14T00:00:00","number":1,"relay":1,"room":null,"startTime":"08:00","endTime":"08:40","subjectName":"Физическая культура","isEaLesson":false},{"classmeetingId":192288056,"day":"2022-01-14T00:00:00","number":2,"relay":1,"room":null,"startTime":"08:45","endTime":"09:25","subjectName":"Физика профиль","assignments":[{"id":218506174,"typeId":3,"assignmentName":"cтр266-267 учить формулы + учить таблицу + п. 55 знать ответы на вопросы задача 5","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":192288056},{"id":218506208,"typeId":10,"assignmentName":"Уравнение Клапейрона—Менделеева (§ 53).","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":192288056}],"isEaLesson":false},{"classmeetingId":191128863,"day":"2022-01-14T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Основы безопасности жизнедеятельности","assignments":[{"id":216708710,"typeId":3,"assignmentName":"не задано","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":191128863}],"isEaLesson":false},{"classmeetingId":191128089,"day":"2022-01-14T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Информатика профиль","assignments":[{"id":216878458,"typeId":3,"assignmentName":"повторить задания 1, 2, 4","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":191128089}],"isEaLesson":false},{"classmeetingId":191128124,"day":"2022-01-14T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","assignments":[{"id":216878463,"typeId":3,"assignmentName":"повторить задания 1, 2, 4","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":191128124}],"isEaLesson":false},{"classmeetingId":190808596,"day":"2022-01-14T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Математика профиль","assignments":[{"id":216933951,"typeId":3,"assignmentName":"п.12; №71; 72; 73","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":190808596}],"isEaLesson":false},{"classmeetingId":190808631,"day":"2022-01-14T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Математика профиль","assignments":[{"id":216878655,"typeId":3,"assignmentName":"сборник Мальцева тест 7; 9","weight":0,"dueDate":"2022-01-14T00:00:00","classMeetingId":190808631}],"isEaLesson":false},{"classmeetingId":191432246,"day":"2022-01-14T00:00:00","number":7,"relay":1,"room":null,"startTime":"12:50","endTime":"13:30","subjectName":"Физическая культура","isEaLesson":false}]},{"date":"2022-01-15T00:00:00","lessons":[{"classmeetingId":194655271,"day":"2022-01-15T00:00:00","number":7,"relay":2,"room":null,"startTime":"14:20","endTime":"15:00","subjectName":"\"Человек и общество\" 10 класс","isEaLesson":true},{"classmeetingId":191431796,"day":"2022-01-15T00:00:00","number":3,"relay":1,"room":null,"startTime":"09:35","endTime":"10:15","subjectName":"Иностранный язык","assignments":[{"id":216878399,"typeId":3,"assignmentName":"изучить to do list p.44","weight":0,"dueDate":"2022-01-15T00:00:00","classMeetingId":191431796}],"isEaLesson":false},{"classmeetingId":191128634,"day":"2022-01-15T00:00:00","number":4,"relay":1,"room":null,"startTime":"10:25","endTime":"11:05","subjectName":"Литература","assignments":[{"id":218477543,"typeId":3,"assignmentName":"выучить план анализа стихотворения, написать анализ стихотворения по группам (см.файл)","weight":0,"dueDate":"2022-01-15T00:00:00","classMeetingId":191128634}],"isEaLesson":false},{"classmeetingId":191128159,"day":"2022-01-15T00:00:00","number":5,"relay":1,"room":null,"startTime":"11:15","endTime":"11:55","subjectName":"Информатика профиль","isEaLesson":false},{"classmeetingId":191128194,"day":"2022-01-15T00:00:00","number":6,"relay":1,"room":null,"startTime":"12:05","endTime":"12:45","subjectName":"Информатика профиль","isEaLesson":false}]}],"laAssigns":[],"termName":"2 триместр","className":"10Б"}"""
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
