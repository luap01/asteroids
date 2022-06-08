package Game;

import FireBase.FireBaseService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LeaderboardControl {

    public static String COLLECTION_PATH = "participants";
    private final FireBaseService fireBaseService;

    public LeaderboardControl() {
        this.fireBaseService = new FireBaseService();
    }

    //methods

    /**
     * Adds a Participant to Firebase database
     * @param part
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addParticipant(Participant part) throws ExecutionException, InterruptedException {
        fireBaseService.saveUserDetails(part, COLLECTION_PATH);
    }

    /**
     * deletes partcipant by name
     * @param name
     */
    public void deleteParticipant(String name) {
        fireBaseService.deleteParticipant(COLLECTION_PATH, name);
    }

    /**
     * returns leaderboard list (with correct place)
     * @return
     */
    public List<Participant> getSortedList() {
        List<Participant> l = getParticipants();
        return sortList(l);
    }

    //Helper methods
    private List<Participant> getParticipants() {
        return fireBaseService.getLeaderboard(COLLECTION_PATH);
    }

    private List<Participant> sortList(List<Participant> list) {
        ArrayList<Participant> x = (ArrayList<Participant>) list
                .stream()
                .sorted(Comparator.comparingInt(Participant::getHighscore))
                .collect(Collectors.toList());
        for (int i = 0; i < x.size(); i++) {
            x.get(i).setPlace(i);
        }
        return x;
    }
}
