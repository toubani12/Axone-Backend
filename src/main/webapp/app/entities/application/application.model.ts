import dayjs from 'dayjs/esm';
import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ITemplate } from 'app/entities/template/template.model';
import { ICriteria } from 'app/entities/criteria/criteria.model';
import { IDomain } from 'app/entities/domain/domain.model';
import { IEmployer } from 'app/entities/employer/employer.model';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { ApplicationStatus } from 'app/entities/enumerations/application-status.model';

export interface IApplication {
  id: number;
  title?: string | null;
  description?: string | null;
  numberOfCandidates?: number | null;
  paymentAmount?: number | null;
  recruiterIncomeRate?: number | null;
  candidateIncomeRate?: number | null;
  deadline?: dayjs.Dayjs | null;
  status?: keyof typeof ApplicationStatus | null;
  createdAt?: dayjs.Dayjs | null;
  openedAt?: dayjs.Dayjs | null;
  closedAt?: dayjs.Dayjs | null;
  doneAt?: dayjs.Dayjs | null;
  contractTypes?: IContractType[] | null;
  contractTemplates?: ITemplate[] | null;
  criteria?: ICriteria[] | null;
  domains?: IDomain[] | null;
  employer?: IEmployer | null;
  recruiters?: IRecruiter[] | null;
  candidates?: ICandidate[] | null;
}

export type NewApplication = Omit<IApplication, 'id'> & { id: null };
