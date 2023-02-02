import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bank-account.reducer';

export const BankAccountDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bankAccountEntity = useAppSelector(state => state.bankAccount.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bankAccountDetailsHeading">Bank Account</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bankAccountEntity.id}</dd>
          <dt>
            <span id="accountName">Account Name</span>
          </dt>
          <dd>{bankAccountEntity.accountName}</dd>
          <dt>
            <span id="cardNumber">Card Number</span>
          </dt>
          <dd>{bankAccountEntity.cardNumber}</dd>
          <dt>
            <span id="openDate">Open Date</span>
          </dt>
          <dd>
            {bankAccountEntity.openDate ? (
              <TextFormat value={bankAccountEntity.openDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="closingDate">Closing Date</span>
          </dt>
          <dd>
            {bankAccountEntity.closingDate ? (
              <TextFormat value={bankAccountEntity.closingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="balance">Balance</span>
          </dt>
          <dd>{bankAccountEntity.balance}</dd>
          <dt>User</dt>
          <dd>{bankAccountEntity.user ? bankAccountEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/bank-account" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bank-account/${bankAccountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BankAccountDetail;
