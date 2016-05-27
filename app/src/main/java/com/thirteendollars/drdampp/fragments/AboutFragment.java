package com.thirteendollars.drdampp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thirteendollars.drdampp.R;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class AboutFragment extends Fragment {

    public static final String FRAGMENT_TAG="AboutFragmentTag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_layout,container,false);
        TextView githubLink = (TextView) view.findViewById(R.id.about_github_link);
        githubLink.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
