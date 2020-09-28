package nl.soffware.madlevel4task1.repository

import android.content.Context
import nl.soffware.madlevel4task1.dao.ProductDao
import nl.soffware.madlevel4task1.database.ShoppingListRoomDatabase
import nl.soffware.madlevel4task1.model.Product

public class ProductRepository(val context: Context) {

    private var productDao: ProductDao

    init {
        val productRoomDatabase = ShoppingListRoomDatabase.getDatabase(context)
        productDao = productRoomDatabase!!.productDao()
    }

    suspend fun getAllItems(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(productToDelete: Product) {
        productDao.deleteProduct(productToDelete)
    }

    suspend fun deletaAll() {
        productDao.deleteAllProducts()
    }


}
