package com.marangoz.travelbook.ui.about

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.marangoz.travelbook.R
import com.marangoz.travelbook.databinding.FragmentAboutBinding
import java.util.*


class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sp.edit()

        if (sp.getBoolean("darkMode", false)) {
            binding.darkMode.isChecked = true
        }
        if (sp.getString("dili","en") == "English"){
            binding.languageText.text = "English"
        }else{
            binding.languageText.text = "Türkçe"

        }



        binding.darkMode.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)//karanlık mod açık
                editor.putBoolean("darkMode", true)
                editor.commit()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("darkMode", false)
                editor.commit()
            }

        }

        binding.cardLanguage.setOnClickListener() {
            val popUp = PopupMenu(requireContext(), binding.cardLanguage)
            popUp.menuInflater.inflate(R.menu.pop_up_menu, popUp.menu)

            popUp.setOnMenuItemClickListener() { item ->
                when (item.itemId) {
                    R.id.action_english -> {
                        binding.languageText.text = "English"
                        changeLanguage("en")
                        editor.putString("dil", "en")
                        editor.putString("dili","English")
                        editor.commit()

                        changeInterface("English")

                        true
                    }
                    R.id.action_turkish -> {
                        binding.languageText.text = "Türkçe"
                        changeLanguage("tr")
                        editor.putString("dil", "tr")
                        changeInterface("Türkçe")
                        editor.putString("dili","Türkçe")
                        editor.commit()
                        true
                    }
                    else -> false
                }
            }


            popUp.show()


        }



        return binding.root
    }

    fun changeLanguage(str: String) {
        val locale = Locale(str)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        requireContext().resources.updateConfiguration(
            configuration,
            requireContext().resources.displayMetrics
        )

    }
   fun changeInterface(str : String){
       binding.apply {
           genareltText.text=resources.getString(R.string.genel)
           darkMode.text=resources.getString(R.string.koyu_tema)
           dilText.text=resources.getString(R.string.dil)
           languageText.text=str
           aboutText.text = resources.getString(R.string.hakkimda)
           versionText.text = resources.getString(R.string.version)
       }
   }

}