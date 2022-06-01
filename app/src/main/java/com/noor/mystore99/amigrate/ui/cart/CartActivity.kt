package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CartModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentActivity
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.Util.showAlert
import com.noor.mystore99.databinding.ActivityNewCartBinding
import com.noor.mystore99.databinding.BottomSheetCartDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityNewCartBinding, CartViewModel>(),cartCallBack {

    override val viewModel: CartViewModel by viewModels()
    override fun layoutId(): Int = R.layout.activity_new_cart
    lateinit var bindingSheet : BottomSheetCartDetailsBinding
    lateinit var  key:String

    companion object{

        var subValue:Int=0
        var finalTotalPrice:Int=0
        var deliverCharge=0
        lateinit var cartValue : ArrayList<CartEntity>
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cart)
        key=prefsUtil.Name.toString()
        initUi()
        addListener()
    }

    private fun addListener() {
        with(binding) {
            update_counter()
            btCheckOut.setOnClickListener {
                if(subValue<100){
                    Toast.makeText(this@CartActivity,"Please add more item",Toast.LENGTH_SHORT).show()
                }
                else{
                    update_counter()
                    var intent=Intent(this@CartActivity, PaymentActivity::class.java)
                    Log.d("checkamt", ""+finalTotalPrice)
                    intent.putExtra("amount", finalTotalPrice.toString())
                    startActivity(intent)
                    //startActivity(Intent(this@CartActivity, PaymentActivity::class.java))
                }

            }
            imgClearCart.setOnClickListener {
                showAlert(
                    this@CartActivity,
                    getString(R.string.txt_clear_cart_question),
                    getString(R.string.all_the_item_in_cart_will_be_cleared)
                ) {
                    viewModel.clearCart()
                    itemsPresentInCart(false)
                    (binding.rvCart.adapter as CartAdapter).clearAdapter()

                }
            }
            tvViewDetails.setOnClickListener {
                update_counter()
                showCartDetailsBottomDialog()


            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btStartBuying.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun showCartDetailsBottomDialog() {
        val cartBottomSheetDialog = BottomSheetDialog(this)
        bindingSheet = DataBindingUtil.inflate<BottomSheetCartDetailsBinding>(
            layoutInflater,
            R.layout.bottom_sheet_cart_details,
            null,
            false
        )
        cartBottomSheetDialog.setContentView(bindingSheet.root)
        bindingSheet.tvSubTotal.text="₹ " +subValue.toString()
        if(deliverCharge==20){
            bindingSheet.tvDeliveryCharge.text="₹ 20"
        }
        else if (deliverCharge==10){
            bindingSheet.tvDeliveryCharge.text="₹ 10"
        }
        else
            bindingSheet.tvDeliveryCharge.text="free"


        bindingSheet.tvTotal.text= "₹ "+finalTotalPrice
        cartBottomSheetDialog.create()
        cartBottomSheetDialog.show()
    }

    private fun initUi() {

        with(binding) {
            rvCart.layoutManager = LinearLayoutManager(this@CartActivity)
            rvCart.adapter = CartAdapter(this@CartActivity)
        }


    }

    override fun addObservers() {
        viewModel.cartFromDB.observe(this) { cartList ->
            if (cartList.isNullOrEmpty()) {
                itemsPresentInCart(false)
                return@observe
            }
            itemsPresentInCart(true)
            (binding.rvCart.adapter as CartAdapter).submitList(cartList)
            cartValue=cartList
            getTotalPrice(cartList)
            update_counter()
            Log.d("cartcheck", "" + cartList)
        }
    }

    fun getTotalPrice(totalPrice:ArrayList<CartEntity>) {

        subValue=0
        var deliveryCharge:Int=0

        for(totals in totalPrice){
            subValue += totals.total!!.toInt()
        }


        finalTotalPrice= subValue

       // update_counter()
    }

    private fun itemsPresentInCart(isPresent: Boolean) {
        binding.btCheckOut.setVisible(isPresent)
        binding.imgClearCart.setVisible(isPresent)
        binding.rvCart.setVisible(isPresent)
        binding.tvViewDetails.setVisible(isPresent)
        binding.clEmptyCart.setVisible(isPresent.not())
        binding.tvTotalPrice.setVisible(isPresent)

    }

    override fun onClick(price: String, id: String, quant: String) {
        viewModel.updateQuant(price,id,quant)
        //addObservers()
        getTotalPrice(cartValue)
        update_counter()
    }


    @SuppressLint("SetTextI18n")
    fun update_counter(){
        if(subValue in 401..1000) {
            deliverCharge=10

        }

        else if(subValue in 100..400) {
            deliverCharge=20
        }
        else {
            deliverCharge=0

        }
        finalTotalPrice= subValue+ deliverCharge
        binding.tvTotalPrice.text="₹ " + finalTotalPrice
        }

    override fun onDelete(id: String,pos:Int) {
        viewModel.clear(id)
        cartValue.removeAt(pos)
        Log.d("checkcart", ""+cartValue.size)
        getTotalPrice(cartValue)
        update_counter()
        if(cartValue.size==0){
            itemsPresentInCart(false)
        }
        showMessage("Removed Successfully")
        //addObservers()
        //(binding.rvCart.adapter as CartAdapter).submitList(cartValue)
    }
}