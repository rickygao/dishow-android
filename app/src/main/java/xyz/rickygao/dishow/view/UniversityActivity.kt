package xyz.rickygao.dishow.view

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_university.*
import kotlinx.android.synthetic.main.layout_login.view.*
import kotlinx.android.synthetic.main.layout_nav_header.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.toast
import xyz.rickygao.dishow.R
import xyz.rickygao.dishow.common.Preference
import xyz.rickygao.dishow.common.awaitAndHandle
import xyz.rickygao.dishow.common.withItems
import xyz.rickygao.dishow.item.CanteenItem
import xyz.rickygao.dishow.network.Service

class UniversityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university)

        setSupportActionBar(toolbar)
        ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)
                .also { drawer_layout.addDrawerListener(it) }
                .syncState()

        val defaultId = Preference.uid
        val id = intent?.extras?.getInt("id", defaultId) ?: defaultId

        toolbar_layout.title = "正在加载 $id 大学"

        rv.layoutManager = LinearLayoutManager(this)

        val ref = asReference()
        launch(UI) {
            Service.getUniversityById(id).awaitAndHandle { t ->
                ref().toast("加载失败 $t")
                ref().toolbar_layout.title = "加载失败"
            }?.let {
                ref().toolbar_layout.title = it.name
                ref().rv.withItems(it.canteens.orEmpty().map(::CanteenItem))
            }
        }

        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_signup -> {
                    val view = layoutInflater.inflate(R.layout.layout_login, null)
                    AlertDialog.Builder(this)
                            .setTitle(R.string.title_signup)
                            .setView(view)
                            .setPositiveButton(R.string.title_signup, { _, _ ->
                                launch(UI) {
                                    val username = view.et_username.text.toString()
                                    val password = view.et_password.text.toString()
                                    Service.putUserByUsernameAndPassword(username, password).awaitAndHandle { t ->
                                        ref().toast("注册失败 $t")
                                    }?.let {
                                        if (it.id != null) {
                                            ref().toast("注册成功，您的 id 是 ${it.id}")
                                            Preference.username = username
                                            Preference.password = password
                                            updateUsername()
                                        } else ref().toast("登录失败")
                                    }
                                }
                            })
                            .setNegativeButton(R.string.button_cancel, { dialog, _ -> dialog.dismiss() })
                            .show()

                }
                R.id.nav_login -> {
                    val view = layoutInflater.inflate(R.layout.layout_login, null)
                    AlertDialog.Builder(this)
                            .setTitle(R.string.title_login)
                            .setView(view)
                            .setPositiveButton(R.string.title_login, { _, _ ->
                                launch(UI) {
                                    val username = view.et_username.text.toString()
                                    val password = view.et_password.text.toString()
                                    Service.getUserByUsernameAndPassword(username, password).awaitAndHandle { t ->
                                        ref().toast("登录失败 $t")
                                    }?.let {
                                        if (it.id != null) {
                                            ref().toast("登录成功，您的 id 是 ${it.id}")
                                            Preference.username = username
                                            Preference.password = password
                                            updateUsername()
                                        } else ref().toast("登录失败")
                                    }
                                }
                            })
                            .setNegativeButton(R.string.button_cancel, { dialog, _ -> dialog.dismiss() })
                            .show()
                }
                R.id.nav_logout -> {
                    Preference.username = null
                    Preference.password = null
                    updateUsername()
                }
                R.id.nav_feedback -> Unit
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() =
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.closeDrawer(GravityCompat.START)
            else super.onBackPressed()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.action_settings -> true
                else -> super.onOptionsItemSelected(item)
            }

    override fun onResume() {
        super.onResume()
        updateUsername()
    }

    private fun updateUsername() {
        nav_view.getHeaderView(0).tv_username.text = Preference.username ?: "请先注册 / 登录"
    }
}