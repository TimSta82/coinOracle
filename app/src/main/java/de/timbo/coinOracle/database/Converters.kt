package de.timbo.coinOracle.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.MyAsset

class Converters {

//    @TypeConverter
//    fun recipeToString(recipeEntity: RecipeEntity): String {
//        return Gson().toJson(recipeEntity)
//    }
//
//    @TypeConverter
//    fun stringToRecipe(recipeString: String): RecipeEntity {
//        val recipeType = object : TypeToken<RecipeEntity>() {}.type
//        return Gson().fromJson(recipeString, recipeType)
//    }
//
//    @TypeConverter
//    fun weekSuggestionToString(weekSuggestionEntity: WeekSuggestionEntity): String {
//        return Gson().toJson(weekSuggestionEntity)
//    }
//
//    @TypeConverter
//    fun stringToWeekSuggestion(weekString: String): WeekSuggestionEntity {
//        val weekType = object : TypeToken<WeekSuggestionEntity>() {}.type
//        return Gson().fromJson(weekString, weekType)
//    }

    @TypeConverter
    fun portfolioToString(portfolioEntity: PortfolioEntity): String {
        return Gson().toJson(portfolioEntity)
    }

    @TypeConverter
    fun stringToPortfolio(portfolioString: String): PortfolioEntity {
        val portfolioType = object : TypeToken<PortfolioEntity>() {}.type
        return Gson().fromJson(portfolioString, portfolioType)
    }

    @TypeConverter
    fun myAssetToString(myAsset: MyAsset): String {
        return Gson().toJson(myAsset)
    }

    @TypeConverter
    fun stringToMyAsset(myAssetString: String): MyAsset {
        val assetType = object : TypeToken<MyAsset>() {}.type
        return Gson().fromJson(myAssetString, assetType)
    }

    @TypeConverter
    fun stringListToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun intListToJson(value: List<Int>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToIntList(value: String): List<Int> = Gson().fromJson(value, Array<Int>::class.java).toList()

//    @TypeConverter
//    fun extendedIngredientsToString(extendedIngredientEntities: List<ExtendedIngredientEntity>): String {
//        return Gson().toJson(extendedIngredientEntities)
//    }
//
//    @TypeConverter
//    fun stringToExtendedIngredients(extendedIngredientString: String): List<ExtendedIngredientEntity> {
//        val type = object : TypeToken<List<ExtendedIngredientEntity>>() {}.type
//        return Gson().fromJson(extendedIngredientString, type)
//    }
}
