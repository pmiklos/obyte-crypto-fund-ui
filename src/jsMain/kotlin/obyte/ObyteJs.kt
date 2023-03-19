@file:JsModule("obyte")
@file:JsNonModule
package obyte

import kotlin.js.Promise

external class Client(nodeAddress: String, options: Map<String, Any>) {
    val api: Api
}

external interface Api {
    fun getWitnesses(): Promise<Array<String>>
    fun getAasByBaseAas(params: GetAasByBaseAasRequest): Promise<Array<GetAasByBaseAasResponse>>
    fun getOfficialTokenRegistryAddress(): String
    fun getSymbolByAsset(registry: String, asset: String): Promise<String>
    fun getDecimalsBySymbolOrAsset(registry: String, assetOrSymbol: String): Promise<Int>
}

external interface GetAasByBaseAasRequest {
    var base_aa: String
}

external interface GetAasByBaseAasResponse {
    val address: String
    val definition: Array<Any>
}
