/*package main.java.com.example.hams;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInitialization {

    public static void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("project-project-group-20\\hams-group-20-firebase-adminsdk-z437x-67989a5c39.json\""); 
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://hams-group-20-default-rtdb.firebaseio.com/") 
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

 */