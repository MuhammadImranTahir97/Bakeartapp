package com.example.bakeart;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView userEmail;
    private Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userEmail = view.findViewById(R.id.userEmail);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        // Load saved session email (or username) from SharedPreferences or static user session
        String email = UserSession.getEmail(); // Custom session manager (you must define this class)

        if (email != null && !email.isEmpty()) {
            userEmail.setText("Logged in as:\n" + email);
        } else {
            userEmail.setText("Guest User");
        }

        logoutBtn.setOnClickListener(v -> {
            UserSession.logout(); // clear session
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish(); // prevent navigating back
        });

        return view;
    }
}
