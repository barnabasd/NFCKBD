package hu.balintq.nfckbd;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        info.flags =
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        // Itt lehet kezelni az accessibility eseményeket
    }

    @Override
    public void onInterrupt() {
        // Itt lehet kezelni az interrupt eseményeket
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        inputText(intent.getStringExtra("input_text"));
        return super.onStartCommand(intent, flags, startId);
    }

    private void inputText(String text) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;
        AccessibilityNodeInfo editTextNode = findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        editTextNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
        Log.d("AccessibilityService", "Input text: " + text);
    }

    @Override
    public AccessibilityNodeInfo findFocus(int focus) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return null;
        return super.findFocus(focus);
    }
}
