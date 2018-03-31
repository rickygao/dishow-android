package xyz.rickygao.dishow

import android.content.Context
import com.orhanobut.hawk.Hawk
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> hawk(key: String, default: T) = object : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = Hawk.get(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Hawk.put(key, value)
    }

}

fun <T> hawk(key: String) = object : ReadWriteProperty<Any?, T?> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = Hawk.get(key)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        Hawk.put(key, value)
    }

}

fun Context.buildHawk() = Hawk.init(this).build()

object Preference {
    var uid: Int by hawk("uid", 1)
}