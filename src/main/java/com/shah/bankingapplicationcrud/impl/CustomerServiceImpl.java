package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.TransferRequest;
import com.shah.bankingapplicationcrud.model.response.*;
import com.shah.bankingapplicationcrud.repository.CustomerRepository;
import com.shah.bankingapplicationcrud.service.CustomerService;
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

import javax.transaction.Transactional;
import java.beans.FeatureDescriptor;
import java.util.UUID;
import java.util.stream.Stream;

import static com.shah.bankingapplicationcrud.exception.CrudError.constructErrorForCrudException;
import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse.success;
import static com.shah.bankingapplicationcrud.model.response.DeleteOneCustomerResponse.fail;
import static com.shah.bankingapplicationcrud.model.response.SearchCustomerResponse.fail;
import static com.shah.bankingapplicationcrud.model.response.TransferAmountResponse.fail;
import static com.shah.bankingapplicationcrud.model.response.TransferAmountResponse.success;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.firstNameLike;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.lastNameLike;
import static com.shah.bankingapplicationcrud.validation.ValidateHeaders.validateHeaders;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
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
    private final CustomerRepository custRepo;


    /**
     * Fetch all customers. If empty will throw exception. Optional query param to search for customer containing by first or last name
     *
     * @param headers request headers coming from client
     * @param name    name of field
     * @param page    page number to display
     * @param size    number of items to display in each page
     * @param field   the parameter to search from eg by firstName, email, etc
     * @return
     */

    public SearchCustomerResponse getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field) {
        try {
            validateHeaders(headers);
            Pageable pageRequest = of(page, size).withSort(by(ASC, field));
            /**
             to test below jpa query
             select * from customers where last_name like '%s%' or first_name like '%s%';
             **/

            if (StringUtils.isNotBlank(name))
                log.info("Performing search like by firstname or lastname by keyword: {}", name);
            else
                log.info("Getting all customers");

            Page<Customer> customers = custRepo.findAll(
                    where(firstNameLike(name)
                            .or(lastNameLike(name))),
                    pageRequest);

            if (customers.stream().findAny().isPresent()) {
                log.info("current customers displayed: {}, total customers found: {}", customers.getSize(), customers.getTotalElements());
                return SearchCustomerResponse.success(customers);
            }
            throw new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        } catch (CrudException e) {
            log.error("Customer: {} not found...", name);
            return fail(constructErrorForCrudException(e));
        }

    }

    /**
     * Fetch one customer. If not found will throw exception
     *
     * @param request
     * @param headers Headers coming from client
     * @return
     */
    @Override
    public GetOneCustomerResponse getOneCustomer(GetOneCustomerRequest request, HttpHeaders headers) {
        log.info("Fetching customer...");
        try {
            validateHeaders(headers);
            Customer customer = custRepo.findById(fromString(request.getId())).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND));
            log.info("Fetch customer success...");
            return GetOneCustomerResponse.success(customer);

        } catch (CrudException e) {
            log.error("Fetch customer failed...");
            return GetOneCustomerResponse.fail(null, constructErrorForCrudException(e));
        }
    }

    /**
     * Create new customer
     *
     * @param request
     * @param headers
     * @return
     */

    @Override
    public CreateOneCustomerResponse createOneCustomer(CreateCustomerRequest request, HttpHeaders headers) {
        log.info("Creating one customer...");
        try {
            validateHeaders(headers);
            Customer customer = new Customer();
            copyProperties(request, customer);
            Customer savedCustomer = custRepo.save(customer);
            return success(savedCustomer);

        } catch (CrudException e) {
            return CreateOneCustomerResponse.fail(null, constructErrorForCrudException(e));
        }
    }

    /**
     * Patch or put customer
     * To edit a single field, simply add that field, we don't need to include rest of the fields
     * To edit all field, simply add all fields.
     *
     * @param request
     * @param headers
     * @return
     */

    @Override
    public CreateOneCustomerResponse updateOneCustomer(PatchCustomerRequest request, HttpHeaders headers) {
        log.info("Editing one customer...");
        try {
            validateHeaders(headers);
            if (isEmpty(request.getId())) throw new CrudException(AC_BAD_REQUEST, EMPTY_ID);

            Customer customer = custRepo.findById(request.getId()).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND));
            copyProperties(request, customer, getNullPropertyNames(request));
            return success(custRepo.save(customer));


        } catch (CrudException e) {
            return CreateOneCustomerResponse.fail(null, constructErrorForCrudException(e));
        }
    }

    /**
     * Delete customer by id
     *
     * @param request
     * @param headers
     * @return
     * @throws CrudException
     */

    @Override
    public DeleteOneCustomerResponse deleteOneCustomer(GetOneCustomerRequest request, HttpHeaders headers) {
        log.info("Check if customer exists...");
        UUID id = fromString(request.getId());
        try {
            validateHeaders(headers);
            Customer customer = custRepo.findById(id).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND));

            log.info("Customer found: \n {} \n Deleting customer...", customer);
            custRepo.deleteById(id);
            return DeleteOneCustomerResponse.success(id);

        } catch (CrudException e) {
            return fail(id, constructErrorForCrudException(e));
        }
    }

    /**
     * To transfer amount from one acc to another
     *
     * @param request
     * @param headers
     * @return
     */
    @Transactional
    @Override
    public TransferAmountResponse transferAmount(TransferRequest request, HttpHeaders headers) {
        String senderId = request.getPayerAccountNumber();

        TransferResponseDto data = TransferResponseDto.builder()
                .payerAccountNumber(senderId)
                .payeeAccountNumber(request.getPayeeAccountNumber())
                .amount(request.getAmount())
                .build();

        try {
            validateHeaders(headers);

            // 1. check if payer acc exists
            Customer payer = custRepo.findById(fromString(request.getPayerAccountNumber())).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, PAYER_ACCOUNT_NOT_FOUND));
            log.info("Payer found: {}", payer);

            // 2. check if payer bal is more than transfer amount
            if (payer.getAccBalance().compareTo(request.getAmount()) < 0)
                throw new CrudException(AC_BAD_REQUEST, INSUFFICIENT_AMOUNT);

            // 3. check if payee acc exists
            Customer payee = custRepo.findById(fromString(request.getPayeeAccountNumber())).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, PAYEE_ACCOUNT_NOT_FOUND));
            log.info("Payee found: {}", payee);

            // 4. transfer
            payee.setAccBalance(payee.getAccBalance().add(request.getAmount()));
            payer.setAccBalance(payer.getAccBalance().subtract(request.getAmount()));
            log.info("Transfer success");

            // 5. update both accounts to db
            Iterable<Customer> customers = custRepo.saveAll(of(payee, payer));
            log.info("Customers updated: {}", customers);

            // 6. create object for response
            data = TransferResponseDto.builder()
                    .payerAccountNumber(senderId)
                    .payerFirstName(payer.getFirstName())
                    .payerOldAccBal(payer.getAccBalance().add(request.getAmount()))
                    .payerNewAccBal(payer.getAccBalance())
                    .payeeAccountNumber(request.getPayeeAccountNumber())
                    .payeeFirstName(payee.getFirstName())
                    .payeeOldAccBal(payee.getAccBalance().subtract(request.getAmount()))
                    .payeeNewAccBal(payee.getAccBalance())
                    .transactionDate(customers.iterator().next().getUpdatedAt())
                    .amount(request.getAmount())
                    .build();

            return success(data);

        } catch (CrudException e) {
            log.error("Transfer operation failed...");
            return fail(data, constructErrorForCrudException(e));
        }
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
