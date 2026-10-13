
package com.portifolio.daniel.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FraseService {

    private static final List<String> FRASES_PT_BR = List.of(
            "Não é bug, é feature de gênio.",
            "Meu código compila de primeira. Só estou dizendo.",
            "Eu não pesquiso no Stack Overflow, eu colaboro com ele.",
            "Se o teste passou, a culpa é do teste.",
            "Código limpo é arte, e eu sou o museu.",
            "Estou refatorando o mundo, uma função por vez.",
            "Primeiro eu faço funcionar, depois eu faço lindo.",
            "Todo mundo tem um plano até o deploy.",
            "Funciona na minha máquina, e isso já é uma conquista.",

            "Mais duro, melhor, mais rápido, mais forte.",
            "Aquilo que não me mata só pode me tornar mais forte.",
            "Tudo aquilo que eu não sou me tornou tudo o que sou.",
            "Nenhum homem deveria ter tanto poder.",
            "Sinto falta do Kanye antigo.",
            "Acredite no seu estilo, vença sua timidez.",
            "Vamos brindar aos babacas.",
            "Mire nas estrelas para que, se cair, aterrisse numa nuvem.",
            "Não sou um homem de negócios, eu sou um negócio.",
            "Acabei de te dizer quem eu acho que sou.",
            "Estou ocupado demais escrevendo a história para lê-la.",
            "Ainda acho que sou o melhor.",
            "Sou bom no pingue-pongue.",
            "Será que você sabe o que significa encontrar seus sonhos?",
            "Você deveria correr atrás dos seus sonhos, cara.",
            "Me diga o nome de um gênio que não seja louco.",
            "Vou consertar os lobos.",
            "Vamos brindar aos idiotas.",
            "O que não me define também faz parte da minha história.",
            "Não nasci para seguir tendências. Nasci para criar as minhas.",
            "Meu maior concorrente sou eu mesmo.",
            "Não preciso que entendam minha visão para continuar construindo.",
            "O plano é tão grande que parece loucura.",
            "A confiança chega antes de qualquer reconhecimento.",
            "Não confunda silêncio com falta de ambição.",
            "Ela me perguntou o que eu queria na minha lista de desejos.",
            "Você já pediu pra sua mina arrumar outras minas pra você?",
            "Às vezes, a melhor resposta é deixar o resultado falar.",
            "Não estou atrasado. Estou criando meu próprio tempo.",
            "Minha autoestima não precisa de atualização.",
            "Se parece impossível, talvez você esteja pensando pequeno.",
            "O mundo critica primeiro e entende depois.",
            "Não vim para ser uma cópia de ninguém.",
            "Transformando ideias malucas em projetos reais.",
            "Grandes ideias não pedem permissão para existir."
    );

    private static final List<String> FRASES_EN = List.of(
            "It's not a bug, it's a genius feature.",
            "My code compiles on the first try. Just saying.",
            "I don't search Stack Overflow, I collaborate with it.",
            "If the test passed, the test is wrong.",
            "Clean code is art, and I'm the museum.",
            "I'm refactoring the world, one function at a time.",
            "First I make it work, then I make it beautiful.",
            "Everybody has a plan until deploy day.",
            "It works on my machine, and that's an achievement.",

            "Harder, better, faster, stronger.",
            "That that don't kill me can only make me stronger.",
            "Everything I'm not made me everything I am.",
            "No one man should have all that power.",
            "I miss the old Kanye.",
            "Believe in your flyness, conquer your shyness.",
            "Let's have a toast for the douchebags.",
            "Reach for the stars so if you fall you land on a cloud.",
            "I'm not a businessman, I'm a business, man.",
            "I just told you who I think I am.",
            "I feel like I'm too busy writing history to read it.",
            "I still think I am the greatest.",
            "I'm nice at ping pong.",
            "I wonder if you know what it means to find your dreams.",
            "You should follow your dreams, man.",
            "Name one genius that ain't crazy.",
            "I'ma fix wolves.",
            "Let's have a toast for the assholes.",
            "What doesn't define me is still part of my story.",
            "I wasn't born to follow trends. I was born to create my own.",
            "My biggest competitor is myself.",
            "I don't need people to understand my vision to keep building.",
            "The plan is so big it looks like madness.",
            "Confidence arrives before recognition.",
            "Don't mistake silence for a lack of ambition.",
            "She asked me what I wished for on my wishlist.",
            "Have you ever asked your bitch for other bitches?",
            "Sometimes the best answer is letting the results speak.",
            "I'm not late. I'm creating my own timeline.",
            "My self-esteem doesn't need an update.",
            "If it seems impossible, maybe you're thinking too small.",
            "The world criticizes first and understands later.",
            "I didn't come here to be a copy of anyone.",
            "Turning crazy ideas into real projects.",
            "Great ideas don't ask for permission to exist."
    );

    private static final String SESSION_INDICE = "fraseIndice";
    private static final String SESSION_IDIOMA = "fraseIdioma";

    public FraseService() {
        if (FRASES_PT_BR.size() != FRASES_EN.size()) {
            throw new IllegalStateException(
                    "As listas de frases precisam ter o mesmo tamanho: PT="
                            + FRASES_PT_BR.size()
                            + ", EN=" + FRASES_EN.size()
            );
        }
    }

    public String sortear(HttpSession session) {
        String idioma = LocaleContextHolder.getLocale().getLanguage();

        Integer indice = (Integer) session.getAttribute(SESSION_INDICE);
        String idiomaAnterior =
                (String) session.getAttribute(SESSION_IDIOMA);

        boolean trocouIdioma =
                indice != null
                        && idiomaAnterior != null
                        && !idiomaAnterior.equals(idioma);

        if (indice == null || indice < 0
                || indice >= FRASES_PT_BR.size()) {
            indice = ThreadLocalRandom.current()
                    .nextInt(FRASES_PT_BR.size());
        } else if (!trocouIdioma) {
            indice = ThreadLocalRandom.current()
                    .nextInt(FRASES_PT_BR.size());
        }

        session.setAttribute(SESSION_INDICE, indice);
        session.setAttribute(SESSION_IDIOMA, idioma);

        List<String> lista = "en".equals(idioma)
                ? FRASES_EN
                : FRASES_PT_BR;

        return lista.get(indice);
    }
}
