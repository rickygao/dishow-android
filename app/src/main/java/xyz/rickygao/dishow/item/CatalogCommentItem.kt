package xyz.rickygao.dishow.item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_catalog_comment.view.*
import org.jetbrains.anko.layoutInflater
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Item
import xyz.rickygao.dishow.common.ItemController
import xyz.rickygao.dishow.network.CatalogComment

class CatalogCommentItem(val catalogComment: CatalogComment) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                parent.context.layoutInflater.inflate(R.layout.item_catalog_comment, parent, false).let(::ViewHolder)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            when {
                holder !is ViewHolder -> throw IllegalArgumentException("$holder should be ${ViewHolder::class.java}")
                item !is CatalogCommentItem -> throw IllegalArgumentException("$item should be ${CatalogCommentItem::class.java}")
                else -> with(holder.itemView) {
                    item.catalogComment.let {
                        tv_star.text = "${it.star} 分"
                        tv_detail.text = it.detail
                        tv_username.text = "来自 ${it.username ?: "匿名用户"}"
                    }
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}