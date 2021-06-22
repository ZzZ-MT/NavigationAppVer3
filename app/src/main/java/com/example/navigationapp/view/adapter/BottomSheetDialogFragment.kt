package com.example.navigationapp.view.adapter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import com.example.navigationapp.R
import com.example.navigationapp.databinding.BottomSheetMapBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/
class BottomSheetDialogFragment: BottomSheetDialogFragment() {
    private val TAG ="BottomSheetDialogFragment"
    private var _binding: BottomSheetMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
//    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        //dialog.setOnShowListener { setupHeight(it as BottomSheetDialog) }
        Log.i(TAG,"onCreateDialog")
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.i(TAG,"onCreateView")
        _binding = BottomSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG,"onViewCreated")
        binding.btnAdd.setOnClickListener {
            Log.i(TAG, "onClick add btn")
        }
        binding.btnRoute.setOnClickListener {
            Log.i(TAG, "onClick route btn")
        }
        binding.btnStart.setOnClickListener {
            Log.i(TAG, "onClick start btn")
        }
    }

    private fun setupHeight(bottomSheetDialog: BottomSheetDialog) {
        val linearLayout = bottomSheetDialog.findViewById<View>(R.id.bottomSheetMap) as LinearLayout
        val behavior = BottomSheetBehavior.from(linearLayout)
        behavior.peekHeight

        //behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        Log.i(TAG,"setup Height")
    }

//    private fun setupButton() {
//        binding.btnClose.setOnClickListener {
//            dismissAllowingStateLoss()
//        }
//    }
}