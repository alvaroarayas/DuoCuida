package com.duocuida.solicitudes.config;

import com.duocuida.solicitudes.model.EstadoSolicitud;
import com.duocuida.solicitudes.model.TipoSolicitud;
import com.duocuida.solicitudes.repository.EstadoSolicitudRepository;
import com.duocuida.solicitudes.repository.TipoSolicitudRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer
{

    @Bean
    CommandLineRunner initData(
            TipoSolicitudRepository tipoRepository,
            EstadoSolicitudRepository estadoRepository)
    {

        return args -> {
            if (tipoRepository.count() == 0) {
                tipoRepository.save(new TipoSolicitud(null, "APOYO_ACADEMICO", true));
                tipoRepository.save(new TipoSolicitud(null, "APOYO_SOCIAL", true));
                tipoRepository.save(new TipoSolicitud(null, "CUIDADO_FAMILIAR", true));
                tipoRepository.save(new TipoSolicitud(null, "SALUD_MENTAL", true));
                tipoRepository.save(new TipoSolicitud(null, "OTRO", true));
            }

            if (estadoRepository.count() == 0) {
                estadoRepository.save(new EstadoSolicitud(null, "REGISTRADA", true));
                estadoRepository.save(new EstadoSolicitud(null, "EN_REVISION", true));
                estadoRepository.save(new EstadoSolicitud(null, "EVALUADA", true));
                estadoRepository.save(new EstadoSolicitud(null, "EN_PLAN", true));
                estadoRepository.save(new EstadoSolicitud(null, "CERRADA", true));
                estadoRepository.save(new EstadoSolicitud(null, "RECHAZADA", true));
            }
        };
    }
}