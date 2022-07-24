package com.mezhendosina.sgo.app

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mezhendosina.sgo.data.Requests
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.uriFromFile
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

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

        val f = File("D:/Programming/extractGrades.html")
        println(uriFromFile(appContext, f))

    }

    @Test
    fun testCreateProfileFile() {
        val context = InstrumentationRegistry.getInstrumentation().context

        val file = context.getDir("testDir", Context.MODE_PRIVATE)

        println(File(file, "photo.png").path)
    }
}