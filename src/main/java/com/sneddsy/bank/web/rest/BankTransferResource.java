package com.sneddsy.bank.web.rest;

import com.sneddsy.bank.domain.BankTransfer;
import com.sneddsy.bank.repository.BankTransferRepository;
import com.sneddsy.bank.service.PaymentService;
import com.sneddsy.bank.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sneddsy.bank.domain.BankTransfer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BankTransferResource {

    private final Logger log = LoggerFactory.getLogger(BankTransferResource.class);

    private static final String ENTITY_NAME = "bankTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankTransferRepository bankTransferRepository;
    private final PaymentService paymentService;

    public BankTransferResource(BankTransferRepository bankTransferRepository, PaymentService paymentService) {
        this.bankTransferRepository = bankTransferRepository;
        this.paymentService = paymentService;
    }

    /**
     * {@code POST  /bank-transfers} : Create a new bankTransfer.
     *
     * @param bankTransfer the bankTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankTransfer, or with status {@code 400 (Bad Request)} if the bankTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bank-transfers")
    public ResponseEntity<BankTransfer> createBankTransfer(@RequestBody BankTransfer bankTransfer) throws URISyntaxException {
        log.debug("REST request to save BankTransfer : {}", bankTransfer);
        if (bankTransfer.getId() != null) {
            throw new BadRequestAlertException("A new bankTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (bankTransfer.getFromAccount().equals(bankTransfer.getToAccount())) {
            throw new BadRequestAlertException(
                "A transfer cannot be made to the same account as the sender",
                ENTITY_NAME,
                "circular-transfer"
            );
        }
        if (bankTransfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestAlertException("A transfer must have a positive non-zero amount", ENTITY_NAME, "invalid-amount");
        }
        Optional<BankTransfer> maybeResult = paymentService.makeTransfer(bankTransfer);
        if (maybeResult.isPresent()) {
            BankTransfer result = maybeResult.get();
            return ResponseEntity
                .created(new URI("/api/bank-transfers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        }
        throw new BadRequestAlertException("Unable to complete transaction", ENTITY_NAME, "insufficient-funds");
    }

    /**
     * {@code PUT  /bank-transfers/:id} : Updates an existing bankTransfer.
     *
     * @param id the id of the bankTransfer to save.
     * @param bankTransfer the bankTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransfer,
     * or with status {@code 400 (Bad Request)} if the bankTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bank-transfers/{id}")
    public ResponseEntity<BankTransfer> updateBankTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BankTransfer bankTransfer
    ) throws URISyntaxException {
        log.debug("REST request to update BankTransfer : {}, {}", id, bankTransfer);
        if (bankTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BankTransfer result = bankTransferRepository.save(bankTransfer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bankTransfer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bank-transfers/:id} : Partial updates given fields of an existing bankTransfer, field will ignore if it is null
     *
     * @param id the id of the bankTransfer to save.
     * @param bankTransfer the bankTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransfer,
     * or with status {@code 400 (Bad Request)} if the bankTransfer is not valid,
     * or with status {@code 404 (Not Found)} if the bankTransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bank-transfers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankTransfer> partialUpdateBankTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BankTransfer bankTransfer
    ) throws URISyntaxException {
        log.debug("REST request to partial update BankTransfer partially : {}, {}", id, bankTransfer);
        if (bankTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankTransfer> result = bankTransferRepository
            .findById(bankTransfer.getId())
            .map(existingBankTransfer -> {
                if (bankTransfer.getAmount() != null) {
                    existingBankTransfer.setAmount(bankTransfer.getAmount());
                }
                if (bankTransfer.getExecutionTime() != null) {
                    existingBankTransfer.setExecutionTime(bankTransfer.getExecutionTime());
                }

                return existingBankTransfer;
            })
            .map(bankTransferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bankTransfer.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-transfers} : get all the bankTransfers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankTransfers in body.
     */
    @GetMapping("/bank-transfers")
    public List<BankTransfer> getAllBankTransfers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all BankTransfers");
        if (eagerload) {
            return bankTransferRepository.findAllWithEagerRelationships();
        } else {
            return bankTransferRepository.findAll();
        }
    }

    /**
     * {@code GET  /bank-transfers/:id} : get the "id" bankTransfer.
     *
     * @param id the id of the bankTransfer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankTransfer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-transfers/{id}")
    public ResponseEntity<BankTransfer> getBankTransfer(@PathVariable Long id) {
        log.debug("REST request to get BankTransfer : {}", id);
        Optional<BankTransfer> bankTransfer = bankTransferRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bankTransfer);
    }

    /**
     * {@code DELETE  /bank-transfers/:id} : delete the "id" bankTransfer.
     *
     * @param id the id of the bankTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bank-transfers/{id}")
    public ResponseEntity<Void> deleteBankTransfer(@PathVariable Long id) {
        log.debug("REST request to delete BankTransfer : {}", id);
        bankTransferRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
