package com.che.zadali.sgoapp.data.services

import androidx.lifecycle.MutableLiveData
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.StatusCodes
import com.che.zadali.sgoapp.data.layout.schools.SchoolsList
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

typealias SchoolsListener = (a: List<SchoolItem>) -> Unit

data class SchoolsListItem(
    val schools: List<SchoolItem>,
    val statusCode: StatusCodes
)

class SchoolService {
    private var schools = mutableListOf<SchoolItem>()

    private var a = schools
    private val listeners = mutableSetOf<SchoolsListener>()

    fun loadSchools(inProgress: MutableLiveData<Boolean>) {
        inProgress.value = true
        println(inProgress.value)
        CoroutineScope(Dispatchers.IO).launch {
            val res = async {
                val client = HttpClient(CIO) {
                    install(JsonFeature) {
                        serializer = GsonSerializer()
                    }
                }
                return@async SchoolsListItem(
                    client.get<SchoolsList>("https://mezhendosina.pythonanywhere.com/schools").schoolItems,
                    StatusCodes.Success
                )
            }.await()
            schools = res.schools.toMutableList()
            withContext(Dispatchers.Main) {
                inProgress.value = false
                notifyChanges()
            }
        }
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



