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

import iftm.suetham.mil_vidas.domain.Cliente;
import iftm.suetham.mil_vidas.domain.Livro;
import iftm.suetham.mil_vidas.domain.ListaDesejo;
import iftm.suetham.mil_vidas.repository.ListaDesejoRepository;

@Controller
@RequestMapping("/listaDesejo")
public class ListaDesejoController {

    private ListaDesejoRepository repository;

    public static final String URL_LISTA_WL = "listaDesejo/lista";
    public static final String URL_FORM_WL = "listaDesejo/form";
    public static final String ATRIBUTO_OBJETO_WL = "listaDesejo";
    public static final String ATRIBUTO_LISTA_WL = "listaDesejos";
    public static final String URL_REDIRECT_LISTA = "redirect:/listaDesejo";
    public static final String ATRIBUTO_MENSAGEM = "mensagem";

    public ListaDesejoController(ListaDesejoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {
        List<ListaDesejo> listaDesejos = repository.getListaDesejos();
        model.addAttribute(ATRIBUTO_LISTA_WL, listaDesejos);
        return URL_LISTA_WL;
    }

    @GetMapping("/buscarPorCliente")
    public String buscarPorCliente(@RequestParam("clienteNome") String clienteNome, Model model) {
        List<ListaDesejo> listaBusca = repository.buscaListaDesejosPorCliente(clienteNome);
        model.addAttribute(ATRIBUTO_LISTA_WL, listaBusca);
        if (listaBusca.isEmpty()) {
            model.addAttribute(ATRIBUTO_MENSAGEM, "Nenhuma lista de desejos encontrada para o cliente " + clienteNome);
        }
        return URL_LISTA_WL;
    }

    @GetMapping("/novaListaDesejo")
    public String abrirFormNovaListaDesejo(Model model) {
        ListaDesejo listaDesejo = new ListaDesejo();
        model.addAttribute(ATRIBUTO_OBJETO_WL, listaDesejo);
        return URL_FORM_WL;
    }

    @GetMapping("/editarListaDesejo/{cod_wl}")
    public String abrirFormEditarListaDesejo(@PathVariable("cod_wl") int cod_wl, Model model, RedirectAttributes redirectAttributes) {
        ListaDesejo listaBusca = repository.buscaListaDesejoPorCodigo(cod_wl);
        if (listaBusca == null) {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Lista de desejos com código " + cod_wl + " não encontrada.");
            return URL_REDIRECT_LISTA;
        } else {
            model.addAttribute(ATRIBUTO_OBJETO_WL, listaBusca);
            return URL_FORM_WL;
        }
    }

    @PostMapping("/novaListaDesejo")
    public String salvarListaDesejo(@ModelAttribute("listaDesejo") ListaDesejo listaDesejo, RedirectAttributes redirectAttributes) {
        repository.novaListaDesejo(listaDesejo);
        redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Lista de desejos salva com sucesso.");
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/excluirListaDesejo/{cod_wl}")
    public String excluirListaDesejo(@PathVariable("cod_wl") int cod_wl, RedirectAttributes redirectAttributes) {
        if (repository.delete(cod_wl)) {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Lista de desejos excluída com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Não foi possível excluir a lista de desejos com código " + cod_wl + ".");
        }
        return URL_REDIRECT_LISTA;
    }

    @PostMapping("/editarListaDesejo/{cod_wl}")
    public String atualizarListaDesejo(@PathVariable("cod_wl") int cod_wl, @ModelAttribute("listaDesejo") ListaDesejo listaDesejo, RedirectAttributes redirectAttributes) {
        if (repository.update(listaDesejo)) {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Lista de desejos atualizada com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute(ATRIBUTO_MENSAGEM, "Não foi possível atualizar a lista de desejos com código " + cod_wl + ".");
        }
        return URL_REDIRECT_LISTA;
    }
}