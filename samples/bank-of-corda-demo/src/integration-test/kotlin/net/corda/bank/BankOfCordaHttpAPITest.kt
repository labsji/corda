package net.corda.bank

import com.google.common.util.concurrent.Futures
import net.corda.bank.api.BankOfCordaClientApi
import net.corda.bank.api.BankOfCordaWebApi.IssueRequestParams
import net.corda.core.getOrThrow
import net.corda.core.node.services.ServiceInfo
import net.corda.node.driver.driver
import net.corda.node.services.transactions.SimpleNotaryService
import org.junit.Test

class BankOfCordaHttpAPITest {
    @Test fun `test issuer flow via Http`() {
        driver(dsl = {
            val (nodeBankOfCorda) = Futures.allAsList(
                startNode("BankOfCorda", setOf(ServiceInfo(SimpleNotaryService.type))),
                startNode("BigCorporation")
            ).getOrThrow()
            val nodeBankOfCordaApiAddr = nodeBankOfCorda.configuration.webAddress
            assert(BankOfCordaClientApi(nodeBankOfCordaApiAddr).requestWebIssue(IssueRequestParams(1000, "USD", "BigCorporation", "1", "BankOfCorda")))
        }, isDebug = true)
    }
}