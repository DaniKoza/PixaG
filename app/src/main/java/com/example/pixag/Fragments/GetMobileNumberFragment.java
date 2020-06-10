 package com.example.pixag.Fragments;

 import android.net.Uri;
 import android.os.Bundle;
 import android.view.KeyEvent;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;

 import com.example.pixag.Activities.LoginActivity;
 import com.example.pixag.R;

 import androidx.fragment.app.Fragment;


 /**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetMobileNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetMobileNumberFragment extends Fragment {
     private EditText login_EDT_phone;
     private Button login_BTN_enter;
     private TextView login_LBL_continue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GetMobileNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetMobileNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetMobileNumberFragment newInstance(String param1, String param2) {
        GetMobileNumberFragment fragment = new GetMobileNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_get_mobile_number, container, false);
        login_EDT_phone = view.findViewById(R.id.login_EDT_phone);
        login_BTN_enter = view.findViewById(R.id.login_BTN_enter);
        login_LBL_continue = view.findViewById(R.id.login_LBL_continue);

        login_BTN_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = login_EDT_phone.getText().toString().trim();

                if (phoneNum.length() != 10) {
                    login_EDT_phone.setError("Please enter valid phone number");
                    login_EDT_phone.requestFocus();
                    return;
                }

                // Go to second fragment
                LoginActivity la = (LoginActivity) getActivity();
                la.LoadVerifyPhoneFragment(phoneNum);
            }

        });
        login_EDT_phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        login_BTN_enter.performClick();
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }


     public interface onGetMobileNumberFragmentInteractionListener {
         // TODO: Update argument type and name
         void onGetMobileNumberFragmentInteraction(Uri uri);
     }
}