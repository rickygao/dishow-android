package xyz.rickygao.dishow.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface Item {
    val controller: ItemController
}

interface ItemController {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)
}

class ItemAdapter(private val itemManager: ItemManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MutableList<Item> by itemManager {

    init {
        itemManager.observer = this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemManager.getController(viewType).onCreateViewHolder(parent)

    override fun getItemCount() = itemManager.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            itemManager[position].controller.onBindViewHolder(holder, itemManager[position])

    override fun getItemViewType(position: Int) = ItemManager.getViewType(itemManager[position].controller)
}

fun RecyclerView.withItems(items: List<Item> = listOf()) {
    adapter = ItemAdapter(ItemManager(items.toMutableList()))
}

fun RecyclerView.withItems(init: MutableList<Item>.() -> Unit) = withItems(mutableListOf<Item>().apply(init))

val RecyclerView.items: MutableList<Item> get() = adapter as ItemAdapter

fun RecyclerView.onItems(block: MutableList<Item>.() -> Unit) = items.block()

class ItemManager(private val delegated: MutableList<Item> = mutableListOf()) : MutableList<Item> by delegated {
    var observer: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    init {
        ensureControllers(delegated)
    }

    internal companion object ItemControllerManager {
        private var size = 0

        // controller to view type
        private val c2vt = mutableMapOf<ItemController, Int>()

        // view type to controller
        private val vt2c = mutableMapOf<Int, ItemController>()

        private fun ensureController(item: Item) {
            val controller = item.controller
            if (!c2vt.contains(controller)) {
                c2vt[controller] = size
                vt2c[size] = controller
                size++
            }
        }

        private fun ensureControllers(items: Collection<Item>): Unit =
                items.distinctBy(Item::controller).forEach(::ensureController)

        fun getViewType(controller: ItemController): Int = c2vt[controller]
                ?: throw IllegalStateException("ItemController $controller is not ensured")

        fun getController(viewType: Int): ItemController = vt2c[viewType]
                ?: throw IllegalStateException("ViewType $viewType is unused")
    }

    override fun add(element: Item) =
            delegated.add(element).also {
                ensureController(element)
                if (it) observer?.notifyItemInserted(size)
            }

    override fun add(index: Int, element: Item) =
            delegated.add(index, element).also {
                ensureController(element)
                observer?.notifyItemInserted(index)
            }

    override fun addAll(index: Int, elements: Collection<Item>) =
            delegated.addAll(elements).also {
                ensureControllers(elements)
                if (it) observer?.notifyItemRangeInserted(index, elements.size)
            }

    override fun addAll(elements: Collection<Item>) =
            delegated.addAll(elements).also {
                if (it) observer?.notifyItemRangeInserted(size, elements.size)
            }

    override fun clear() =
            delegated.clear().also {
                observer?.notifyItemRangeRemoved(0, size)
            }

    override fun remove(element: Item): Boolean =
            delegated.remove(element).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun removeAll(elements: Collection<Item>): Boolean =
            delegated.removeAll(elements).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun removeAt(index: Int) =
            delegated.removeAt(index).also {
                observer?.notifyItemRemoved(index)
            }

    override fun retainAll(elements: Collection<Item>) =
            delegated.retainAll(elements).also {
                if (it) observer?.notifyDataSetChanged()
            }

    override fun set(index: Int, element: Item) =
            delegated.set(index, element).also {
                ensureController(element)
                observer?.notifyItemChanged(index)
            }

}