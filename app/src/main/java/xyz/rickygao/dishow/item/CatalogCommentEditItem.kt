package xyz.rickygao.dishow.item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_catalog_comment_edit.view.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Item
import xyz.rickygao.dishow.common.ItemController
import xyz.rickygao.dishow.common.Preference
import xyz.rickygao.dishow.common.awaitAndHandle
import xyz.rickygao.dishow.network.CatalogCommentBody
import xyz.rickygao.dishow.network.Service

class CatalogCommentEditItem(val id: Int) : Item {

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                parent.context.layoutInflater.inflate(R.layout.item_catalog_comment_edit, parent, false).let(::ViewHolder)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            when {
                holder !is ViewHolder -> throw IllegalArgumentException("$holder should be ${ViewHolder::class.java}")
                item !is CatalogCommentEditItem -> throw IllegalArgumentException("$item should be ${CatalogCommentEditItem::class.java}")
                else -> with(holder.itemView) {
                    val ref = this@with.context.asReference()
                    btn_submit.onClick {
                        if (Preference.username != null)
                            Service.postCatalogComment(
                                    item.id,
                                    CatalogCommentBody(rb.rating.toInt(),
                                            et_detail.text.toString(),
                                            cb_anonymous.isChecked)
                            ).awaitAndHandle { t ->
                                ref().toast("评论失败 $t")
                            }?.id?.let {
                                ref().toast("评论成功，序号是 $it")
                            }
                        else ref().toast("请先注册 / 登录")
                    }
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = Controller

}