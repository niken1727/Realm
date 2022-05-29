package com.niken.assignment.ui.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.niken.assignment.R;
import com.niken.assignment.component.FilterBottomSheet;
import com.niken.assignment.model.ClickListener;
import com.niken.assignment.model.User;
import com.niken.assignment.model.UserListCallBack;
import com.niken.assignment.ui.user.adapter.UserRecyclerAdapter;
import com.niken.assignment.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {
    private static final String TAG = "UserListFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<User> userArrayList = new ArrayList<>();
    
    //adapter
    private UserRecyclerAdapter userRecyclerAdapter;

    //viewmodel
    private UserViewModel userViewModel;

    //view
    private FloatingActionButton btnFab;
    private LinearLayout noUserFoundLayout;
    private RecyclerView userRecyclerView;


    public UserListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserListFragment newInstance(String param1, String param2) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter_button) {
            FilterBottomSheet filterBottomSheet = new FilterBottomSheet();
            filterBottomSheet.setOnClickListener(new ClickListener() {
                @Override
                public void onClicked(String fromDate, String toDate) {
                    userViewModel.filterUserList(fromDate, toDate, new UserListCallBack() {
                        @Override
                        public void onUserListRetrieved(List<User> userList) {
                            userArrayList.clear();
                            userArrayList.addAll(userList);
                            setViewVisibility();
                        }
                    });
                }

                @Override
                public void onReset() {
                    getUserList();
                }

            });
            filterBottomSheet.show(getActivity().getSupportFragmentManager(), TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViewVisibility() {
        if(userArrayList.size() > 0) {
            userRecyclerView.setVisibility(View.VISIBLE);
            noUserFoundLayout.setVisibility(View.GONE);
            userRecyclerAdapter.notifyDataSetChanged();
        } else {
            userRecyclerView.setVisibility(View.GONE);
            noUserFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initViews(view);
        initRecyclerView();
        getUserList();
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(btnFab).navigate(R.id.action_userListFragment_to_createUserFragment);
            }
        });

    }

    private void initRecyclerView() {
        userRecyclerAdapter = new UserRecyclerAdapter(getContext(), userArrayList);
        userRecyclerView.setAdapter(userRecyclerAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getUserList() {
        userViewModel.retrieveUserList(new UserListCallBack() {
            @Override
            public void onUserListRetrieved(List<User> userList) {
                userArrayList.clear();
                userArrayList.addAll(userList);
                setViewVisibility();
            }
        });
    }

    private void initViewModel() {
        userViewModel =  new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initViews(View view) {
        btnFab = view.findViewById(R.id.fab_button);
        noUserFoundLayout = view.findViewById(R.id.no_user_found_layout);
        userRecyclerView = view.findViewById(R.id.user_list_recycler_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }
}