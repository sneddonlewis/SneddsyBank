import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBankAccount } from 'app/shared/model/bank-account.model';
import { getEntities } from './bank-account.reducer';

export const BankAccount = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const bankAccountList = useAppSelector(state => state.bankAccount.entities);
  const loading = useAppSelector(state => state.bankAccount.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="bank-account-heading" data-cy="BankAccountHeading">
        Bank Accounts
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/bank-account/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Bank Account
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bankAccountList && bankAccountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Account Name</th>
                <th>Card Number</th>
                <th>Type Of Account</th>
                <th>Open Date</th>
                <th>Closing Date</th>
                <th>Balance</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bankAccountList.map((bankAccount, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bank-account/${bankAccount.id}`} color="link" size="sm">
                      {bankAccount.id}
                    </Button>
                  </td>
                  <td>{bankAccount.accountName}</td>
                  <td>{bankAccount.cardNumber}</td>
                  <td>{bankAccount.typeOfAccount}</td>
                  <td>
                    {bankAccount.openDate ? <TextFormat type="date" value={bankAccount.openDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {bankAccount.closingDate ? (
                      <TextFormat type="date" value={bankAccount.closingDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{bankAccount.balance}</td>
                  <td>{bankAccount.user ? bankAccount.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bank-account/${bankAccount.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/bank-account/${bankAccount.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bank-account/${bankAccount.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Bank Accounts found</div>
        )}
      </div>
    </div>
  );
};

export default BankAccount;
