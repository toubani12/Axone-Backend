import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { WalletStatus } from 'app/entities/enumerations/wallet-status.model';

export interface IWallet {
  id: number;
  balance?: number | null;
  status?: keyof typeof WalletStatus | null;
  relatedToAccount?: IAppAccount | null;
}

export type NewWallet = Omit<IWallet, 'id'> & { id: null };
