package com.es.inminiapplication.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.es.inminiapplication.R
import com.es.inminiapplication.view.my.MyActivity
import com.es.inminiapplication.view.app.AppActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val cardFAQ: CardView = findViewById(R.id.cardFAQ)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        viewPager = findViewById(R.id.viewPager)

        supportActionBar?.title = "ebebeveyn.com"

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, AppActivity::class.java))
                }
                R.id.navigation_dashboard -> {
                    // startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, MyActivity::class.java))
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }
        bottomNavigationView.selectedItemId = R.id.navigation_dashboard

        // Sık Sorulan Sorular Kartı
        cardFAQ.setOnClickListener {
            val intent = Intent(this, FaqCategoryActivity::class.java)
            startActivity(intent)
        }

        // ViewPager'ı hazırla
        setupViewPager()
    }

    private fun setupViewPager() {
        val imageUrls = listOf(
            "https://firebasestorage.googleapis.com/v0/b/inminiapplication.appspot.com/o/photo%2Fresim_2024-01-14_094614810.png?alt=media&token=7e8f09ac-c91d-4480-8746-6c604240bd38", // İlgili Firebase Storage URL'lerini ekleyin
            "https://firebasestorage.googleapis.com/v0/b/inminiapplication.appspot.com/o/photo%2Fresim_2024-01-14_094838132.png?alt=media&token=14ee14c4-2681-41a5-b70d-71d7da3ba3d2",
            "https://firebasestorage.googleapis.com/v0/b/inminiapplication.appspot.com/o/photo%2Fresim_2024-01-14_095254203.png?alt=media&token=f7723ad2-9d81-4681-9365-9b66e4cc073c"
            //...
        )

        val fragmentList = imageUrls.map { imageUrl ->
            FragmentImage.newInstance(imageUrl)
        }

        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = adapter
    }

    // ViewPagerAdapter sınıfını ekleyin
    class ViewPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragmentList: List<Fragment>
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = fragmentList.size

        override fun getItem(position: Int): Fragment = fragmentList[position]
    }
}
