package com.demir.chachi.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demir.chachi.R
import com.demir.chachi.databinding.ListItemBinding
import com.demir.chachi.databinding.ProductItemBinding
import com.demir.chachi.model.Product

class ListAdapter:RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root)
    private val diffUtil=object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id &&
            oldItem.urunAdi==newItem.urunAdi

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view= ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val product=differ.currentList[position]
        holder.binding.apply {
            listProductName.text=product.urunAdi
            listProductPrice.text="${product.fiyat} "
            listProductQuantity.text="${product.listeAdedi} "
            product.image?.let {
                listProductImage.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
            }
            listTotalProductPrice.text="${product.listeAdedi*product.fiyat!!.toInt()}"
        }
        holder.binding.listDelete.setOnClickListener {
            onlickDelete?.let {
                it.invoke(product)
            }
        }
        holder.binding.editQuantity.setOnClickListener {
            editQuantity?.let {
                it.invoke(product)
            }
        }

    }



    var onlickDelete:((Product)-> Unit)? = null
    var editQuantity:((Product)-> Unit)? = null

    override fun getItemCount(): Int {
      return  differ.currentList.size
    }
    fun updateList(newList: List<Product>) {
        differ.submitList(newList)
        notifyDataSetChanged()
    }
}