package com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.presentationlayer;

import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.User;
import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.repositories.UserRepository;
import com.champlain.ateliermecaniquews.authenticationsubdomain.utils.security.services.UserDetailsImpl;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.businesslayer.CustomerInvoiceService;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.datalayer.CustomerInvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
//@CrossOrigin("*")
@AllArgsConstructor
public class CustomerInvoiceController {

    final private CustomerInvoiceService customerInvoiceService;
    final private CustomerInvoiceRepository customerInvoiceRepository;
    final private UserRepository userRepository;

    @GetMapping("/invoices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerInvoiceResponseModel>> getAllInvoicesAdmin() {
        List<CustomerInvoiceResponseModel> invoices = customerInvoiceService.getAllInvoices();
        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/customers/{customerId}/invoices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<List<CustomerInvoiceResponseModel>> getAllInvoicesByCustomerId(@PathVariable String customerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            // Check if it's an admin, and go straight to checking if the user exists
            User user = userRepository.findUserByUserIdentifier_UserId(customerId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            // It's a customer, check if the authenticated user's ID matches the path variable
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String authenticatedUserId = userDetails.getUserId();

            if (!authenticatedUserId.equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        List<CustomerInvoiceResponseModel> invoices = customerInvoiceService.getAllInvoicesByCustomerId(customerId);

        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(invoices);
    }


    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/customers/{customerId}/invoices")
    public ResponseEntity<CustomerInvoiceResponseModel> addInvoiceToCustomerAccountCustomer(@PathVariable String customerId, @RequestBody CustomerInvoiceRequestModel customerInvoiceRequestModel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            // Check if it's an admin, and go straight to checking if the user exists
            User user = userRepository.findUserByUserIdentifier_UserId(customerId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            // It's a customer, check if the authenticated user's ID matches the path variable
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String authenticatedUserId = userDetails.getUserId();

            if (!authenticatedUserId.equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        CustomerInvoiceResponseModel invoice = customerInvoiceService.addInvoiceToCustomerAccount(customerId, customerInvoiceRequestModel);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/invoices")
    public ResponseEntity<CustomerInvoiceResponseModel> addInvoiceToCustomerAccountAdmin(@PathVariable String customerId, @RequestBody CustomerInvoiceRequestModel customerInvoiceRequestModel) {
        CustomerInvoiceResponseModel invoice = customerInvoiceService.addInvoiceToCustomerAccount(customerId, customerInvoiceRequestModel);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoice);
    }


    @GetMapping("/invoices/{invoiceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<CustomerInvoiceResponseModel> getInvoiceById(@PathVariable String invoiceId) {

        CustomerInvoiceResponseModel invoice = customerInvoiceService.getInvoiceById(invoiceId);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/invoices/{invoiceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerInvoiceResponseModel> updateCustomerInvoice(@PathVariable String invoiceId, @RequestBody CustomerInvoiceRequestModel customerInvoiceRequestModel) {

        CustomerInvoiceResponseModel invoice = customerInvoiceService.updateCustomerInvoice(invoiceId, customerInvoiceRequestModel);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/invoices/{invoiceId}")
    public ResponseEntity<Void> deleteCustomerInvoiceByCustomerInvoiceIdAdmin(@PathVariable String invoiceId) {
        try {
            customerInvoiceService.deleteInvoiceByInvoiceId(invoiceId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Generated
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @DeleteMapping("/customers/{customerId}/invoices/{invoiceId}")
    public ResponseEntity<Void> deleteCustomerInvoiceByCustomerInvoiceIdCustomer(@PathVariable String invoiceId,@PathVariable String customerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                // Check if it's an admin, and go straight to checking if the user exists
                User user = userRepository.findUserByUserIdentifier_UserId(customerId);

                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                // It's a customer, check if the authenticated user's ID matches the path variable
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String authenticatedUserId = userDetails.getUserId();

                if (!authenticatedUserId.equals(customerId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
            customerInvoiceService.deleteInvoiceByInvoiceId(invoiceId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
