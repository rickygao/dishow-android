package xyz.rickygao.dishow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnTitleChangeListener, OnCanteenClickListener, OnCatalogClickListener, OnDishClickListener {

    private val universityFragment: UniversityFragment by lazy { UniversityFragment() }

    private val canteenFragment: CanteenFragment by lazy { CanteenFragment() }

    private val catalogFragment: CatalogFragment by lazy { CatalogFragment() }

    override fun onTitleChange(title: String) {
        toolbar_layout.title = title
    }

    override fun onCanteenClick(item: Canteen) {
        supportFragmentManager
                .beginTransaction()
                .hide(universityFragment)
                .add(R.id.fl, canteenFragment.apply { loadCatalogs(item.id) })
                .addToBackStack(null)
                .commit()
    }

    override fun onCatalogClick(item: Catalog) {
        supportFragmentManager
                .beginTransaction()
                .hide(canteenFragment)
                .add(R.id.fl, catalogFragment.apply { loadDishes(item.id) })
                .addToBackStack(null)
                .commit()
    }

    override fun onDishClick(item: Dish) {
        Toast.makeText(this@MainActivity, item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl, universityFragment.apply { loadCanteens(Preference.uid) })
                .commit()

        fab.setOnClickListener { _ -> }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

interface OnTitleChangeListener {
    fun onTitleChange(title: String)
}
