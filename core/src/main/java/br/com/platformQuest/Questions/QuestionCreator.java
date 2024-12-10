/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Questions;

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
    private static List<Question> quests = new ArrayList<>();

    static {
        // Adicionando questões hardcoded na lista questsSolved
        quests.add(new Question(
            "Qual e a capital da Franca?",
            "Paris",
            "Londres"
        ));
        quests.add(new Question(
            "Quantos planetas existem no sistema solar?",
            "8",
            "9"
        ));
        quests.add(new Question(
            "Quem escreveu 'Dom Quixote'?",
            "Miguel de Cervantes",
            "William Shakespeare"
        ));
        quests.add(new Question(
            "Qual e o elemento quimico com simbolo 'O'?",
            "Oxigenio",
            "Ouro"
        ));
        quests.add(new Question(
            "Quantos continentes existem no mundo?",
            "7",
            "6"
        ));
    }

    public static Question getQuest() {
        return quest;
    }

    public void selectRandomQuest(){
        Random r = new Random();
        int nextInt = r.nextInt(quests.size());
        quest = quests.get(nextInt);
        quests.remove(nextInt);
    }

    public static void draw(SpriteBatch batch){
    // Criando o GlyphLayout para calcular o tamanho do texto
    String question = QuestionCreator.getQuest().getQuestion();
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

}
