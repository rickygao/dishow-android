package xyz.rickygao.dishow.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_catalog.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.awaitAndHandle
import xyz.rickygao.dishow.common.withItems
import xyz.rickygao.dishow.item.DishItem
import xyz.rickygao.dishow.network.Service

class CatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.extras.getInt("id")

        toolbar_layout.title = "正在加载 $id 分类"

        rv.layoutManager = LinearLayoutManager(this)

        val ref = asReference()
        launch(UI) {
            Service.getCatalogById(id).awaitAndHandle { t ->
                ref().toast("加载失败 $t")
                ref().toolbar_layout.title = "加载失败"
            }?.let {
                ref().toolbar_layout.title = it.name
                ref().rv.withItems(it.dishes.orEmpty().map(::DishItem))
            }
        }

        fab.onClick {
            CatalogCommentFragment(id).show(supportFragmentManager, "CatalogCommentFragment")
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