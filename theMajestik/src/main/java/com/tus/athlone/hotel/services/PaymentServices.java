package com.tus.athlone.hotel.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.tus.athlone.hotel.models.BookingDetail;

@Service
public class PaymentServices {
	
	@Autowired
    private APIContext apiContext;
    private static final String CLIENT_ID = System.getenv("CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
    private static final String MODE = "sandbox";
    private static final String SUCCESS_URL = System.getenv("URL") + "success";
    private static final String ERROR_URL = System.getenv("URL") + "error";
    
    public String authorizePayment(BookingDetail bookingDetail)        
            throws PayPalRESTException {       
 
        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectURLs(bookingDetail);
        List<Transaction> listTransaction = getTransactionInformation(bookingDetail);
         
        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");
 
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
 
        Payment approvedPayment = requestPayment.create(apiContext);
 
        return getApprovalLink(approvedPayment);
 
    }
     
    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
         
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("Dhara")
                 .setLastName("Vasnawala")
                 .setEmail("sb-yqxln22206460@business.example.com");
         
        payer.setPayerInfo(payerInfo);
         
        return payer;
    }
     
    private RedirectUrls getRedirectURLs(BookingDetail bookingDetail) {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(ERROR_URL);
        redirectUrls.setReturnUrl(SUCCESS_URL + "/" + bookingDetail.getBookingId() );  
        return redirectUrls;
    }
     
    private List<Transaction> getTransactionInformation(BookingDetail bookingDetail) {
        Details details = new Details();
        details.setSubtotal(bookingDetail.getTotalRoomCost());
        details.setTax(bookingDetail.getTax());
     
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(bookingDetail.getTotal());
        amount.setDetails(details);
     
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(bookingDetail.getRoomType());
         
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();
         
        Item item = new Item();
        item.setCurrency("EUR");
        item.setName(bookingDetail.getRoomType());
        item.setPrice(bookingDetail.getTotalRoomCost());
        item.setTax(bookingDetail.getTax());
        item.setQuantity("1");
         
        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);
     
        List<Transaction> listTransaction = new ArrayList<Transaction>();
        listTransaction.add(transaction);  
         
        return listTransaction;
    }
     
    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
         
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }      
         
        return approvalLink;
    }
//    @Bean
//    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
//        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
//        return Payment.get(apiContext, paymentId);
//    }
    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
     
        Payment payment = new Payment().setId(paymentId);
     
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
     
        return payment.execute(apiContext, paymentExecution);
    }
}
