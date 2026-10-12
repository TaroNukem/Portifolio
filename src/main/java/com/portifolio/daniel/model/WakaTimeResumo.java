package com.portifolio.daniel.model;

import java.util.List;

public class WakaTimeResumo {

    private final String total;
    private final List<Linguagem> linguagens;

    public WakaTimeResumo(String total, List<Linguagem> linguagens) {
        this.total = total;
        this.linguagens = linguagens;
    }

    public static WakaTimeResumo vazio() {
        return new WakaTimeResumo("0 min", List.of());
    }

    public String getTotal() {
        return total;
    }

    public List<Linguagem> getLinguagens() {
        return linguagens;
    }

    public static class Linguagem {

        private final String nome;
        private final String tempo;
        private final int percentual;
        private final String slug;
        private final String icone;
        private final long segundos;

        public Linguagem(String nome, String tempo, int percentual, String slug, String icone, long segundos) {
            this.nome = nome;
            this.tempo = tempo;
            this.percentual = percentual;
            this.slug = slug;
            this.icone = icone;
            this.segundos = segundos;
        }

        public String getNome() {
            return nome;
        }

        public String getTempo() {
            return tempo;
        }

        public int getPercentual() {
            return percentual;
        }

        public String getSlug() {
            return slug;
        }

        public String getIcone() {
            return icone;
        }

        public long getSegundos() {
            return segundos;
        }
    }
}