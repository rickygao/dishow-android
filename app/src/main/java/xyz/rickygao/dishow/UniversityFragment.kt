package xyz.rickygao.dishow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_canteen.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class UniversityFragment : Fragment() {

    private lateinit var adapter: CanteenAdapter

    private var title: String by Delegates.observable("") { _, _, newValue ->
        if (!isHidden) (activity as? OnTitleChangeListener)?.onTitleChange(newValue)
    }

    fun loadCanteens(universityId: Int) {
        title = "加载中"
        Service.getUniversityById(universityId).enqueue(object : Callback<University> {
            override fun onFailure(call: Call<University>, t: Throwable) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<University>, response: Response<University>) {
                response.body()?.let {
                    title = it.name
                    Service.getCanteensByUniversity(it.id).enqueue(object : Callback<List<Canteen>> {
                        override fun onFailure(call: Call<List<Canteen>>, t: Throwable) {
                            Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<List<Canteen>>, response: Response<List<Canteen>>) {
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
                adapter = CanteenAdapter(context as? OnCanteenClickListener).also { this@UniversityFragment.adapter = it }
                arguments?.getInt(ARG_UNIVERSITY_ID)?.let(this@UniversityFragment::loadCanteens)
            }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) (activity as? OnTitleChangeListener)?.onTitleChange(title)
    }

    companion object {
        const val ARG_UNIVERSITY_ID = "university_id"
        @JvmName("newInstance")
        operator fun invoke(universityId: Int) = UniversityFragment().apply {
            arguments = Bundle().apply { putInt(ARG_UNIVERSITY_ID, universityId) }
        }
    }
}

class CanteenAdapter(private val listener: OnCanteenClickListener?)
    : RecyclerView.Adapter<CanteenAdapter.ViewHolder>() {

    var items: List<Canteen> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val itemListener = View.OnClickListener { v ->
        listener?.onCanteenClick(v.tag as Canteen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_canteen, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            tag = item
            setOnClickListener(itemListener)
            tv_name.text = item.name
            tv_location.text = "${item.location}(${item.longitude}, ${item.latitude})"
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

interface OnCanteenClickListener {
    fun onCanteenClick(item: Canteen)
}