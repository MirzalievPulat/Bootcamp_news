package uz.frodo.bootcampnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import uz.frodo.bootcampnews.adapters.FragmentAdapter
import uz.frodo.bootcampnews.databinding.ActivityMainBinding
import uz.frodo.bootcampnews.databinding.CustomDialogBinding
import uz.frodo.bootcampnews.db.MyDbHelper
import uz.frodo.bootcampnews.fragments.InsideFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: FragmentAdapter
    var currentIF: InsideFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myDbHelper = MyDbHelper(this)

        val list = arrayListOf("Asosiy","Dunyo","Ijtimoiy")
        adapter = FragmentAdapter(list,this)
        binding.viewpager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpager2){tab,position->
            when(position){
                0 -> tab.text = "Asosiy"
                1 -> tab.text = "Dunyo"
                2 -> tab.text = "Ijtimoiy"
            }

        }.attach()

        binding.viewpager2.registerOnPageChangeCallback(object :OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentIF = adapter.getInsideFragment(position)
            }
        })

        binding.plus.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            dialog.setTitle("Bootcamp news qo'shish")
            dialog.setCancelable(false)
            val customBinding = CustomDialogBinding.inflate(layoutInflater)
            dialog.setView(customBinding.root)


            customBinding.positive.setOnClickListener {
                val sarlavha = customBinding.editSarlavha.text.toString()
                val matni = customBinding.editMatni.text.toString()
                println(customBinding.spinner.selectedItem.toString())
                val type = customBinding.spinner.selectedItem.toString()
                if (sarlavha.isNotBlank() && matni.isNotBlank()){
                    val news = News(0,type,sarlavha,matni)
                    myDbHelper.addNews(news)
                    if(currentIF != null) currentIF?.updateAdapterWithData()
                    dialog.dismiss()
                }else
                    Toast.makeText(applicationContext, "Fill the Blank", Toast.LENGTH_SHORT).show()
            }
            customBinding.negative.setOnClickListener { dialog.dismiss() }

            dialog.show()

        }
    }
}