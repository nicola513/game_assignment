package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Game {
    static final int board_x=4;
    static int board_y = 4;
    static int style = 0;

    ImageData imageData;
    List<CardData> gameData = new ArrayList<CardData>();
    List<ImageData> orderList = new ArrayList<>();

    long startTime = -1;
    long endTime = -1;
    int numMoves = 0;
    int imageNo = 0;
    int numPair = 0 ;

    /** Generates the game data. */
    void generateData(Context context) {
        startTime = -1;
        endTime = -1;
        numMoves = 0;
        gameData.clear();
        imageData.imageList.clear();

        for(int i = 0; i < imageData.imageStyle[style].length; i++){
            imageData.imageList.add(imageData.imageStyle[style][i]);
        }

        Collections.shuffle(imageData.imageList);


        for (int i = 0; i < board_x * board_y / 2; i++) {
            CardData data = new CardData(context,i);//TODO imageList number
            gameData.add(data);
            gameData.add(new CardData(data));
        }
        Collections.shuffle(gameData);
    }

    void setOrderList(Context context){
        imageNo = 0;
        orderList.clear();
        for (int i = 0; i < board_x * board_y / 2; i++) {
            ImageData imageData = new ImageData(context,i);
            orderList.add(imageData);
        }

        for(int i=0 ; i < 7 ; i++){
            ImageData space = new ImageData(context);
            orderList.add(space);
        }
    }

    /** Returns whether the user wins, i.e., all cards are cleared. */
    boolean isGameWon() {
        for (CardData data : gameData) {
            if (data.state != CardData.State.Clear)
                return false;
        }
        return true;
    }

    /** Opens a card, if it is currently close. */
    void openCard(CardData data) {
        if (numberOpenCards() < 2 && data.state == CardData.State.Close) {
            if (startTime == -1) // First move, keep time
                startTime = System.currentTimeMillis();
            endTime = System.currentTimeMillis();
            numMoves++;

            data.state = CardData.State.Open;
        }
    }

    /** Returns the number of open cards. */
    int numberOpenCards() {
        return getOpenCards().size();
    }

    /** Returns whether there is a good pair of open cards. */
    boolean hasGoodPairOpen() {
        List<CardData> openCards = getOpenCards();
        return openCards.size() == 2 && openCards.get(0).equals(openCards.get(1));
    }

    /** Returns whether there is a bad pair of open cards. */
    boolean hasBadPairOpen() {
        List<CardData> openCards = getOpenCards();
        return openCards.size() == 2 && ! openCards.get(0).equals(openCards.get(1));
    }

    /** Handles the open cards, either clearing them or closing them. */
    void handleOpenCards() {
        List<CardData> openCards = getOpenCards();
        if (hasGoodPairOpen()) {
            openCards.get(0).state = CardData.State.Clear;
            openCards.get(1).state = CardData.State.Clear;
            numPair++;
        } else {
            for (CardData data : openCards)
                data.state = CardData.State.Close;
        }
    }

    boolean handleOrderOpenCards(){
        List<CardData> openCards = getOpenCards();
        if (hasGoodPairOpen()&& openCards.get(0).imageNo==imageNo) {
            openCards.get(0).state = CardData.State.Clear;
            openCards.get(1).state = CardData.State.Clear;
            imageNo++;
            return true;
        } else {

            for (CardData data : openCards)
                data.state = CardData.State.Close;
            return false;
        }
    }

    /** Returns a list of open cards. */
    private List<CardData> getOpenCards() {
        List<CardData> openCards = new ArrayList<CardData>();
        for (CardData data : gameData) {
            if (data.state == CardData.State.Open)
                openCards.add(data);
        }
        return openCards;
    }

}
