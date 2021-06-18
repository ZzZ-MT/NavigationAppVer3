package com.example.navigationapp.view.adapter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.navigationapp.R
import com.example.navigationapp.databinding.BottomSheetMapBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFragment: BottomSheetDialogFragment() {
    private val TAG ="BottomSheetDialogFragment"
    lateinit var binding: BottomSheetMapBinding
    private lateinit var dialog: BottomSheetDialog
//    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupHeight(it as BottomSheetDialog) }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_map, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            Log.i(TAG, "onViewCreated")
        }
    }

    private fun setupHeight(bottomSheetDialog: BottomSheetDialog) {
        val linearLayout = bottomSheetDialog.findViewById<View>(R.id.bottom_sheet_map)
        val behavior = linearLayout?.let { BottomSheetBehavior.from(it) }
        linearLayout?.background = ResourcesCompat.getDrawable(resources,R.drawable.bg_bottomsheet_dialog, null)
        behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

//    private fun setupButton() {
//        binding.btnClose.setOnClickListener {
//            dismissAllowingStateLoss()
//        }
//    }
}