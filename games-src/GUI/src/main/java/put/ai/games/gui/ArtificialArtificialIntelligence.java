/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import javax.swing.JOptionPane;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.MoveMove;
import put.ai.games.game.moves.PlaceMove;
import put.ai.games.game.moves.ShiftMove;

public class ArtificialArtificialIntelligence extends Player implements MouseListener {

    private static class Point {

        private int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }
    private BlockingDeque<Point> clickQueue = new LinkedBlockingDeque<>();

    public ArtificialArtificialIntelligence(BoardViewPanel view) {
        view.addFieldMouseListener(this);
    }

    @Override
    public String getName() {
        return "Sztuczna sztuczna inteligencja";
    }

    @Override
    public Move nextMove(Board board) {
        try {
            clickQueue.clear();
            List<Move> moves = board.getMovesFor(getColor());
            int needs = 0;
            for (Move m : moves) {
                if (m instanceof PlaceMove) {
                    needs = Math.max(needs, 1);
                } else if (m instanceof MoveMove || m instanceof ShiftMove) {
                    needs = Math.max(needs, 2);
                }
            }
            if (needs == 0) {
                return null;
            }
            Point[] points = new Point[needs];
            for (;;) {
                for (int i = 0; i < points.length; ++i) {
                    points[i] = clickQueue.takeFirst();
                }
                for (Move m : moves) {
                    if (m instanceof PlaceMove) {
                        if (match((PlaceMove) m, points[0])) {
                            return m;
                        }
                    } else if (m instanceof MoveMove) {
                        if (match((MoveMove) m, points)) {
                            return m;
                        }
                    } else if (m instanceof ShiftMove) {
                        if (match((ShiftMove) m, points)) {
                            return m;
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Niedozwolony ruch sztucznej sztucznej inteligencji", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (InterruptedException ex) {
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickQueue.add(new Point(e.getX(), e.getY()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private boolean match(PlaceMove pm, Point point) {
        return pm.getX() == point.getX() && pm.getY() == point.getY();
    }

    private boolean match(MoveMove mm, Point[] points) {
        return mm.getSrcX() == points[0].getX() && mm.getSrcY() == points[0].getY()
                && mm.getDstX() == points[1].getX() && mm.getDstY() == points[1].getY();
    }

    private boolean match(ShiftMove m, Point[] points) {
        int dx = points[1].getX() - points[0].getX();
        int dy = points[1].getY() - points[0].getY();
        if (dx != 0 && dy != 0) {
            return false;
        }
        if (dx > 0 && m.getDirection() == ShiftMove.Direction.RIGHT && m.getX() == points[0].getY()) {
            return true;
        }
        if (dx < 0 && m.getDirection() == ShiftMove.Direction.LEFT && m.getX() == points[0].getY()) {
            return true;
        }
        if (dy > 0 && m.getDirection() == ShiftMove.Direction.DOWN && m.getX() == points[0].getX()) {
            return true;
        }
        if (dy < 0 && m.getDirection() == ShiftMove.Direction.UP && m.getX() == points[0].getX()) {
            return true;
        }
        return false;
    }
}
