package com.duocuida.beneficios.service;

import com.duocuida.beneficios.dto.BeneficioRequestDTO;
import com.duocuida.beneficios.dto.BeneficioResponseDTO;
import com.duocuida.beneficios.exception.ResourceNotFoundException;
import com.duocuida.beneficios.model.Beneficio;
import com.duocuida.beneficios.repository.BeneficioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private BeneficioService service;

    private Beneficio crearBeneficioEjemplo(Long id) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setEstudianteId(1L);
        b.setTipo("BECA");
        b.setDescripcion("Beca de alimentacion");
        b.setEstado("ACTIVO");
        b.setFechaInicio(LocalDate.of(2025, 6, 1));
        return b;
    }

    private BeneficioRequestDTO crearRequestEjemplo() {
        BeneficioRequestDTO dto = new BeneficioRequestDTO();
        dto.setEstudianteId(1L);
        dto.setTipo("BECA");
        dto.setDescripcion("Beca de alimentacion");
        dto.setEstado("ACTIVO");
        dto.setFechaInicio(LocalDate.of(2025, 6, 1));
        return dto;
    }

    @Test
    void findAll_deberiaRetornarListaDeBeneficios() {
        when(beneficioRepository.findAll()).thenReturn(List.of(crearBeneficioEjemplo(1L)));

        List<BeneficioResponseDTO> respuesta = service.findAll();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getTipo()).isEqualTo("BECA");
    }

    @Test
    void save_deberiaGuardarBeneficioYRetornarRespuesta() {
        BeneficioRequestDTO request = crearRequestEjemplo();
        Beneficio guardado = crearBeneficioEjemplo(10L);

        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(guardado);

        BeneficioResponseDTO respuesta = service.save(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getEstado()).isEqualTo("ACTIVO");
        assertThat(respuesta.getTipo()).isEqualTo("BECA");
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Beneficio no encontrado con id: 99");
    }
}
