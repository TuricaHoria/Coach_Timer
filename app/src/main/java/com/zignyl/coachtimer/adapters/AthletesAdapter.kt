package com.zignyl.coachtimer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zignyl.coachtimer.R
import com.zignyl.coachtimer.models.User

class AthletesAdapter(private val athletes : MutableList<User> , val onAthleteSelected : (Int) -> Unit) : RecyclerView.Adapter<AthletesAdapter.AthletesViewHolder>(){

    var context : Context ?= null

    class AthletesViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val name : TextView = v.findViewById(R.id.tv_user_name)
        val picture : ImageView = v.findViewById(R.id.iv_user_photo)
        val layout : ConstraintLayout = v.findViewById(R.id.cl_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthletesViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return AthletesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  athletes.size
    }

    override fun onBindViewHolder(holder: AthletesViewHolder, position: Int) {

        val name = "${athletes[position].name.first}   ${athletes[position].name.last}"

        holder.name.text = name
        context?.let {
            Glide
                .with(it)
                .load(athletes[position].picture.thumbnail)
                .into(holder.picture)
        }
        holder.layout.setOnClickListener {  }
    }
}