package uz.frodo.bootcampnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import uz.frodo.bootcampnews.databinding.ActivityDetailBinding
import uz.frodo.bootcampnews.databinding.CustomDialogBinding
import uz.frodo.bootcampnews.db.MyDbHelper
import uz.frodo.bootcampnews.fragments.InsideFragment

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myDbHelper = MyDbHelper(this)

        val id = intent.getIntExtra("id",-1)

        val news = myDbHelper.getById(id)

        binding.detailTitle.text = news.title
        binding.detailBody.text = news.body

        binding.fab.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            dialog.setTitle("O'zgartirish")
            dialog.setCancelable(false)
            val customBinding = CustomDialogBinding.inflate(layoutInflater)
            dialog.setView(customBinding.root)

            customBinding.editSarlavha.setText(news.title)
            customBinding.editMatni.setText(news.body)

            customBinding.positive.setOnClickListener {
                val sarlavha = customBinding.editSarlavha.text.toString()
                val matni = customBinding.editMatni.text.toString()
                val type = customBinding.spinner.selectedItem.toString()

                if (sarlavha.isNotBlank() && matni.isNotBlank()){
                    val n = News(id,type,sarlavha,matni)
                    myDbHelper.editNews(n)

                    binding.detailTitle.text = n.title
                    binding.detailBody.text = n.body
                    dialog.dismiss()
                }else
                    Toast.makeText(applicationContext, "Fill the Blank", Toast.LENGTH_SHORT).show()
            }
            customBinding.negative.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        binding.backArrow.setOnClickListener { 
            finish()
        }

    }
}