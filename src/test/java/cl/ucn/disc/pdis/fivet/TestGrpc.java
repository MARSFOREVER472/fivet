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


import cl.ucn.disc.pdis.fivet.grpc.AuthenticateReq;
import cl.ucn.disc.pdis.fivet.grpc.FivetServiceGrpc;
import cl.ucn.disc.pdis.fivet.grpc.PersonaReply;
import com.asarkar.grpc.test.GrpcCleanupExtension;
import com.asarkar.grpc.test.Resources;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.time.Duration;

/**
 * Test the gRPC.
 */
@Slf4j
@ExtendWith(GrpcCleanupExtension.class)
public final class TestGrpc {

    /**
     * Test the RPC.
     *
     * @param resources to use to autoclose the server.
     */
    @Test
    public void testGrpc(Resources resources) throws IOException {

        log.debug("Starting TestGrpc ..");

        // Unique server name
        String serverName = InProcessServerBuilder.generateName();
        log.debug("Testing serverName <{}> ..", serverName);

        //Initialize the server
        Server server = InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new FivetServiceImpl("jdbc:h2:mem:fivet")).build().start();
        resources.register(server, Duration.ofSeconds(10));

        // Initialize the channel
        ManagedChannel channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        resources.register(channel, Duration.ofSeconds(10));

        // The Stub testing
        FivetServiceGrpc.FivetServiceBlockingStub stub = FivetServiceGrpc.newBlockingStub(channel);

        // Authenticate
        log.debug("Testing Authenticate ..");
        {
            // Empty
            Assertions.assertThrows(StatusRuntimeException.class, () -> {
                PersonaReply personaReply = stub.authenticate(AuthenticateReq.newBuilder()
                        .build());
            });

            // No password
            Assertions.assertThrows(StatusRuntimeException.class, () -> {
                PersonaReply personaReply = stub.authenticate(AuthenticateReq.newBuilder()
                        .setLogin("admin@ucn.cl")
                        .build());
            });

            // Wrong password
            Assertions.assertThrows(StatusRuntimeException.class, () -> {
                PersonaReply personaReply = stub.authenticate(AuthenticateReq.newBuilder()
                        .setLogin("admin@ucn.cl")
                        .setPassword("wrong password").build());
            });

            // Wrong login
            Assertions.assertThrows(StatusRuntimeException.class, () -> {
                PersonaReply personaReply = stub.authenticate(AuthenticateReq.newBuilder()
                        .setLogin("unknow@ucn.cl")
                        .setPassword("admin123")
                        .build());
            });

            PersonaReply personaReply = stub.authenticate(AuthenticateReq.newBuilder()
                    .setLogin("admin@ucn.cl")
                    .setPassword("admin123").build());

            log.debug("PersonaReply: {}", personaReply);
            Assertions.assertEquals("815184009", personaReply.getPersona().getRut(), "Wrong RUT");

        }
    }
}