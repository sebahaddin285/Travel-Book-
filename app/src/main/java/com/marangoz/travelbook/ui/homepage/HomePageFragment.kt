package com.marangoz.travelbook.ui.homepage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.parseAsHtml
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.marangoz.travelbook.R
import com.marangoz.travelbook.adapter.TravelAdapter
import com.marangoz.travelbook.databinding.FragmentHomePageBinding
import com.marangoz.travelbook.repository.Repository
import com.marangoz.travelbook.room.TravelBookDao
import com.marangoz.travelbook.room.TravelBookDataBase
import java.util.*


private lateinit var binding: FragmentHomePageBinding
private lateinit var viewModel: HomePageFragmentViewModel
class HomePageFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {
    private var mainMenu : Menu? = null
    private val db: TravelBookDataBase by lazy { TravelBookDataBase.accsessDatabase(requireContext())!! }
    private val tDao: TravelBookDao by lazy { db.getTravelBookDao() }
    private val repo: Repository by lazy { Repository(tDao) }
    val viewModelFactory by lazy { HomePageFragmentViewModelFactory(repo) }
    val adap: TravelAdapter by lazy { TravelAdapter(requireActivity(), viewModel){ show -> showDeleteMenu(show)} }
    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        binding.swipeRefleshLayout.isRefreshing = true

        val sp = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        if (sp.getBoolean("darkMode",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)//karanlık mod açık
        }
        if (sp.getString("dil","tr")=="tr"){
            val locale = Locale("tr")
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.locale= locale
            requireContext().resources.updateConfiguration(configuration,requireContext().resources.displayMetrics)
        }else{
            val locale = Locale("en")
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.locale= locale
            requireContext().resources.updateConfiguration(configuration,requireContext().resources.displayMetrics)

        }





        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(HomePageFragmentViewModel::class.java)

        //setting recyclerview
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adap
        }
        binding.swipeRefleshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                removeSelectTick()
                viewModel.allTravelBook()
                val timer = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        binding.swipeRefleshLayout.isRefreshing = false
                    }
                }
                timer.start()
            }
        })
        //pull data from database
        object : CountDownTimer(1200, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                viewModel.allTravelBook()
                binding.swipeRefleshLayout.isRefreshing = false
            }
        }.start()

        //travellist listener
        viewModel.travelBookList.observe(viewLifecycleOwner){
            adap.setList(it)
        }



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
        if (p0 != null){
            removeSelectTick()
            searchDataBase(p0)
        } else {
            viewModel.allTravelBook()
            adap.trigger = false
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        mainMenu = menu
        menuInflater.inflate(R.menu.toolbarmenu, mainMenu)
        val item = menu.findItem(R.id.app_bar_search)
        showDeleteMenu(false)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_delete ->{
                deleteTravelBook()
                adap.trigger = true
            }
            R.id.action_ayarlar ->{
                Navigation.findNavController(binding.floatingActionButton).navigate(R.id.action_homePageFragment_to_aboutFragment)
            }
        }
        return true
    }
   fun showDeleteMenu(show:Boolean){
       mainMenu?.findItem(R.id.action_delete)?.isVisible = show
   }

   fun deleteTravelBook(){
        val alertDialog = AlertDialog.Builder(requireContext())
       alertDialog.setTitle(resources.getString(R.string.silmeislem))
       alertDialog.setMessage(resources.getString(R.string.silmeemin))
       alertDialog.setPositiveButton(resources.getString(R.string.sil)){_,_->
           adap.deleteSelectedItem()
           val sb = Snackbar.make(binding.recyclerView, resources.getString(R.string.islembasari), Snackbar.LENGTH_SHORT)
           sb.setTextColor(Color.BLUE)
           sb.setBackgroundTint(Color.WHITE)
           sb.show()
           showDeleteMenu(false)

       }
       alertDialog.setNegativeButton(resources.getString(R.string.iptal)){_,_ -> }
       alertDialog.show()
    }

    fun searchDataBase(query : String){
        val searchQuery = "%$query%"
        viewModel.byTitle(searchQuery)
    }

    fun removeSelectTick(){
        showDeleteMenu(false)
        adap.trigger = true
        adap.isEnabled = false
        adap.itemSelectedList.clear()
    }


}