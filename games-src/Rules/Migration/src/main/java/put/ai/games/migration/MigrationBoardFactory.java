/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.migration;

import put.ai.games.engine.UniversalBoardFactory;

public class MigrationBoardFactory extends UniversalBoardFactory {

    public MigrationBoardFactory() throws NoSuchMethodException {
        super(MigrationBoard.class, "Migration", "http://homepages.di.fc.ul.pt/~jpn/gv/migration.htm");
    }
}
