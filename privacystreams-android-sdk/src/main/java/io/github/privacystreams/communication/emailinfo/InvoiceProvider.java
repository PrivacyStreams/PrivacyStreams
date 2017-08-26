package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.communication.EmailInfoProvider;
import io.github.privacystreams.core.PStreamProvider;


public class InvoiceProvider extends EmailInfoProvider {
    private static final String REQUEST_DOMAIN = "invoice";

    public InvoiceProvider(String api_key, String api_secret){
        super(api_key,api_secret,REQUEST_DOMAIN);
    }

    @Override
    protected void provide() {
        super.provide();
    }

    @Override
    public void isSiftAvailable(JsonNode jsonNode){
        getInvoiceInfo(jsonNode);
    }

    private void getInvoiceInfo(JsonNode jsonNode){
        Invoice invoice = new Invoice();
        invoice.setFieldValue(Invoice.TYPE,jsonNode.get("@type").toString());
        invoice.setFieldValue(Invoice.TOTAL_PAYMENT_DUE,jsonNode.get("totalPaymentDue").toString());
        invoice.setFieldValue(Invoice.URL,jsonNode.get("url").toString());
        invoice.setFieldValue(Invoice.PAYMENT_DUE_DATE,jsonNode.get("paymentDueDate").toString());
        invoice.setFieldValue(Invoice.DESCRIPTION,jsonNode.get("description").toString());
        invoice.setFieldValue(Invoice.ACCOUNT_ID,jsonNode.get("accountId").toString());
        invoice.setFieldValue(Invoice.ACCOUNT_BALANCE,jsonNode.get("x-accountBalance").toString());
        output(invoice);
    }
}
