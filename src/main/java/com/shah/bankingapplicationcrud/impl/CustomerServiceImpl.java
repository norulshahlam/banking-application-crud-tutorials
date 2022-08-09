package com.shah.bankingapplicationcrud.impl;

import com.shah.bankingapplicationcrud.exception.CrudError;
import com.shah.bankingapplicationcrud.exception.CrudException;
import com.shah.bankingapplicationcrud.model.entity.Customer;
import com.shah.bankingapplicationcrud.model.request.CreateCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.GetOneCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.PatchCustomerRequest;
import com.shah.bankingapplicationcrud.model.request.TransferRequestDto;
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

import java.beans.FeatureDescriptor;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.shah.bankingapplicationcrud.exception.CrudErrorCodes.*;
import static com.shah.bankingapplicationcrud.model.response.CreateOneCustomerResponse.success;
import static com.shah.bankingapplicationcrud.model.response.DeleteOneCustomerResponse.fail;
import static com.shah.bankingapplicationcrud.model.response.SearchCustomerResponse.fail;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.firstNameLike;
import static com.shah.bankingapplicationcrud.repository.CustomerRepository.lastNameLike;
import static com.shah.bankingapplicationcrud.validation.ValidateHeaders.validateGetOneEmployee;
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
     * @param headers
     * @param name
     * @param page
     * @param size
     * @param field
     * @return
     */

    public SearchCustomerResponse getAllCustomersOrSearchByLastAndFirstName(HttpHeaders headers, String name, int page, int size, String field) {
        try {
            validateGetOneEmployee(headers);
            Pageable pageRequest = of(page, size).withSort(by(ASC, field));
            /*
             to test below jpa query
             select * from customers where last_name like '%s%' or first_name like '%s%';
             */

            if (StringUtils.isNotBlank(name)) log.info("Performing search like by firstname or lastname by keyword: {}", name);
            else log.info("Getting all customers");

            Page<Customer> customers = custRepo.findAll(
                    where(firstNameLike(name)
                            .or(lastNameLike(name))),
                    pageRequest);

            if (!customers.isEmpty()) {
                log.info("current customers displayed: {}, total customers found: {}", customers.getSize(), customers.getTotalElements());
                return SearchCustomerResponse.success(customers);
            }
            throw new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
        } catch (CrudException e) {
            log.error("Customer: {} not found...", name);
            return fail(CrudError.constructErrorForCrudException(e));
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
            validateGetOneEmployee(headers);
            Optional<Customer> customer = custRepo.findById(UUID.fromString(request.getId()));
            if (customer.isEmpty()) {
                throw new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
            }
            log.info("Fetch customer success...");
            return GetOneCustomerResponse.success(customer.get());

        } catch (CrudException e) {
            log.error("Fetch customer failed...");
            return GetOneCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
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
            validateGetOneEmployee(headers);
            Customer customer = new Customer();
            copyProperties(request, customer);
            return success(custRepo.save(customer));

        } catch (CrudException e) {
            log.error("Creating one customer failed...");
            return CreateOneCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
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
    public CreateOneCustomerResponse patchOneCustomer(PatchCustomerRequest request, HttpHeaders headers) {
        log.info("Editing one customer...");
        try {
            validateGetOneEmployee(headers);
            if (isEmpty(request.getId())) throw new CrudException(AC_BAD_REQUEST, EMPTY_ID);

            Customer customer = custRepo.findById(request.getId()).orElseThrow(() -> new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND));
            copyProperties(request, customer, getNullPropertyNames(request));
            return success(custRepo.save(customer));


        } catch (CrudException e) {
            return CreateOneCustomerResponse.fail(null, CrudError.constructErrorForCrudException(e));
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
        UUID id = UUID.fromString(request.getId());
        try {
            validateGetOneEmployee(headers);

            Optional<Customer> customer = custRepo.findById(id);
            if (customer.isEmpty()) {
                throw new CrudException(AC_BAD_REQUEST, CUSTOMER_NOT_FOUND);
            }

            log.info("Customer found: \n {} \n Deleting customer...", customer);
            custRepo.deleteById(id);
            log.info("Delete customer success...");
            return DeleteOneCustomerResponse.success(id);

        } catch (CrudException e) {
            log.error("Delete customer failed...");
            return fail(id, CrudError.constructErrorForCrudException(e));
        }
    }

    @Override
    public TransferAmountResponse transferAmount(TransferRequestDto request, HttpHeaders headers) {
        log.info("Check if customer exists...");
        String senderId = request.getSenderId();

        try {
            validateGetOneEmployee(headers);

            // 1. check if sender acc exists
            // 2. check if sender bal is more than transfer amount
            // 3. check if receiver acc exists
            // 4. transfer

        }
        catch (CrudException e) {
            log.error("Delete customer failed...");
            TransferResponseDto data = TransferResponseDto.builder().senderId(senderId).receiverId(request.getReceiverId()).amount(request.getAmount()).build();
            return TransferAmountResponse.fail(data,CrudError.constructErrorForCrudException(e));
        }

            return null;
    }

    /**
     * For ignoring empty fields during copy property
     *
     * @param source
     * @return
     */


    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors()).map(FeatureDescriptor::getName).filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null).toArray(String[]::new);
    }
}
