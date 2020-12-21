package com.lukasz.witkowski.android.moviestemple.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteAllFavouriteMoviesDialogFragment: DialogFragment() {

    companion object{
        const val TAG = "DeleteAllFavouriteMoviesDialogFragment"
    }

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.delete_all_custom_dialog, null)
        builder.setView(view)
        val dialog = builder.create()
        setButtonClickListeners(view, dialog)
        return dialog
    }


    private fun setButtonClickListeners(view: View, dialog: AlertDialog){
        view.findViewById<Button>(R.id.bt_delete_all_yes).setOnClickListener {
            sharedViewModel.deleteAllFavouriteMovies()
            Toast.makeText(requireContext(), R.string.all_favourite_movies_deleted_info, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.bt_delete_all_cancel).setOnClickListener {
            dialog.dismiss()
        }
    }
}