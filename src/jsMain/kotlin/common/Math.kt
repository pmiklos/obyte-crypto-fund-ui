package common

// TODO use external Javascript function
fun Int.powerOf(exponent: Int): Long =
    if (exponent == 0) 1
    else 1.rangeTo(exponent).fold(1L) { acc, _ -> this * acc }
