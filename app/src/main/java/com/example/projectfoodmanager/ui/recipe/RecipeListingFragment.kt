package com.example.projectfoodmanager.ui.recipe

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.projectfoodmanager.R
import com.example.projectfoodmanager.data.model.Recipe
import com.example.projectfoodmanager.databinding.FragmentRecipeListingBinding
import com.example.projectfoodmanager.ui.auth.AuthViewModel
import com.example.projectfoodmanager.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.floor
private const val ARG_PARAM1 = "param1"
@AndroidEntryPoint
class RecipeListingFragment : Fragment() {


    private var isFirstTimeCall = true
    private var snapHelper : SnapHelper = PagerSnapHelper()
    lateinit var manager: LinearLayoutManager
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var list: MutableList<Recipe> = arrayListOf()
    private var searchMode: Boolean = false

    val TAG: String = "RecipeListingFragment"


    lateinit var binding: FragmentRecipeListingBinding
    val viewModel: RecipeViewModel by viewModels()
    private val authModel: AuthViewModel by viewModels()
    private val adapter by lazy {
        RecipeListingAdapter(
            onItemClicked = {pos,item ->

                findNavController().navigate(R.id.action_receitaListingFragment_to_receitaDetailFragment,Bundle().apply {

                    putParcelable("note",item)
                })
            },
            this.authModel,
            this.viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //todo check for internet connection
        if (this::binding.isInitialized){
            return binding.root
        }else {
        binding = FragmentRecipeListingBinding.inflate(layoutInflater)
        manager = LinearLayoutManager(activity)
        manager.orientation=LinearLayoutManager.HORIZONTAL
        manager.reverseLayout=false
        binding.recyclerView.layoutManager = manager
        snapHelper.attachToRecyclerView(binding.recyclerView)


        setRecyclerViewScrollListener()
        return binding.root
        }
        }



    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!searchMode) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (isFirstTimeCall) {
                            isFirstTimeCall = false;
                            binding.recyclerView.removeOnScrollListener(scrollListener)
                            val visibleItemCount: Int = manager.childCount
                            val pastVisibleItem: Int =
                                manager.findLastCompletelyVisibleItemPosition()
                            val pag_index =
                                floor(((pastVisibleItem + 1) / FireStorePaginations.RECIPE_LIMIT).toDouble())
                            if ((pastVisibleItem + 1) % FireStorePaginations.RECIPE_LIMIT.toInt() == 0) {
                                viewModel.getRecipesPaginated(false)
                            }
                            Log.d(TAG, pag_index.toString())
                            Log.d(TAG, visibleItemCount.toString())
                            Log.d(TAG, pastVisibleItem.toString())


                            binding.recyclerView.addOnScrollListener(scrollListener)
                        }
                    }

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        isFirstTimeCall = true
                    }

                    super.onScrollStateChanged(recyclerView, newState)
                }
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
         authModel.getUserSession{
             if (it != null)
                binding.tvName.text = it.first_name + " " + it.last_name
        }

        binding.SVsearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null){
                    viewModel.getRecipesByTitle(text,true)
                }

                return true
            }
        })


        viewModel.getRecipesPaginated(true)
        var firstTimeLoading = true
        viewModel.recipe.observe(viewLifecycleOwner){state ->

            when(state){
                is UiState.Loading ->{
                    if (firstTimeLoading)
                        binding.progressBar.show()
                        firstTimeLoading = false

                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    for (item in state.data.toMutableList())
                        if (list.indexOf(item)==-1)
                            list.add(item)
                    adapter.updateList(list)
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
            }
        }
        viewModel.recipe_search.observe(viewLifecycleOwner){state ->
            val lista:MutableList<Recipe> = arrayListOf()
            when(state){

                is UiState.Loading ->{
                    adapter.updateList(arrayListOf())
                    binding.progressBar.show()

                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    for (item in state.data.toMutableList())
                        if (lista.indexOf(item)==-1)
                            lista.add(item)
                    adapter.updateList(lista)
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
            }
        }

        //nav search toppom

        binding.SSUGESTOES.setOnClickListener {
            toast("Em desenvolvimento...")
        }
        binding.SMELHORES.setOnClickListener {
            toast("Em desenvolvimento...")
        }
        binding.SRECENTES.setOnClickListener {
            toast("Em desenvolvimento...")
        }
        binding.SPERSONALIZADAS.setOnClickListener {
            toast("Em desenvolvimento...")
        }




        //nav search bottom

        binding.IBMeat.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.CARNE,true)
        }
        binding.IBFish.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.PEIXE,true)
        }
        binding.IBSoup.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.SOPA,true)
        }
        binding.IBVegi.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.VEGETARIANO,true)
        }
        binding.IBFruit.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.FRUTA,true)
        }
        binding.IBDrink.setOnClickListener {
            viewModel.getRecipesByTitleAndTags(RecipeListingFragmentFilters.BEBIDAS,true)
        }
    }


}