package xyz.rickygao.dishow.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_catalog_comment.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.toast
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.awaitAndHandle
import xyz.rickygao.dishow.common.withItems
import xyz.rickygao.dishow.item.CatalogCommentEditItem
import xyz.rickygao.dishow.item.CatalogCommentHeaderItem
import xyz.rickygao.dishow.item.CatalogCommentItem
import xyz.rickygao.dishow.network.Service

class CatalogCommentFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_catalog_comment, container).apply {
                rv.layoutManager = LinearLayoutManager(context)

                val ref = asReference()
                arguments?.getInt("id")?.let { id ->
                    launch(UI) {
                        val comments = Service.getCatalogCommentsByCatalog(id)
                        comments.awaitAndHandle { t ->
                            ref().context.toast("加载失败 $t")
                        }?.let {
                            ref().rv.withItems {
                                add(CatalogCommentHeaderItem(it))
                                add(CatalogCommentEditItem(id))
                                addAll(it.comments.map(::CatalogCommentItem))
                            }
                        }
                    }
                }
            }

    companion object {
        @JvmName("NewInstance")
        operator fun invoke(id: Int) = CatalogCommentFragment().apply {
            arguments = Bundle().apply {
                putInt("id", id)
            }
        }
    }
}