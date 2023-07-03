package com.lksnext.parkingmlonbide;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.NavFragments.ProfileFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProfileFragmentTest {

    private ProfileFragment profileFragment;

    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private CollectionReference mockCollectionReference;
    @Mock
    private DocumentReference mockDocumentReference;
    @Mock
    private TextView mockTextViewUsername;
    @Mock
    private TextView mockTextViewEmail;
    @Mock
    private Task mockTask;
    @Mock
    private DocumentSnapshot mockSnapshot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        profileFragment = new ProfileFragment();
    }

    @Test
    public void getUser_withValidUid_setsTextViews() {
    }
}
