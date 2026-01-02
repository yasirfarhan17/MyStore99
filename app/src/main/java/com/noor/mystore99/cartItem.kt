package com.noor.mystore99

data class cartItem(
    var name: String? = null,
    var price: String? = null,
    var quant: Int = 0,
    var total: Int = 0,
    var weight: String? = null
) {
    // Secondary constructors to match Java source, though default args usually handle this.
    // However, Java code might call specific constructors.
    // The primary constructor with defaults handles the no-arg constructor (for Firebase).

    constructor(name: String?, price: String?, total: Int) : this(
        name = name,
        price = price,
        quant = 0,
        total = total,
        weight = null
    )
}
