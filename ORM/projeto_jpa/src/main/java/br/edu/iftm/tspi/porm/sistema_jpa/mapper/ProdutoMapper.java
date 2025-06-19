package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Produto;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ProdutoDto;

@Mapper(componentModel="spring")
public interface ProdutoMapper {
    
    @Mapping(source = "categoriaId", target = "categoria.id")
    Produto toEntity(ProdutoDto dto);

    @Mapping(source = "categoria.id", target = "categoriaId")
    ProdutoDto toDto(Produto entity);

    List<ProdutoDto> toDtoList(List<Produto> entities);

    List<Produto> toEntityList(List<ProdutoDto> dtos);

}
