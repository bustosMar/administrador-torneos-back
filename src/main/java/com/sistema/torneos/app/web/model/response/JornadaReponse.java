package com.sistema.torneos.app.web.model.response;

import lombok.*;

@Getter
@Setter
public class JornadaReponse {
   
	private String torneo;
	private String grupo;
	private Long idTorneo;
	private Long idGrupo;
    private String local;
    private String visitante;
    private Long idLocal;
	private Long idVisitante;
}
