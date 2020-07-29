package com.example.pencast.ui.me

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.example.pencast.R
import com.example.pencast.login.User
import com.example.pencast.ui.me.MeFragmentDirections
import com.example.pencast.ui.me.aboutMe.AboutMeFragment
import com.example.pencast.ui.me.latestMessage.LatestMessageFragment
import com.google.android.material.tabs.TabLayout


class MeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_me, container, false)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                childFragmentManager
            )
        val viewPager: ViewPager = view.findViewById(R.id.me_viewpager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.me_tabs)
        tabs.setupWithViewPager(viewPager)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_me, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_my_profile -> {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(requireActivity().application)

                NavHostFragment.findNavController(this).navigate(
                    MeFragmentDirections.actionNavigationMeToNavigationProfile(
                        User(
                            sharedPreferences.getString("UID", "No-Uid")!!,
                            sharedPreferences.getString("USERNAME", "Guest User")!!,
                            sharedPreferences.getString(
                                "PROFILE_IMAGE_URL",
                                "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
                            )!!,
                            sharedPreferences.getString("STATUS", "Come join us at PenCast!!")!!
                        )
                    )
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal class SectionsPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AboutMeFragment.newInstance()
                else -> LatestMessageFragment.newInstance()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "ABOUT ME"
                else -> "CONVERSATIONS"
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}