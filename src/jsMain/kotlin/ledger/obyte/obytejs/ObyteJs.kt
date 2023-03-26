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
    fun getAssetMetadata(asset: String): Promise<RegistryUnit>
    fun getJoint(unit: String): Promise<JointResponse>
    fun getOfficialTokenRegistryAddress(): String
    fun getSymbolByAsset(registry: String, asset: String): Promise<String>
    fun getDecimalsBySymbolOrAsset(registry: String, assetOrSymbol: String): Promise<Int>
    fun getDefinition(address: String): Promise<Array<Any>>
    fun getAaStateVars(params: GetAaStateVarsRequest): Promise<dynamic>
    fun getBalances(addresses: Array<String>): Promise<dynamic>
}

external interface GetAasByBaseAasRequest {
    var base_aa: String
}

external interface GetAasByBaseAasResponse {
    val address: String
    val definition: Array<Any>
}

external interface GetAaStateVarsRequest {
    var address: String
    var var_prefix: String?
}

external interface BalanceResponse {
    val stable: Double
    val pending: Double
}

external interface RegistryUnit {
    val metadata_unit: String
    val registry_address: String
    val suffix: String?
}

external interface JointResponse {
    val joint: Joint
}

external interface Joint {
    val unit: Unit
}

external interface Unit {
    val unit: String
    val version: String
    val alt: String
    val messages: Array<Message>
}

external interface Message {
    val app: String
    val payload: Any
}

external interface AssetMetadataPayload {
    val asset: String
    val decimals: Int
    val name: String
    val shortName: String?
    val issuer: String?
    val ticker: String?
    val description: String?
}