package xyz.rickygao.dishow

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import xyz.rickygao.dishow.common.Preference
import xyz.rickygao.dishow.common.buildHawk

@RunWith(AndroidJUnit4::class)
class PreferenceInstrumentedTest {

    private fun buildHawk() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("xyz.rickygao.dishow", appContext.packageName)
        appContext.buildHawk()
    }

    @Test
    fun testUid() {
        buildHawk()
        Preference.uid = 1
        assertEquals(1, Preference.uid)
    }


}
