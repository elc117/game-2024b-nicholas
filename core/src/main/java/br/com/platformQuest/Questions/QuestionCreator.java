/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Questions;

import br.com.platformQuest.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author zorte
 */
public class QuestionCreator {
    public static final BitmapFont QUESTION_FONT = new BitmapFont(Gdx.files.internal("fonts/question.fnt"), Gdx.files.internal("fonts/question.png"), false);
    private static Question quest;
    private final List<Question> quests = new ArrayList<>();

    public QuestionCreator (){
        initQuests();
    }
        

    public Question getActualQuest() {
        return quest;
    }

    public void selectRandomQuest(){
        if (!quests.isEmpty()) {
            Random r = new Random();
            int nextInt = r.nextInt(quests.size());
            quest = quests.get(nextInt);
            quests.remove(nextInt);
        } else {
            Main.STATUS = Main.status.WIN;
        }
    }

    public void draw(SpriteBatch batch){
    // Criando o GlyphLayout para calcular o tamanho do texto
    String question = getActualQuest().getQuestion();
    GlyphLayout layout = new GlyphLayout();
    layout.setText(QuestionCreator.QUESTION_FONT, question);

    // Obter o tamanho da tela
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();

    // Calcular a posição X e Y para centralizar
    float centerX = (screenWidth - layout.width) / 2;
    float centerY = (screenHeight + layout.height) / 2;

    // Garantir que o texto não ultrapasse os limites da tela (se necessário)
    centerX = Math.max(0, centerX); // Impede que o texto saia da borda esquerda
    centerY = Math.max(0, centerY); // Impede que o texto saia da borda superior

    // Desenhar o texto centralizado na tela
    QuestionCreator.QUESTION_FONT.draw(batch, layout, centerX, 1000);
    }

    private void initQuests() {
        // Adicionando questões hardcoded na lista questsSolved
        quests.add(new Question(
            "Qual municipio nao faz \n parte da Quarta Colonia",
            "Erechim",
            "Agudo"
        ));
        quests.add(new Question(
            "Qual a extensao territorial \nda Quarta Colonia?",
            "2.923",
            "3.122"
        ));
        quests.add(new Question(
            "Quantos habitantes possui o \nterritorio da Quarta Colonia?",
            "58 mil",
            "23 mil"
        ));
        quests.add(new Question(
            "Qual o periodo das rochas \nsedimentares da Quarta Colonia?",
            "Periodo \nTriassico",
            "Periodo \nJurassico"
        ));
        quests.add(new Question(
            "Quantos municipios formam \na Quarta Colonia?",
            "9",
            "8"
        ));
        quests.add(new Question(
            "Qual dessas especies foi \nencontrada na Quarta Colonia?",
            "Bagualosaurus \nagudoensis",
            "Brachiosaurus \naltithorax"
        ));
    }

}
