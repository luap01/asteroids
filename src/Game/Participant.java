package Game;

/**
 * Class needed for Leaderboard
 */
public class Participant {
    private String name;
    private int highscore;
    private int place;


    public Participant(String name, int highscore) {
        this.name = name;
        this.highscore = highscore;
    }

    public Participant() {
        //needed for Firebase
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getPlace() {
        return this.place;
    }
}
