package kolmachikhin.firemessenger.presentation.viewmodel

import kolmachikhin.alexander.validation.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <DATA, INCORRECT_REASON> Flow<DATA>.validateWith(
    validator: Validator<DATA, INCORRECT_REASON>
) = map {
    validator.validate(it)
}

fun <DATA, INCORRECT_REASON> Flow<DATA?>.validateNullableWith(
    validator: Validator<DATA, INCORRECT_REASON>
) = map {
    it?.let {
        validator.validate(it)
    }
}