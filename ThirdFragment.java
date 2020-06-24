package com.example.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

public class ThirdFragment extends Fragment {
    String examine_str, prescribe_str;
    RemoteMongoCollection<Document> coll;
    Details1 data;
    EditText examine,prescribe;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout. fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prescribe = view.findViewById(R.id.prescribe);

        view.findViewById(R.id.go_to_examination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_thirdFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.savee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save1();
            }
        });
    }
    /***************************************************************************
     * INSERTION                                                         *
     ***************************************************************************/
    public void save1() {
        StitchAppClient stitchClient = Stitch.getDefaultAppClient();
        final RemoteMongoClient mongoClient =
                stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Patients_records").getCollection("Recommendation2");

        prescribe_str = prescribe.getText().toString();

        data = new Details1(prescribe_str);
        Document exm1 = new Document()

                .append("prescription",data.getPrescribe());

        final Task<RemoteInsertOneResult> insertTask = coll.insertOne(exm1);
        insertTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("insert", String.format("successfully inserted item with id %s",
                        task.getResult()));
                Toast.makeText(getActivity(), "insertion successful", Toast.LENGTH_LONG).show();

            } else {
                Log.d("insert", "failed to insert document with: ", task.getException());
                Toast.makeText(getActivity(), "insertion Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}
