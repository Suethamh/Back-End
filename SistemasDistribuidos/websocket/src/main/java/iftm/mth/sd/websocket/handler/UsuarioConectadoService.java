package iftm.mth.sd.websocket.handler;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class UsuarioConectadoService {
    
    private final ConcurrentHashMap<String, String> usuariosConectados = new ConcurrentHashMap<>();

    public void adicionarUsuario(String sessionId, String username) {
        usuariosConectados.put(sessionId, username);
    }

    public String removerUsuario(String sessionId){
        return usuariosConectados.remove(sessionId);
    }

    public List<String> getUsuarios(){
        List<String> usuarios;
        synchronized (usuariosConectados){
            usuarios = List.copyOf(usuariosConectados.values());
        }

        return usuarios;
    }
}
