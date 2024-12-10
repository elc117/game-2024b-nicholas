/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Questions;

/**
 *
 * @author zorte
 */
public class Question {
    private Answer correctAnswer;
    private Answer wrongAnswer;
    private String question;

    public Question(String q, String crctA, String wrngA){
        question = q;
        correctAnswer = new Answer(crctA);
        wrongAnswer = new Answer(wrngA);
    }

    public Answer getCorrectAnswer(){
        return this.correctAnswer;
    }

    public Answer getWrongAnswer() {
        return wrongAnswer;
    }

    public String getQuestion() {
        return question;
    }

    
}
