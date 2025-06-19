package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Categoria;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.CategoriaDto;
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
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public Categoria toEntity(CategoriaDto dto) {
        if ( dto == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setDescricao( dto.getDescricao() );
        categoria.setId( dto.getId() );
        categoria.setNome( dto.getNome() );

        return categoria;
    }

    @Override
    public CategoriaDto toDto(Categoria categoria) {
        if ( categoria == null ) {
            return null;
        }

        CategoriaDto.CategoriaDtoBuilder categoriaDto = CategoriaDto.builder();

        categoriaDto.descricao( categoria.getDescricao() );
        categoriaDto.id( categoria.getId() );
        categoriaDto.nome( categoria.getNome() );

        return categoriaDto.build();
    }

    @Override
    public List<CategoriaDto> toDtoList(List<Categoria> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoriaDto> list = new ArrayList<CategoriaDto>( entities.size() );
        for ( Categoria categoria : entities ) {
            list.add( toDto( categoria ) );
        }

        return list;
    }

    @Override
    public List<Categoria> toEntityList(List<CategoriaDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Categoria> list = new ArrayList<Categoria>( dtos.size() );
        for ( CategoriaDto categoriaDto : dtos ) {
            list.add( toEntity( categoriaDto ) );
        }

        return list;
    }
}
