package com.che.zadali.sgoapp.data.layout.schools

import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.StatusCodes
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

typealias SchoolsListener = (a: List<SchoolItem>) -> Unit

data class SchoolsListItem(
    val schools: List<SchoolItem>,
    val statusCode: StatusCodes
)

class SchoolService {
    private var schools = mutableListOf<SchoolItem>()

    private var a = schools
    private val listeners = mutableSetOf<SchoolsListener>()

    fun loadSchools() {
        CoroutineScope(Dispatchers.IO).launch {
            val res = async {
                val client = HttpClient(CIO) {
                    install(JsonFeature) {
                        serializer = GsonSerializer()
                    }
                }
                try {
                    val s: HttpResponse =
                        client.get("https://mezhendosina.pythonanywhere.com/schools")
                    if (s.status != HttpStatusCode.OK) {
                        val a: SchoolsList =
                            client.get("https://che-zadali-server.herokuapp.com/schools")
                        client.close()
                        return@async SchoolsListItem(a.schoolItems, StatusCodes.Success)
                    } else {
                        val a: SchoolsList =
                            Gson().fromJson(s.receive<String>(), SchoolsList::class.java)
                        client.close()
                        return@async SchoolsListItem(a.schoolItems, StatusCodes.Success)
                    }
                } catch (e: UnresolvedAddressException) {
                    return@async SchoolsListItem(
                        emptyList(),
                        StatusCodes.NetworkError
                    )
                }
            }.await()

            schools = res.schools.toMutableList()

        }
        notifyChanges()
    }

    fun searchSchools(string: String) {
        a = schools.filter { schoolItem -> schoolItem.school.contains(string) }.toMutableList()
        notifyChanges()
    }

    fun getBySchoolId(SchoolId: Int): SchoolItem? {
        return schools.firstOrNull { it.schoolId == SchoolId }
    }

    fun addListener(listener: SchoolsListener) {
        listeners.add(listener)
        listener.invoke(schools)
    }

    fun removeListener(listener: SchoolsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(a) }
    }
}



