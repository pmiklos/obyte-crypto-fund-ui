@file:JsModule("obyte")
@file:JsNonModule
package ledger.obyte.obytejs

import kotlin.js.Promise

external class Client(nodeAddress: String, options: Map<String, Any>) {
    val options: Map<String, Any>
    val client: WSClient
    val api: Api
}

external interface WSClient {
    val address: String
    val open: Boolean
}

external interface Api {
    fun getWitnesses(): Promise<Array<String>>
    fun getAasByBaseAas(params: GetAasByBaseAasRequest): Promise<Array<GetAasByBaseAasResponse>>
    fun getOfficialTokenRegistryAddress(): String
    fun getSymbolByAsset(registry: String, asset: String): Promise<String>
    fun getDecimalsBySymbolOrAsset(registry: String, assetOrSymbol: String): Promise<Int>
    fun getDefinition(address: String): Promise<Array<Any>>
}

external interface GetAasByBaseAasRequest {
    var base_aa: String
}

external interface GetAasByBaseAasResponse {
    val address: String
    val definition: Array<Any>
}
