package FireBase;

import Game.Participant;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of FireBase for leaderboard
 */
public class FireBaseService {

    //attribute
    private FirebaseDatabase firebaseDatabase;

    //constructor
    public FireBaseService() {
        FirebaseInitialize firebaseInitialize = new FirebaseInitialize();
        firebaseInitialize.initialize();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    //methods
    public FirebaseDatabase getFirebaseDatabase() {
        return this.firebaseDatabase;
    }

    /**
     * adds person to collection
     * @param person
     * @param collectionPath
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void saveUserDetails(Participant person, String collectionPath) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(collectionPath).document(person.getName()).create(person);
        //ApiFuture<DocumentReference> collections = dbFirestore.collection(collectionPath).add(person);
    }

    /**
     * retrieves collection as list
     * @param collectionPath
     * @return
     */
    public List<Participant> getLeaderboard(String collectionPath) {
        List<Participant> l = new ArrayList<>();
        Firestore fs = FirestoreClient.getFirestore();
        fs.collection(collectionPath).listDocuments().forEach(e -> {
            try {
                Participant p = e.get().get().toObject(Participant.class);
                l.add(p);
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        return l;
    }

    /**
     * deletes person according to name
     * @param collectionPath
     * @param name
     */
    public void deleteParticipant(String collectionPath, String name) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = firestore.collection(collectionPath).document(name).delete();
    }

}
