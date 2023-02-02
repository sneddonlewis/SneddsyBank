import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBankTransfer } from 'app/shared/model/bank-transfer.model';
import { getEntities } from './bank-transfer.reducer';

export const BankTransfer = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const bankTransferList = useAppSelector(state => state.bankTransfer.entities);
  const loading = useAppSelector(state => state.bankTransfer.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="bank-transfer-heading" data-cy="BankTransferHeading">
        Bank Transfers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/bank-transfer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Bank Transfer
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bankTransferList && bankTransferList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Amount</th>
                <th>Execution Time</th>
                <th>From Account</th>
                <th>To Account</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bankTransferList.map((bankTransfer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bank-transfer/${bankTransfer.id}`} color="link" size="sm">
                      {bankTransfer.id}
                    </Button>
                  </td>
                  <td>{bankTransfer.amount}</td>
                  <td>
                    {bankTransfer.executionTime ? (
                      <TextFormat type="date" value={bankTransfer.executionTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bankTransfer.fromAccount ? (
                      <Link to={`/bank-account/${bankTransfer.fromAccount.id}`}>{bankTransfer.fromAccount.cardNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {bankTransfer.toAccount ? (
                      <Link to={`/bank-account/${bankTransfer.toAccount.id}`}>{bankTransfer.toAccount.cardNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bank-transfer/${bankTransfer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/bank-transfer/${bankTransfer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bank-transfer/${bankTransfer.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Bank Transfers found</div>
        )}
      </div>
    </div>
  );
};

export default BankTransfer;
