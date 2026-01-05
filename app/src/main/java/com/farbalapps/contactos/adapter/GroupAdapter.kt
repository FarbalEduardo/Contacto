package com.farbalapps.contactos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farbalapps.contactos.ContactAdapter
import com.farbalapps.contactos.databinding.ItemGroupBinding
import com.farbalapps.contactos.model.ContactEntity
import com.farbalapps.contactos.R
import kotlin.collections.listOf

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    data class Group(
        val name: String,
        val contacts: List<ContactEntity>,
        var isExpanded: Boolean = false
    )

    private var groups: List<Group> = listOf()

    fun updateGroups(newGroups: List<Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int = groups.size

    inner class ViewHolder(private val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        private val contactAdapter = ContactAdapter()

        init {
            binding.contactsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = contactAdapter
            }

            binding.headerLayout.setOnClickListener {
                val group = groups[adapterPosition]
                groups = groups.mapIndexed { index, g ->
                    if (index == adapterPosition) g.copy(isExpanded = !g.isExpanded) else g
                }
                binding.expandIcon.setImageResource(
                    if (group.isExpanded) R.drawable.ic_arrow_up
                    else R.drawable.ic_arrow_down
                )
                binding.contactsRecyclerView.visibility =
                    if (group.isExpanded) View.VISIBLE else View.GONE
            }
        }

        fun bind(group: Group) {
            binding.groupNameText.text = group.name
            binding.expandIcon.setImageResource(
                if (group.isExpanded) R.drawable.ic_arrow_up
                else R.drawable.ic_arrow_down
            )
            binding.contactsRecyclerView.visibility =
                if (group.isExpanded) View.VISIBLE else View.GONE
            //contactAdapter.updateContacts(group.contacts)
        }
    }
}