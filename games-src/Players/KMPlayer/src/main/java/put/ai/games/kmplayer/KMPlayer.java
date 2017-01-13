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

import static java.lang.Math.sqrt;

public class KMPlayer extends Player {

    private Random random=new Random(0xdeadbeef);
    private long startTime;

    /**
     * Funcka oceny stanu
     * @param board
     * @return Wartość float im większa tym lepsza nasza sutuacja
     */
    private Float evaluateBoard(Board board){
        Color opponent = this.getOpponent(getColor());
        Integer opponentMoves = board.getMovesFor(opponent).size();
        Integer myMoves = board.getMovesFor(getColor()).size();
        if(opponentMoves==0) return (float) (board.getSize() * board.getSize());
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
            else value = checkMove(board,depth,false);
            if(value>=max){
                max = value;
                selectedMove=m;
            }
        }
        return selectedMove;
    }
    private float checkMove(Board b, int depth, boolean myMove){
        depth--;
        if(depth>0 && System.currentTimeMillis()-startTime > getTime()/2) depth = 0;
        List<Move> moves;
        if(!myMove) moves = b.getMovesFor(getOpponent(this.getColor()));
        else moves = b.getMovesFor(this.getColor());
        if(moves.size()==0 && myMove) return 0;
        if(moves.size()==0 && !myMove) return (float) b.getSize()*b.getSize();

        Float returnValue=0f;
        if(!myMove) returnValue= (float) b.getSize()*b.getSize();
        for(Move m : moves){
            Board board = b.clone();
            board.doMove(m);
            Float value = 0f;
            if(depth==0) value = evaluateBoard(board);
            else value = checkMove(board, depth, !myMove);
            if(myMove && value>=returnValue) returnValue=value;
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
        this.startTime = System.currentTimeMillis();
        int depth=3;
        return selectMove(b, depth);
    }

    public static void main(String args[]){}
}
