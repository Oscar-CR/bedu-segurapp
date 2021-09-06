package org.bedu.segurapp.ui.socialSecurity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.EmergencyServicesAdapter
import org.bedu.segurapp.databinding.ActivitySocialSecurityBinding
import org.bedu.segurapp.interfaces.IWebServices
import org.bedu.segurapp.models.EmergencyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SocialSecurityActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySocialSecurityBinding.inflate(layoutInflater) }
    private lateinit var adapter: EmergencyServicesAdapter
    private lateinit var skeleton: Skeleton
    private var emergencyServiceList: MutableList<EmergencyService> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        createSkeleton()
        setUpRecyclerView()
    }

    private fun createSkeleton(){
        skeleton = binding.recyclerCalls.applySkeleton(R.layout.item_emergency_service)
        skeleton.showSkeleton()
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView() {
        with(binding) {
            recyclerCalls.setHasFixedSize(true)
            recyclerCalls.layoutManager = LinearLayoutManager(this@SocialSecurityActivity)

            getServices { responseList ->
                if (responseList.isNotEmpty()) {
                    skeleton.showOriginal()
                    emergencyServiceList = responseList
                    adapter = EmergencyServicesAdapter(
                        { makeACall(it.telephone) },
                        emergencyServiceList,
                    )

                    //asignando el Adapter al RecyclerView
                    recyclerCalls.adapter = adapter
                    initListener()
                }
            }
        }
    }

    //Generando datos
    private fun getServices(response: (MutableList<EmergencyService>) -> Unit) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://segurapi.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endpoint = retrofit.create(IWebServices::class.java)

        val call = endpoint.getServices()

        call.enqueue(object : Callback<List<EmergencyService>> {
            override fun onFailure(call: Call<List<EmergencyService>>, t: Throwable) {
                Log.e("error", "Error: $t")
                response(ArrayList())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<EmergencyService>>,
                response: Response<List<EmergencyService>>
            ) {
                if (response.code() == 200) {
                    val body = response.body()
                    body?.toMutableList()?.let { response(it) }
                } else {
                    Log.e("Not200", "Error not 200: $response")
                    response(ArrayList())
                }
            }

        })

    }


    private fun initListener() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    private fun filter(text: String) {

        val filteredList: MutableList<EmergencyService> = ArrayList()
        for (item in emergencyServiceList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }

        adapter.filterList(filteredList)
    }

    private fun makeACall(phoneCall: String) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE),
                123
            )
        } else {
            startActivity(
                Intent(Intent.ACTION_DIAL)
                    .setData(Uri.parse("tel:${phoneCall.trim()}"))
            )
        }
    }

}