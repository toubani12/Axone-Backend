import { IApplication } from 'app/entities/application/application.model';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IRequest {
  id: number;
  status?: keyof typeof RequestStatus | null;
  expressionOfInterest?: string | null;
  relatedApplication?: IApplication | null;
  recruiter?: IRecruiter | null;
}

export type NewRequest = Omit<IRequest, 'id'> & { id: null };
