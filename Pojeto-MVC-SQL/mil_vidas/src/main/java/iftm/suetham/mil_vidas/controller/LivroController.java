package iftm.suetham.mil_vidas.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import iftm.suetham.mil_vidas.domain.Livro;
import iftm.suetham.mil_vidas.repository.LivroRepository;

@Controller
@RequestMapping("/livro")
public class LivroController {

    private LivroRepository repository;

    public static final String URL_LISTA_LIVRO = "livro/lista";
    public static final String URL_FORM_LIVRO = "livro/form";
    public static final String ATRIBUTO_OBJETO_LIVRO = "livro";
    public static final String ATRIBUTO_LISTA_LIVRO = "livros";
    public static final String URL_REDIRECT_LISTA = "redirect:/livro";
    public static final String ATRIBUTO_MENSAGEM = "mensagem";

    public LivroController(LivroRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model){
        List<Livro> livros = repository.getLivros();
        model.addAttribute(ATRIBUTO_LISTA_LIVRO, livros);
        return URL_LISTA_LIVRO;
    }

    @GetMapping("/buscarLivros")
    public String buscaLivroPorTitulo(@RequestParam("titulo") String titulo, Model model) {
        List<Livro> livroBusca = repository.buscaLivroPorTitulo(titulo);
        model.addAttribute(ATRIBUTO_LISTA_LIVRO, livroBusca);
        if(livroBusca.isEmpty()){
            model.addAttribute(ATRIBUTO_MENSAGEM, titulo+" não encontrado");
        }
        return URL_LISTA_LIVRO;
    }

    
    @GetMapping("/novoLivro")
    public String abrirFormNovoLivro(Model model){
        Livro livro = new Livro();
        model.addAttribute(ATRIBUTO_OBJETO_LIVRO, livro);
        return URL_FORM_LIVRO;
    }

    @GetMapping("/editarlivro/{codigo}")
    public String abrirFormEditarlivro (@PathVariable("codigo") int codigo, Model model, RedirectAttributes redirectAttributes) {
        Livro livroBusca = repository.buscaLivroPorCodigo(codigo);
        if(livroBusca == null){
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, codigo+" não encontrado");
            return URL_REDIRECT_LISTA;
        }else{
            model.addAttribute(ATRIBUTO_OBJETO_LIVRO, livroBusca);
            return URL_FORM_LIVRO;
        }
    }

    @PostMapping("/novolivro")
    public String salvarlivro(@ModelAttribute("livro") Livro livro, RedirectAttributes redirectAttributes){
        repository.novoLivro(livro);
        redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, livro.getTitulo()+" salvo com sucesso");
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/excluirlivro/{codigo}")
    public String excluirlivro(@PathVariable("codigo") int codigo, RedirectAttributes redirectAttributes) {
        repository.delete(codigo);
        redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "livro excluído com sucesso.");
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/editarlivro/{codigo}")
    public String atualiarlivro(@PathVariable("codigo") String codigo, @ModelAttribute("livro") Livro livro, RedirectAttributes redirectAttributes) {
        if (repository.update(livro)) {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, livro.getTitulo()+" atualizado com sucesso");
        }else{
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Não foi possível atualizar " +livro.getTitulo());
        }
        return URL_REDIRECT_LISTA;
    }
}
