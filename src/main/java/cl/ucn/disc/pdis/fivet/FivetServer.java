/*
 * Copyright (c) 2022 Marcelo Lam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cl.ucn.disc.pdis.fivet;


import cl.ucn.disc.pdis.fivet.grpc.*;
import cl.ucn.disc.pdis.fivet.services.FivetController;
import cl.ucn.disc.pdis.fivet.services.FivetControllerImpl;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import io.grpc.Server;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class FivetServer {

    @SneakyThrows({InterruptedException.class, IOException.class})
    public static void main(String[] args) {
        FivetServiceImpl fivetService = new FivetServiceImpl("jdbc:h2:mem:fivet");
        Server server = ServerBuilder.forPort(5000)
                .addService(fivetService)
                .build()
                .start();

        server.awaitTermination();
    }

    private static class FivetServiceImpl extends FivetServiceGrpc.FivetServiceImplBase {
        private final FivetController fivetController;
        public FivetServiceImpl(String databaseUrl){
            this.fivetController = new FivetControllerImpl(databaseUrl);
        }

        @Override
        public void autenticar(Credencial request, StreamObserver<cl.ucn.disc.pdis.fivet.grpc.Persona> responseObserver) {
            Optional<cl.ucn.disc.pdis.fivet.model.Persona>  persona = this.fivetController.retrieveByLogin(request.getLogin());
            if(persona.isPresent()) {
                responseObserver.onNext(Persona.newBuilder()
                        .setRut(persona.get().getRut())
                        .setNombre(persona.get().getNombre())
                        .setEmail(persona.get().getEmail())
                        .setDireccion(persona.get().getDireccion())
                        .build()
                );
                responseObserver.onCompleted();
            }
            else {
                responseObserver.onError(new RuntimeException("Persona not found"));
            }
        }
    }
}
