package com.sneddsy.bank.web.rest;

import static com.sneddsy.bank.web.rest.TestUtil.sameInstant;
import static com.sneddsy.bank.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sneddsy.bank.IntegrationTest;
import com.sneddsy.bank.domain.BankAccount;
import com.sneddsy.bank.domain.BankTransfer;
import com.sneddsy.bank.domain.enumeration.AccountType;
import com.sneddsy.bank.repository.BankAccountRepository;
import com.sneddsy.bank.repository.BankTransferRepository;
import com.sneddsy.bank.service.BankAccountService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BankTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BankTransferResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_EXECUTION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/bank-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Mock
    private BankTransferRepository bankTransferRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankTransferMockMvc;

    private BankTransfer bankTransfer;
    private BankAccount sender;
    private BankAccount receiver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransfer createEntity(EntityManager em) {
        BankTransfer bankTransfer = new BankTransfer().amount(DEFAULT_AMOUNT).executionTime(DEFAULT_EXECUTION_TIME);
        return bankTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransfer createUpdatedEntity(EntityManager em) {
        BankTransfer bankTransfer = new BankTransfer().amount(UPDATED_AMOUNT).executionTime(UPDATED_EXECUTION_TIME);
        return bankTransfer;
    }

    @BeforeEach
    public void initTest() {
        sender =
            bankAccountService
                .createNewAccount(BankAccountResourceIT.createEntity(em).balance(DEFAULT_AMOUNT).cardNumber("4000002280847383"))
                .get();
        receiver =
            bankAccountService
                .createNewAccount(
                    BankAccountResourceIT
                        .createEntity(em)
                        .cardNumber("5000000101023553")
                        .typeOfAccount(AccountType.SAVING)
                        .balance(DEFAULT_AMOUNT)
                )
                .get();
        bankTransfer = createEntity(em).amount(DEFAULT_AMOUNT).fromAccount(sender).toAccount(receiver);
    }

    @Test
    @Transactional
    void createBankTransfer() throws Exception {
        int databaseSizeBeforeCreate = bankTransferRepository.findAll().size();
        // Create the BankTransfer
        RequestBuilder requestBuilder = post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bankTransfer));
        restBankTransferMockMvc.perform(requestBuilder).andExpect(status().isCreated());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeCreate + 1);
        BankTransfer testBankTransfer = bankTransferList.get(bankTransferList.size() - 1);
        assertThat(testBankTransfer.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createBankTransferWithExistingId() throws Exception {
        // Create the BankTransfer with an existing ID
        bankTransfer.setId(1L);

        int databaseSizeBeforeCreate = bankTransferRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBankTransfers() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        // Get all the bankTransferList
        restBankTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(sameInstant(DEFAULT_EXECUTION_TIME))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(bankTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBankTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bankTransferRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bankTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBankTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bankTransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBankTransfer() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        // Get the bankTransfer
        restBankTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, bankTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankTransfer.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.executionTime").value(sameInstant(DEFAULT_EXECUTION_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingBankTransfer() throws Exception {
        // Get the bankTransfer
        restBankTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankTransfer() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();

        // Update the bankTransfer
        BankTransfer updatedBankTransfer = bankTransferRepository.findById(bankTransfer.getId()).get();
        // Disconnect from session so that the updates on updatedBankTransfer are not directly saved in db
        em.detach(updatedBankTransfer);
        updatedBankTransfer.amount(UPDATED_AMOUNT).executionTime(UPDATED_EXECUTION_TIME);

        restBankTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBankTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBankTransfer))
            )
            .andExpect(status().isOk());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
        BankTransfer testBankTransfer = bankTransferList.get(bankTransferList.size() - 1);
        assertThat(testBankTransfer.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBankTransfer.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void putNonExistingBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankTransferWithPatch() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();

        // Update the bankTransfer using partial update
        BankTransfer partialUpdatedBankTransfer = new BankTransfer();
        partialUpdatedBankTransfer.setId(bankTransfer.getId());

        partialUpdatedBankTransfer.amount(UPDATED_AMOUNT);

        restBankTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankTransfer))
            )
            .andExpect(status().isOk());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
        BankTransfer testBankTransfer = bankTransferList.get(bankTransferList.size() - 1);
        assertThat(testBankTransfer.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBankTransfer.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void fullUpdateBankTransferWithPatch() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();

        // Update the bankTransfer using partial update
        BankTransfer partialUpdatedBankTransfer = new BankTransfer();
        partialUpdatedBankTransfer.setId(bankTransfer.getId());

        partialUpdatedBankTransfer.amount(UPDATED_AMOUNT).executionTime(UPDATED_EXECUTION_TIME);

        restBankTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankTransfer))
            )
            .andExpect(status().isOk());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
        BankTransfer testBankTransfer = bankTransferList.get(bankTransferList.size() - 1);
        assertThat(testBankTransfer.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testBankTransfer.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankTransfer() throws Exception {
        int databaseSizeBeforeUpdate = bankTransferRepository.findAll().size();
        bankTransfer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransferMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bankTransfer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransfer in the database
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankTransfer() throws Exception {
        // Initialize the database
        bankTransferRepository.saveAndFlush(bankTransfer);

        int databaseSizeBeforeDelete = bankTransferRepository.findAll().size();

        // Delete the bankTransfer
        restBankTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankTransfer> bankTransferList = bankTransferRepository.findAll();
        assertThat(bankTransferList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
