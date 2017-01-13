/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.migration;

import put.ai.games.game.Player;
import put.ai.games.game.moves.impl.MoveMoveImpl;

public class MigrationMove extends MoveMoveImpl {

    public MigrationMove(int x1, int y1, int x2, int y2, Player.Color color) {
        super(x1, y1, x2, y2, color);
    }

}
