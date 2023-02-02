import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bank-transfer.reducer';

export const BankTransferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bankTransferEntity = useAppSelector(state => state.bankTransfer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bankTransferDetailsHeading">Bank Transfer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bankTransferEntity.id}</dd>
          <dt>
            <span id="amount">Amount</span>
          </dt>
          <dd>{bankTransferEntity.amount}</dd>
          <dt>
            <span id="executionTime">Execution Time</span>
          </dt>
          <dd>
            {bankTransferEntity.executionTime ? (
              <TextFormat value={bankTransferEntity.executionTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>From Account</dt>
          <dd>{bankTransferEntity.fromAccount ? bankTransferEntity.fromAccount.cardNumber : ''}</dd>
          <dt>To Account</dt>
          <dd>{bankTransferEntity.toAccount ? bankTransferEntity.toAccount.cardNumber : ''}</dd>
        </dl>
        <Button tag={Link} to="/bank-transfer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bank-transfer/${bankTransferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BankTransferDetail;
