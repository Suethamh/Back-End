package br.edu.iftm.tspi.porm.sistema_jpa.mapper;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Categoria;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Produto;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ProdutoDto;
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
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public Produto toEntity(ProdutoDto dto) {
        if ( dto == null ) {
            return null;
        }

        Produto.ProdutoBuilder produto = Produto.builder();

        produto.categoria( produtoDtoToCategoria( dto ) );
        produto.caminhoImagem( dto.getCaminhoImagem() );
        produto.codigo( dto.getCodigo() );
        if ( dto.getEstoque() != null ) {
            produto.estoque( dto.getEstoque().shortValue() );
        }
        produto.nome( dto.getNome() );
        produto.preco( dto.getPreco() );

        return produto.build();
    }

    @Override
    public ProdutoDto toDto(Produto entity) {
        if ( entity == null ) {
            return null;
        }

        ProdutoDto.ProdutoDtoBuilder produtoDto = ProdutoDto.builder();

        produtoDto.categoriaId( entityCategoriaId( entity ) );
        produtoDto.caminhoImagem( entity.getCaminhoImagem() );
        produtoDto.codigo( entity.getCodigo() );
        if ( entity.getEstoque() != null ) {
            produtoDto.estoque( entity.getEstoque().intValue() );
        }
        produtoDto.nome( entity.getNome() );
        produtoDto.preco( entity.getPreco() );

        return produtoDto.build();
    }

    @Override
    public List<ProdutoDto> toDtoList(List<Produto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProdutoDto> list = new ArrayList<ProdutoDto>( entities.size() );
        for ( Produto produto : entities ) {
            list.add( toDto( produto ) );
        }

        return list;
    }

    @Override
    public List<Produto> toEntityList(List<ProdutoDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Produto> list = new ArrayList<Produto>( dtos.size() );
        for ( ProdutoDto produtoDto : dtos ) {
            list.add( toEntity( produtoDto ) );
        }

        return list;
    }

    protected Categoria produtoDtoToCategoria(ProdutoDto produtoDto) {
        if ( produtoDto == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setId( produtoDto.getCategoriaId() );

        return categoria;
    }

    private Integer entityCategoriaId(Produto produto) {
        Categoria categoria = produto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        return categoria.getId();
    }
}
