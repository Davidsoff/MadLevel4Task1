package nl.soffware.madlevel4task1.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main._item.view.*
import nl.soffware.madlevel4task1.R
import nl.soffware.madlevel4task1.model.Product

class ShoppingListAdapter(private val items: List<Product>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun databind(product: Product){
            itemView.txt_product_name.text = product.name
            itemView.txt_quantity.text = product.amount.toString()
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