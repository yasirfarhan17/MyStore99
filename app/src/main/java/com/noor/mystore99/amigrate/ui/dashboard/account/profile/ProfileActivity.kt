package com.noor.mystore99.amigrate.ui.dashboard.account.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.address.Address
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding,ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()
     val payment: PaymentViewModel by viewModels()

    override fun layoutId(): Int =R.layout.activity_profile
    private val PICK_IMAGE_REQUEST = 1
    private  var  uri: Uri?=null
    lateinit var key:String
    var ref2=FirebaseDatabase.getInstance().reference

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
                //Log.d("insideupdate",uri.toString())
                val storageReference=FirebaseStorage.getInstance().reference
                val ref1 = FirebaseDatabase.getInstance().getReference("UserNew")
                lifecycleScope.launch {

                    val ref: StorageReference =
                        storageReference.child(System.currentTimeMillis().toString())
                    if (uri != null) {

                        val stream = ByteArrayOutputStream()
                        var bitmap: Bitmap? = null

                        if (uri != null) {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(
                                    this@ProfileActivity.getContentResolver(),
                                    uri
                                )
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            bitmap = (binding.imageProfile.getDrawable() as BitmapDrawable).bitmap
                        }


                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val byteFormat = stream.toByteArray()
                        val encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP)
                        ref1.child(key!!).child("photo").setValue(encodedImage)
                                        showToast("Successfully updated")
                        Log.d("insideupdate","1st")
//                        ref.putFile(uri!!).addOnSuccessListener(object :
//                            OnSuccessListener<UploadTask.TaskSnapshot> {
//                            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
//                                Log.d("insideupdate","2nd")
//                                p0!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener(object :
//                                    OnSuccessListener<Uri> {
//                                    override fun onSuccess(p0: Uri?) {
//                                        val url = p0.toString()
//                                        val obj = UserModel(
//                                            name = binding.name.text.toString(),
//                                            address = binding.adress.text.toString(),
//                                            photo = url
//                                        )
//
//                                        Log.d("insideupdate",url.toString())
//                                        ref1.child(key!!).child("photo").setValue(url)
//                                        if(binding.name.text!=null && binding.name.text.toString()
//                                                .isNotEmpty()
//                                        )
//                                            ref1.child(key!!).child("name").setValue(binding.name.text.toString())
//                                        showToast("Successfully updated")
//                                    }
//
//                                })
//
//                            }

                    //    })
                    } else if ( uri ==null && (binding.name.text.toString()!=null ||binding.adress.text.toString()!=null)){
                        val obj = UserModel(
                            name = binding.name.text.toString(),
                            address = binding.adress.text.toString(),
                        )
                        ref1.child(key!!).child("name").setValue(binding.name.text.toString())
                        //Log.d("insideupdate",uri.toString())
                        showToast("Successfully updated")
                    }
                }
            }
            binding.adress.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,Address::class.java))
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
                val `val`: String = it.photo.toString()

                val decodedString = Base64.decode(`val`, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                binding.imageProfile.setImageBitmap(decodedByte)
                imageProfile.load(decodedByte) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    }
}