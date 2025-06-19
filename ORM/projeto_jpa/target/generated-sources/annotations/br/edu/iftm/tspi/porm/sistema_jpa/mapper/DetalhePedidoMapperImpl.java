package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.DetalhePedidoDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T16:05:05-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class DetalhePedidoMapperImpl implements DetalhePedidoMapper {

    @Override
    public DetalhePedido toEntity(DetalhePedidoDto detalhePedidoDto) {
        if ( detalhePedidoDto == null ) {
            return null;
        }

        DetalhePedido.DetalhePedidoBuilder detalhePedido = DetalhePedido.builder();

        detalhePedido.desconto( detalhePedidoDto.getDesconto() );
        detalhePedido.id( detalhePedidoDto.getId() );
        detalhePedido.pedido( detalhePedidoDto.getPedido() );
        detalhePedido.precoVenda( detalhePedidoDto.getPrecoVenda() );
        detalhePedido.produto( detalhePedidoDto.getProduto() );
        detalhePedido.quantidade( detalhePedidoDto.getQuantidade() );

        return detalhePedido.build();
    }

    @Override
    public DetalhePedidoDto toDto(DetalhePedido detalhePedido) {
        if ( detalhePedido == null ) {
            return null;
        }

        DetalhePedidoDto.DetalhePedidoDtoBuilder detalhePedidoDto = DetalhePedidoDto.builder();

        detalhePedidoDto.desconto( detalhePedido.getDesconto() );
        detalhePedidoDto.id( detalhePedido.getId() );
        detalhePedidoDto.pedido( detalhePedido.getPedido() );
        detalhePedidoDto.precoVenda( detalhePedido.getPrecoVenda() );
        detalhePedidoDto.produto( detalhePedido.getProduto() );
        detalhePedidoDto.quantidade( detalhePedido.getQuantidade() );

        return detalhePedidoDto.build();
    }

    @Override
    public List<DetalhePedidoDto> toDtoList(List<DetalhePedido> detalhePedidos) {
        if ( detalhePedidos == null ) {
            return null;
        }

        List<DetalhePedidoDto> list = new ArrayList<DetalhePedidoDto>( detalhePedidos.size() );
        for ( DetalhePedido detalhePedido : detalhePedidos ) {
            list.add( toDto( detalhePedido ) );
        }

        return list;
    }

    @Override
    public List<DetalhePedido> toEntityList(List<DetalhePedidoDto> detalhePedidoDto) {
        if ( detalhePedidoDto == null ) {
            return null;
        }

        List<DetalhePedido> list = new ArrayList<DetalhePedido>( detalhePedidoDto.size() );
        for ( DetalhePedidoDto detalhePedidoDto1 : detalhePedidoDto ) {
            list.add( toEntity( detalhePedidoDto1 ) );
        }

        return list;
    }
}
