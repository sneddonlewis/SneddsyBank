import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bank-account.reducer';
import { getTransactions } from '../bank-transfer/bank-transfer.reducer';
import { TextFormat } from 'react-jhipster';

export const BankAccount = () => {
  const dispatch = useAppDispatch();

  const bankAccountList = useAppSelector(state => state.bankAccount.entities);
  const bankTransferList = useAppSelector(state => state.bankTransfer.entities);
  const loading = useAppSelector(state => state.bankAccount.loading);
  const transactionsLoading = useAppSelector(state => state.bankTransfer.loading);

  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getTransactions({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="bank-account-heading" data-cy="BankAccountHeading">
        My Accounts
        <div className="d-flex justify-content-end">
          <Link to="/bank-transfer/new" className="btn btn-default jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="money-check" />
            &nbsp; Transfer
          </Link>
          <Link to="/bank-account/new" className="btn btn-default jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; New Account
          </Link>
        </div>
      </h2>
      <div>
        {bankAccountList && bankAccountList.length > 0 ? (
          <div>
            <Table>
              <thead>
                <tr>
                  <th>Account Name</th>
                  <th>Type Of Account</th>
                  <th>Balance</th>
                  <th>User</th>
                  <th>Transactions</th>
                </tr>
              </thead>
              <tbody>
                {bankAccountList
                  .filter(a => a.closingDate === null)
                  .map((bankAccount, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{bankAccount.accountName}</td>
                      <td>{bankAccount.typeOfAccount}</td>
                      <td>{bankAccount.balance}</td>
                      <td>{bankAccount.user ? bankAccount.user.login : ''}</td>
                      <td>
                        <div className="table-responsive">
                          {bankTransferList && bankTransferList.length > 0 ? (
                            <Table responsive>
                              <thead>
                                <tr>
                                  <th>Amount</th>
                                  <th>Execution Time</th>
                                  <th>From Account</th>
                                  <th>To Account</th>
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
                                        <Link to={`/bank-account/${bankTransfer.fromAccount.id}`}>
                                          {bankTransfer.fromAccount.cardNumber}
                                        </Link>
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
                                        <Button
                                          tag={Link}
                                          to={`/bank-transfer/${bankTransfer.id}`}
                                          color="info"
                                          size="sm"
                                          data-cy="entityDetailsButton"
                                        >
                                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                                        </Button>
                                        <Button
                                          tag={Link}
                                          to={`/bank-transfer/${bankTransfer.id}/edit`}
                                          color="primary"
                                          size="sm"
                                          data-cy="entityEditButton"
                                        >
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
                            !transactionsLoading && <div className="alert alert-warning">No Bank Transfers found</div>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </Table>
          </div>
        ) : (
          !loading && <div className="alert alert-warning">No Bank Accounts found</div>
        )}
      </div>
    </div>
  );
};

export default BankAccount;
