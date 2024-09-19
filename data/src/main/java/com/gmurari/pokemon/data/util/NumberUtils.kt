package com.gmurari.pokemon.data.util

import com.gmurari.pokemon.domain.repository.Percentage

infix fun Number.percentOf(value: Number): Percentage {
    return if (this.toDouble() == 0.0) 0.0
    else (value.toDouble() / this.toDouble())
}