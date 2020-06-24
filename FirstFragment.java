package com.example.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import org.bson.Document;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirstFragment extends Fragment {
    RemoteMongoCollection<Document> remoteMongoCollection;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anonymousLogin();
            }
        });



    }

    /***************************************************************************
     * AUTHENTICATION                                                          *
     ***************************************************************************/
    public void anonymousLogin() {
        // Get the default AppClient
        StitchAppClient stitchClient = Stitch.getDefaultAppClient();

        // Login with Anonymous credentials and handle the result
        stitchClient.getAuth().loginWithCredential(new AnonymousCredential()).addOnCompleteListener(new OnCompleteListener<StitchUser>() {
            @Override
            public void onComplete(@com.mongodb.lang.NonNull final Task<StitchUser> task) {
                if (task.isSuccessful()) {
                    Log.d("login", String.format("successfully logged in with id %s",
                            task.getResult()));
                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);

                } else {
                    Log.d("insert", "failed to login! ", task.getException());
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
