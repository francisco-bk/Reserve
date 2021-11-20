package com.example.reserve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_PAGES = 4
private val TABS = arrayOf("Central", "North", "West", "East")

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewPager = view.findViewById(R.id.viewPager2)
        viewPager.adapter = ViewPagerAdapter(this);

        tabLayout = view.findViewById(R.id.tabLayout2)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TABS[position]
        }.attach()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
            }
    }

    private inner class ViewPagerAdapter(activity: HomeFragment) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            return HallFragment.newInstance()
        }
    }
}