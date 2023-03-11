package com.fido.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fido.app.entity.Invoice;
import com.fido.app.model.CartProducts;
import com.fido.app.services.AuthDetail;
import com.fido.app.services.CardService;
import com.fido.app.services.InvoiceGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class InvoiceController {
	
	@Autowired
	 private InvoiceGenerator invoiceGenerator;
	
	@Autowired
	private AuthDetail authDetail; 
	
	@Autowired
	private CardService cardService;
	
	
	
	@PostMapping("/buyProduct/{cid}/{cardId}")
	public long getProductFromCart(@PathVariable("cid") long customerId,@RequestBody List<CartProducts> products,@PathVariable long cardId) throws Exception {
		log.info(products.toString());
		if(products.isEmpty()) throw new Exception("No product is selected");
		var vendor =authDetail.getVendorDetail();
		var customer= authDetail.getCustomerDetailsById(customerId);
		var card= cardService.getCardByCustomerIdAndCardId(customerId,cardId);
		log.info(card.toString());
		 
		if(!card.isActivate()) throw new Exception("Card is not activated");
		 
		var invoice=invoiceGenerator.getInvoiceData(customer, vendor, products, card);
		
		log.info(invoice.toString());
		
		return invoice.getInvoiceId();
	}
	
	
	
	
	@GetMapping("/invoice/{id}")
	public Invoice getInvoiceById(@PathVariable long id) {
		return invoiceGenerator.getInvoiceById(id);
	}
	
	

}