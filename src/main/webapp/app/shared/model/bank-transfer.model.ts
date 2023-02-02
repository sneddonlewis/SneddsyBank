import dayjs from 'dayjs';
import { IBankAccount } from 'app/shared/model/bank-account.model';

export interface IBankTransfer {
  id?: number;
  amount?: number | null;
  executionTime?: string | null;
  fromAccount?: IBankAccount | null;
  toAccount?: IBankAccount | null;
}

export const defaultValue: Readonly<IBankTransfer> = {};
