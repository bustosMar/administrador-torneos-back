package com.sistema.torneos.app.web.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HuellaResponse {

    private Boolean capturada;

    private String template;


}
