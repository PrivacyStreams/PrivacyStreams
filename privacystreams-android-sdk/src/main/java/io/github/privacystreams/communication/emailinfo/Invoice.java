package io.github.privacystreams.communication.emailinfo;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;


public class Invoice extends Item {


    public static PStreamProvider getInvoices(String api_key, String api_secret) {
        return new InvoiceProvider(api_key, api_secret);
    }

    //TODO delete this method when debug ends
    public static PStreamProvider getInvoices(String api_key, String api_secret, String userName) {
        return new InvoiceProvider(api_key, api_secret, userName);
    }

    /*fields*/
    @PSItemField(type = String.class)
    public static final String TYPE = "@type";

    @PSItemField(type = JsonNode.class)
    public static final String TOTAL_PAYMENT_DUE = "total_payment_due";

    @PSItemField(type = String.class)
    public static final String URL = "url";

    @PSItemField(type = String.class)
    public static final String PAYMENT_DUE_DATE = "payment_due_date";

    @PSItemField(type = String.class)
    public static final String DESCRIPTION = "description";

    @PSItemField(type = String.class)
    public static final String ACCOUNT_ID = "account_id";

    @PSItemField(type = String.class)
    public static final String ACCOUNT_BALANCE = "account_balance";

}
