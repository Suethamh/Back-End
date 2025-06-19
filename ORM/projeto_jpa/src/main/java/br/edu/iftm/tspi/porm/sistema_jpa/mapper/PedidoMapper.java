package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.PedidoDto;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    Pedido toEntity(PedidoDto pedidoDto);

    PedidoDto toDto(Pedido pedido);
    
    List<Pedido> toEntityList(List<PedidoDto> clienteDto);

    List<PedidoDto> toDtoList(List<Pedido> cliente);

}
