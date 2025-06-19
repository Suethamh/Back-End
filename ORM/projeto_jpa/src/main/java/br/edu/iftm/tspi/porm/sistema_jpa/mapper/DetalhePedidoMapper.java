package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.DetalhePedidoDto;

@Mapper(componentModel = "spring")
public interface DetalhePedidoMapper {
    
    DetalhePedido toEntity(DetalhePedidoDto detalhePedidoDto);

    DetalhePedidoDto toDto(DetalhePedido detalhePedido);

    List<DetalhePedidoDto> toDtoList(List<DetalhePedido> detalhePedidos);

    List<DetalhePedido> toEntityList(List<DetalhePedidoDto> detalhePedidoDto);
}
