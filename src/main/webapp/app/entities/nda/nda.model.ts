import dayjs from 'dayjs/esm';
import { IEmployer } from 'app/entities/employer/employer.model';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { NDAStatus } from 'app/entities/enumerations/nda-status.model';

export interface INDA {
  id: number;
  document?: string | null;
  documentContentType?: string | null;
  status?: keyof typeof NDAStatus | null;
  period?: dayjs.Dayjs | null;
  employer?: IEmployer | null;
  mediator?: IRecruiter | null;
  candidate?: ICandidate | null;
}

export type NewNDA = Omit<INDA, 'id'> & { id: null };
