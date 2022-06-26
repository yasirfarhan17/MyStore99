package com.noor.mystore99.amigrate.ui.dashboard.account.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.ProductModelNew
import com.example.networkmodule.model.UserModel
import com.example.networkmodule.storage.PrefsUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.noor.mystore99.ProfileEdit
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.address.Address
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding,ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()
     val payment: PaymentViewModel by viewModels()

    override fun layoutId(): Int =R.layout.activity_profile
    private val PICK_IMAGE_REQUEST = 1
    private  var  uri: Uri?=null
    lateinit var key:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_profile)
        key=prefsUtil.Name.toString()
        initUi()
        payment.getUserDet(key)
    }

    fun initUi(){
        with(binding){
            imageEdit.setOnClickListener {
                openFileChooser()
            }
            btAddress.setOnClickListener {
                val storageReference=FirebaseStorage.getInstance().reference
                val ref1 = FirebaseDatabase.getInstance().getReference("UserNew")
                lifecycleScope.launch {

                    val ref: StorageReference =
                        storageReference.child(System.currentTimeMillis().toString())
                    if (uri != null) {
                        ref.putFile(uri!!).addOnSuccessListener(object :
                            OnSuccessListener<UploadTask.TaskSnapshot> {
                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                                p0!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener(object :
                                    OnSuccessListener<Uri> {
                                    override fun onSuccess(p0: Uri?) {
                                        val url = p0.toString()
                                        val obj = UserModel(
                                            name = binding.name.text.toString(),
                                            address = binding.adress.text.toString(),
                                            photo = url
                                        )


                                        ref1.child(key!!).setValue(obj)
                                        showToast("Successfully updated")
                                    }

                                })

                            }

                        })
                    } else if (binding.name.text.toString()!=null ||binding.adress.text.toString()!=null){
                        val obj = UserModel(
                            name = binding.name.text.toString(),
                            address = binding.adress.text.toString(),
                        )
                        ref1.child(key!!).setValue(obj)
                        showToast("Successfully updated")
                    }
                }
            }
            changeAddress.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,Address::class.java))
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uri = data.data!!
            binding.imageProfile.load(uri){
                transformations(CircleCropTransformation())
            }
        }
    }



    @SuppressLint("SetTextI18n")
    override fun addObservers() {
        payment.userDetail.observe(this){
            with(binding){
                tvName.text="Hi ${it.name}"
                name.setText(it.name)
                phone.setText(key.toString())
                adress.setText(it.address)
                if(it.photo!=null)
                imageProfile.load(it.photo){
                    transformations(CircleCropTransformation())
                }
            }
        }

    }
}