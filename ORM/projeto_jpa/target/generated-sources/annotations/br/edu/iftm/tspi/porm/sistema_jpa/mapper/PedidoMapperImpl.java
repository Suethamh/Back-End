package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.PedidoDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T16:01:39-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PedidoMapperImpl implements PedidoMapper {

    @Override
    public Pedido toEntity(PedidoDto pedidoDto) {
        if ( pedidoDto == null ) {
            return null;
        }

        Pedido.PedidoBuilder pedido = Pedido.builder();

        pedido.cliente( pedidoDto.getCliente() );
        pedido.dataPedido( pedidoDto.getDataPedido() );
        List<DetalhePedido> list = pedidoDto.getDetalhesPedido();
        if ( list != null ) {
            pedido.detalhesPedido( new ArrayList<DetalhePedido>( list ) );
        }
        pedido.id( pedidoDto.getId() );

        return pedido.build();
    }

    @Override
    public PedidoDto toDto(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }

        PedidoDto.PedidoDtoBuilder pedidoDto = PedidoDto.builder();

        pedidoDto.cliente( pedido.getCliente() );
        pedidoDto.dataPedido( pedido.getDataPedido() );
        List<DetalhePedido> list = pedido.getDetalhesPedido();
        if ( list != null ) {
            pedidoDto.detalhesPedido( new ArrayList<DetalhePedido>( list ) );
        }
        pedidoDto.id( pedido.getId() );

        return pedidoDto.build();
    }

    @Override
    public List<Pedido> toEntityList(List<PedidoDto> clienteDto) {
        if ( clienteDto == null ) {
            return null;
        }

        List<Pedido> list = new ArrayList<Pedido>( clienteDto.size() );
        for ( PedidoDto pedidoDto : clienteDto ) {
            list.add( toEntity( pedidoDto ) );
        }

        return list;
    }

    @Override
    public List<PedidoDto> toDtoList(List<Pedido> cliente) {
        if ( cliente == null ) {
            return null;
        }

        List<PedidoDto> list = new ArrayList<PedidoDto>( cliente.size() );
        for ( Pedido pedido : cliente ) {
            list.add( toDto( pedido ) );
        }

        return list;
    }
}
