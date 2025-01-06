import { IAppAccount } from 'app/entities/app-account/app-account.model';

export interface IAppAccountType {
  id: number;
  type?: string | null;
  appAccounts?: IAppAccount[] | null;
}

export type NewAppAccountType = Omit<IAppAccountType, 'id'> & { id: null };
