package com.gauvain.seigneur.presentation.model

import androidx.annotation.DrawableRes
import com.gauvain.seigneur.presentation.utils.StringPresenter

data class ErrorData(
    val type: ErrorDataType,
    val title: StringPresenter? = null,
    val description: StringPresenter? = null,
    val buttonText: StringPresenter? = null,
    @DrawableRes
    val iconRes: Int? = null
)

enum class ErrorDataType {
    INFORMATIVE,
    RECOVERABLE,
    NOT_RECOVERABLE
}


