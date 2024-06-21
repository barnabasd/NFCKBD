package hu.balintq.nfckbd;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private PendingIntent pendingIntent = null; // TODO ez nincs használva...
    private NfcAdapter nfcAdapter = null;
    private EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        // Initialize the nfc adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Create PendingIntent for nfc events
        Intent intent = new Intent(this, this.getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable NFC foreground dispatch
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Handle NFC intent
        if (Objects.equals(intent.getAction(), NfcAdapter.ACTION_NDEF_DISCOVERED))
            processNfcIntent(intent);
    }

    private void processNfcIntent(@NonNull Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages == null) return;

        List<NdefMessage> messages = new ArrayList<>();

        // Convert each object in list to NdefMessage, then add them to the list
        // It's actually pretty simple even tho it looks complicated
        Arrays.stream(rawMessages).map(x -> (NdefMessage)x).forEach(messages::add);

        if (messages.isEmpty()) return;

        NdefRecord record = messages.get(0).getRecords()[0];
        byte[] payload = record.getPayload();
        String payloadText = Arrays.toString(payload);

        editText.append(payloadText);

        if (payloadText.endsWith("/n"))
            editText.append("\n");
    }
}