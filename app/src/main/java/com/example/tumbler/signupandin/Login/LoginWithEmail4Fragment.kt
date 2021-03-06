package com.example.tumbler.signupandin.Login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.tumbler.BaseApplication
import com.example.tumbler.UserPagesActivity
import com.example.tumbler.databinding.FragmentLoginWithEmail4Binding
import com.example.tumbler.model.entity.LoginResponse.LoginRequest
// import com.example.tumbler.signupandin.LoginWithEmail4FragmentDirections
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LoginWithEmail4Fragment : Fragment() {

    private val viewModel: LoginViewModel by sharedViewModel()
    var myshared: SharedPreferences? = null

    lateinit var binding: FragmentLoginWithEmail4Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginWithEmail4Binding.inflate(inflater, container, false)
        binding.loginWithEmail4ForgetPasswordTxt.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(LoginWithEmail4FragmentDirections.actionLoginWithEmail4FragmentToForgetPasswordFragment())
        }

        viewModel.LoginLiveData.observe(
            viewLifecycleOwner,
            Observer {
                Log.i("Elonsol11", it.meta.status.toString())
                if (it.meta.status.toInt() == 422 || it.meta.status.toInt() == 500) {
                    Log.i("Elonsol", "Inside condition")
                    Toast.makeText(context, it.meta.msg, Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(context, it.meta.msg, Toast.LENGTH_SHORT).show()
                    myshared = getActivity()?.getSharedPreferences("myshared", 0)
                    var editor: SharedPreferences.Editor = myshared!!.edit()
                    editor.putString("access_token", it.response.access_token.toString())
                    editor.putString("id", it.response.id.toString())
                    editor.putString("blog_username", it.response.blog_username.toString())
                    editor.putString("email", it.response.email.toString())
                    editor.putString("blog_avatar", it.response.blog_avatar.toString())
                    editor.putString("blog_id", it.response.blog_id.toString())
                    editor.putString("is_verified", it.response.is_verified.toString())
                    editor.commit()
                    Log.i("YY", "HERE")
                    (activity?.applicationContext as BaseApplication).setUser(
                        it.response.access_token.toString(),
                        it.response.blog_id.toInt(),
                        it.response.blog_avatar.toString(),
                        it.response.id.toInt(),
                        it.response.blog_username.toString(),
                        it.response.is_verified.toBoolean(),
                        it.response.email.toString()
                    )
                    val INTENT = Intent(this.context, UserPagesActivity::class.java)
                    startActivity(INTENT)
                }
            }
        )

        binding.loginWithEmail4LoginTxt.setOnClickListener {
            if (binding.login4TxtEmail.text.isNullOrEmpty() || binding.login4TxtPassword.text.isNullOrEmpty()) {
                Toast.makeText(context, "missed inputs", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.Login(
                    LoginRequest(
                        binding.login4TxtEmail.text.toString(),
                        binding.login4TxtPassword.text.toString()
                    )
                )
            }
        }

        return binding.root
    }
}
