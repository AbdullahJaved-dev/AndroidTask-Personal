package com.example.androidtask.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtask.R
import com.example.androidtask.model.User


class UserAdapter(private var users: List<User>) : RecyclerView.Adapter<UserAdapter.MViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_user, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val user = users[position]

        //render
        vh.textViewName.text = user.name
        vh.textViewCity.text = user.address.city
        vh.textViewEmail.text = user.email
        vh.textViewCompany.text = user.company.name
        //Glide.with(vh.imageView.context).load(user.photo).into(vh.imageView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun update(data: List<User>) {
        users = data
        notifyDataSetChanged()
    }

    class MViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewEmail: TextView = view.findViewById(R.id.textViewEmail)
        val textViewCity: TextView = view.findViewById(R.id.textViewCity)
        val textViewCompany: TextView = view.findViewById(R.id.textViewCompany)
    }
}