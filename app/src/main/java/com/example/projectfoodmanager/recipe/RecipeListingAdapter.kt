package com.example.projectfoodmanager.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfoodmanager.data.repository.models.Recipe
import com.example.projectfoodmanager.databinding.ItemRecipeLayoutBinding

class NoteListingAdapter(
    val onItemClicked: (Int, Recipe) -> Unit,
    val onEditClicked: (Int, Recipe) -> Unit,
    val onDeleteClicked: (Int, Recipe) -> Unit
) : RecyclerView.Adapter<NoteListingAdapter.MyViewHolder>() {

    private var list: MutableList<Recipe> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemRecipeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: MutableList<Recipe>){
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemRecipeLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recipe){
            binding.recipeId.text = item.id
            binding.msg.text = item.description
            binding.dateLabel.text = item.date.toString()
            binding.edit.setOnClickListener { onEditClicked.invoke(adapterPosition,item) }
            binding.delete.setOnClickListener { onDeleteClicked.invoke(adapterPosition,item) }
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition,item) }
        }
    }
}