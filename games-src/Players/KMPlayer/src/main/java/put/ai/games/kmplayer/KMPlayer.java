/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kmplayer;

import java.util.List;
import java.util.Random;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class KMPlayer extends Player {

    private long startTime;
    private int maxDepth=5;
    private float maxValue;
    private boolean stop;

    /**
     * Funcka oceny stanu
     * @param board plansza
     * @return Wartość float im większa tym lepsza nasza sutuacja
     */
    private Float evaluateBoard(Board board){
        Color opponent = getOpponent(getColor());
        Integer opponentMoves = board.getMovesFor(opponent).size();
        Integer myMoves = board.getMovesFor(getColor()).size();
        if(opponentMoves==0) return this.maxValue;
        return (float) (myMoves) / opponentMoves;
    }

    private Move selectMove(Board b, int depth){
        float max = 0;
        Move selectedMove = null;
        for(Move m :b.getMovesFor(this.getColor())){
            Board board = b.clone();
            board.doMove(m);
            Float value;
            if(depth==0) value = evaluateBoard(board);
            else {
                value = checkMove(board,depth,false, null);
                if(value==null || this.stop) {
                    depth=0;
                    value = evaluateBoard(board);
                }
            }
            if(value>=max){
                max = value;
                selectedMove=m;
            }
        }
        return selectedMove;
    }

    private Float checkMove(Board b, int depth, boolean myMove, Float cutValue){
        depth--;
        if(depth>0 && System.currentTimeMillis()-startTime > getTime()/2) {
            this.maxDepth--;
            this.stop = true;
            return null;
        }
        List<Move> moves;
        if(!myMove) moves = b.getMovesFor(getOpponent(this.getColor()));
        else moves = b.getMovesFor(this.getColor());
        if(moves.size()==0 && myMove) return 0f;
        if(moves.size()==0 && !myMove) return this.maxValue;

        Float returnValue=0f;
        if(!myMove) returnValue = this.maxValue;
        for(Move m : moves){
            Board board = b.clone();
            board.doMove(m);
            Float value = 0f;
            if(depth==0) value = evaluateBoard(board);
            else {
                value = checkMove(board, depth, !myMove, returnValue);
                if(value==null || this.stop) {
                    depth=0;
                    value = evaluateBoard(board);
                }
            }
            if(cutValue!=null){ //odcięcia
                if(myMove && value>=cutValue) return cutValue; //odcięcie alfa
                if(!myMove && value<=cutValue) return cutValue; //odcięcie beta
            }
            if(myMove && value>=returnValue) {
                //if(value == this.maxValue) return this.maxValue;
                returnValue=value;
            }
            if(!myMove && value<=returnValue) returnValue=value;
        }
        return returnValue;
    }

    @Override
    public String getName() {
        return "Kamil Piotrowski 122491 Michał Lewiński 122505";
    }

    @Override
    public Move nextMove(Board b) {
        stop=false;
        this.maxValue = b.getSize()*b.getSize();
        this.startTime = System.currentTimeMillis();
        Move move = selectMove(b, this.maxDepth);
        if( (double)(System.currentTimeMillis()-startTime) < getTime()/2) this.maxDepth++;
        return move;
    }

    public static void main(String args[]){}
}
