package obyte

/**
 * <pre>
 *     ["autonomous agent", {
 *         "base_aa": "YSOBOFK4AVXHYV2GC7MVZMOYCNKP52TX",
 *         "params": {
 *              "portfolio": [{
 *                  "asset": "fSwaCprr3OSNHXTLDtOK3lflKEKvQi7ypWQSh1FfK1E=",
 *                  "percentage": 0.95
 *              }, {
 *                  "asset": "3aw7r8dm0C/TG3w2CQGyCc8ukbMpeHJ/SXgbej/WXz8=",
 *                  "percentage": 0.05
 *              }]
 *          }
 *      }]
</pre>
 */
data class FundDefinition(val baseAa: String, val portfolio: List<PortfolioItem>) {}

data class PortfolioItem(val asset: String, val percentage: Double)

fun AddressDefinition.asFundDefinition(): FundDefinition {
    val baseAa = params["base_aa"] as String
    val portfolio = params["portfolio"] as Iterable<Map<String, Any>>
    return FundDefinition(
        baseAa = baseAa,
        portfolio = portfolio.map {
            PortfolioItem(
                asset = it["asset"] as String,
                percentage = it["percentage"] as Double
            )
        }
    )
}