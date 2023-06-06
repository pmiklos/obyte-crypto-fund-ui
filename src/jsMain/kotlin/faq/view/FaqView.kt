package faq.view

import androidx.compose.runtime.Composable
import bootstrap.Accordion
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun Faq() {
    H3(attrs = { classes("mb-3") }) {
        Text("Frequently Asked Questions")
    }
    Accordion(id = "faqAccordion") {
        item(title = "What is Crypto Funds?", expand = true) {
            Text(
                """Crypto Funds is a DeFi protocol that allows users to create and manage a basket of crypto assets. 
                |Each fund issues shares that represent the underlying assets. 
                |The fund shares can be traded on decentralized exchanges (DEXes) and stored in crypto wallets.""".trimMargin()
            )
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
    }
}

