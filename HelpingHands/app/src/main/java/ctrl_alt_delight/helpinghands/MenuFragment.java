package ctrl_alt_delight.helpinghands;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Fragment class for each nav menu item
 */
public class MenuFragment extends Fragment {
    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_COLOR = "arg_color";

    private String mText;
    private int mColor;

    private View mContent;
    private TextView mTextView;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public static Fragment newInstance(String text, int color) {
        Fragment frag = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_COLOR, color);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tmp = inflater.inflate(R.layout.fragment_menu, container, false);
        /*container.removeView(tmp.findViewById(R.id.message1));
        container.removeView(tmp.findViewById(R.id.message2));
        container.get*/
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            if (v.getId() == R.id.message1 || v.getId() == R.id.message2) {
                v.setVisibility(View.GONE);
            }
        }
        return tmp;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mText = args.getString(ARG_TEXT);
            mColor = args.getInt(ARG_COLOR);
        } else {
            mText = savedInstanceState.getString(ARG_TEXT);
            mColor = savedInstanceState.getInt(ARG_COLOR);
        }

        mTextView = (TextView) view.findViewById(R.id.text);

        // initialize views
        if (!mText.equals("Chat")) {
            mContent = view.findViewById(R.id.fragment_content);
        } else {
            ViewGroup container = (ViewGroup) view;
            for (int i = 0; i < container.getChildCount(); i++) {
                View v = container.getChildAt(i);
                if (v.getId() == R.id.message1 || v.getId() == R.id.message2) {
                    v.setVisibility(View.GONE);
                }
            }


            mContent = view.findViewById(R.id.ButtonSendFeedback);
            mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText inquiry = view.findViewById(R.id.inquiry_text);
                    String inquiryText = inquiry.getText().toString();
                    mDatabase.child("inquiries").child("test").setValue(inquiryText);
                }
            });
        }

        // set text and background color
        mTextView.setText(mText);
        //mContent.setBackgroundColor(mColor);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_TEXT, mText);
        outState.putInt(ARG_COLOR, mColor);
        super.onSaveInstanceState(outState);
    }

    public void sendFeedback(View view) {
        mTextView.setText("yeeeeet");
        final EditText inquiry = view.findViewById(R.id.inquiry_text);
        String inquiryText = inquiry.getText().toString();

        mDatabase.child("inquiries").child("test").setValue(inquiryText);

    }
}