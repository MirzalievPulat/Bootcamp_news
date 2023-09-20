package uz.frodo.bootcampnews.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.frodo.bootcampnews.fragments.InsideFragment

class FragmentAdapter(private var list: ArrayList<String>, fa:FragmentActivity):FragmentStateAdapter(fa) {
    private val insideFragments = mutableListOf<InsideFragment>()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val insideFragment = InsideFragment.newInstance(list[position])
        insideFragments.add(insideFragment)
        return insideFragment

    }

    fun getInsideFragment(position: Int):InsideFragment?{
        println("mana: "+position)
        return if (position>=0 && position<insideFragments.size) insideFragments[position] else null
    }
}