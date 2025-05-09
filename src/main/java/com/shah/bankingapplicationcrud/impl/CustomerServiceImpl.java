package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.constant.ErrorConstants;
import com.shah.bankingapplicationcrud.exception.MyException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.*;
import com.shah.bankingapplicationcrud.model.response.MyResponse;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import com.shah.bankingapplicationcrud.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.UUID;
import java.util.stream.Stream;

import static com.shah.bankingapplicationcrud.constant.ErrorConstants.*;
import static com.shah.bankingapplicationcrud.model.response.MyResponse.successResponse;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.firstNameLike;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.lastNameLike;
import static java.util.List.of;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Data
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final CustomerRepository repository;

    /**
     * Fetch all customers. If empty will throw exception. Optional query param to search for customer containing by first or last name
     * <p>
     * request headers coming from client
     *
     * @param name  name of field
     * @param page  page number to display
     * @param size  number of items to display in each page
     * @param field the parameter to search from eg by firstName, email, etc
     * @return
     */

    @Override
    public MyResponse<Page<Customer>> getAllCustomersOrSearchByLastAndFirstName(
            HttpHeaders headers, String name, int page, int size, String field) {

        Pageable pageRequest = of(page, size).withSort(by(ASC, field));
        /** TODO
         to test below jpa query
         select * from customers where last_name like '%s%' or first_name like '%s%';
         **/

        if (StringUtils.isNotBlank(name)) {
            log.info("Performing search like by firstname or lastname by keyword: {}", name);
        } else {
            log.info("Getting all customers");
        }

        Page<Customer> customers = repository.findAll(
                where(firstNameLike(name)
                        .or(lastNameLike(name))),
                pageRequest);

        if (customers.stream().findAny().isPresent()) {
            log.info("current customers displayed: {}, total customers found: {}", customers.getSize(), customers.getTotalElements());
            return successResponse(customers);
        }
        throw new MyException(ErrorConstants.CUSTOMER_NOT_FOUND);
    }

    /**
     * Fetch one customer. If not found will throw exception
     *
     * @param request Headers coming from client
     * @return
     */
    @Override
    public MyResponse getOneCustomer(UUID request) {
        log.info("Fetching customer...");

        Customer customer = repository.findById(request).orElseThrow(
                () -> new MyException(CUSTOMER_NOT_FOUND));
        log.info("Fetch customer success...");
        return successResponse(customer);
    }

    /**
     * Create new customer
     *
     * @param request
     * @return
     */

    @Override
    public MyResponse<Customer> createOneCustomer(CreateCustomerRequest request) {
        log.info("Creating one customer...");

        repository.findByEmail(request.getEmail()).ifPresent(i -> {
            throw new MyException(CUSTOMER_EMAIL_ALREADY_EXISTS);
        });
        Customer customer = new Customer();
        copyProperties(request, customer);
        return successResponse(repository.save(customer));
    }

    /**
     * Patch or put customer
     * To edit a single field, simply add that field, we don't need to include rest of the fields
     * To edit all field, simply add all fields.
     *
     * @param request
     * @return
     */

    @Override
    public MyResponse<Customer> updateOneCustomer(PatchCustomerRequest request) {
        log.info("Editing one customer...");

        Customer customer = repository.findById(request.getAccountNumber()).orElseThrow(() -> new MyException(CUSTOMER_NOT_FOUND));
        copyProperties(request, customer, getNullPropertyNames(request));
        return successResponse(repository.save(customer));
    }

    /**
     * Delete customer by id
     *
     * @param request
     * @return
     * @throws MyException
     */

    @Override
    public MyResponse<UUID> deleteOneCustomer(GetOneCustomerRequest request) {
        log.info("Check if customer exists...");

        UUID id = request.getAccountNumber();
        Customer customer = repository.findById(request.getAccountNumber()).orElseThrow(() -> new MyException(CUSTOMER_NOT_FOUND));

        log.info("Customer with account number {} found! Deleting customer...", customer.getAccountNumber());
        repository.deleteById(id);
        return successResponse(id);
    }

    /**
     * To transfer amount from one acc to another
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public MyResponse<TransferResponseDto> transferAmount(TransferRequest request) {
        UUID senderId = request.getPayerAccountNumber();

        // 1. check if payer acc exists
        Customer payer = repository.findById(request.getPayerAccountNumber()).orElseThrow(() -> new MyException(PAYER_NOT_FOUND));
        log.info("Payer found: {}", payer);

        // 2. check if payer bal is more than transfer amount
        if (payer.getAccBalance().compareTo(request.getAmount()) < 0) {
            throw new MyException(INSUFFICIENT_AMOUNT_FOR_PAYER);
        }

        // 2. check if payer account number is same as payee account number
        if (request.getPayeeAccountNumber().compareTo(request.getPayerAccountNumber()) == 0) {
            throw new MyException(PAYER_AND_PAYEE_ACCOUNT_NUMBERS_ARE_THE_SAME);
        }

        // 3. check if payee acc exists
        Customer payee = repository.findById(request.getPayeeAccountNumber()).orElseThrow(() -> new MyException(PAYEE_NOT_FOUND));
        log.info("Payee found: {}", payee);

        // 4. transfer
        payee.setAccBalance(payee.getAccBalance().add(request.getAmount()));
        payer.setAccBalance(payer.getAccBalance().subtract(request.getAmount()));
        log.info("Transfer success");

        // 5. update both accounts to db
        Iterable<Customer> customers = repository.saveAll(of(payee, payer));
        log.info("Customers updated: {}", customers);

        // 6. create object for response
        TransferResponseDto data = TransferResponseDto.builder()
                .payerAccountNumber(senderId)
                .payerFirstName(payer.getFirstName())
                .payerOldAccBal(payer.getAccBalance().add(request.getAmount()))
                .payerNewAccBal(payer.getAccBalance())
                .payeeAccountNumber(request.getPayeeAccountNumber())
                .payeeFirstName(payee.getFirstName())
                .payeeOldAccBal(payee.getAccBalance().subtract(request.getAmount()))
                .payeeNewAccBal(payee.getAccBalance())
                .transactionDate(customers.iterator().next().getUpdatedAt())
                .amountTransferred(request.getAmount())
                .build();

        return successResponse(data);
    }

    /**
     * For ignoring empty fields during copy property
     *
     * @param source
     * @return
     */

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors()).map(FeatureDescriptor::getName).filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null).toArray(String[]::new);
    }
}
