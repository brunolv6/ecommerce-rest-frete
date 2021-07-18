package br.com.zup.edu


import br.ccom.zup.edu.CalculoFreteRequest
import br.ccom.zup.edu.ErrorDetails
import br.ccom.zup.edu.FretesServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.HttpStatusException
import javax.inject.Inject

@Controller
class ConsultarFreteController(@Inject val gRpcClient: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get("/api/fretes")
    fun pegar(@QueryValue cep: String): FreteResponse {

        val request = CalculoFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        try {
            val response = gRpcClient.calculaFrete(request)

            return FreteResponse(response.cep, response.valor)
        } catch (e: StatusRuntimeException) {
            val status = e.status
            val statusCode = status.code
            val description = status.description

            if(statusCode == Status.Code.PERMISSION_DENIED) {
                val statusProto = StatusProto.fromThrowable(e) // status do Proto
                if(statusProto == null) {
                    throw  HttpStatusException(HttpStatus.FORBIDDEN, description) // relaciona o status  de gRPC com o de REST
                }

                val anyDetails = statusProto.detailsList.get(0) // com.google;protobuf.Any la da definicao do proto
                val details = anyDetails.unpack(ErrorDetails::class.java)

                throw HttpStatusException(HttpStatus.FORBIDDEN, "${details.code}: ${details.message}")
            }

            // caso contrário
            throw throw HttpStatusException(HttpStatus.FORBIDDEN, e.message)
        }
    }
}

data class FreteResponse(
    val cep: String,
    val valor: Double
){

}