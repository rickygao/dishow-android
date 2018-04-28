package xyz.rickygao.dishow.item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_dish.view.*
import org.jetbrains.anko.layoutInflater
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Item
import xyz.rickygao.dishow.common.ItemController
import xyz.rickygao.dishow.network.Dish

class DishItem(val dish: Dish) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                parent.context.layoutInflater.inflate(R.layout.item_dish, parent, false).let(::ViewHolder)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            when {
                holder !is ViewHolder -> throw IllegalArgumentException("$holder should be ${ViewHolder::class.java}")
                item !is DishItem -> throw IllegalArgumentException("$item should be ${DishItem::class.java}")
                else -> with(holder.itemView) {
                    item.dish.let {
                        tv_name.text = it.name
                        tv_price.text = "${it.price ?: "未知"} 元"
                    }
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}