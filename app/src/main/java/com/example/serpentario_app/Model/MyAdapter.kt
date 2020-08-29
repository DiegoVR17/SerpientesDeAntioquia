package com.example.serpentario_app.Model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.serpentario_app.*
import kotlinx.android.synthetic.main.cardview_main.view.*


class MyAdapter(val arrayList: ArrayList<CardViewModel>, val context: Context) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(model: CardViewModel){

            itemView.TitleCard.text = model.title
            itemView.DescriptionCard.text = model.des
            itemView.imageViewCard.setImageResource(model.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_main, parent, false)

        return ViewHolder(v)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            val model = arrayList.get(position)

            if(model.title == "Serpientes venenosas y no venenosas"){
                    val intent = Intent(context, VenNoVenActivity::class.java)
                    context.startActivity(intent)
            }else if (model.title == "Mapa: Serpientes en Antioquia"){
                val intent = Intent(context, MapaActivity::class.java)
                context.startActivity(intent)
            }else if (model.title == "Avistamientos"){
                val intent = Intent(context, AvistamientoActivity::class.java)
                context.startActivity(intent)
            }else{
                val intent = Intent(context, InfoActivity::class.java)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
}