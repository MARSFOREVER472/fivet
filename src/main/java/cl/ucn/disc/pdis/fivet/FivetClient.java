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

import cl.ucn.disc.pdis.fivet.grpc.Credencial;
import cl.ucn.disc.pdis.fivet.grpc.FivetServiceGrpc;
import cl.ucn.disc.pdis.fivet.grpc.Persona;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FivetClient {

    public static void main(String[] args) {

        log.debug("Starting the Client ..");

        // The Channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 8080)
                .usePlaintext()
                .build();

        // The Stub
        FivetServiceGrpc.FivetServiceBlockingStub stub = FivetServiceGrpc.newBlockingStub(channel);

        try {
            Persona persona = stub.autenticar(Credencial.newBuilder()
                    .setLogin("marcelo.lam@alumnos.ucn.cl")
                    .setPassword("mlam123")
                    .build());

            log.debug("Persona: {}", persona);
        } catch (StatusRuntimeException e) {
            log.warn("RPC Failed: {}", e.getStatus());
        }

        log.debug("Done.");
    }
}
