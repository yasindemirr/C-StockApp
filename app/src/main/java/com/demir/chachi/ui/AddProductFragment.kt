package com.demir.chachi.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demir.chachi.R
import com.demir.chachi.adapter.ProductAdapter
import com.demir.chachi.databinding.FragmentAddProductBinding
import com.demir.chachi.model.Product
import com.demir.chachi.viewModel.ProdeuctViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    private lateinit var binding:FragmentAddProductBinding
    private val productViewModel: ProdeuctViewModel by viewModels()
    private lateinit var addAdapter:ProductAdapter
    private var isCheckedAdd:Boolean=false
    private lateinit var alertBuilder: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddProductBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openShet.setOnClickListener {
            ProductBottomSheet(null).show(requireActivity().supportFragmentManager,
                "new sheet task")
        }
        setRec()
        observeProductData()
        addAdapter.onlickEdit = {
            ProductBottomSheet(it).show(requireActivity().supportFragmentManager, "new sheet task")
        }
        addAdapter.addToList = {
            if (it.listeAdedi!=0) {
                productViewModel.updateAddList(it.id, 1, false)
                isCheckedAdd = false
                Snackbar.make(view, "Listeye eklendi", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(view, "Girilen adet boş olamaz", Snackbar.LENGTH_SHORT).show()
            }
        }
       binding.listBack.setOnClickListener {
           if (isCheckedAdd==false){
               findNavController().navigate(R.id.action_addProductFragment_to_listProductFragment)
           }else{
               Snackbar.make(view,"Lütfen girilen adedi listeye ekleyin",Snackbar.LENGTH_SHORT).show()
           }
           }
    removeProduct()
        AddQuantity()
        checkFloatingButton()
    }

    private fun checkFloatingButton() {
        binding.productRec.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.openShet.hide()
                } else if (dy < 0) {
                    binding.openShet.show()
                }
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun AddQuantity() {
        addAdapter.addQuantity={product->
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
                    productViewModel.updateAddList(product.id,1,true)
                    isCheckedAdd=true
                    dialog.dismiss()
                }else{
                    Toast.makeText(context,"Seçtiğin adet için teterli stok yok",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchText(list: List<Product>) {
        binding.searcBar.addTextChangedListener(object :TextWatcher{
            private var beforeList: List<Product>? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if (beforeList==null){
                        beforeList=list
                    }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    addAdapter.differ.submitList(list)
                }
                val filterList=list.filter {
                    it.urunAdi!!.contains(s.toString(), ignoreCase = true)
                }.toMutableList()
                filterList.sortWith(compareBy({ it.urunAdi!!.startsWith(s.toString(), ignoreCase = true).not() }, { it.urunAdi }))

                addAdapter.differ.submitList(filterList)

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    addAdapter.differ.submitList(beforeList)
                    beforeList = null
                }
            }

        })
    }

    private fun setRec() {
        addAdapter=ProductAdapter()
            binding.productRec.apply{
                adapter=addAdapter
                layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            }

    }


    private fun observeProductData() {
        productViewModel.realAllProduct().observe(viewLifecycleOwner, Observer {listPorduct->
            addAdapter.differ.submitList(listPorduct)
            searchText(listPorduct)
        })

    }
    private fun removeProduct() {
       addAdapter.deleteItem={
           alertBuilder= AlertDialog.Builder(requireContext())
           alertBuilder.setTitle("Uyarı")
           alertBuilder.setMessage("Silmek istediğinizden emin misiniz?")
           alertBuilder.setPositiveButton("Evet"){dialog,which->
               productViewModel.deleteProduct(it)
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (isCheckedAdd==false){
                    findNavController().navigate(R.id.action_addProductFragment_to_listProductFragment)
                }else{
                    Toast.makeText(requireContext(),"Lütfen girilen adedi listeye ekleyin",Toast.LENGTH_SHORT).show()
                }


            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

}