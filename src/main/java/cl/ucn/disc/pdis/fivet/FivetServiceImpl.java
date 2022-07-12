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
import cl.ucn.disc.pdis.fivet.model.Control;
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.ModelAdapter;
import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.services.FivetController;
import cl.ucn.disc.pdis.fivet.services.FivetControllerImpl;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
public class FivetServiceImpl extends FivetServiceGrpc.FivetServiceImplBase {

    /**
     * The Controller.
     */
    private final FivetController fivetController;

    /**
     * @param databaseUrl to use.
     */
    public FivetServiceImpl(String databaseUrl) {
        this.fivetController = new FivetControllerImpl(databaseUrl);
    }

    /**
     * authenticate
     * @param request to use, contains a login and password.
     * @param responseObserver to use.
     */
    public void autenticate(AuthenticateReq request, StreamObserver<PersonaReply> responseObserver) {
        // Retrieve from Controller
        Optional<Persona> persona = this.fivetController
                .retrieveByLogin(request
                        .getLogin());
        if (persona.isPresent()) {
            // Return the observer
            responseObserver.onNext(PersonaReply.newBuilder()
                    .setPersona(ModelAdapter.build(persona.get()))
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(PersonaReply.newBuilder()
                    .setPersona(PersonaEntity.newBuilder()
                            .setNombre("Marcelo")
                            .setRut("197105992")
                            .setEmail("marcelo.lam@alumnos.ucn.cl")
                            .setDireccion("angamos 0610")
                            .build())
                    .build());
            responseObserver.onCompleted();
            //responseObserver.onError(buildException(Code.PERMISSION_DENIED, "Wrong Credentials"));
        }
    }

    /**
     * add a control
     * @param request to use, contains a Control
     * @param responseObserver to use.
     */
    public void addControl(AddControlReq request, StreamObserver<FichaMedicaReply> responseObserver) {
        Optional<FichaMedica> fichaMedica = this.fivetController.getFichaMedica(request
                .getControl().getFichaMedica().getNumeroFicha());
        if (fichaMedica.isPresent()) {
            Control control = ModelAdapter.build(request.getControl());
            this.fivetController.addControl(control);
            responseObserver.onNext(FichaMedicaReply.newBuilder().setFichaMedica(ModelAdapter.build(fichaMedica.get()))
                    .build());
            responseObserver.onCompleted();
        }
        else {
            //responseObserver.onError(buildException(Code.PERMISSION_DENIED, "Wrong control"));
        }
    }

    /**
     * retrieve a FichaMedica
     * @param request to use, contains a NumeroFicha.
     * @param responseObserver to use.
     */
    public void retrieveFichaMedica(RetrieveFichaMedicaReq request,
                                    StreamObserver<FichaMedicaReply> responseObserver) {
        Optional<FichaMedica> fichaMedica = this.fivetController.getFichaMedica(request
                .getNumeroFicha());
        if (fichaMedica.isPresent()) {
            responseObserver.onNext(FichaMedicaReply.newBuilder().setFichaMedica(ModelAdapter.build(fichaMedica.get()))
                    .build());
            responseObserver.onCompleted();
        }
        else {
            //responseObserver.onError(buildException(Code.PERMISSION_DENIED, "Wrong number"));
        }
    }

    /**
     * Search a FichaMedica
     * @param request to use, contains a FichaMedica
     * @param responseObserver to use.
     */
    public void searchFichaMedica(SearchFichaMedicaReq request, StreamObserver<FichaMedicaReply> responseObserver) {
        Collection<FichaMedica> fichasMedicasEncontradas = new ArrayList<>();
        Collection<FichaMedica> fichasMedicasBD = this.fivetController.getAllFichaMedica();
        // Busqueda si el query contiene numeros
        if (NumberUtils.isCreatable(request.getQuery())) {
            // Busqueda por numero de ficha
            Optional<FichaMedica> fichaMedicaNumero = this.fivetController.getFichaMedica(Integer.valueOf(request.getQuery()));
            if (fichaMedicaNumero.isPresent()) {
                fichasMedicasEncontradas.add(fichaMedicaNumero.get());
            }
            // Busqueda por coincidencias en el rut
            Collection<FichaMedica> fichasMedicasRut = this.fivetController.searchFichaMedica
                    (request.getQuery(), fichasMedicasBD, 1);
            fichasMedicasEncontradas.addAll(fichasMedicasRut);
        }
        // Busqueda si el query tiene letras o simbolos
        else {
            // Busqueda por coincidencias en el rut
            Collection<FichaMedica> fichasMedicasRut = this.fivetController.searchFichaMedica
                    (request.getQuery(), fichasMedicasBD, 1);
            fichasMedicasEncontradas.addAll(fichasMedicasRut);

            // Busqueda por nombre del paciente
            Collection<FichaMedica> fichasMedicasNomPaciente = this.fivetController.searchFichaMedica
                    (request.getQuery(), fichasMedicasBD, 2);
            fichasMedicasEncontradas.addAll(fichasMedicasNomPaciente);

            // Busqueda por nombre del dueño
            Collection<FichaMedica> fichasMedicasNomDuenio = this.fivetController.searchFichaMedica
                    (request.getQuery(), fichasMedicasBD, 3);
            fichasMedicasEncontradas.addAll(fichasMedicasNomDuenio);
        }

        // TODO hacer que no se repitan las fichas obtenidas o algo asi

        if (fichasMedicasEncontradas.size() < 1) {
            for (FichaMedica fichaMedica : fichasMedicasEncontradas) {
                responseObserver.onNext(FichaMedicaReply.newBuilder().setFichaMedica(ModelAdapter.build(fichaMedica))
                        .build());
            }
            responseObserver.onCompleted();
        }
        else {
            //responseObserver.onError(buildException(Code.PERMISSION_DENIED, "Wrong number"));
        }
    }

    /**
     * Add a FichaMedica.
     * @param request to use, contains a Control.
     * @param responseObserver to use.
     */
    public void addFicha(AddFichaReq request, StreamObserver<FichaMedicaReply> responseObserver) {
        FichaMedica fichaMedica = ModelAdapter.build(request.getFichaMedica());
        this.fivetController.addFichaMedica(fichaMedica);
        responseObserver.onNext(FichaMedicaReply.newBuilder().setFichaMedica(request.getFichaMedica()).build());
        responseObserver.onCompleted();
    }

    public void addPersona(AddPersonaReq request, StreamObserver<PersonaReply> responseObserver) {
        Persona persona = ModelAdapter.build(request.getPersona());
        this.fivetController.addPersona(persona, "");
        responseObserver.onNext(PersonaReply.newBuilder().setPersona(request.getPersona()).build());
        responseObserver.onCompleted();

    }
}