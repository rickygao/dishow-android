package xyz.rickygao.dishow.item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_canteen.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Item
import xyz.rickygao.dishow.common.ItemController
import xyz.rickygao.dishow.network.Canteen
import xyz.rickygao.dishow.view.CanteenActivity

class CanteenItem(val canteen: Canteen) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                parent.context.layoutInflater.inflate(R.layout.item_canteen, parent, false).let(::ViewHolder)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            when {
                holder !is ViewHolder -> throw IllegalArgumentException("$holder should be ${ViewHolder::class.java}")
                item !is CanteenItem -> throw IllegalArgumentException("$item should be ${CanteenItem::class.java}")
                else -> with(holder.itemView) {
                    item.canteen.let {
                        tv_name.text = it.name
                        tv_location.text = it.location ?: "经纬度 (${it.latitude}, ${it.longitude})"
                        setOnClickListener { v ->
                            v.context.startActivity<CanteenActivity>("id" to it.id)
                        }
                    }
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}