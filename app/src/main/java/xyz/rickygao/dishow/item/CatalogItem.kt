package xyz.rickygao.dishow.item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_catalog.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Item
import xyz.rickygao.dishow.common.ItemController
import xyz.rickygao.dishow.common.roundFraction
import xyz.rickygao.dishow.network.Catalog
import xyz.rickygao.dishow.view.CatalogActivity

class CatalogItem(val catalog: Catalog) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                parent.context.layoutInflater.inflate(R.layout.item_catalog, parent, false).let(::ViewHolder)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            when {
                holder !is ViewHolder -> throw IllegalArgumentException("$holder should be ${ViewHolder::class.java}")
                item !is CatalogItem -> throw IllegalArgumentException("$item should be ${CatalogItem::class.java}")
                else -> with(holder.itemView) {
                    item.catalog.let {
                        tv_name.text = it.name
                        tv_location.text = it.location
                        tv_avg_star.text = it.avgStar?.roundFraction()?.let { "平均 $it 分" } ?: "暂无评分"
                        setOnClickListener { v -> v.context.startActivity<CatalogActivity>("id" to it.id) }
                    }
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}