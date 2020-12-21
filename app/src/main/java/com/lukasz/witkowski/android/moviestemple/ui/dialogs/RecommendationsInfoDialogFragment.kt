package com.lukasz.witkowski.android.moviestemple.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.lukasz.witkowski.android.moviestemple.R

class RecommendationsInfoDialogFragment: DialogFragment() {

    companion object{
        const val TAG = "RecommendationsInfoDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.recommendations_info_dialog, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        view.findViewById<Button>(R.id.bt_ok).setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }

}