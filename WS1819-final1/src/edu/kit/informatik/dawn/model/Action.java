package edu.kit.informatik.dawn.model;

import edu.kit.informatik.dawn.ui.Command;

/**
 * Die verschiedenen Aktionen des Spiels. NONE falls es vorbei ist.
 * @author Moritz Brödel
 * @version 1.2
 */
enum Action {
    PLACE_ORB,
    ROLL,
    PLACE_PROBE,
    MOVE_ORB,
    NONE;

    private String string;

    Action() {
        string = name(); // takes care of none, which is not a command
        for (Command cmd: Command.values()) {
            if (name().equals(cmd.name())) {
                string = cmd.toString();
            }
        }
    }

    @Override
    public String toString() {
        return string;
    }
}
