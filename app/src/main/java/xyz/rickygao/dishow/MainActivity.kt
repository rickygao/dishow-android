package xyz.rickygao.dishow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnTitleChangeListener, OnCanteenClickListener, OnCatalogClickListener, OnDishClickListener {

    private lateinit var universityFragment: UniversityFragment
    private lateinit var canteenFragment: CanteenFragment
    private lateinit var catalogFragment: CatalogFragment

    override fun onTitleChange(title: String) {
        toolbar_layout.title = title
    }

    override fun onCanteenClick(item: Canteen) {
        supportFragmentManager
                .beginTransaction()
                .hide(universityFragment)
                .add(R.id.fl, CanteenFragment(item.id).also { canteenFragment = it })
                .addToBackStack(null)
                .commit()
    }

    override fun onCatalogClick(item: Catalog) {
        supportFragmentManager
                .beginTransaction()
                .hide(canteenFragment)
                .add(R.id.fl, CatalogFragment(item.id).also { catalogFragment = it })
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
                .replace(R.id.fl, UniversityFragment(Preference.uid).also { universityFragment = it })
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
