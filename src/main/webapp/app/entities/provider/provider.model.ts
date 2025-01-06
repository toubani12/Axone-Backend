import { IAppAccount } from 'app/entities/app-account/app-account.model';

export interface IProvider {
  id: number;
  name?: string | null;
  country?: string | null;
  appAccounts?: IAppAccount[] | null;
}

export type NewProvider = Omit<IProvider, 'id'> & { id: null };
