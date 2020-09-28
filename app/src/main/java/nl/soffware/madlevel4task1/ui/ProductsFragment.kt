package nl.soffware.madlevel4task1.ui

import android.annotation.SuppressLint
import android.app.AlertDialog.*
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_product_dialog.*
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.soffware.madlevel4task1.R
import nl.soffware.madlevel4task1.repository.ProductRepository
import nl.soffware.madlevel4task1.model.Product

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProductsFragment : Fragment() {

    private lateinit var productRepository: ProductRepository

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val products = arrayListOf<Product>()
    private val productAdapter = ShoppingListAdapter(products)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ProductRepository(requireContext())
        getProductsFromDatabase()

        initViews()

        fb_add_product.setOnClickListener {
            showAddProductDialog()
        }

        fb_remove_all.setOnClickListener {
            removeAllProducts()
        }
    }

    private fun removeAllProducts() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deletaAll()
            }
            getProductsFromDatabase()
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddProductDialog() {
        val builder = Builder(requireContext())
        builder.setTitle(getString(R.string.add_product_dialog_title))
        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.et_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.et_amount)


        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) { _: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun addProduct(txtProductName: EditText, txtAmount: EditText) {
        if (validateFields(txtProductName, txtAmount)) {
            mainScope.launch {
                val product = Product(
                    name = txtProductName.text.toString(),
                    amount = txtAmount.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getProductsFromDatabase()
            }
        }
    }

    private fun validateFields(txtProductName: EditText
                               , txtAmount: EditText
    ): Boolean {
        return if (txtProductName.text.toString().isNotBlank()
            && txtAmount.text.toString().isNotBlank()
        ) {
            true
        } else {
            Toast.makeText(activity, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }


    private fun getProductsFromDatabase() {
        mainScope.launch {
            val products = withContext(Dispatchers.IO) {
                productRepository.getAllItems()
            }
            this@ProductsFragment.products.clear()
            this@ProductsFragment.products.addAll(products)
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        val viewManager = LinearLayoutManager(activity)
        rvProducts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        createItemTouchHelper().attachToRecyclerView(rvProducts)

        rvProducts.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = productAdapter
        }

    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = products[position]

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getProductsFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

}