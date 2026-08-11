package com.sistema.torneos.app.web.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TablaGoleoResponse {

	private List<TablaGoleadoresResponse> goleadores;
}
