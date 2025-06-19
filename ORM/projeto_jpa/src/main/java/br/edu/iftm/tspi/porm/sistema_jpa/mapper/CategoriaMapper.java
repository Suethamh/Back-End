package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Categoria;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.CategoriaDto;

@Mapper(componentModel="spring")
public interface CategoriaMapper {

    Categoria toEntity(CategoriaDto dto);

    CategoriaDto toDto(Categoria categoria);

    List<CategoriaDto> toDtoList(List<Categoria> entities);
    
    List<Categoria> toEntityList(List<CategoriaDto> dtos);

}
