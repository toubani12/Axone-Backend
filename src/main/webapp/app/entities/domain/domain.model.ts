import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { IApplication } from 'app/entities/application/application.model';
import { IEmployer } from 'app/entities/employer/employer.model';

export interface IDomain {
  id: number;
  name?: string | null;
  recruiters?: IRecruiter[] | null;
  candidates?: ICandidate[] | null;
  applications?: IApplication[] | null;
  employer?: IEmployer | null;
}

export type NewDomain = Omit<IDomain, 'id'> & { id: null };
