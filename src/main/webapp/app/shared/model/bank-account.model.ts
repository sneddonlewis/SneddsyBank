import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBankTransfer } from 'app/shared/model/bank-transfer.model';
import { AccountType } from 'app/shared/model/enumerations/account-type.model';

export interface IBankAccount {
  id?: number;
  accountName?: string;
  cardNumber?: string;
  typeOfAccount?: AccountType;
  openDate?: string;
  closingDate?: string | null;
  balance?: number;
  user?: IUser | null;
  transfersOuts?: IBankTransfer[] | null;
  transfersIns?: IBankTransfer[] | null;
}

export const defaultValue: Readonly<IBankAccount> = {};
