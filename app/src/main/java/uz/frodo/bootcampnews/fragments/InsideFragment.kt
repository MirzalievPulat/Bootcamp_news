package uz.frodo.bootcampnews.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import uz.frodo.bootcampnews.DetailActivity
import uz.frodo.bootcampnews.ItemClick
import uz.frodo.bootcampnews.News
import uz.frodo.bootcampnews.R
import uz.frodo.bootcampnews.adapters.RvAdapter
import uz.frodo.bootcampnews.databinding.CustomDialogBinding
import uz.frodo.bootcampnews.databinding.FragmentInsideBinding
import uz.frodo.bootcampnews.db.MyDbHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [InsideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InsideFragment : Fragment(),ItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var binding: FragmentInsideBinding
    lateinit var list:ArrayList<News>
    lateinit var adapter: RvAdapter
    lateinit var myDbHelper:MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInsideBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())

        when(param1){
            "Asosiy"-> list = myDbHelper.getAllByType("Asosiy")
            "Dunyo"-> list = myDbHelper.getAllByType("Dunyo")
            "Ijtimoiy"-> list = myDbHelper.getAllByType("Ijtimoiy")
        }
        println(param1)
        println(list)

        adapter = RvAdapter(this)
        adapter.submitList(list)
        binding.rv.adapter = adapter


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment InsideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            InsideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }

    }

    fun updateAdapterWithData() {
        adapter.submitList(myDbHelper.getAllByType(param1!!))
        binding.rv.adapter = adapter
    }

    override fun onMoreClick(view: View, news: News) {
        val pupupMenu = PopupMenu(requireContext(),view)
        pupupMenu.inflate(R.menu.pupup_menu)
        pupupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.edit_popup ->{
                    val dialog = AlertDialog.Builder(requireContext()).create()
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
                            val n = News(news.id,type,sarlavha,matni)
                            myDbHelper.editNews(n)
                            adapter.submitList(myDbHelper.getAllByType(param1!!))
                            binding.rv.adapter = adapter
                            dialog.dismiss()
                        }else
                            Toast.makeText(requireContext(), "Fill the Blank", Toast.LENGTH_SHORT).show()
                    }
                    customBinding.negative.setOnClickListener { dialog.dismiss() }

                    dialog.show()
                }
                R.id.delete_popup ->{
                    myDbHelper.deleteNews(news.id)
                    adapter.submitList(myDbHelper.getAllByType(param1!!))
                    binding.rv.adapter = adapter
                }
            }
            true
        }
        pupupMenu.show()

    }

    override fun onItemClick(news: News) {
        val intent = Intent(requireContext(),DetailActivity::class.java)
        intent.putExtra("id",news.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(myDbHelper.getAllByType(param1!!))
        binding.rv.adapter = adapter
    }
}