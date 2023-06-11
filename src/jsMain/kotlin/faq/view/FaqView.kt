package faq.view

import androidx.compose.runtime.Composable
import bootstrap.Accordion
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Composable
fun Faq() {
    H3(attrs = { classes("mb-3") }) {
        Text("Frequently Asked Questions")
    }
    Accordion(id = "faqAccordion") {
        item(title = "What is Crypto Funds?", expand = true) {
            Text(
                """Crypto Funds is a DeFi protocol that allows users to create and manage a basket of crypto assets. 
                |Users can receive shares directly from the Fund by locking up the managed assets in the requests ratio.
                |Similarly, users can redeem the underlying assets by retuning the shares.""".trimMargin()
            )
        }
        item(title = "Who manages the Crypto Funds?") {
            Text(
                """Crypto Funds are managed by the community. There is no central entity that manages the funds.
                |Anyone can deploy a fund on the distributed ledger and anyone can send assets to it to receive shares
                |or return shares to redeem assets.""".trimMargin()
            )
        }
        item(title = "What are Fund Shares?") {
            Text(
                """Fund shares are tokens that represent the underlying assets held by the fund.
                |For example, if a fund holds 1 BTC and 10 ETH and the there are 100 shares issued,
                |then each share of the fund represents 0.01 BTC and 0.1 ETH. 
                |Shares are issued proportionate to the assets sent to the fund, 
                |and shares can be returned to the fund to redeem the underlying assets in the same proportion.
                |Using the previous example, if a user sends 1 share to the fund, then they will receive 0.01 BTC and 0.1 ETH.
                |The fund shares can be traded on decentralized exchanges and stored in crypto wallets.""".trimMargin()
            )
        }
        item(title = "Where can I buy Fund Shares?") {
            Text(
                """Fund shares can be bought either directly from the fund in this app or on secondary exchanges. 
                |When buying shares directly from the fund, the payment is made in the underlying assets
                |in the required proportion defined by the fund. 
                |When purchasing shares on secondary markets, the payment is made in the quote currency. 
                |For example, if an exchange lists the Fund Share in USDC coins, one can buy shares in USDC.
                |Popular decentralized exchanges on Obyte:
                |""".trimMargin()
            )
            Ul {
                Li {
                    A(href = "https://odex.ooo") { Text("Odex") }
                    Text(
                        """ - a community-owned decentralized exchange where trades are settled directly 
                            |between the two exchanging parties without any intermediate""".trimMargin()
                    )
                }
                Li {
                    A(href = "https://oswap.io") { Text("Oswap") }
                    Text(
                        """ - a decentralized exchange that enables users to swap tokens 
                            |using liquidity pools instead of traditional order books.""".trimMargin()
                    )
                }
            }
        }
        item(title = "What blockchain does Crypto Funds use?") {
            Text(
                """Crypto Funds is built on the Obyte distributed ledger. 
                |Obyte is a DAG-based ledger that uses a directed acyclic graph (DAG) instead of a blockchain. 
                |The DAG architecture makes Obyte an ideal platform for DeFi applications: 
                |it is fast and scalable and since there are no miners it is censorship resistant, 
                |users append their transactions directly to the DAG making 
                |MEV (miner extractable value) attacks impossible. """.trimMargin()
            )
            A(href = "https://blog.obyte.org/dag-vs-blockchain-6d2d99f10bd9") {
                Text("Learn more about DAG vs Blockchain.")
            }
        }
        item(title = "What assets can I hold in a Crypto Fund?") {
            Text(
                """Crypto Funds supports all assets that are available on the Obyte platform. 
                |This includes those issued by users as well as the ones issued by autonomous agents
                |such as coins imported from other chains via """.trimMargin()
            )
            A(href = "https://counterstake.org") { Text("Counterstake") }
            Text(" such as ETH, WBTC or BNB or algorithmic stablecoins issued via ")
            A(href = "https://ostable.org") { Text("Ostable") }
            Text(" such as OUSD.")
        }
    }
}

