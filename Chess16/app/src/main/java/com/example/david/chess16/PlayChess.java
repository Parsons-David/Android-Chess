package com.example.david.chess16;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PlayChess extends Activity {

    String[][] displayBoard;
    Button[][] boardBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_chess);

        createButtonArray();

        createBoard();

    }

    public void onButtonClick(View v){
        Button clicked = (Button) v;
        String idName = v.getResources().getResourceName(clicked.getId());
        idName = idName.substring(idName.length() - 2);
        Toast.makeText(this, "Space " + idName + " pressed.", Toast.LENGTH_SHORT).show();
    }

    private void createBoard() {
        displayBoard = new String[8][8];

        displayBoard[0][7] = "bR";
        displayBoard[1][7] = "bN";
        displayBoard[2][7] = "bB";
        displayBoard[3][7] = "bQ";
        displayBoard[4][7] = "bK";
        displayBoard[5][7] = "bB";
        displayBoard[6][7] = "bN";
        displayBoard[7][7] = "bR";

        for (int i = 0; i < 8; i++) {
            displayBoard[i][6] = "bP";
            displayBoard[i][1] = "wP";
        }

        displayBoard[0][0] = "wR";
        displayBoard[1][0] = "wN";
        displayBoard[2][0] = "wB";
        displayBoard[3][0] = "wQ";
        displayBoard[4][0] = "wK";
        displayBoard[5][0] = "wB";
        displayBoard[6][0] = "wN";
        displayBoard[7][0] = "wR";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (displayBoard[i][j] == null) {
                    displayBoard[i][j] = "";
                }
            }
        }

        pushBoardView();
    }

    private void pushBoardView(){
        ViewGroup view = (ViewGroup)getWindow().getDecorView();
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
               String id =  displayBoard[i][j].toString().toLowerCase();
               if(id != ""){
                   boardBtns[i][j].setBackgroundResource(getPieceIconId(id));
               }
                if((i % 2) == 0){ // Even Rows
                    if((j % 2) == 0){ // Even Cols
                        boardBtns[i][j].setBackgroundColor(Color.WHITE);
                    } else { // Odd Cols
                        boardBtns[i][j].setBackgroundColor(Color.GREEN);
                    }
                } else { // Odd rows
                    if((j % 2) == 0){ // Even Cols
                        boardBtns[i][j].setBackgroundColor(Color.GREEN);
                    } else { // Odd Cols
                        boardBtns[i][j].setBackgroundColor(Color.WHITE);
                    }
                }
            }
        }
    }

    public void createButtonArray(){

        boardBtns = new Button[8][8];

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String id =  String.valueOf(8 - j) + ((char) ('a' + i));
                id = "btn" + id;
                View v = findViewById(getResources().getIdentifier(id, "id", getApplicationContext().getPackageName()));
                Button b = (Button) v;
                if(b == null){
                    Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show();
                } else {
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
