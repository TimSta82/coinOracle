package de.timbo.coinOracle.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.MyAsset

class Converters {

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
    fun myAssetsListToString(myAssets: List<MyAsset>): String {
        return Gson().toJson(myAssets)
    }

    @TypeConverter
    fun stringToMyAssetsList(myAssetsListString: String): List<MyAsset> {
        val assetType = object : TypeToken<List<MyAsset>>() {}.type
        return Gson().fromJson(myAssetsListString, assetType)
    }

    @TypeConverter
    fun assetsToString(assets: List<Asset>): String {
        return Gson().toJson(assets)
    }

    @TypeConverter
    fun stringToAssetsList(assetsListString: String): List<Asset> {
        val assetType = object : TypeToken<List<Asset>>() {}.type
        return Gson().fromJson(assetsListString, assetType)
    }

    @TypeConverter
    fun correlationsListToString(correlations: List<CorrelationEntity>): String {
        return Gson().toJson(correlations)
    }

    @TypeConverter
    fun stringToCorrelationsList(correlationsString: String): List<CorrelationEntity> {
        val correlationType = object : TypeToken<List<CorrelationEntity>>() {}.type
        return Gson().fromJson(correlationsString, correlationType)
    }

    @TypeConverter
    fun correlationToString(correlations: CorrelationEntity): String {
        return Gson().toJson(correlations)
    }

    @TypeConverter
    fun stringToCorrelation(correlationsString: String): CorrelationEntity {
        val correlationType = object : TypeToken<CorrelationEntity>() {}.type
        return Gson().fromJson(correlationsString, correlationType)
    }

    @TypeConverter
    fun stringListToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun intListToJson(value: List<Int>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToIntList(value: String): List<Int> = Gson().fromJson(value, Array<Int>::class.java).toList()

}
