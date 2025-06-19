package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Cliente;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ClienteDto;
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
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toEntity(ClienteDto clientedto) {
        if ( clientedto == null ) {
            return null;
        }

        Cliente.ClienteBuilder cliente = Cliente.builder();

        cliente.cargo( clientedto.getCargo() );
        cliente.cep( clientedto.getCep() );
        cliente.cidade( clientedto.getCidade() );
        cliente.endereco( clientedto.getEndereco() );
        cliente.fax( clientedto.getFax() );
        cliente.id( clientedto.getId() );
        cliente.nome( clientedto.getNome() );
        cliente.pais( clientedto.getPais() );
        List<Pedido> list = clientedto.getPedidos();
        if ( list != null ) {
            cliente.pedidos( new ArrayList<Pedido>( list ) );
        }
        cliente.telefone( clientedto.getTelefone() );

        return cliente.build();
    }

    @Override
    public ClienteDto toDto(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDto.ClienteDtoBuilder clienteDto = ClienteDto.builder();

        clienteDto.cargo( cliente.getCargo() );
        clienteDto.cep( cliente.getCep() );
        clienteDto.cidade( cliente.getCidade() );
        clienteDto.endereco( cliente.getEndereco() );
        clienteDto.fax( cliente.getFax() );
        clienteDto.id( cliente.getId() );
        clienteDto.nome( cliente.getNome() );
        clienteDto.pais( cliente.getPais() );
        List<Pedido> list = cliente.getPedidos();
        if ( list != null ) {
            clienteDto.pedidos( new ArrayList<Pedido>( list ) );
        }
        clienteDto.telefone( cliente.getTelefone() );

        return clienteDto.build();
    }

    @Override
    public List<Cliente> toEntityList(List<ClienteDto> clienteDto) {
        if ( clienteDto == null ) {
            return null;
        }

        List<Cliente> list = new ArrayList<Cliente>( clienteDto.size() );
        for ( ClienteDto clienteDto1 : clienteDto ) {
            list.add( toEntity( clienteDto1 ) );
        }

        return list;
    }

    @Override
    public List<ClienteDto> toDtoList(List<Cliente> cliente) {
        if ( cliente == null ) {
            return null;
        }

        List<ClienteDto> list = new ArrayList<ClienteDto>( cliente.size() );
        for ( Cliente cliente1 : cliente ) {
            list.add( toDto( cliente1 ) );
        }

        return list;
    }
}
