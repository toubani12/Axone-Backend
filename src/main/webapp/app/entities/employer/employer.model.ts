import { IUser } from 'app/entities/user/user.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';

export interface IEmployer {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  profileImage?: string | null;
  profileImageContentType?: string | null;
  address?: string | null;
  role?: keyof typeof UserRole | null;
  status?: keyof typeof UserStatus | null;
  name?: string | null;
  label?: string | null;
  relatedUser?: Pick<IUser, 'id' | 'login'> | null;
  wallet?: IWallet | null;
}

export type NewEmployer = Omit<IEmployer, 'id'> & { id: null };
