package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Cliente;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ClienteDto;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    Cliente toEntity(ClienteDto clientedto);

    ClienteDto toDto(Cliente cliente);

    List<Cliente> toEntityList(List<ClienteDto> clienteDto);

    List<ClienteDto> toDtoList(List<Cliente> cliente);

}
