package com.lukasz.witkowski.android.moviestemple.dialogs

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.databinding.ActorCustomDialogBinding

class ActorDialogFragment: DialogFragment() {


    companion object{

        const val TAG = "ActorDialogFragment"

        private const val ACTOR_NAME_KEY = "ACTOR_NAME_KEY"
        private const val ACTOR_PHOTO_KEY = "ACTOR_PHOTO_KEY"
        private const val ACTOR_LINK_KEY = "ACTOR_LINK_KEY"


        fun newInstance(name: String, photo: String?, link: String): ActorDialogFragment{
            val args = Bundle()
            args.putString(ACTOR_NAME_KEY, name)
            args.putString(ACTOR_PHOTO_KEY, photo)
            args.putString(ACTOR_LINK_KEY, link)
            val fragment = ActorDialogFragment()
            fragment.arguments = args
            return fragment
        }


    }

    private lateinit var binding: ActorCustomDialogBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.actor_custom_dialog, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        setUpOnClickListeners(view)
    }



    private fun setUpView(view: View) {
        binding.actorName.text = arguments?.getString(ACTOR_NAME_KEY)
        Glide.with(view)
                .load(Uri.parse(arguments?.getString(ACTOR_PHOTO_KEY)))
                .placeholder(R.drawable.actor_photo_default)
                .into(binding.actorPhoto)
    }


    private fun setUpOnClickListeners(view: View) {
        binding.moreInfoButton.setOnClickListener {
            openTMDBPageAboutActor()
        }
        binding.exitIcon.setOnClickListener {
            dismiss()
        }
    }


    private fun openTMDBPageAboutActor() {
        try {
            val path = arguments?.getString(ACTOR_LINK_KEY)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(path)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}