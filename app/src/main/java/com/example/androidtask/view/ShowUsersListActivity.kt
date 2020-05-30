package com.example.androidtask.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.emedinaa.kotlinmvvm.di.Injection
import com.example.androidtask.R
import com.example.androidtask.model.User
import com.example.androidtask.viewmodel.UserViewModel
import com.example.androidtask.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_show_users_list.*
import kotlinx.android.synthetic.main.layout_error.*

class ShowUsersListActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    companion object {
        const val TAG= "CONSOLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users_list)
        setupViewModel()
        setupUI()
    }

    //ui
    private fun setupUI(){
        adapter= UserAdapter(viewModel.users.value?: emptyList())
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= adapter
    }

    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this, ViewModelFactory(Injection.providerRepository())).get(UserViewModel::class.java)
        viewModel.users.observe(this,renderUsers)
        viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        viewModel.onMessageError.observe(this,onMessageErrorObserver)
        viewModel.isEmptyList.observe(this,emptyListObserver)
    }

    //observers
    private val renderUsers= Observer<List<User>> {
        Log.v(TAG, "data updated $it")
        layoutError.visibility= View.GONE
        layoutEmpty.visibility= View.GONE
        adapter.update(it)
    }

    private val isViewLoadingObserver= Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility=if(it) View.VISIBLE else View.GONE
        progressBar.visibility= visibility
    }

    private val onMessageErrorObserver= Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutError.visibility= View.VISIBLE
        layoutEmpty.visibility= View.GONE
        textViewError.text= "Error $it"
    }

    private val emptyListObserver= Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility= View.VISIBLE
        layoutError.visibility= View.GONE
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }
}
