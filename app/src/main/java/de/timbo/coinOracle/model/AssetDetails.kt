package de.timbo.coinOracle.model

import android.os.Parcelable
import de.timbo.coinOracle.api.model.AssetHistoryDto
import kotlinx.android.parcel.Parcelize

@Parcelize
class AssetDetails(
    val asset: Asset,
    val assetHistory: AssetHistoryDto
) : Parcelable
