package br.com.zup.edu

import br.ccom.zup.edu.FretesServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton // poderia ser @Bean e @GrpcChannel cuidará para o micronaut injetar o tipo de canal aqui (definido no properties.yml)
    fun fretesClientStub(@GrpcChannel("fretes") channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub? {

//        val channel: ManagedChannel = ManagedChannelBuilder
//                                            .forAddress("localhost", 50051)
//                                            .usePlaintext()
//                                            .maxRetryAttempts(10)
//                                            .build()

        return FretesServiceGrpc.newBlockingStub(channel)
    }
}