package com.example.mediahider

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediahider.databinding.FragmentAppDetailBinding
import java.io.Serializable


class AppDetailFragment : Fragment() {

    private var _binding: FragmentAppDetailBinding? = null
    private val binding get() = _binding!!

    private var fragmentType: FragmentType? = null

    enum class FragmentType : Serializable {
        SCREEN1, SCREEN2
    }

    companion object {
        private const val KEY_FRAGMENT_TYPE = "key_fragment_type"

        @JvmStatic
        fun newInstance(fragmentType: FragmentType): AppDetailFragment {
            val fragment = AppDetailFragment()
            val args = Bundle()
            args.putSerializable(KEY_FRAGMENT_TYPE, fragmentType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentType = arguments?.getSerializable(KEY_FRAGMENT_TYPE) as? FragmentType

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAppDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            initView()
        } catch (ex: Resources.NotFoundException) {
            // Crashlytics.logException(Throwable("Resource not found Exception in onboarding", ex))
        } catch (e: Exception) {
            // Crashlytics.logException(Throwable("Exception in onboarding", e))
        }
    }

    private fun initView() {
        when (fragmentType) {
            FragmentType.SCREEN1 -> {
                context?.let {
                    binding.textOne.text = it.getString(R.string.onboardtxt1)
//                    binding.imageViewOnboard.setImageResource(R.drawable.first)
                    binding.secondaryTxt.text = it.getString(R.string.onboardtxt2)
                }
            }

            FragmentType.SCREEN2 -> {
//                binding.imageViewOnboard.setImageResource(R.drawable.secondd)
                context?.let {
                    binding.textOne.text = it.getString(R.string.onboardtxt3)
                    binding.secondaryTxt.text = it.getString(R.string.onboardtxt4)
                }
            }

          

            else -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}