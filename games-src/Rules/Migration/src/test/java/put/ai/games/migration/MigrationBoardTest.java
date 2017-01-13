/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.migration;

import put.ai.games.migration.MigrationBoard;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import put.ai.games.game.Move;
import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.MoveMove;

public class MigrationBoardTest {

    private Color[][] transpose(Color[][] input) {
        Color[][] result = new Color[input.length][input.length];
        for (int x = 0; x < input.length; ++x) {
            for (int y = 0; y < input.length; ++y) {
                result[x][y] = input[y][x];
            }
        }
        return result;
    }

    @Test
    public void constructorEven() {
        MigrationBoard b = new MigrationBoard(6);
        Color[][] state = Whitebox.getInternalState(b, "state");
        Color[][] expected = transpose(new Color[][]{
            new Color[]{Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.EMPTY, Color.PLAYER2, Color.PLAYER2, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.EMPTY, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.EMPTY}});
        assertTrue(Arrays.deepEquals(expected, state));
    }

    @Test
    public void constructorOdd() {
        MigrationBoard b = new MigrationBoard(7);
        Color[][] state = Whitebox.getInternalState(b, "state");
        Color[][] expected = transpose(new Color[][]{
            new Color[]{Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.PLAYER1, Color.PLAYER1, Color.EMPTY, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.PLAYER1, Color.EMPTY, Color.PLAYER2, Color.EMPTY, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.PLAYER1, Color.EMPTY, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.EMPTY, Color.EMPTY},
            new Color[]{Color.EMPTY, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.PLAYER2, Color.EMPTY}
        });
        System.err.println(b);
        assertTrue(Arrays.deepEquals(expected, state));
    }

    @Test
    public void getMovesFor1() {
        MigrationBoard b = new MigrationBoard(6);
        List<Move> moves = b.getMovesFor(Color.PLAYER1);
        assertEquals(4, moves.size());
        for (Move m : moves) {
            assertTrue(m instanceof MoveMove);
            MoveMove mm = (MoveMove) m;
            assertEquals(Color.PLAYER1, b.getState(mm.getSrcX(), mm.getSrcY()));
            assertEquals(mm.getSrcY(), mm.getDstY());
            assertEquals(mm.getSrcX() + 1, mm.getDstX());
            assertEquals(Color.EMPTY, b.getState(mm.getDstX(), mm.getDstY()));
        }
    }

    @Test
    public void getMovesFor2() {
        MigrationBoard b = new MigrationBoard(6);
        List<Move> moves = b.getMovesFor(Color.PLAYER2);
        assertEquals(4, moves.size());
        for (Move m : moves) {
            assertTrue(m instanceof MoveMove);
            MoveMove mm = (MoveMove) m;
            assertEquals(Color.PLAYER2, b.getState(mm.getSrcX(), mm.getSrcY()));
            assertEquals(mm.getSrcY() - 1, mm.getDstY());
            assertEquals(mm.getSrcX(), mm.getDstX());
            assertEquals(Color.EMPTY, b.getState(mm.getDstX(), mm.getDstY()));
        }
    }
}
