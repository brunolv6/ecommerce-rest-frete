package br.com.zup.edu

import br.ccom.zup.edu.CalculoFreteRequest
import br.ccom.zup.edu.FretesServiceGrpc
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import javax.inject.Inject

@Controller
class ConsultarFreteController(@Inject val gRpcClient: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get("/api/fretes")
    fun pegar(@QueryValue cep: String): FreteResponse {

        val request = CalculoFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        val response = gRpcClient.calculaFrete(request)

        return FreteResponse(response.cep, response.valor)
    }
}

data class FreteResponse(
    val cep: String,
    val valor: Double
){

}