package com.example.david.chess16;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import control.Match;
import control.Engine;
import control.Move;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayChess extends Activity {

    private Match match;
    private Move currentMove;

    private Button[][] boardBtns;
    private Button btnUndo, btnAI, btnDraw, btnResign;
    private LinearLayout promoButtons;

    private Button selected = null;
    private Color sColor = null;
    private Boolean sendDraw = false;

    // HANDLE INITIAL UNDO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_chess);

        match = Engine.startNewMatch("");
        unpack(match.executeMove("1e", "1f", false, ' '));

        createButtonArray();

//        createBoard();

        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnAI = (Button) findViewById(R.id.btnAI);
        btnDraw = (Button) findViewById(R.id.btnDraw);
        btnResign = (Button) findViewById(R.id.btnResign);

        promoButtons = (LinearLayout) findViewById(R.id.promoButtons);
        promoButtons.setVisibility(View.INVISIBLE);

        btnUndo.setEnabled(false);

    }

    public void onButtonClick(View v){
        String idName = "";
        Button clicked = (Button) v;

        if(clicked == btnUndo){
            btnUndo.setEnabled(false);

            idName = "Undo";
//            Board tmp = match.undo();
//            unpackBoard(tmp);
//            return;

        } else if(clicked == btnAI){

            idName = "AI";
//            Board tmp = match.executeAIMove();
//            unpackBoard(tmp);
//            return;


        } else if(clicked == btnDraw){

            sendDraw = !sendDraw;
            btnDraw.setText(sendDraw ? "Cancel Draw" : "Send Draw");
            idName = "Draw";

        } else if(clicked == btnResign){

            idName = "Resign";
//            String message = match.resignation();
//            endMatch(message);
//            return;

        } else {
            idName = v.getResources().getResourceName(clicked.getId());
        }

        Toast.makeText(this, idName + " pressed.", Toast.LENGTH_SHORT).show();
    }

    public void onPromoClick(View v){

    }

    public void onBoardClick(View v){
        Button clicked = (Button) v;
        String idName = v.getResources().getResourceName(clicked.getId());
        idName = idName.substring(idName.length() - 2);
//        Toast.makeText(this, "Space " + idName + " pressed.", Toast.LENGTH_SHORT).show();


        if(selected == null){ // Selecting First Piece to move (getting src)

            // HANDLE PROMOTION
            ArrayList<String> possiblePromos = new ArrayList<String>();
            possiblePromos.add("8a");
            possiblePromos.add("1h");
//            possiblePromos = playingBoard.getPossiblePromoteSpaces();
            if(possiblePromos.contains(idName)){
                promoButtons.setVisibility(View.VISIBLE);
            }

            // Control
            selected = clicked;
            selected.setBackgroundColor(Color.RED);

            // View
            btnUndo.setEnabled(false);
            btnAI.setEnabled(false);

        } else {

            if (selected != clicked) { // Second Piece Selected (gathered dst), attempting move...

                String sidName = selected.getResources().getResourceName(selected.getId());
                sidName = sidName.substring(sidName.length() - 2);

                Toast.makeText(getApplicationContext(), sidName + " -> " + idName, Toast.LENGTH_SHORT).show();



//                Board tmp = match.executeMove(sidName, idName, sendDraw, ' ');
//                unpackBoard(tmp);
//                return;

            }


            // Control

            setToBoardColor(selected);
            selected = null;

            // View
            promoButtons.setVisibility(View.INVISIBLE);
            btnUndo.setEnabled(true);
            btnAI.setEnabled(true);

        }


    }

    private void unpack (Move m){
        currentMove = m;
        ((TextView) findViewById(R.id.txtTurn)).setText(currentMove.getTurn() == 'w' ? R.string.white_turn : R.string.black_turn);

        displayBoard(currentMove.getPieces());
    }

    private void displayBoard(String[][] pieces){

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String p = pieces[i][j];
                if(p != ""){
                    boardBtns[i][j].setBackgroundResource(getPieceIconId(p));
                }
                setToBoardColor(boardBtns[i][j]);
            }
        }

    }

    private void endMatch(String message){

    }

    private void toggleEnable(Button b){
        b.setEnabled(!b.isEnabled());
    }

    private void setToBoardColor(Button b){
        String idName = b.getResources().getResourceName(b.getId());
        idName = idName.substring(idName.length() - 2);

        int i = Integer.parseInt(idName.charAt(0) + "");
        int j = idName.charAt(1) - 'a';

        if((i % 2) == 0){ // Even Rows
            if((j % 2) == 0){ // Even Cols
                b.setBackgroundColor(getResources().getColor(R.color.boardWhite, null));
            } else { // Odd Cols
                b.setBackgroundColor(getResources().getColor(R.color.boardGreen, null));
            }
        } else { // Odd rows
            if((j % 2) == 0){ // Even Cols
                b.setBackgroundColor(getResources().getColor(R.color.boardGreen, null));
            } else { // Odd Cols
                b.setBackgroundColor(getResources().getColor(R.color.boardWhite, null));
            }
        }
    }

    private void createBoard() {
        String tmpTxtBoard[][] = new String[8][8];

        tmpTxtBoard[0][7] = "bR";
        tmpTxtBoard[1][7] = "bN";
        tmpTxtBoard[2][7] = "bB";
        tmpTxtBoard[3][7] = "bQ";
        tmpTxtBoard[4][7] = "bK";
        tmpTxtBoard[5][7] = "bB";
        tmpTxtBoard[6][7] = "bN";
        tmpTxtBoard[7][7] = "bR";

        for (int i = 0; i < 8; i++) {
            tmpTxtBoard[i][6] = "bP";
            tmpTxtBoard[i][1] = "wP";
        }

        tmpTxtBoard[0][0] = "wR";
        tmpTxtBoard[1][0] = "wN";
        tmpTxtBoard[2][0] = "wB";
        tmpTxtBoard[3][0] = "wQ";
        tmpTxtBoard[4][0] = "wK";
        tmpTxtBoard[5][0] = "wB";
        tmpTxtBoard[6][0] = "wN";
        tmpTxtBoard[7][0] = "wR";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tmpTxtBoard[i][j] == null) {
                    tmpTxtBoard[i][j] = "";
                }
            }
        }

        displayBoard(tmpTxtBoard);
    }



    public void createButtonArray(){

        boardBtns = new Button[8][8];

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String id =  "btn"  + String.valueOf(8 - j) + ((char) ('a' + i));
                Button b = (Button) findViewById(getResources().getIdentifier(id, "id", getApplicationContext().getPackageName()));
                if(b == null){
                    Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, id + " -> " + i + ", " + j, Toast.LENGTH_SHORT).show();
                    boardBtns[i][j] = b;
                }
            }
        }
    }

    public int getPieceIconId(String piece){
        int id = 0;
        piece = piece.toLowerCase();
        if(piece.charAt(0) == 'b'){
            switch (piece.charAt(0)){
                case 'r':
                    id = R.drawable.br;
                    break;
                case 'n':
                    id = R.drawable.bn;
                    break;
                case 'b':
                    id = R.drawable.bb;
                    break;
                case 'q':
                    id = R.drawable.bq;
                    break;
                case 'k':
                    id = R.drawable.bk;
                    break;
                case 'p':
                    id = R.drawable.bp;
                    break;
            }
        } else {
            switch (piece.charAt(0)){
                case 'r':
                    id = R.drawable.wr;
                    break;
                case 'n':
                    id = R.drawable.wn;
                    break;
                case 'b':
                    id = R.drawable.wb;
                    break;
                case 'q':
                    id = R.drawable.wq;
                    break;
                case 'k':
                    id = R.drawable.wk;
                    break;
                case 'p':
                    id = R.drawable.wp;
                    break;
            }
        }
        return id;
    }
}
