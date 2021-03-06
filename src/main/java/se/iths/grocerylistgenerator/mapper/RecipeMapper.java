package se.iths.grocerylistgenerator.mapper;

import org.springframework.stereotype.Service;
import se.iths.grocerylistgenerator.dto.AddRecipeDto;
import se.iths.grocerylistgenerator.dto.RecipeDto;
import se.iths.grocerylistgenerator.entity.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeMapper {

    private final IngredientMapper ingredientMapper;

    public RecipeMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public Recipe mapp(RecipeDto recipeDto) {
        return new Recipe(recipeDto.getName(), ingredientMapper.mappToIngredientSet(recipeDto.getIngredients()));
    }

    public RecipeDto mapp(Recipe recipe) {
        return new RecipeDto(recipe.getId(), recipe.getName(), ingredientMapper.mapp(recipe.getIngredients()));
    }

    public Recipe mapp(AddRecipeDto addRecipeDto) {
        return new Recipe(addRecipeDto.getName());
    }

    public List<RecipeDto> mapp(List<Recipe> recipes) {
        return recipes
                .stream()
                .map(this::mapp)
                .toList();
    }

    public Set<RecipeDto> mapp(Set<Recipe> ingredients) {
        return ingredients
                .stream()
                .map(this::mapp)
                .collect(Collectors.toSet());
    }

    public Optional<RecipeDto> mapp(Optional<Recipe> optionalRecipe) {
        if (optionalRecipe.isEmpty())
            return Optional.empty();
        return Optional.of(mapp(optionalRecipe.get()));
    }
}
