package com.marangoz.travelbook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.marangoz.travelbook.R
import com.marangoz.travelbook.model.TravelBook
import com.marangoz.travelbook.ui.homepage.HomePageFragmentViewModel

class TravelAdapter(
    val mContext: Context,
    val viewModel : HomePageFragmentViewModel,
     private val showMenuDelete : (Boolean) -> Unit
   ) : RecyclerView.Adapter<TravelAdapter.ViewHolderClass>(){
    var travelList: List<TravelBook> = listOf()
    var isEnabled : Boolean = false
    var trigger : Boolean =false
   // val itemPositionSelectedList = mutableListOf<Int>()
    val itemSelectedList = mutableListOf<TravelBook>()

    inner class ViewHolderClass(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val dateText: TextView
        val travelImage: ImageView
        val selectImage: ImageView
        val cardView: CardView

        init {
            cardView = view.findViewById(R.id.cardView)
            titleText = view.findViewById(R.id.titleTextA)
            dateText = view.findViewById(R.id.dateTextA)
            travelImage = view.findViewById(R.id.travelImage)
            selectImage = view.findViewById(R.id.selectImage)

        }

    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val travelBook = travelList[position]

        holder.titleText.text = travelBook.title
        holder.dateText.text = travelBook.date
        holder.travelImage.setImageBitmap(travelBook.images)

        holder.cardView.setOnLongClickListener {
            trigger = false
            selectItem(holder,travelBook,position)
            true
        }
        holder.cardView.setOnClickListener(){
           if (itemSelectedList.contains(travelBook)){
                itemSelectedList.remove(travelBook)
                holder.selectImage.isVisible = false
               if (itemSelectedList.isEmpty()){
                   showMenuDelete(false)
                   isEnabled = false
               }
           }
           else if (isEnabled){
                selectItem(holder,travelBook,position)
           }
        }

        //when a search is made
        if(trigger){
           holder.selectImage.isVisible = false
        }

    }


    private fun selectItem(holder: TravelAdapter.ViewHolderClass, travelBook: TravelBook, position: Int) {
        isEnabled = true
        itemSelectedList.add(travelBook)
        showMenuDelete(true)
        holder.selectImage.isVisible = true
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val design = LayoutInflater.from(mContext).inflate(R.layout.adapter_design, parent, false)
        return ViewHolderClass(design)
    }

    override fun getItemCount(): Int {
        return travelList.size
    }
    fun deleteSelectedItem(){
        if (itemSelectedList.isNotEmpty()){
            itemSelectedList.forEach(){
                viewModel.deleteTravelBook(it)
            }
            isEnabled = false
            itemSelectedList.clear()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<TravelBook>) {
        travelList = list
        notifyDataSetChanged()
    }
}