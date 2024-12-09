package iftm.suetham.mil_vidas.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import iftm.suetham.mil_vidas.domain.Cliente;
import iftm.suetham.mil_vidas.domain.Livro;
import iftm.suetham.mil_vidas.repository.ClienteRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/cliente")
public class MilVidasController {

    private ClienteRepository repository;

    public static final String URL_LISTA = "cliente/lista";
    public static final String URL_FORM = "cliente/form";
    
    public static final String URL_REDIRECT_LISTA = "redirect:/cliente";

    public static final String ATRIBUTO_MENSAGEM = "mensagem";
    public static final String ATRIBUTO_OBJETO_CLIENTE = "cliente";
    public static final String ATRIBUTO_LISTA_CLIENTE = "clientes";
    
    
    
    public static final String ATRIBUTO_OBJETO_WISHLIST = "wl";
    public static final String ATRIBUTO_LISTA_WISHLIST = "wls";


    public MilVidasController(ClienteRepository repository){
        this.repository = repository;
    }

    // CLIENTES
    @GetMapping
    public String listar(Model model){
        List<Cliente> clientes = repository.getClientes();
        model.addAttribute(ATRIBUTO_LISTA_CLIENTE, clientes);
        return URL_LISTA;
    }


    @GetMapping("/buscarClientes")
    public String buscaClientePorNome(@RequestParam("nome") String nome, Model model) {
        List<Cliente> clienteBusca = repository.buscaClientePorNome(nome);
        model.addAttribute(ATRIBUTO_LISTA_CLIENTE, clienteBusca);
        if(clienteBusca.isEmpty()){
            model.addAttribute(ATRIBUTO_MENSAGEM, nome+" não encontrado");
        }
        return URL_LISTA;
    }

    @GetMapping("/novoCliente")
    public String abrirFormNovoCliente(Model model){
        Cliente cliente = new Cliente();
        model.addAttribute(ATRIBUTO_OBJETO_CLIENTE, cliente);
        return URL_FORM;
    }

    @GetMapping("/editarCliente/{login}")
    public String abrirFormEditarCliente (@PathVariable("login") String login, Model model, RedirectAttributes redirectAttributes) {
        Cliente clienteBusca = repository.buscaClientePorLogin(login);
        if(clienteBusca == null){
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, login+" não encontrado");
            return URL_REDIRECT_LISTA;
        }else{
            model.addAttribute(ATRIBUTO_OBJETO_CLIENTE, clienteBusca);
            return URL_FORM;
        }
    }

    @PostMapping("/novoCliente")
    public String salvarCliente(@ModelAttribute("cliente") Cliente cliente, RedirectAttributes redirectAttributes){
        repository.novoCliente(cliente);
        redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, cliente.getNome()+" salvo com sucesso");
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/excluirCliente/{login}")
    public String excluirCliente(@PathVariable("login") String login, RedirectAttributes redirectAttributes) {
        repository.delete(login);
        redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Cliente excluído com sucesso.");
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/editarCliente/{login}")
    public String atualiarCliente(@PathVariable("login") String login, @ModelAttribute("cliente") Cliente cliente, RedirectAttributes redirectAttributes) {
        if (repository.update(cliente)) {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, cliente.getNome()+" atualizado com sucesso");
        }else{
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Não foi possível atualizar " +cliente.getNome());
        }
        return URL_REDIRECT_LISTA;
    }


}
