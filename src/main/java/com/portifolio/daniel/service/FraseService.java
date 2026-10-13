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
            "Commit na main numa sexta-feira. Eu sou assim.",
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
            "Eu sou um deus.",
            "Acabei de te dizer quem eu acho que sou.",
            "Mercedes cor de maionese, eu dirijo como se fosse Miracle Whip.",
            "Estou ocupado demais escrevendo a história para lê-la.",
            "Ainda acho que sou o melhor.",
            "Sou bom no pingue-pongue.",
            "Será que você sabe o que significa encontrar seus sonhos?",
            "Você deveria correr atrás dos seus sonhos, cara.",
            "Sinto que sou o melhor.",
            "Me diga o nome de um gênio que não seja louco.",
            "Odeio ser bipolar, é incrível.",
            "Vou consertar os lobos.",
            "Vamos brindar aos idiotas.",
            "Será que você sabe o que significa encontrar seus sonhos?",
            "Mais duro, melhor, mais rápido, mais forte.",
            "Tudo aquilo que eu não sou me tornou tudo o que sou.",
            "Eu sou um deus.",
            "Nenhum homem deveria ter tanto poder.",
            "Acredite no seu estilo, vença sua timidez.",
            "Mire nas estrelas para que, se cair, aterrisse numa nuvem."
    );

    private static final List<String> FRASES_EN = List.of(
            "It's not a bug, it's a genius feature.",
            "My code compiles on the first try. Just saying.",
            "Pushing to main on a Friday. That's who I am.",
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
            "I am a God.",
            "I just told you who I think I am.",
            "Mayonnaise-colored Benz, I push Miracle Whips.",
            "I feel like I'm too busy writing history to read it.",
            "I still think I am the greatest.",
            "I'm nice at ping pong.",
            "I wonder if you know what it means to find your dreams.",
            "You should follow your dreams, man.",
            "I feel like I'm the best.",
            "Name one genius that ain't crazy.",
            "I hate being Bi-Polar its awesome.",
            "I'ma fix wolves.",
            "Let's have a toast for the assholes.",
            "I wonder if you know what it means to find your dreams.",
            "Harder, better, faster, stronger.",
            "Everything I'm not made me everything I am.",
            "I am a God.",
            "No one man should have all that power.",
            "Believe in your flyness, conquer your shyness.",
            "Reach for the stars so if you fall you land on a cloud."
    );

    private static final String SESSION_INDICE = "fraseIndice";
    private static final String SESSION_IDIOMA = "fraseIdioma";

    public FraseService() {
        if (FRASES_PT_BR.size() != FRASES_EN.size()) {
            throw new IllegalStateException(
                    "As listas de frases precisam ter o mesmo tamanho: PT="
                            + FRASES_PT_BR.size() + ", EN=" + FRASES_EN.size());
        }
    }

    public String sortear(HttpSession session){
        String idioma = LocaleContextHolder.getLocale().getLanguage();
        Integer indice = (Integer) session.getAttribute(SESSION_INDICE);
        String idiomaAnterior = (String) session.getAttribute(SESSION_IDIOMA);

        boolean trocouIdioma = indice != null && idiomaAnterior != null && !idiomaAnterior.equals(idioma);

        if(!trocouIdioma){
            indice = ThreadLocalRandom.current().nextInt(FRASES_PT_BR.size());
        }

        session.setAttribute(SESSION_INDICE, indice);
        session.setAttribute(SESSION_IDIOMA, idioma);

        List<String> lista = "en".equals(idioma) ?  FRASES_EN : FRASES_PT_BR;

        return lista.get(indice);
    }
}
