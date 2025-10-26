package iftm.mth.sd.websocket.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketListener {
    
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UsuarioConectadoService usuarioConectado;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("origem");
        if(username != null) {
            usuarioConectado.adicionarUsuario(headerAccessor.getSessionId(), username);
            messagingTemplate.convertAndSend("/topic/users", usuarioConectado.getUsuarios());
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = usuarioConectado.removerUsuario(sessionId);
        
        if(username != null) {
            Mensagem mensagem = new Mensagem();
            mensagem.setTipo(TipoMensagem.SAIR);
            mensagem.setOrigem(username);
            mensagem.setTexto(username + " saiu");
            
            messagingTemplate.convertAndSend("/topic/public", mensagem);
            messagingTemplate.convertAndSend("/topic/users", usuarioConectado.getUsuarios());
        }
    }
}