/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.migration;

import java.util.ArrayList;
import java.util.List;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.TypicalBoard;
import put.ai.games.game.moves.MoveMove;

public class MigrationBoard extends TypicalBoard {

    public MigrationBoard(int size) {
        super(size);
        int k = (int) Math.ceil(getSize() / 2.0 - 1);
        for (int x = 0; x < k; ++x) {
            for (int y = x + 1; y < getSize() - x - 1; ++y) {
                state[x][y] = Color.PLAYER1;
                state[y][getSize() - x - 1] = Color.PLAYER2;
            }
        }
    }

    protected MigrationBoard(MigrationBoard other) {
        super(other);
    }

    @Override
    public TypicalBoard clone() {
        return new MigrationBoard(this);
    }

    @Override
    public Color getWinner(Color currentPlayer) {
        if (getMovesFor(currentPlayer).isEmpty()) {
            return Player.getOpponent(currentPlayer);
        }
        return null;
    }

    @Override
    protected boolean canMove(Color color) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doMove(Move move) {
        MoveMove m = (MoveMove) move;
        if (state[m.getSrcX()][m.getSrcY()] != m.getColor()) {
            throw new IllegalArgumentException();
        }
        if (state[m.getDstX()][m.getDstY()] != Color.EMPTY) {
            throw new IllegalArgumentException();
        }
        if (m.getColor() == Color.PLAYER1 && (m.getSrcX() + 1 != m.getDstX() || m.getSrcY() != m.getDstY())) {
            throw new IllegalArgumentException();
        }
        if (m.getColor() == Color.PLAYER2 && (m.getSrcX() != m.getDstX() || m.getSrcY() - 1 != m.getDstY())) {
            throw new IllegalArgumentException();
        }
        state[m.getSrcX()][m.getSrcY()] = Color.EMPTY;
        state[m.getDstX()][m.getDstY()] = m.getColor();
    }

    @Override
    public List<Move> getMovesFor(Color color) {
        int dx = 0, dy = 0;
        switch (color) {
            case PLAYER1:
                dx = 1;
                dy = 0;
                break;
            case PLAYER2:
                dx = 0;
                dy = -1;
                break;
            default:
                throw new IllegalArgumentException();
        }
        assert dx != 0 || dy != 0;
        List<Move> result = new ArrayList<>();
        for (int x = 0; x < getSize(); ++x) {
            for (int y = 0; y < getSize(); ++y) {
                int nx = x + dx;
                int ny = y + dy;
                if (!isValid(nx, ny)) {
                    continue;
                }
                if (state[x][y] == color && state[nx][ny] == Color.EMPTY) {
                    result.add(new MigrationMove(x, y, nx, ny, color));
                }
            }
        }
        return result;
    }

    @Override
    public void undoMove(Move move) {
        MoveMove m = (MoveMove) move;
        state[m.getSrcX()][m.getSrcY()] = m.getColor();
        state[m.getDstX()][m.getDstY()] = Color.EMPTY;
    }

}
