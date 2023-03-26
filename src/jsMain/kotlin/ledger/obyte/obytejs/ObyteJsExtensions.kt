package ledger.obyte.obytejs

fun GetAasByBaseAasRequest() = js("{}").unsafeCast<GetAasByBaseAasRequest>()
fun GetAaStateVarsRequest() = js("{}").unsafeCast<GetAaStateVarsRequest>()
fun jsObjectToMap(vars: Any?) = (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
    .invoke(vars)
    .associate { (key, value) -> key as String to value }