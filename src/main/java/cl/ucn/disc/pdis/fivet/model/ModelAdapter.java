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

package cl.ucn.disc.pdis.fivet.model;

import cl.ucn.disc.pdis.fivet.grpc.ControlEntity;
import cl.ucn.disc.pdis.fivet.grpc.FichaMedicaEntity;
import cl.ucn.disc.pdis.fivet.grpc.PersonaEntity;
import cl.ucn.disc.pdis.fivet.grpc.SexoEntity;
import cl.ucn.disc.pdis.fivet.model.Control;
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The model Adapter.
 *
 * @author Marcelo Lam.
 */
@UtilityClass
public final class ModelAdapter {

    /**
     * Persona java Class to Persona gRPC.
     *
     * @param persona Persona java.
     * @return a personaEntity.
     */
    public static PersonaEntity build(final Persona persona) {
        return PersonaEntity.newBuilder()
                .setNombre(persona.getNombre())
                .setEmail(persona.getEmail())
                .setRut(persona.getRut())
                .setDireccion(persona.getDireccion())
                .build();
    }

    /**
     * Persona gRPC to Persona java.
     *
     * @param persona PersonaEntity.
     * @return a persona java.
     */
    public static Persona build(final PersonaEntity persona) {
        return Persona.builder()
                .nombre(persona.getNombre())
                .email(persona.getEmail())
                .rut(persona.getRut())
                .direccion(persona.getDireccion())
                .build();
    }

    /**
     * FichaMedica java to FichaMedica gRPC.
     *
     * @param fichaMedica FichaMedica java
     * @return a FichaMedicaEntity.
     */
    public static FichaMedicaEntity build(final FichaMedica fichaMedica) {
        return FichaMedicaEntity.newBuilder()
                .setColor(fichaMedica.getColor())
                .setDuenio(build(fichaMedica.getDuenio()))
                .setEspecie(fichaMedica.getEspecie())
                .setFechaNacimiento(String.valueOf(fichaMedica.getFechaNacimiento()))
                .setNombrePaciente(fichaMedica.getNombrePaciente())
                .setNumeroFicha(fichaMedica.getNumeroFicha())
                .setRaza(fichaMedica.getRaza())
                .setSexo(SexoEntity.valueOf(fichaMedica.getSexo().name()))
                .setTipo(fichaMedica.getTipo())
                .addAllControles(buildControlsEntity(fichaMedica.getControles()))
                .build();
    }

    /**
     * FichaMedica gRPC to FichaMedica java.
     *
     * @param fichaMedica FichaMedicaEntity.
     * @return a FichaMedica java.
     */
    public static FichaMedica build(final FichaMedicaEntity fichaMedica) {
        return FichaMedica.builder()
                .color(fichaMedica.getColor())
                .duenio(build(fichaMedica.getDuenio()))
                .especie(fichaMedica.getEspecie())
                .fechaNacimiento(ZonedDateTime.parse(fichaMedica.getFechaNacimiento()))
                .nombrePaciente(fichaMedica.getNombrePaciente())
                .numeroFicha(fichaMedica.getNumeroFicha())
                .raza(fichaMedica.getRaza())
                .sexo(FichaMedica.Sexo.valueOf(fichaMedica.getSexo().name()))
                .tipo(fichaMedica.getTipo())
                .controles(buildControles(fichaMedica.getControlesList()))
                .build();
    }

    /**
     * Control java to Control gRPC.
     *
     * @param control Control java
     * @return ControlEntity
     */
    public static ControlEntity build(final Control control) {
        return ControlEntity.newBuilder()
                .setAltura(Float.valueOf(String.valueOf(control.getAltura())))
                .setDiagnostico(control.getDiagnostico())
                .setFecha(String.valueOf(control.getFecha()))
                .setFichaMedica(build(control.getFichaMedica()))
                .setPeso(Float.valueOf(String.valueOf(control.getPeso())))
                .setTemperatura(Float.valueOf(String.valueOf(control.getTemperatura())))
                .setVeterinario(build(control.getVeterinario()))
                .build();
    }

    /**
     * Control gRPC to Control java.
     *
     * @param control ControlEntity
     * @return a Control java
     */
    public static Control build(final ControlEntity control) {
        return Control.builder()
                .altura(control.getAltura())
                .diagnostico(control.getDiagnostico())
                .fecha(build(control.getFecha()))
                .fichaMedica(build(control.getFichaMedica()))
                .peso(control.getPeso())
                .temperatura(control.getTemperatura())
                .veterinario(build(control.getVeterinario()))
                .build();
    }

    /**
     * String to ZonedDateTime.
     *
     * @param dateTime the date
     * @return a ZonedDateTime
     */
    public static ZonedDateTime build(final String dateTime) {
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     * Collection of ControlEntity to Collection of Control.
     *
     * @param controlsEntity to use
     * @return Collection of Control
     */
    public static Collection<Control> buildControles(final Collection<ControlEntity> controlsEntity) {
        List<Control> controles = new ArrayList<>();
        for (ControlEntity controlEntity : controlsEntity) {
            controles.add(build(controlEntity));
        }
        return controles;
    }

    /**
     * Collection of Control to Collection of ControlEntity.
     *
     * @param controles to use
     * @return Collection of ControlEntity
     */
    public static Collection<ControlEntity> buildControlsEntity(final Collection<Control> controles) {
        List<ControlEntity> controlsEntity = new ArrayList<>();
        for (Control control : controles) {
            controlsEntity.add(build(control));
        }
        return controlsEntity;
    }

}