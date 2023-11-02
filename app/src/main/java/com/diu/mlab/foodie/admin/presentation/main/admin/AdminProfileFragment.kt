package com.diu.mlab.foodie.admin.presentation.main.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.diu.mlab.foodie.admin.R
import com.diu.mlab.foodie.admin.databinding.FragmentAdminProfileBinding
import com.diu.mlab.foodie.admin.presentation.auth.AdminRegFragment
import com.diu.mlab.foodie.admin.presentation.auth.LoginActivity
import com.diu.mlab.foodie.admin.util.getDrawable
import com.diu.mlab.foodie.admin.util.setBounceClickListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminProfileFragment(private val viewModel: AdminMainViewModel) : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var emailId : String
    private lateinit var binding: FragmentAdminProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        preferences = requireActivity().getSharedPreferences(getString(R.string.preference_file_key), AppCompatActivity.MODE_PRIVATE)
        emailId = preferences.getString("email", "nai")!!
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            viewModel.getMyProfile(emailId){
                Log.e("TAG", "getMyProfile failed: $it")
            }
        }
        viewModel.getMyProfile(emailId){
            Log.d("TAG", "onCreate getMyProfile: $it")
        }
        viewModel.myProfile.observe(requireActivity()){ user->
            binding.nm.text = user.nm
            binding.pn.text = user.phone
            binding.des.text = user.loc

            user.pic.getDrawable{ binding.pic.setImageDrawable(it) }
            Log.d("TAG", "onCreate: myProfile observe $user")
        }

        binding.edit.setBounceClickListener{
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.run {
                addToBackStack("xyz")
                hide(this@AdminProfileFragment)
                add(R.id.requestFragment, AdminRegFragment.newInstance("server"))
                commit()
            }

        }
        binding.logout.setBounceClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
        return binding.root
    }

}