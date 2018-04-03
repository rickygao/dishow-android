package xyz.rickygao.dishow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_catalog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class CanteenFragment : Fragment() {

    private lateinit var adapter: CatalogAdapter

    private var title: String by Delegates.observable("") { _, _, newValue ->
        if (!isHidden) (activity as? OnTitleChangeListener)?.onTitleChange(newValue)
    }

    fun loadCanteen(canteenId: Int) {
        title = "加载中"
        Service.getCanteenById(canteenId).enqueue(object : Callback<Canteen> {
            override fun onFailure(call: Call<Canteen>, t: Throwable) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Canteen>, response: Response<Canteen>) {
                response.body()?.let {
                    title = it.name
                    Service.getCatalogsByCanteen(it.id).enqueue(object : Callback<List<Catalog>> {
                        override fun onFailure(call: Call<List<Catalog>>, t: Throwable) {
                            Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<List<Catalog>>, response: Response<List<Catalog>>) {
                            adapter.items = response.body().orEmpty()
                        }
                    })
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            (inflater.inflate(R.layout.fragment_list, container, false) as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CatalogAdapter(context as? OnCatalogClickListener).also { this@CanteenFragment.adapter = it }
                arguments?.getInt(ARG_CANTEEN_ID)?.let(this@CanteenFragment::loadCanteen)
            }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) (activity as? OnTitleChangeListener)?.onTitleChange(title)
    }

    companion object {
        const val ARG_CANTEEN_ID = "canteen_id"
        @JvmName("newInstance")
        operator fun invoke(canteenId: Int) = CanteenFragment().apply {
            arguments = Bundle().apply { putInt(ARG_CANTEEN_ID, canteenId) }
        }
    }
}

class CatalogAdapter(private val listener: OnCatalogClickListener?)
    : RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    var items: List<Catalog> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val itemListener = View.OnClickListener { v ->
        listener?.onCatalogClick(v.tag as Catalog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_catalog, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            tag = item
            setOnClickListener(itemListener)
            tv_name.text = item.name
            tv_location.text = item.location
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

interface OnCatalogClickListener {
    fun onCatalogClick(item: Catalog)
}