package com.example.burhanqurickbot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        if (pdus != null) {
            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = message.getOriginatingAddress();
                String body = message.getMessageBody();

                // Generate a response based on the received message
                String response = generateResponse(body);

                // Send response
                if (response != null) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sender, null, response, null, null);
                }
            }
        }
    }

    private String generateResponse(String input) {
        // Simple response generation logic
        if (input.equalsIgnoreCase("Hello")) {
            return "Hello! How can I help you today?";
        } else if (input.contains("Help")) {
            return "Sure, I'm here to assist you. What do you need help with?";
        } else {
            return "Thank you for your message!";
        }
    }
}
