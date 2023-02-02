import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBankTransfer } from 'app/shared/model/bank-transfer.model';

export interface IBankAccount {
  id?: number;
  cardNumber?: string;
  openDate?: string;
  closingDate?: string | null;
  balance?: number;
  user?: IUser | null;
  transfersOuts?: IBankTransfer[] | null;
  transfersIns?: IBankTransfer[] | null;
}

export const defaultValue: Readonly<IBankAccount> = {};
