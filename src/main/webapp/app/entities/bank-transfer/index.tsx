import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BankTransfer from './bank-transfer';
import BankTransferDetail from './bank-transfer-detail';
import BankTransferUpdate from './bank-transfer-update';
import BankTransferDeleteDialog from './bank-transfer-delete-dialog';

const BankTransferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BankTransfer />} />
    <Route path="new" element={<BankTransferUpdate />} />
    <Route path=":id">
      <Route index element={<BankTransferDetail />} />
      <Route path="edit" element={<BankTransferUpdate />} />
      <Route path="delete" element={<BankTransferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BankTransferRoutes;