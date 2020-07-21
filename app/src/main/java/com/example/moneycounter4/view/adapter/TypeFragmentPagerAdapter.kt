package com.example.moneycounter4.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction

class TypeFragmentPagerAdapter(
    private val fm: FragmentManager,
    behavior: Int,
    private var fragments: ArrayList<Fragment>
) :
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    fun removeAllFragment() {
        for (i in fragments.indices.reversed()) {
            val fragment = fragments[i]
            //然后从List中移除
            fragments.remove(fragment)
            //先从Transaction移除
            removeFragmentInternal(fragment)
        }
        //最后刷新Adapter
        notifyDataSetChanged()
    }

    /**
     * 从Transaction移除Fragment
     * @param fragment 目标Fragment
     */
    private fun removeFragmentInternal(fragment: Fragment) {
        val transaction: FragmentTransaction = fm.beginTransaction()
        transaction.remove(fragment)
        transaction.commitNow()
    }

}
