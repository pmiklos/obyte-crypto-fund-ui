package common

fun Double.movePointRight(places: Int): Double =
    if (places < 1) this
    else 0.rangeTo(places).fold(this) { result, _ -> result * 10.0 }


fun Double.movePointLeft(places: Int): Double =
    if (places < 1) this
    else 0.rangeTo(places).fold(this) { result, _ -> result / 10.0 }