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

public class SecondFragment extends Fragment {
    String examine_str, prescribe_str;
    RemoteMongoCollection<Document> coll;
    Details data;
    EditText examine,prescribe;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        examine = view.findViewById(R.id.examine);


        view.findViewById(R.id.go_to_prescription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_thirdFragment);
            }
        });
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save();
            }
        });
    }
    /***************************************************************************
     * INSERTION                                                         *
     ***************************************************************************/
    public void save() {
        StitchAppClient stitchClient = Stitch.getDefaultAppClient();
        final RemoteMongoClient mongoClient =
                stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Patients_records").getCollection("Examination");

        examine_str = examine.getText().toString();


        data = new Details(examine_str);
        Document exm = new Document()
                .append("examination", data.getExamine());


        final Task<RemoteInsertOneResult> insertTask = coll.insertOne(exm);
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
