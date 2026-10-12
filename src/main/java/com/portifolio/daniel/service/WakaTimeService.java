
package com.portifolio.daniel.service;

import com.portifolio.daniel.model.WakaTimeResumo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class WakaTimeService {

    private static final long VALIDADE_MS = 60 * 60 * 1000L;
    private static final long NOVA_TENTATIVA_MS = 2 * 60 * 1000L;

    private static final Map<String, String[]> LINGUAGENS = Map.of(
            "java", new String[]{"lang-java", "fa-brands fa-java"},
            "html", new String[]{"lang-html", "fa-brands fa-html5"},
            "python", new String[]{"lang-python", "fa-brands fa-python"},
            "javascript", new String[]{"lang-javascript", "fa-brands fa-js"}
    );

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;
    private final String usuario;
    private final String periodo;

    private WakaTimeResumo cache;
    private long valeAte = 0;

    public WakaTimeService(
            @Value("${wakatime.api-key:}") String apiKey,
            @Value("${wakatime.user:b860e6a6-2e86-4589-9069-df3ceeda98b1}") String usuario,
            @Value("${wakatime.range:all_time}") String periodo) {

        this.apiKey = apiKey;
        this.usuario = usuario;
        this.periodo = periodo;
    }

    public synchronized WakaTimeResumo buscarResumo() {
        long agora = System.currentTimeMillis();

        if (cache != null && agora < valeAte) {
            return cache;
        }

        try {
            ResponseEntity<Map> resposta = consultar();

            Map<?, ?> corpo = resposta.getBody();

            if (corpo == null || !(corpo.get("data") instanceof Map<?, ?> data)) {
                throw new IllegalStateException(
                        "Resposta inesperada do WakaTime"
                );
            }

            System.out.println(
                    "WakaTime: período recebido = " + data.get("range")
                            + " (" + data.get("human_readable_range")
                            + "), total = " + data.get("human_readable_total")
            );

            boolean pronto = resposta.getStatusCode().value() == 200
                    && !Boolean.FALSE.equals(data.get("is_up_to_date"));

            cache = montar(data);

            valeAte = agora + (
                    pronto ? VALIDADE_MS : NOVA_TENTATIVA_MS
            );

            return cache;

        } catch (Exception e) {
            System.err.println(
                    "Não foi possível buscar os dados do WakaTime: "
                            + e.getMessage()
            );

            if (cache == null) {
                cache = WakaTimeResumo.vazio();
            }

            valeAte = agora + NOVA_TENTATIVA_MS;

            return cache;
        }
    }

    private ResponseEntity<Map> consultar() {
        try {
            return restTemplate.exchange(
                    urlStats(usuario),
                    HttpMethod.GET,
                    new HttpEntity<>(cabecalhos(false)),
                    Map.class
            );

        } catch (Exception e) {
            if (apiKey == null || apiKey.isBlank()) {
                throw e;
            }

            System.err.println(
                    "WakaTime (perfil público) falhou: "
                            + e.getMessage()
                            + " - tentando com a API key."
            );

            return restTemplate.exchange(
                    urlStats("current"),
                    HttpMethod.GET,
                    new HttpEntity<>(cabecalhos(true)),
                    Map.class
            );
        }
    }

    private String urlStats(String quem) {
        return "https://wakatime.com/api/v1/users/"
                + quem + "/stats/" + periodo;
    }

    private HttpHeaders cabecalhos(boolean autenticado) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Accept", "application/json");

        if (autenticado) {
            headers.set(
                    "Authorization",
                    "Basic " + Base64.getEncoder().encodeToString(
                            apiKey.getBytes(StandardCharsets.UTF_8)
                    )
            );
        }

        return headers;
    }

    private WakaTimeResumo montar(Map<?, ?> data) {
        long totalSegundos = (long) numero(data.get("total_seconds"));

        List<WakaTimeResumo.Linguagem> lista = new ArrayList<>();

        if (data.get("languages") instanceof List<?> linguagens) {
            for (Object item : linguagens) {
                if (!(item instanceof Map<?, ?> l)) {
                    continue;
                }

                String nome = String.valueOf(l.get("name"));
                String chave = nome.toLowerCase(Locale.ROOT);

                String[] visual = LINGUAGENS.get(chave);

                if (visual == null) {
                    continue;
                }

                long segundos = (long) numero(l.get("total_seconds"));

                int percentual = (int) Math.round(
                        numero(l.get("percent"))
                );

                lista.add(
                        new WakaTimeResumo.Linguagem(
                                nome,
                                formatar(segundos),
                                percentual,
                                visual[0],
                                visual[1],
                                segundos
                        )
                );
            }
        }

        lista.sort(
                Comparator.comparingLong(
                        WakaTimeResumo.Linguagem::getSegundos
                ).reversed()
        );

        return new WakaTimeResumo(
                formatar(totalSegundos),
                lista
        );
    }

    private static double numero(Object valor) {
        return valor instanceof Number n ? n.doubleValue() : 0;
    }

    private static String formatar(long segundos) {
        long minutos = segundos / 60;

        if (minutos < 1) {
            return "< 1 min";
        }

        long horas = minutos / 60;
        long resto = minutos % 60;

        if (horas == 0) {
            return resto + " min";
        }

        return horas + " h " + String.format("%02d", resto) + " min";
    }
}
