import { ITemplate } from 'app/entities/template/template.model';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { IEmployer } from 'app/entities/employer/employer.model';
import { IApplication } from 'app/entities/application/application.model';
import { TemplateContractType } from 'app/entities/enumerations/template-contract-type.model';
import { ContractStatus } from 'app/entities/enumerations/contract-status.model';

export interface IContract {
  id: number;
  label?: string | null;
  type?: keyof typeof TemplateContractType | null;
  status?: keyof typeof ContractStatus | null;
  directContract?: boolean | null;
  paymentAmount?: number | null;
  recruiterIncomeRate?: number | null;
  candidateIncomeRate?: number | null;
  template?: ITemplate | null;
  candidate?: ICandidate | null;
  recruiter?: IRecruiter | null;
  employer?: IEmployer | null;
  application?: IApplication | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
