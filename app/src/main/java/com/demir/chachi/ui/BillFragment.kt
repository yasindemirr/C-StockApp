package com.demir.chachi.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demir.chachi.R
import com.demir.chachi.adapter.BillAdapter
import com.demir.chachi.adapter.ListAdapter
import com.demir.chachi.databinding.FragmentBillBinding
import com.demir.chachi.model.Product
import com.demir.chachi.viewModel.ProdeuctViewModel
import com.demir.chachi.viewModel.ProductBillViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BillFragment : Fragment() {
    private lateinit var binding: FragmentBillBinding
    private val productViewModel: ProdeuctViewModel by viewModels()
    private val productBillViewModel: ProductBillViewModel by viewModels()
    private var addTolist= arrayListOf<Product>()
    private var priceList= arrayListOf<Int>()

    private val billAdapter= BillAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentBillBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRec()
        observeData()
        binding.infoLineer.setOnClickListener{
            findNavController().navigate(R.id.action_billFragment_to_billProductInfoFragment)
        }
        productBillViewModel.getInfo().observe(viewLifecycleOwner, Observer {productBill->
            productBill?.let {
                binding.billAdress.setText(it.adres)
                binding.billPhone.setText(it.telNo)
                binding.billMail.setText(it.mail)
                binding.billSevkDate.setText("Tarih:${it.sevkTarihi}")
                if (it.logoImage != null) {
                    binding.billLogo.setImageBitmap(BitmapFactory.decodeByteArray(it.logoImage,
                        0,
                        it.logoImage.size))
                }
            }
        })
        findNavController().popBackStack(R.id.action_billFragment_to_listProductFragment,true)

    }

    private fun observeData() {
        productViewModel.realAllProduct().observe(viewLifecycleOwner, Observer {
            for(item in it){
               if(item.isAddList==1){
                   addTolist.add(item)
                   priceList.add(item.fiyat!!.toInt()*item.listeAdedi)
               }
            }
            binding.tamamla.setOnClickListener {view->
                for (item in it){
                    productViewModel.updateAddList(item.id,0,false)
                    productViewModel.updateQuantityList(item.id,0,item.adet!!)
                }
                view.findNavController().navigate(R.id.action_billFragment_to_listProductFragment)
                Toast.makeText(context,"Liste Tamamlandı",Toast.LENGTH_SHORT).show()
            }
            billAdapter.differ.submitList(addTolist)
            binding.totalResult.text="${priceList.sum().toString()}TL"

        })
    }
    private fun setRec() {
        binding.billRec.apply {
            adapter=billAdapter
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_billFragment_to_listProductFragment)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }
}