package ledger.obyte.obytejs

import kotlinx.coroutines.await
import ledger.obyte.AssetMetadata

class AssetRegistries(client: Client) {

    private val trustedRegistries = mapOf(
        "AM6GTUKENBYA54FYDAKX2VLENFZIMXWG" to AssetManagerRegistry,
        "O6H6ZIFI57X3PLTYHOCVYPP5A553CYFQ" to TokensOooRegistry("O6H6ZIFI57X3PLTYHOCVYPP5A553CYFQ", client)
    )

    operator fun get(address: String): AssetRegistry = trustedRegistries[address] ?: UntrustedRegistry
}

interface AssetRegistry {
    suspend fun getAssetMetadata(unit: Unit): AssetMetadata
}

private object UntrustedRegistry : AssetRegistry {
    override suspend fun getAssetMetadata(unit: Unit) = AssetMetadata(
        ticker = unit.assetMetadataPayload.asset,
        decimals = 0,
        description = ""
    )
}

private object AssetManagerRegistry : AssetRegistry {
    override suspend fun getAssetMetadata(unit: Unit): AssetMetadata {
        val payload = unit.assetMetadataPayload

        return AssetMetadata(
            ticker = payload.ticker ?: payload.asset,
            decimals = payload.decimals,
            description = payload.description ?: ""
        )
    }
}

private class TokensOooRegistry(
    private val registryAddress: String,
    private val client: Client
) : AssetRegistry {

    override suspend fun getAssetMetadata(unit: Unit): AssetMetadata {
        val payload = unit.assetMetadataPayload
        val description = client.api.getStateVar("current_desc_${payload.asset}")?.let { id ->
            client.api.getStateVar("desc_$id")
        }

        return AssetMetadata(
            ticker = payload.name,
            decimals = payload.decimals,
            description = description ?: ""
        )
    }

    private suspend fun Api.getStateVar(stateVar: String): String? {
        val currentDescriptionObject = getAaStateVars(GetAaStateVarsRequest().apply {
            address = registryAddress
            var_prefix = stateVar
        }).await()

        val state = jsObjectToMap(currentDescriptionObject)
        return state[stateVar] as String?
    }
}

private val Unit.assetMetadataPayload: AssetMetadataPayload
    get() {
        val metadata =
            messages.firstOrNull { it.app == "data" } ?: throw RuntimeException("Invalid asset metadata")
        return metadata.payload.unsafeCast<AssetMetadataPayload>()
    }