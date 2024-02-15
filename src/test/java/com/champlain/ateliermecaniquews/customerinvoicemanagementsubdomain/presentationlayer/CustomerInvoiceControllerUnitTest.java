package com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.presentationlayer;

import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Status;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentRequestModel;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentResponseModel;
import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.UserIdentifier;
import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.repositories.UserRepository;
import com.champlain.ateliermecaniquews.authenticationsubdomain.utils.security.services.UserDetailsImpl;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.businesslayer.CustomerInvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class CustomerInvoiceControllerUnitTest {


    @Mock
    private CustomerInvoiceService customerInvoiceService;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private CustomerInvoiceController customerInvoiceController;



    @Test
    void testGetAllInvoicesAdmin(){
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<CustomerInvoiceResponseModel> invoice = List.of(
                CustomerInvoiceResponseModel.builder()
                        .invoiceId("1")
                        .customerId("1")
                        .appointmentId("1")
                        .invoiceDate(LocalDateTime.parse("2021-01-01 12:00", formatter))
                        .mechanicNotes("notes")
                        .sumOfServices(100.0)
                        .build());

        List<CustomerInvoiceResponseModel> invoices = new ArrayList<>();
        invoices.add(invoice.get(0));
        when(customerInvoiceService.getAllInvoices()).thenReturn(invoices);

        ResponseEntity<List<CustomerInvoiceResponseModel>> response = customerInvoiceController.getAllInvoicesAdmin();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoices, response.getBody());
    }


  @Test
  void testGetAllInvoicesAdmin_NotFound() {

        List<CustomerInvoiceResponseModel> invoices = new ArrayList<>();

        when(customerInvoiceService.getAllInvoices()).thenReturn(invoices);

        ResponseEntity<List<CustomerInvoiceResponseModel>> response = customerInvoiceController.getAllInvoicesAdmin();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testAddInvoiceToCustomerAccountAdmin() {
        CustomerInvoiceRequestModel customerInvoiceRequestModel =new CustomerInvoiceRequestModel("123","123", LocalDateTime.now(),"services",100.0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CustomerInvoiceResponseModel invoice = CustomerInvoiceResponseModel.builder()
                        .invoiceId("1")
                        .customerId("1")
                        .appointmentId("1")
                        .invoiceDate(LocalDateTime.parse("2021-01-01 12:00", formatter))
                        .mechanicNotes("notes")
                        .sumOfServices(100.0)
                        .build();

        // Mock the behavior of the appointmentService
        when(customerInvoiceService.addInvoiceToCustomerAccount(anyString(), eq(customerInvoiceRequestModel))).thenReturn(invoice);

        // Call the controller method
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.addInvoiceToCustomerAccountAdmin("456", customerInvoiceRequestModel);

        // Assert the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoice, response.getBody());
    }


    @Test
    void testAddInvoiceToCustomerAccountAdmin_NotFound() {
        CustomerInvoiceRequestModel customerInvoiceRequestModel =new CustomerInvoiceRequestModel("123","123", LocalDateTime.now(),"services",100.0);

        // Mock the behavior of the customerInvoiceService
        when(customerInvoiceService.addInvoiceToCustomerAccount(anyString(), eq(customerInvoiceRequestModel))).thenReturn(null);

        // Call the controller method
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.addInvoiceToCustomerAccountAdmin("456", customerInvoiceRequestModel);
        // Assert the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testGetAllInvoicesByCustomerId_CustomerRole_AuthenticatedUserIdNotMatching() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<List<CustomerInvoiceResponseModel>> response = customerInvoiceController.getAllInvoicesByCustomerId("customerId");

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetAllInvoicesByCustomerId_CustomerRole_AuthenticatedUserIdMatching() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CustomerInvoiceResponseModel invoice = CustomerInvoiceResponseModel.builder()
                .invoiceId("1")
                .customerId("1")
                .appointmentId("1")
                .invoiceDate(LocalDateTime.parse("2021-01-01 12:00", formatter))
                .mechanicNotes("notes")
                .sumOfServices(100.0)
                .build();

        List<CustomerInvoiceResponseModel> invoices = new ArrayList<>();
        invoices.add(invoice);

        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(customerInvoiceService.getAllInvoicesByCustomerId(any())).thenReturn(invoices);
        // Act
        ResponseEntity<List<CustomerInvoiceResponseModel>> response = customerInvoiceController.getAllInvoicesByCustomerId(userDetails.getUserId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // You can add more assertions based on the specific behavior of the method
    }

    @Test
    void testGetAllInvoicesByCustomerId_CustomerRole_NotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(customerInvoiceService.getAllInvoicesByCustomerId(anyString())).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<List<CustomerInvoiceResponseModel>> response = customerInvoiceController.getAllInvoicesByCustomerId(userDetails.getUserId());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testAddAppointmentToCustomerAccountCustomer_CustomerRole_AuthenticatedUserIdNotMatching() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        CustomerInvoiceRequestModel invoiceRequestModel =new CustomerInvoiceRequestModel("123","123",LocalDateTime.now(),"services",100.0);


        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.addInvoiceToCustomerAccountCustomer("customerId", invoiceRequestModel);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testAddInvoiceToCustomerAccountCustomer_CustomerRole_AuthenticatedUserIdMatching() {
        // Arrange

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CustomerInvoiceResponseModel invoice = CustomerInvoiceResponseModel.builder()
                .invoiceId("1")
                .customerId("1")
                .appointmentId("1")
                .invoiceDate(LocalDateTime.parse("2021-01-01 12:00", formatter))
                .mechanicNotes("notes")
                .sumOfServices(100.0)
                .build();

        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        CustomerInvoiceRequestModel invoiceRequestModel =new CustomerInvoiceRequestModel("123","123",LocalDateTime.now(),"services",100.0);


        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(customerInvoiceService.addInvoiceToCustomerAccount(any(),any())).thenReturn(invoice);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.addInvoiceToCustomerAccountCustomer(userDetails.getUserId(),invoiceRequestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddInvoiceToCustomerAccountCustomer_CustomerRole_NotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        CustomerInvoiceRequestModel invoiceRequestModel =new CustomerInvoiceRequestModel("123","123",LocalDateTime.now(),"services",100.0);


        UserDetailsImpl userDetails = new UserDetailsImpl(1, new UserIdentifier().getUserId(), "John", "Vegas", "444", "john@email.com", "pass", authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(customerInvoiceService.addInvoiceToCustomerAccount(any(),any())).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.addInvoiceToCustomerAccountCustomer(userDetails.getUserId(),invoiceRequestModel);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetInvoiceById_Found_AdminRole() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CustomerInvoiceResponseModel invoice = CustomerInvoiceResponseModel.builder()
                .invoiceId("1")
                .customerId("1")
                .appointmentId("1")
                .invoiceDate(LocalDateTime.parse("2021-01-01 12:00", formatter))
                .mechanicNotes("notes")
                .sumOfServices(100.0)
                .build();

        when(customerInvoiceService.getInvoiceById("1")).thenReturn(invoice);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.getInvoiceById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoice, response.getBody());
    }


    @Test
    void testGetInvoiceById_NotFound() {
        // Arrange
        when(customerInvoiceService.getInvoiceById("nonExistentId")).thenReturn(null);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.getInvoiceById("nonExistentId");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testUpdateCustomerInvoice_Found() {
        // Arrange
        CustomerInvoiceRequestModel requestModel = new CustomerInvoiceRequestModel("123", "456", LocalDateTime.now(), "Updated notes", 200.0);

        CustomerInvoiceResponseModel updatedInvoice = CustomerInvoiceResponseModel.builder()
                .invoiceId("1")
                .customerId("123")
                .appointmentId("456")
                .invoiceDate(LocalDateTime.now())
                .mechanicNotes("Updated notes")
                .sumOfServices(200.0)
                .build();

        when(customerInvoiceService.updateCustomerInvoice(eq("1"), any(CustomerInvoiceRequestModel.class))).thenReturn(updatedInvoice);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.updateCustomerInvoice("1", requestModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedInvoice, response.getBody());
    }


    @Test
    void testUpdateCustomerInvoice_NotFound() {
        // Arrange
        CustomerInvoiceRequestModel requestModel = new CustomerInvoiceRequestModel("123", "456", LocalDateTime.now(), "Updated notes", 200.0);

        when(customerInvoiceService.updateCustomerInvoice(eq("nonExistentId"), any(CustomerInvoiceRequestModel.class))).thenReturn(null);

        // Act
        ResponseEntity<CustomerInvoiceResponseModel> response = customerInvoiceController.updateCustomerInvoice("nonExistentId", requestModel);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testDeleteCustomerInvoiceByCustomerInvoiceIdAdmin_Success() {
        // Arrange
        doNothing().when(customerInvoiceService).deleteInvoiceByInvoiceId("1");

        // Act
        ResponseEntity<Void> response = customerInvoiceController.deleteCustomerInvoiceByCustomerInvoiceIdAdmin("1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    void testDeleteCustomerInvoiceByCustomerInvoiceIdAdmin_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Invoice not found")).when(customerInvoiceService).deleteInvoiceByInvoiceId("nonExistentId");

        // Act
        ResponseEntity<Void> response = customerInvoiceController.deleteCustomerInvoiceByCustomerInvoiceIdAdmin("nonExistentId");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteInvoiceByInvoiceIdCustomer_CustomerRole_AuthenticatedUserIdNotMatching() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(1,new UserIdentifier().getUserId(),"John","Vegas","444","john@email.com","pass",authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<Void> response = customerInvoiceController.deleteCustomerInvoiceByCustomerInvoiceIdCustomer("appointmentId", "customerId");

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    void testDeleteInvoiceByInvoiceIdCustomer_CustomerRole_AuthenticatedUserIdMatching() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(1,new UserIdentifier().getUserId(),"John","Vegas","444","john@email.com","pass",authorities);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        ResponseEntity<Void> response = customerInvoiceController.deleteCustomerInvoiceByCustomerInvoiceIdCustomer("appointmentId", userDetails.getUserId());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerInvoiceService, times(1)).deleteInvoiceByInvoiceId(anyString());
    }


}

