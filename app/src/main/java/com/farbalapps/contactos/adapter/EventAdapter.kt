package com.farbalapps.contactos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.farbalapps.contactos.R
import com.farbalapps.contactos.model.EventEntity
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var events: List<EventEntity> = emptyList()

    fun updateEvents(newEvents: List<EventEntity>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.eventTitle)
        private val descriptionText: TextView = itemView.findViewById(R.id.eventDescription)
        private val timeText: TextView = itemView.findViewById(R.id.eventTime)

        fun bind(event: EventEntity) {
            titleText.text = event.title
            descriptionText.text = event.description

        }
    }
}