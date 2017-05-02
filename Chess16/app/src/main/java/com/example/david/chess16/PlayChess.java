package com.example.david.chess16;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.david.chess16.control.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayChess extends Activity {

    Match match;
    private Move currentMove;
    private Move prevMove = null;

    // Move Stuff
    private ArrayList<String> possiblePromos = new ArrayList<String>();
    private char promo = 'Q';

    private ImageButton[][] boardBtns;
    private Button btnUndo, btnAI, btnDraw, btnResign, btnPR, btnPN, btnPB, btnPQ;
    private LinearLayout promoButtons;

    private ImageButton selected = null;
    private Color sColor = null;
    private Boolean sendDraw = false;

    Boolean acceptedDraw = false;
    boolean canUndo = false;

    // HANDLE INITIAL UNDO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_chess);

        createButtonArray();

        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnAI = (Button) findViewById(R.id.btnAI);
        btnDraw = (Button) findViewById(R.id.btnDraw);
        btnResign = (Button) findViewById(R.id.btnResign);

        btnPR = (Button) findViewById(R.id.btnpr);
        btnPB = (Button) findViewById(R.id.btnpb);
        btnPN = (Button) findViewById(R.id.btnpn);
        btnPQ = (Button) findViewById(R.id.btnpq);

        promoButtons = (LinearLayout) findViewById(R.id.promoButtons);
        promoButtons.setVisibility(View.INVISIBLE);

        match = Engine.startNewMatch("");
        displayBoard(match.getCurrentDisplayBoard());

        btnUndo.setEnabled(canUndo);

    }

    private void unpack (Move m){
//        Toast.makeText(getApplicationContext(),"Unpacking...", Toast.LENGTH_SHORT).show();
        if(currentMove == m){
//        Toast.makeText(getApplicationContext(),"No New Move Made", Toast.LENGTH_SHORT).show();
            return;
        }

        // Can assume valid new move has been made
        canUndo = true;
        currentMove = m;

        if(currentMove.hasPendingDraw()){
            Toast.makeText(getApplicationContext(),"Pending Draw", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString(ChessDialogFragment.MESSAGE_KEY, "Pending Draw");

            DialogFragment draw = new ChessDialogFragment();
            draw.setArguments(bundle);
            draw.setCancelable(false);
            draw.show(getFragmentManager(), "badfields");
        }

        btnDraw.setText("Send Draw");
        sendDraw = false;
        promo = 'Q';


        if(currentMove.getPossiblePromoteSpaces() == null){
            Toast.makeText(getApplicationContext(),"Error Promote Spaces returned null.", Toast.LENGTH_SHORT).show();
        } else {
            possiblePromos = currentMove.getPossiblePromoteSpaces();
        }


        ((TextView) findViewById(R.id.txtTurn)).setText(currentMove.getTurn() == 'w' ? R.string.white_turn : R.string.black_turn);
        displayBoard(currentMove.getPieces());

        if(!match.isOngoing()){
            endMatch(match.getEndMessage());
        }

    }

    public void onButtonClick(View v){
        String idName = "";
        Button clicked = (Button) v;

        if(clicked == btnUndo){
            idName = "Undo";
            Move tmp = match.undo();
            unpack(tmp);
//            return;
            canUndo = false;
            btnUndo.setEnabled(canUndo);


        } else if(clicked == btnAI){

            idName = "AI";
            canUndo = true;
            btnUndo.setEnabled(canUndo);

            Move tmp = match.makeAIMove(sendDraw);
            if(tmp == null){
                Toast.makeText(this, "AI is null.", Toast.LENGTH_SHORT).show();
            }
            unpack(tmp);
            return;


        } else if(clicked == btnDraw){

            sendDraw = !sendDraw;
            btnDraw.setText(sendDraw ? "Cancel Draw" : "Send Draw");
            idName = "Draw";

        } else if(clicked == btnResign){
            canUndo = true;
            btnUndo.setEnabled(canUndo);
            idName = "Resign";
            String message = match.resignation();
            endMatch(message);
//            Engine.saveMatch();
//            return;

        } else {
            idName = v.getResources().getResourceName(clicked.getId());
        }

        Toast.makeText(this, idName + " pressed.", Toast.LENGTH_SHORT).show();
    }

    void acceptDraw(){
        endMatch(match.acceptDraw());
    }

    public void onPromoClick(View v){
        btnPB.setBackgroundColor(Color.GRAY);
        btnPN.setBackgroundColor(Color.GRAY);
        btnPR.setBackgroundColor(Color.GRAY);
        btnPQ.setBackgroundColor(Color.GRAY);
        Button clicked = (Button) v;
        clicked.setBackgroundColor(Color.GREEN);
        if(clicked == btnPB){
            promo = 'B';
        } else if(clicked == btnPR){
            promo = 'R';
        } else if(clicked == btnPN){
            promo = 'N';
        } else {
            promo = 'Q';
        }
    }

    public void onBoardClick(View v){
        ImageButton clicked = (ImageButton) v;
        String idName = v.getResources().getResourceName(clicked.getId());
        idName = idName.substring(idName.length() - 2);
//        Toast.makeText(this, "Space " + idName + " pressed.", Toast.LENGTH_SHORT).show();


        if(selected == null){ // Selecting First Piece to move (getting src)

            // HANDLE PROMOTION
            if(possiblePromos.contains(idName)){
                promoButtons.setVisibility(View.VISIBLE);
                onPromoClick(btnPQ);
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

//                Toast.makeText(getApplicationContext(), sidName + "," + idName + ", " + sendDraw + ", \' \'", Toast.LENGTH_SHORT).show();

                Move tmp = match.executeMove(sidName, idName, sendDraw, promo);
                if(tmp != null){
                    unpack(tmp);
                } else {
                    Toast.makeText(getApplicationContext(),"Null Move", Toast.LENGTH_SHORT).show();
                }

            }


            // Control

            setToBoardColor(selected);
            selected = null;

            // View
            promoButtons.setVisibility(View.INVISIBLE);
            btnUndo.setEnabled(canUndo);
            btnAI.setEnabled(true);

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

    }

    private void endMatch(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        btnUndo.setEnabled(false);
        btnAI.setEnabled(false);
        btnDraw.setEnabled(false);
        btnResign.setEnabled(false);

        Bundle bundle = new Bundle();
        bundle.putString(MessageDialogFragment.MESSAGE_KEY, message);
        DialogFragment displayResult = new MessageDialogFragment();
        displayResult.setArguments(bundle);
        displayResult.setCancelable(false);
        displayResult.show(getFragmentManager(),"badfield");
    }

    private void toggleEnable(Button b){
        b.setEnabled(!b.isEnabled());
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

    private String[][] createBoard() {
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

        return tmpTxtBoard;
    }



    public void createButtonArray(){

        boardBtns = new ImageButton[8][8];

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String id =  "btn"  + String.valueOf(j + 1) + ((char) ('a' + i));
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
