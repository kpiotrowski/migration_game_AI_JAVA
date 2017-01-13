package secondd.ai.games.myplayer;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class MyPlayer extends Player {

    private Random random = new Random(System.currentTimeMillis());
    
    /**
     * class made for, and used by tree searching algorithms ( min-max, AB )
     * @author Witold Janik 106637 Marek Szawioła 106517
     *
     */
    private class MoveValueTree {
    	
        public int value;
        public Move move; 
        
        public MoveValueTree() {
            this.value = 0;
        }

        public MoveValueTree(int value) {
            this.value = value;
        }
        
        public MoveValueTree(Move move, int value) {
            this.move = move;
            this.value = value;
        }
    }
    
    @Override
    public String getName() {
        return "Witold Janik 106637 Marek Szawioła 106517";
    }
    
    /**
     * heuristic function to evaluate board state's "value"
     * @param board : Board
     * @return integer
     */
    private int evaluateBoard(Board board) {
        int n = 0;
        List<Move> moves = board.getMovesFor(getColor());
        Iterator<Move> movesIterator = moves.iterator();
        boolean p1wonField = (board.getState(board.getSize()-1, board.getSize()-1) == Color.PLAYER1);
        boolean p2wonField = (board.getState(0,0) == Color.PLAYER2);
        // if next move == win for each player
        if (p1wonField == true) {
        	n += 20;	// 4*4 == 16
        }
        if (p2wonField == true) {
        	n -= 20;
        }
        // loop checking block
        for (int i = 0; i < board.getSize(); ++i)
        {
            for (int j = 0; j < board.getSize(); ++j)
            {
                // counting # of each player checkers
            	if (board.getState(i,j) == Color.PLAYER1)
                {
                    ++n;
                } else if (board.getState(i,j) == Color.PLAYER2) {
                	--n;
                }
            	//
                // if have beat in next move
//                while (movesIterator.hasNext()) {
//                	Move movex = movesIterator.next();
//                	if (board.getState(i, j) == getOpponent(getColor()) //&&
//                			/* mozna bic i,j przeciwnika */) {
//                		int z = 0;
//                	}
//                } 
            }
        }
        return n;
    }
    
    /**
     * min-max algorithm for tree searching
     * @param node is actual processed node
     * @param depth setting max depth of searching
     * @param player actual player, just use getColor()
     * @param board actual board state
     * @return private MoveValueTree
     */
    private MoveValueTree minMax(Move node, int depth, Color player, Board board) {
    	
    	List<Move> moves = board.getMovesFor(player);
        
    	// on the leaf
    	if (depth == 0 || moves.isEmpty()) {
        	int v_heuristic = evaluateBoard(board);
            return new MoveValueTree(node,v_heuristic);
        }
    	
    	// not leaf - do below
        Iterator<Move> movesIterator = moves.iterator();
        boolean isMaximizer = (player.equals(Color.PLAYER1));
        MoveValueTree ret = new MoveValueTree();
        
        if(isMaximizer) {
        	int bestValue = Integer.MIN_VALUE;
        	while (movesIterator.hasNext()) {
        		Move child = movesIterator.next();
        		Board b = board.clone();
        		b.doMove(child);
        		MoveValueTree child_node = minMax(child, depth - 1, getOpponent(player), b);
        		b.undoMove(child); 
        		if(child_node.value >= bestValue) {// taking max(oldValue,newValue
        			bestValue = child_node.value;
        			ret.move = child; 
        			ret.value = bestValue;
        		}
        	}
        	return ret;
        } else {
        	int bestValue = Integer.MAX_VALUE;
        	while (movesIterator.hasNext()) {
        		Move child = movesIterator.next();
        		Board b = board.clone();
        		b.doMove(child);
        		MoveValueTree child_node = minMax(child, depth - 1, getOpponent(player), b);
        		b.undoMove(child);
        		if(child_node.value <= bestValue) {// taking min(oldValue,newValue
        			bestValue = child_node.value;
        			ret.move = child; 
        			ret.value = bestValue;
        		}
        	}
        	return ret;
        }
    }
    
    /**
     * alpha-beta algorithm for tree searching
     * @param node is actual processed node
     * @param depth setting max depth of searching
     * @param player actual player, just use getColor()
     * @param board actual board state
     * @return private MoveValueTree
     */
    private MoveValueTree alphaBeta(Move node, int depth, int alpha, int beta, Color player, Board board) {
    	
    	List<Move> moves = board.getMovesFor(player);
        
    	// on the leaf
    	if (depth == 0 || moves.isEmpty()) {
        	int v_heuristic = evaluateBoard(board);
            return new MoveValueTree(node,v_heuristic);
        }
    	
    	// not leaf - do below
        Iterator<Move> movesIterator = moves.iterator();
        boolean isMaximizer = (player.equals(Color.PLAYER1));
        MoveValueTree ret = new MoveValueTree();
        
        if(isMaximizer) {
        	int bestValue = Integer.MIN_VALUE;
        	while (movesIterator.hasNext()) {
        		Move child = movesIterator.next();
        		Board b = board.clone();
        		b.doMove(child);
        		MoveValueTree child_node = alphaBeta(child, depth - 1, alpha, beta, getOpponent(player), b);
        		b.undoMove(child); 
        		if(child_node.value >= bestValue) { // taking max(oldValue,newValue)
        			bestValue = child_node.value;
        			ret.move = child; 
        			ret.value = bestValue;
        		}
        		alpha = Integer.max(alpha, bestValue);
        		if(beta <= alpha) {	// (* β cut-off *)
        			break;
        		}
        			
        	}
        	return ret;
        } else {
        	int bestValue = Integer.MAX_VALUE;
        	while (movesIterator.hasNext()) {
        		Move child = movesIterator.next();
        		Board b = board.clone();
        		b.doMove(child);
        		MoveValueTree child_node = alphaBeta(child, depth - 1, alpha, beta, getOpponent(player), b);
        		b.undoMove(child);
        		if(child_node.value <= bestValue) { // taking min(oldValue,newValue) 
        			bestValue = child_node.value;
        			ret.move = child; 
        			ret.value = bestValue;
        		}
        		beta = Integer.min(beta, bestValue);
        		if(beta <= alpha) { // (* α cut-off *)
        			break;
        		}
        	}
        	return ret;
        }
    }
    
    /**
     * implementation of function nextMove by out team 
     * <p>
     * comment/uncomment blocks depending what algorithm you want to use, available:
     * - naive
     * - minmax
     * - alphabeta
     */
    @Override
    public Move nextMove(Board b) {
    	
    	MoveValueTree bestNode = null;
    	Move bestMove = null;
    	
    	/* list of moves at the beginning */
    	List<Move> moves = b.getMovesFor(getColor());
    	
    	/* set depth */
    	int depth = 3;
		
    	/* minimax below */
    	bestNode = minMax(
    					moves.get(random.nextInt(moves.size())), depth, 
    					getColor(), b);
    	
    	/* alphabeta below */
//    	bestNode = alphaBeta(
//    					moves.get(random.nextInt(moves.size())), depth,
//    					Integer.MIN_VALUE, Integer.MAX_VALUE,
//    					getColor(), b);
		
    	/* naive below */
//    	List<Move> moves = b.getMovesFor(getColor());
//        return moves.get(random.nextInt(moves.size()));
    	
    	/* HARD-CODED */
//    	Color which = getColor();
//    	if (which == Color.PLAYER1) {
//    		bestNode = alphaBeta(
//					moves.get(random.nextInt(moves.size())), depth,
//					Integer.MIN_VALUE, Integer.MAX_VALUE,
//					getColor(), b);
//    	} else if (which == Color.PLAYER2) {
//    		bestNode = minMax(
//					moves.get(random.nextInt(moves.size())), depth, 
//					getColor(), b);
//    	}
    	
		/* return: best value possible */
    	bestMove = bestNode.move;
		return bestMove;
    }
    
    /**
     * function required for JAR making in Eclipse with "export" option
     * @param args
     */
    public static void main(String [] args) {
    	// nothing
    }
}