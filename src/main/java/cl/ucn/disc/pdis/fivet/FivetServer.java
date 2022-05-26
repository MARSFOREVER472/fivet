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
 *//*

package cl.ucn.disc.pdis.fivet;

import cl.ucn.disc.pdis.fivet.model.Persona;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.h2.tools.Server;

import java.io.IOException;

public class FivetServer {

    @SneakyThrows({InterruptedException.class, IOException.class})
    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(5000)
                .addService(new FivetServiceImpl())
                .build()
                .start();

        server.awaitTermination();
    }

    private static class FivetServiceImpl extends FivetServiceGrpc.FivetServiceImplBase {
        public void autenticar(Credencial c, StreamObserver<Persona> sop) {
            super.autenticar(c, sop);
        }
    }
} */
