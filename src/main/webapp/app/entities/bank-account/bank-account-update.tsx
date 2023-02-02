import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { getEntity, updateEntity, createEntity, reset } from './bank-account.reducer';

export const BankAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const bankAccountEntity = useAppSelector(state => state.bankAccount.entity);
  const loading = useAppSelector(state => state.bankAccount.loading);
  const updating = useAppSelector(state => state.bankAccount.updating);
  const updateSuccess = useAppSelector(state => state.bankAccount.updateSuccess);

  const handleClose = () => {
    navigate('/bank-account');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...bankAccountEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...bankAccountEntity,
          user: bankAccountEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sneddsyBankApp.bankAccount.home.createOrEditLabel" data-cy="BankAccountCreateUpdateHeading">
            Create or edit a Bank Account
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bank-account-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Account Name"
                id="bank-account-accountName"
                name="accountName"
                data-cy="accountName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Card Number"
                id="bank-account-cardNumber"
                name="cardNumber"
                data-cy="cardNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 16, message: 'This field is required to be at least 16 characters.' },
                  maxLength: { value: 16, message: 'This field cannot be longer than 16 characters.' },
                }}
              />
              <ValidatedField
                label="Open Date"
                id="bank-account-openDate"
                name="openDate"
                data-cy="openDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Closing Date" id="bank-account-closingDate" name="closingDate" data-cy="closingDate" type="date" />
              <ValidatedField
                label="Balance"
                id="bank-account-balance"
                name="balance"
                data-cy="balance"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="bank-account-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bank-account" replace color="info">
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

export default BankAccountUpdate;
