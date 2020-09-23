package nl.soffware.madlevel4task1.dataaccess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main._item.view.*
import nl.soffware.madlevel4task1.R

class ShoppingListAdapter(private val items: List<Item>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun databind(item: Item){
            itemView.tvName.text = item.name
            itemView.tvAmount.text = item.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout._item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}