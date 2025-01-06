import { IUser } from 'app/entities/user/user.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { IApplication } from 'app/entities/application/application.model';
import { IDomain } from 'app/entities/domain/domain.model';
import { UserRole } from 'app/entities/enumerations/user-role.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';

export interface IRecruiter {
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
  linkedinUrl?: string | null;
  approvedExperience?: boolean | null;
  score?: number | null;
  relatedUser?: Pick<IUser, 'id' | 'login'> | null;
  wallet?: IWallet | null;
  applications?: IApplication[] | null;
  operationalDomains?: IDomain[] | null;
}

export type NewRecruiter = Omit<IRecruiter, 'id'> & { id: null };
