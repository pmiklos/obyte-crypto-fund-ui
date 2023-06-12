package ledger.obyte

fun ObyteApi.cached(): ObyteApi = CachedObyteApi(this)

private class CachedObyteApi(private val delegate: ObyteApi) : ObyteApi,
    AddressDefinitionService by CachedAddressDefinitionService(delegate),
    AssetMetadataService by CachedAssetMetadataService(delegate),
    AutonomousAgentService by delegate,
    BalanceService by delegate,
    BaseAgentService by delegate,
    ConfigurationService by delegate,
    ValidationService by delegate

private class CachedAddressDefinitionService(private val addressDefinitionService: AddressDefinitionService) :
    AddressDefinitionService {

    // TODO use LRU cache
    private val cache = mutableMapOf<String, AddressDefinition>()

    override suspend fun getDefinitionForAddress(address: String): AddressDefinition =
        cache.getOrPut(address) { addressDefinitionService.getDefinitionForAddress(address) }
}

private class CachedAssetMetadataService(private val assetMetadataService: AssetMetadataService) :
    AssetMetadataService {
    private val cache = mutableMapOf<String, AssetMetadata>()

    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata =
        cache.getOrPut(assetHash) { assetMetadataService.getAssetMetadata(assetHash) }
}
