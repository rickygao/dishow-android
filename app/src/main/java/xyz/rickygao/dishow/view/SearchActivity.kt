package xyz.rickygao.dishow.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onEditorAction
import org.jetbrains.anko.toast
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.awaitAndHandle
import xyz.rickygao.dishow.common.withItems
import xyz.rickygao.dishow.item.CatalogItem
import xyz.rickygao.dishow.network.Service

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.extras.getInt("id")
        val from = intent.extras.getString("from")

        val ref = asReference()

        rv.layoutManager = LinearLayoutManager(this)

        et_name.onEditorAction { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                btn_search.callOnClick()
            }
        }

        btn_search.onClick {
            val name = et_name.text.toString()
            if (name.isBlank()) {
                ref().toast("请输入搜索关键字")
                return@onClick
            }

            when (from) {
                "university" -> Service.getCatalogsByUniversityAndName(id, name)
                "canteen" -> Service.getCatalogsByCanteenAndName(id, name)
                else -> return@onClick
            }.awaitAndHandle { t ->
                ref().toast("加载失败 $t")
            }?.let {
                ref().rv.withItems(it.map(::CatalogItem))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
