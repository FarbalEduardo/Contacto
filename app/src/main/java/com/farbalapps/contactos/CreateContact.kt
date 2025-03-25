package com.farbalapps.contactos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farbalapps.contactos.databinding.FragCreateContactBinding

class CreateContact : Fragment() {

    private var mActivity: MainActivity? = null
    private lateinit var mBinding: FragCreateContactBinding
    private var isExpanded = false // Estado inicial: campos ocultos

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as? MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inicializar el Binding
        mBinding = FragCreateContactBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el clic en "Ver más"
        mBinding.tvShowMore.setOnClickListener {
            isExpanded = !isExpanded
            showOptionalFields(isExpanded)
            // cambiar el icono y el texto del view mas
            updateShowMoreButton(isExpanded)
        }
    }
    private fun updateShowMoreButton(expanded: Boolean) {
        if (expanded) {
            mBinding.tvShowMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            mBinding.tvShowMore.setText(R.string.label_hide)
        } else {
            mBinding.tvShowMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            mBinding.tvShowMore.setText(R.string.label_show_more)
        }
    }

    private fun showOptionalFields(show: Boolean) {
        with(mBinding) {
            // Mostrar u ocultar los campos opcionales
            tilCreateNickname.visibility = if (show) View.VISIBLE else View.GONE
            tilCreateContactGroup.visibility = if (show) View.VISIBLE else View.GONE
            tilCreateWorkInfo.visibility = if (show) View.VISIBLE else View.GONE
            tilCreateWorkPhone.visibility = if (show) View.VISIBLE else View.GONE
          //  tilCreateWorkEmail.visibility = if (show) View.VISIBLE else View.GONE
        }
    }
    override fun onDestroy() {
        mActivity= null
        super.onDestroy()
    }
}