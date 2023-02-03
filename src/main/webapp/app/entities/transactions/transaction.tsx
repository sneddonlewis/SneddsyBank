import React, { useEffect } from 'react';
import { Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/bank-transfer/bank-transfer.reducer';

export const Transaction = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const bankTransferList = useAppSelector(state => state.bankTransfer.entities);
  const transactionsLoading = useAppSelector(state => state.bankTransfer.loading);

  return (
    <>
      <div className="table-responsive">
        {bankTransferList && bankTransferList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Amount</th>
                <th>Time</th>
                <th>From Account</th>
                <th>To Account</th>
              </tr>
            </thead>
            <tbody>
              {bankTransferList.map((bankTransfer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
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
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !transactionsLoading && <div className="alert alert-warning">No Bank Transfers found</div>
        )}
      </div>
    </>
  );
};

export default Transaction;
