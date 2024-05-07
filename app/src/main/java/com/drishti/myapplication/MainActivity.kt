package com.drishti.myapplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drishti.myapplication.databinding.ActivityMainBinding
import com.drishti.myapplication.databinding.ItemsPostBinding
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("users")
    fun GetApiKey(): retrofit2.Call<GetApiKey>

    @GET("users/2322070")
    fun GetSingleUser(): retrofit2.Call<GetApiKeyItem>

    @GET("users/{id}")
    fun GetSingleUserPath(@Path("id") string: String): retrofit2.Call<GetApiKeyItem>

    @POST("users")
    @FormUrlEncoded
    fun postUser(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("gender") gender: String,
        @Field("status") status: String,
    ) : retrofit2.Call<GetApiKeyItem>

    @GET("users")
    fun getUsersPerPage(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int) : retrofit2.Call<GetApiKey>
}
class MainActivity : AppCompatActivity() {
    var page = 0
    var perPageQuery = 10
    var retrofit: Retrofit? = null
    var list = ArrayList<GetApiKeyItem>()
    private val TAG = "RetrofitActivity"
    lateinit var arrayAdapter: ArrayAdapter<GetApiKeyItem>
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var retrofitAdapter = RetrofitAdapter(list)
    var apiInterface: RetrofitInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView?.adapter = retrofitAdapter
        retrofit = Retrofit.Builder()
            .baseUrl("https://gorest.co.in/public/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiInterface = retrofit?.create(RetrofitInterface::class.java)
        binding?.btnAPI?.setOnClickListener {
            apiInterface?.GetApiKey()?.enqueue(object : Callback<GetApiKey> {
                override fun onResponse(
                    call: retrofit2.Call<GetApiKey>,
                    response: Response<GetApiKey>
                ) {
                    Log.e(TAG, "Api response ${response.body()}")
                    var responseBody = response.body()
                    list.addAll(responseBody as ArrayList<GetApiKeyItem>)
                    arrayAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: retrofit2.Call<GetApiKey>, t: Throwable) {
                }
            })
        }

        binding?.btnSingleUser?.setOnClickListener {
//            apiInterface?.GetSingleUser()?.enqueue(object: Callback<GetApiKeyItem>{
//                 override fun onResponse(
//                     call: Call<GetApiKeyItem>,
//                     response: Response<GetApiKeyItem>
//                 ) {
//                     Log.e(TAG, "single item response ${response.body()}")
//                 }
//
//                 override fun onFailure(call: Call<GetApiKeyItem>, t: Throwable) {
//                 }
//             })
            apiInterface?.GetSingleUserPath("2322070")
                ?.enqueue(object : Callback<GetApiKeyItem> {
                    override fun onResponse(
                        call: retrofit2.Call<GetApiKeyItem>,
                        response: Response<GetApiKeyItem>
                    ) {
                        Log.e(TAG, "single item response with path ${response.body()}")
                    }

                    override fun onFailure(call: retrofit2.Call<GetApiKeyItem>, t: Throwable) {
                    }
                })
        }
        binding.fab.setOnClickListener {
            showCustomDialog()
        }
        binding.btnPaginationUser.setOnClickListener {
            page = 0
            hitPaginationApi()
        }
        binding.listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e(TAG, "in scroll")
                page++
                hitPaginationApi()
            }
        })
    }

    private fun hitPaginationApi() {
        apiInterface?.getUsersPerPage(page, perPageQuery)
            ?.enqueue(object : Callback<GetApiKey> {
                override fun onResponse(
                    call: retrofit2.Call<GetApiKey>,
                    response: Response<GetApiKey>
                ) {
                    Log.e(TAG, " pagination response ")
                    var responseBody = response.body()
                    list.addAll(responseBody as ArrayList<GetApiKeyItem>)
                    retrofitAdapter.notifyDataSetChanged()
                }
                override fun onFailure(call: retrofit2.Call<GetApiKey>, t: Throwable) {
                }
            })
    }

    private fun showCustomDialog() {
        val dialogView = ItemsPostBinding.inflate(layoutInflater)
        var dialog = Dialog(this)
        dialog.setContentView(dialogView.root)
        dialog.show()

        dialogView.btnadd.setOnClickListener {
            if (dialogView.etName.text.isNullOrEmpty()) {

            } else if (dialogView.etEmail.text.isNullOrEmpty()) {

            } else {
                var selectedGender = if (dialogView.rbMale.isSelected)
                    "Male" else
                    "Female"
                var isActive = if (dialogView.rbActive.isChecked)
                    "active"
                else
                    "inactive"
                dialog.dismiss()
                apiInterface?.postUser(
                    "Bearer 0450c96e19906083cf61b82e0c5112762f3e3814b87a64a7a577d84a20ad891a",
                    dialogView.etEmail.text.toString(),
                    dialogView.etName.text.toString(),
                    selectedGender,
                    isActive
                )?.enqueue(object : Callback<GetApiKeyItem> {
                    override fun onResponse(
                        call: retrofit2.Call<GetApiKeyItem>,
                        response: Response<GetApiKeyItem>
                    ) {
                        Log.e(TAG, "response body ${response.body()}")
                        if (response.isSuccessful)
                            AlertDialog.Builder(this@MainActivity).apply {
                                setTitle("Added")
                                show()
                            }
                    }

                    override fun onFailure(call: retrofit2.Call<GetApiKeyItem>, t: Throwable) {
                    }

                })
            }
        }

    }
}