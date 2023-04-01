package com.demir.chachi.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demir.chachi.R
import com.demir.chachi.adapter.ListAdapter
import com.demir.chachi.adapter.ProductAdapter
import com.demir.chachi.databinding.FragmentListProductBinding
import com.demir.chachi.model.Product
import com.demir.chachi.viewModel.ProdeuctViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListProductFragment : Fragment() {
    private lateinit var binding: FragmentListProductBinding
    private var addTolist= arrayListOf<Product>()
    private val productViewModel: ProdeuctViewModel by viewModels()
    private lateinit var alertBuilder: AlertDialog.Builder
    val listAdapter= ListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentListProductBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRec()
      binding.addProducT.setOnClickListener {
          it.findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)
      }
        binding.bill.setOnClickListener {
            it.findNavController().navigate(R.id.action_listProductFragment_to_billFragment)
        }
        observeData()
        removeProduct()
        editQuantity()
        checkFloatingButton()
    }

    private fun observeData() {
        productViewModel.realAllListProduct().observe(viewLifecycleOwner, Observer {

            listAdapter.differ.submitList(it)
        })
        /*
        productViewModel.realAllProduct().observe(viewLifecycleOwner, Observer {
            for (item in it){
                if (item.isAddList==1){
                        addTolist.add(item)
                }
            }
            listAdapter.differ.submitList(addTolist)
        })

         */

    }
    private fun setRec() {
        binding.cRec.apply {
            adapter=listAdapter
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }

    }
    private fun checkFloatingButton() {
        binding.cRec.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.floatingMenu.visibility=View.GONE
                } else if (dy < 0) {
                    binding.floatingMenu.visibility=View.VISIBLE
                }
            }
        })
    }
    private fun removeProduct() {
        listAdapter.onlickDelete={product->
            alertBuilder= AlertDialog.Builder(requireContext())
            alertBuilder.setTitle("Uyarı")
            alertBuilder.setMessage("Silmek istediğinizden emin misiniz?")
            alertBuilder.setPositiveButton("Evet"){dialog,which->
                productViewModel.updateAddList(product.id,0,false)
                productViewModel.updateQuantityList(product.id,0,product.adet!!+product.listeAdedi)
                alertBuilder.create().dismiss()
            }
            alertBuilder.setNegativeButton("Hayır"){dialog,which->
                alertBuilder.create().dismiss()
            }
            alertBuilder.create().setOnShowListener {
                alertBuilder.create().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFE500"))
                alertBuilder.create().getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFE500"))
            }
            alertBuilder.show()
        }
    }
    private fun editQuantity(){
        listAdapter.editQuantity={product->
            val alertView =LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog,null)
            alertBuilder= AlertDialog.Builder(requireContext())
            alertBuilder.setView(alertView)
            val adet=alertView.findViewById<EditText>(R.id.addQuantityhey)
            val button=alertView.findViewById<Button>(R.id.alertButton)
            val dialog=alertBuilder.create()
            dialog.show()
            button.setOnClickListener {
                if (adet.text.toString().toInt() <= product.adet!!.toInt()) {
                    productViewModel.updateQuantityList(product.id, adet.text.toString().toInt(),
                        product.adet+product.listeAdedi-adet.text.toString().toInt())
                    productViewModel.updateAddList(product.id,1,false)
                    dialog.dismiss()
                }else{
                    Toast.makeText(context,"Seçtiğin adet için teterli stok yok", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}