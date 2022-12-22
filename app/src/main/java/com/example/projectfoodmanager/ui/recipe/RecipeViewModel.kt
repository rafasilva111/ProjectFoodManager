package com.example.projectfoodmanager.ui.recipe


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectfoodmanager.data.model.Recipe
import com.example.projectfoodmanager.data.model.User
import com.example.projectfoodmanager.data.repository.RecipeRepository
import com.example.projectfoodmanager.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor (
    val repository: RecipeRepository

): ViewModel() {
    private val _recipes = MutableLiveData<UiState<List<Recipe>>>()
    val recipe: LiveData<UiState<List<Recipe>>>
            get() = _recipes

    private val _updateRecipe = MutableLiveData<UiState<Pair<Recipe,String>>>()
    val updateRecipe: LiveData<UiState<Pair<Recipe,String>>>
        get() = _updateRecipe

    fun getRecipes(){
        _recipes.value = UiState.Loading
        repository.getRecipes {
            _recipes.value = UiState.Loading
            repository.getRecipes { _recipes.value = it }
        }

    }

    fun getRecipesPaginated(page: Long){
        _recipes.value = UiState.Loading
        repository.getRecipesPaginated(page) {

            _recipes.value = UiState.Loading
            repository.getRecipesPaginated(page){ _recipes.value = it }
        }

    }

    private val _addRecipe = MutableLiveData<UiState<String>>()
    val addRecipe: LiveData<UiState<String>>
        get() = _addRecipe


    fun addRecipe(recipe: Recipe){
        _addRecipe.value = UiState.Loading
        repository.addRecipe(recipe) { _addRecipe.value = it}
    }

    fun removeLikeOnRecipe(recipe: Recipe) {
        _updateRecipe.value = UiState.Loading
        repository.removeLikeOnRecipe(recipe) { _updateRecipe.value = it}
    }

    fun addLikeOnRecipe(recipe: Recipe) {
        _updateRecipe.value = UiState.Loading
        repository.addLikeOnRecipe(recipe) { _updateRecipe.value = it}
    }


}