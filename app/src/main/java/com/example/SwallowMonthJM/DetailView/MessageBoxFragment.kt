package com.example.SwallowMonthJM.DetailView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.SwallowMonthJM.databinding.MessaegBoxBinding

class MessageBoxFragment: DialogFragment() {
    private var message : String? = null
    private var onItemClickListener : OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(){}
    }
    fun setOnclickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        message = arguments?.getString("message")
    }

    private lateinit var binding : MessaegBoxBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MessaegBoxBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (message!=null){
            binding.text.text = message
        }

        binding.closeButton.setOnClickListener {
            this@MessageBoxFragment.dismissNow()
        }

        binding.commitButton.setOnClickListener {
            if (onItemClickListener!=null){
                onItemClickListener?.onItemClick()
            }
        }
    }


    companion object{

        fun newInstance(message: String) =
            MessageBoxFragment().apply {
                arguments = Bundle().apply {
                    putString("message",message)
                }
            }
    }
}