package com.example.david.chess16;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chess16.control.Engine;
import com.example.david.chess16.control.WatchableMatch;

public class WatchMatch extends Activity {

    private ImageButton[][] boardBtns;
    private TextView txtTurn, txtCount;

    private Button btnNext;
    private Button btnPrev;

    private WatchableMatch wMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_chess);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrevious);

        txtCount = (TextView) findViewById(R.id.txtCount);
        txtTurn = (TextView) findViewById(R.id.txtTurn);

        createButtonArray();

        int matchNum = getIntent().getExtras().getInt("Match");

//        Toast.makeText(getApplicationContext(), "Watching Match " + String.valueOf(matchNum) , Toast.LENGTH_SHORT).show();

        wMatch = Engine.getMatchByIndex(matchNum);

        if(wMatch == null){

            Toast.makeText(getApplicationContext(), "Match not Found" , Toast.LENGTH_SHORT).show();
            finish();
        } else {
            displayBoard(wMatch.start());
        }

        btnPrev.setEnabled(false);



    }


    public void onNextClick(View v){
        if(btnNext.getText().equals("Exit to Match Menu")){
            finish();
        }
        Button clicked = (Button) v;
        if(clicked != btnNext){
            return;
        }
        displayBoard(wMatch.getNextMove());
        btnPrev.setEnabled(true);
        if(wMatch.getCurrentMove() + 1 == wMatch.getTotalNumberOfMoves()){
            txtCount.setText(wMatch.getEndMessage());
            btnNext.setText("Exit to Match Menu");
        }
    }

    public void onPrevClick(View v){
        Button clicked = (Button) v;
        if(clicked != btnPrev){
            return;
        }
        displayBoard(wMatch.getPrevMove());
        btnPrev.setEnabled(wMatch.getCurrentMove() != 0);
        btnNext.setEnabled(true);
        btnNext.setText("Next");
    }

    private void updateText(){
        txtTurn.setText("Turn: " + (((wMatch.getCurrentMove() % 2) == 0) ? "White" : "Black"));
        txtCount.setText("Turn " + String.valueOf(wMatch.getCurrentMove() + 1) + " of " + wMatch.getTotalNumberOfMoves());
    }

    public void createButtonArray(){

        boardBtns = new ImageButton[8][8];

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String id =  "btnW"  + String.valueOf(j + 1) + ((char) ('a' + i));
                ImageButton b = (ImageButton) findViewById(getResources().getIdentifier(id, "id", getApplicationContext().getPackageName()));
                if(b == null){
                    Toast.makeText(this, "Error Loading", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, id + " -> " + i + ", " + j, Toast.LENGTH_SHORT).show();
                    boardBtns[i][j] = b;
                }
            }
        }
    }


    private void displayBoard(String[][] pieces){

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String p = pieces[i][j];
                if(p != ""){
//                    Toast.makeText(getApplicationContext(), String.valueOf(i) + ", " + String.valueOf(j) + " -> " + getPieceIconId(p) + " " + p, Toast.LENGTH_SHORT).show();
                    boardBtns[i][j].setImageResource(getPieceIconId(p));
                } else {
                    boardBtns[i][j].setImageResource(0);
                }
                setToBoardColor(boardBtns[i][j]);
            }
        }

        updateText();

    }


    private void setToBoardColor(ImageButton b){
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

    public int getPieceIconId(String piece){
        int id = 0;
        piece = piece.toLowerCase();
        if(piece.charAt(0) == 'b'){
            switch (piece.charAt(1)){
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
            switch (piece.charAt(1)){
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
