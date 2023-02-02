import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBankAccount } from 'app/shared/model/bank-account.model';
import { getEntities as getBankAccounts } from 'app/entities/bank-account/bank-account.reducer';
import { IBankTransfer } from 'app/shared/model/bank-transfer.model';
import { getEntity, updateEntity, createEntity, reset } from './bank-transfer.reducer';

export const BankTransferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccounts = useAppSelector(state => state.bankAccount.entities);
  const bankTransferEntity = useAppSelector(state => state.bankTransfer.entity);
  const loading = useAppSelector(state => state.bankTransfer.loading);
  const updating = useAppSelector(state => state.bankTransfer.updating);
  const updateSuccess = useAppSelector(state => state.bankTransfer.updateSuccess);

  const handleClose = () => {
    navigate('/bank-transfer');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBankAccounts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.executionTime = convertDateTimeToServer(values.executionTime);

    const entity = {
      ...bankTransferEntity,
      ...values,
      fromAccount: bankAccounts.find(it => it.id.toString() === values.fromAccount.toString()),
      toAccount: bankAccounts.find(it => it.id.toString() === values.toAccount.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          executionTime: displayDefaultDateTime(),
        }
      : {
          ...bankTransferEntity,
          executionTime: convertDateTimeFromServer(bankTransferEntity.executionTime),
          fromAccount: bankTransferEntity?.fromAccount?.id,
          toAccount: bankTransferEntity?.toAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sneddsyBankApp.bankTransfer.home.createOrEditLabel" data-cy="BankTransferCreateUpdateHeading">
            Create or edit a Bank Transfer
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="bank-transfer-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Amount" id="bank-transfer-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField
                label="Execution Time"
                id="bank-transfer-executionTime"
                name="executionTime"
                data-cy="executionTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="bank-transfer-fromAccount" name="fromAccount" data-cy="fromAccount" label="From Account" type="select">
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cardNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="bank-transfer-toAccount" name="toAccount" data-cy="toAccount" label="To Account" type="select">
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cardNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bank-transfer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BankTransferUpdate;
