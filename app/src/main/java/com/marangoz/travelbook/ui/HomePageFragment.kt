package com.marangoz.travelbook.ui

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import com.marangoz.travelbook.R
import com.marangoz.travelbook.databinding.FragmentHomePageBinding


class HomePageFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {
    private lateinit var binding: FragmentHomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


        binding.floatingActionButton.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.passToSaveFragment)
        }

        return binding.root
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {

       return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {

        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val item2 = menu.findItem(R.id.action_delete)
        item2.isVisible =false
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_delete ->{

            }
        }
        return true
    }


}