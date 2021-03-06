package com.crossoverproject.fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crossoverproject.R;
import com.crossoverproject.activity.DoctorActivity;
import com.crossoverproject.adapter.ConferenceAdapter;
import com.crossoverproject.provider.ConferenceContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 0;
    public static final String LOG_TAG = DoctorActivityFragment.class.getSimpleName();

    private ListView listView = null;
    ConferenceAdapter mConferenceAdapter = null;
    int mPosition = ListView.INVALID_POSITION;

    public DoctorActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_doctor_activity, container, false);
        listView = (ListView)view.findViewById(R.id.doctorActivityListView);
        mConferenceAdapter = new ConferenceAdapter(getActivity(), null, 0);
        listView.setAdapter(mConferenceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if(cursor != null)
                {
                    ((Callback) getActivity())
                            .onItemSelected(ConferenceContract.ConferenceEntry
                                    .buildConferenceUri(cursor.getLong(DoctorActivity.COLUMN_CONFERENCE_ID))
                            );
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = ConferenceContract.ConferenceEntry._ID+ " ASC";
        Uri ConferenceUri = ConferenceContract.ConferenceEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), ConferenceUri, DoctorActivity.CONFERENCE_COLUMNS , null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mConferenceAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            listView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mConferenceAdapter.swapCursor(null);
    }

    public interface Callback
    {
        void onItemSelected(Uri uri);
    }
}
