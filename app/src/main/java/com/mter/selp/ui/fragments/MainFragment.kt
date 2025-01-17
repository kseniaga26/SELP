package com.mter.selp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mter.selp.R
import com.mter.selp.databinding.FragmentMainBinding
import com.mter.selp.viewmodels.DataModel
import com.mter.selp.model.UserService
import kotlinx.coroutines.launch

class MainFragment: BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private val dataModel : DataModel by activityViewModels()
    var prefSelpPrem : SharedPreferences? = null
    var selpPremium = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefSelpPrem = activity?.getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        selpPremium = prefSelpPrem?.getBoolean("selpPremium", false)!!

        premiumOn()
        initAction()
        initNavigation()
        showHintDialog(
            title = resources.getString(R.string.title_hint_reminder_mood),
            titleConfirm = resources.getString(R.string.confirm_ok),
            titleCancel = resources.getString(R.string.cancel_later),
            message = resources.getString(R.string.message_hint_reminder_mood)
        )
    }

    private fun saveSelectPrem(result: Boolean){
        val editor = prefSelpPrem?.edit()
        editor?.putBoolean("selpPremium", result)
        editor?.apply()
    }


    private fun premiumOn() {
        dataModel.selpPremium.observe(activity as LifecycleOwner) {
            selpPremium = it
        }
        if (selpPremium){
            binding.psychologicalTest.visibility = View.VISIBLE
            binding.meditation.visibility = View.VISIBLE
        } else {
            binding.psychologicalTest.visibility = View.INVISIBLE
            binding.meditation.visibility = View.INVISIBLE
        }
        saveSelectPrem(selpPremium)
    }

    private fun initNavigation() {

        binding.exercisesProgessive.setOnClickListener {
            openFragment(ProgressFragment())
        }
        binding.myMood.setOnClickListener {
            openFragment(MoodFragment())
        }
        binding.account.setOnClickListener{
            openFragment(AccountFragment())
        }
        binding.sleep.setOnClickListener {
            openFragment(SleepAnalyzedFragment())
        }
        binding.notes.setOnClickListener{
            openFragment(ArticlesFragment())
        }
        binding.psychologicalTest.setOnClickListener{
            openFragment(TestFragment())
        }
        binding.meditation.setOnClickListener{
            openFragment(MeditationFragment())
        }
    }

    override fun onStart() {
        super.onStart()

        val settings = this.activity?.getSharedPreferences(SETTINGS_APP, Context.MODE_PRIVATE)
        if (settings?.getBoolean(HELP_WITH_SOUND, true) == true){
            binding.optionHelpBreathSound.backgroundTintList = context?.getColorStateList(R.color.md_theme_light_colorSecondaryVariant)
            binding.optionHelpBreathVideo.backgroundTintList =
                ColorStateList.valueOf(Color.WHITE)
        } else {
            binding.optionHelpBreathVideo.backgroundTintList = context?.getColorStateList(R.color.md_theme_light_colorSecondaryVariant)
            binding.optionHelpBreathSound.backgroundTintList =
                ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun initAction(){

        binding.helpCard.setOnClickListener {
            val settings = this.activity?.getSharedPreferences(SETTINGS_APP, Context.MODE_PRIVATE)
            if (settings?.getBoolean(HELP_WITH_SOUND, true) == true){
                openFragment(BreathHelpVolumeFragment())
            } else {
                openFragment(BreathHelpVideoFragment())
            }
        }
        binding.optionHelpBreathSound.setOnClickListener {
            it.backgroundTintList = context?.getColorStateList(R.color.md_theme_light_colorSecondaryVariant)
            binding.optionHelpBreathVideo.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

            val settings = this.activity?.getSharedPreferences(SETTINGS_APP, Context.MODE_PRIVATE)
            settings?.edit()?.putBoolean(HELP_WITH_SOUND, true)?.apply()
            openFragment(BreathHelpVolumeFragment())
        }
        binding.optionHelpBreathVideo.setOnClickListener {
            it.backgroundTintList = context?.getColorStateList(R.color.md_theme_light_colorSecondaryVariant)
            binding.optionHelpBreathSound.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

            val settings = this.activity?.getSharedPreferences(SETTINGS_APP, Context.MODE_PRIVATE)
            settings?.edit()?.putBoolean(HELP_WITH_SOUND, false)?.apply()
            openFragment(BreathHelpVideoFragment())
        }
    }

    private companion object {
        const val SETTINGS_APP = "SettingsApp"
        const val HELP_WITH_SOUND = "SettingOptionHelpBreath"
    }
}
