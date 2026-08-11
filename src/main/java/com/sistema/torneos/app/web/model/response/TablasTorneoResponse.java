package com.sistema.torneos.app.web.model.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TablasTorneoResponse {
	
	 private List<TablasPosicionResponse> general;

	 private Map<Long, List<TablasPosicionResponse>> porGrupo;

}
