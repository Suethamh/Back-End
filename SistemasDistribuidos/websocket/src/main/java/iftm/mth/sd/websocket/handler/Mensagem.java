package iftm.mth.sd.websocket.handler;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mensagem {
    
    private TipoMensagem tipo;
    private String origem;
    private String destino;
    private String texto;
    private Instant dataHora;
}
