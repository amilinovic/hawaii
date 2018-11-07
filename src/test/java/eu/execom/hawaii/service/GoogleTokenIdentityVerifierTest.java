package eu.execom.hawaii.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class) public class GoogleTokenIdentityVerifierTest {
    private static final String SAMPLE_ID_TOKEN = "-- id token --";

    @Mock private GoogleIdTokenVerifier googleVerifier;

    @InjectMocks private GoogleTokenIdentityVerifier googleTokenIdentityVerifier;

    @Test public void shouldReturnEmptyTokenIdentityWhenGoogleVerifierThrowsException()
            throws GeneralSecurityException, IOException {
        given(googleVerifier.verify(SAMPLE_ID_TOKEN)).willThrow();

        Optional<String> tokenIdentity = googleTokenIdentityVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN);

        assertThat(tokenIdentity.isPresent(), is(false));
    }

    @Test public void shouldReturnEmptyTokenIdentityWhenGoogleIdTokenIsNull() {
        Optional<String> tokenIdentity = googleTokenIdentityVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN);

        assertThat(tokenIdentity.isPresent(), is(false));
    }
}