package ledger.obyte.mock

import kotlinx.coroutines.delay
import ledger.obyte.AddressDefinition
import ledger.obyte.AddressDefinitionService
import ledger.obyte.AssetMetadata
import ledger.obyte.AssetMetadataService
import ledger.obyte.AutonomousAgentService
import ledger.obyte.Balance
import ledger.obyte.BalanceService
import ledger.obyte.BaseAgentService
import ledger.obyte.ObyteApi
import ledger.obyte.SubAgent

object MockObyteApi : ObyteApi,
    AddressDefinitionService by MockAddressDefinitionService,
    AssetMetadataService by MockAssetMetadataService,
    AutonomousAgentService by MockAutonomousAgentService,
    BalanceService by MockBalanceService,
    BaseAgentService by MockBaseAgentService

object MockAssetMetadataService : AssetMetadataService {
    override suspend fun getAssetMetadata(assetHash: String): AssetMetadata {
        delay(500)
        return when (assetHash) {
            "q7QPuV2Gd31wcXUeg/IRxfD0Q1kWimei7g+IFKJaIXc=" -> AssetMetadata(
                ticker = "PYPL",
                decimals = 6
            )

            "cr0klQISM8PqhjFRXnKK20O9DRq5Yrcx3eEE1xBoCjw=" -> AssetMetadata(
                ticker = "FISV",
                decimals = 4
            )

            "GFmWjNQKoJcRPdc5ms22/p14izrM5QUDWKPFoPRccV0=" -> AssetMetadata(
                ticker = "FUND-FINX",
                decimals = 4
            )

            else -> throw RuntimeException("No such asset")
        }
    }
}

object MockBaseAgentService : BaseAgentService {
    override suspend fun getSubAgents(baseAgent: String): List<SubAgent> {
        delay(1000L)
        return listOf(
            SubAgent(
                "V4KPOLQM2GB2EY4LTBLJHKC2MIVZNB5B",
                MockAddressDefinitionService.getDefinitionForAddress("V4KPOLQM2GB2EY4LTBLJHKC2MIVZNB5B")
            ),
            SubAgent(
                "FUND0000000000000000000000000002",
                MockAddressDefinitionService.getDefinitionForAddress("FUND0000000000000000000000000002")
            )
        )
    }
}

object MockAddressDefinitionService : AddressDefinitionService {
    override suspend fun getDefinitionForAddress(address: String): AddressDefinition =
        when (address) {
            "V4KPOLQM2GB2EY4LTBLJHKC2MIVZNB5B" -> AddressDefinition(
                type = "autonomous agent",
                params = js("""{
                        base_aa: "FTKLXQND4LS65OTQK7RXOABB7DENBSEI",
                        params: {
                            portfolio: [{
                                asset: "q7QPuV2Gd31wcXUeg/IRxfD0Q1kWimei7g+IFKJaIXc=",
                                percentage: 0.95
                            }, {
                                asset: "cr0klQISM8PqhjFRXnKK20O9DRq5Yrcx3eEE1xBoCjw=",
                                percentage: 0.05
                            }]
                        }
                    }"""
                ).unsafeCast<Map<String, Any>>()
            )
            "FUND0000000000000000000000000002" -> AddressDefinition(
                type = "autonomous agent",
                params = js("""{
                        base_aa: "FTKLXQND4LS65OTQK7RXOABB7DENBSEI",
                        params: {
                            portfolio: [{
                                asset: "q7QPuV2Gd31wcXUeg/IRxfD0Q1kWimei7g+IFKJaIXc=",
                                percentage: 0.30
                            }, {
                                asset: "cr0klQISM8PqhjFRXnKK20O9DRq5Yrcx3eEE1xBoCjw=",
                                percentage: 0.70
                            }]
                        }
                    }"""
                ).unsafeCast<Map<String, Any>>()
            )

            else -> throw RuntimeException("No such address")
        }
}

object MockBalanceService : BalanceService {
    override suspend fun getBalances(addresses: List<String>): Map<String, Map<String, Balance>> {
        return addresses.associateWith {
            mapOf(
                "q7QPuV2Gd31wcXUeg/IRxfD0Q1kWimei7g+IFKJaIXc=" to Balance(stable = 12345649, pending = 0),
                "cr0klQISM8PqhjFRXnKK20O9DRq5Yrcx3eEE1xBoCjw=" to Balance(stable = 875698, pending = 0),
            )
        }
    }

}

object MockAutonomousAgentService : AutonomousAgentService {
    override suspend fun getState(address: String): Map<String, String> {
        return mapOf(
            "asset" to "GFmWjNQKoJcRPdc5ms22/p14izrM5QUDWKPFoPRccV0=",
            "shares" to "873405271"
        )
    }
}