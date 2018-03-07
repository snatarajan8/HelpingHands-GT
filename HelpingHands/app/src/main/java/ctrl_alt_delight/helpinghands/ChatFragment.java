package ctrl_alt_delight.helpinghands;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatFragment extends Fragment {

    private EditText inquiry;
    Button submitButton;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        submitButton = container.findViewById(R.id.send_feedback_button);
        inquiry = container.findViewById(R.id.inquiry_text);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inquiryText = inquiry.getText().toString();
                mDatabase.child("inquiries").child("test").setValue(inquiryText);

            }
        });
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

}
