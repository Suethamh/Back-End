package iftm.mth.sd.websocket.handler;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    
    @Autowired
    private SimpUserRegistry userRegistry;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UsuarioConectadoService usuarioConectado;

    @MessageMapping("/chat.users")
    @SendTo("/topic/users")
    public List<String> usuariosConectados() {
        return usuarioConectado.getUsuarios();
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Mensagem enviarTexto(Mensagem mensagem){
        mensagem.setTipo(TipoMensagem.ENVIAR_TEXTO);
        mensagem.setDataHora(Instant.now());
        return mensagem;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public Mensagem entrar(Mensagem mensagem, SimpMessageHeaderAccessor accessor){
        mensagem.setTipo(TipoMensagem.ENTRAR);
        mensagem.setDataHora(Instant.now());

        accessor.getSessionAttributes().put("origem", mensagem.getOrigem());
        mensagem.setTexto("Entrou");
        return mensagem;
    }

    @MessageMapping("chat.leave")
    @SendTo("topic/public")
    public Mensagem sair(Mensagem mensagem){
        mensagem.setTipo(TipoMensagem.SAIR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " Saiu");
        return mensagem;
    }

    @MessageMapping("/chat.private")
    public void enviarMensagemPrivada(Mensagem mensagem){
        mensagem.setTipo(TipoMensagem.PRIVADO);
        mensagem.setDataHora(Instant.now());

        messagingTemplate.convertAndSendToUser(mensagem.getDestino(), "/queue/private", mensagem);

        messagingTemplate.convertAndSendToUser(mensagem.getOrigem(), "/queue/private", mensagem);
    }
}
