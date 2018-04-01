package xyz.rickygao.dishow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_dish.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class CatalogFragment : Fragment() {

    private lateinit var adapter: DishAdapter

    private var title: String by Delegates.observable("加载中") { _, _, newValue ->
        if (!isHidden) (activity as? OnTitleChangeListener)?.onTitleChange(newValue)
    }

    fun loadDishes(catalogId: Int) {
        Service.getCatalogById(catalogId).enqueue(object : Callback<Catalog> {
            override fun onFailure(call: Call<Catalog>, t: Throwable) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Catalog>, response: Response<Catalog>) {
                response.body()?.let(this@CatalogFragment::loadDishes)
            }
        })
    }

    fun loadDishes(catalog: Catalog) {
        title = catalog.name
        Service.getDishesByCatalog(catalog.id).enqueue(object : Callback<List<Dish>> {
            override fun onFailure(call: Call<List<Dish>>, t: Throwable) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Dish>>, response: Response<List<Dish>>) {
                adapter.items = response.body().orEmpty()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            (inflater.inflate(R.layout.fragment_list, container, false) as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DishAdapter(context as? OnDishClickListener).also { this@CatalogFragment.adapter = it }
            }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) (activity as? OnTitleChangeListener)?.onTitleChange(title)
    }

}

class DishAdapter(private val listener: OnDishClickListener?)
    : RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    var items: List<Dish> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val itemListener = View.OnClickListener { v ->
        listener?.onDishClick(v.tag as Dish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dish, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            tag = item
            setOnClickListener(itemListener)
            tv_name.text = item.name
            tv_price.text = "${item.price} 元"
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

interface OnDishClickListener {
    fun onDishClick(item: Dish)
}