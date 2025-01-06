import { IUser } from 'app/entities/user/user.model';
import { IWallet } from 'app/entities/wallet/wallet.model';

export interface IAdmin {
  id: number;
  systemName?: string | null;
  relatedUser?: Pick<IUser, 'id' | 'login'> | null;
  systemWallet?: IWallet | null;
}

export type NewAdmin = Omit<IAdmin, 'id'> & { id: null };
