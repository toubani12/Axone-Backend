import dayjs from 'dayjs/esm';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { IApplication } from 'app/entities/application/application.model';

export interface IInterview {
  id: number;
  label?: string | null;
  entryLink?: string | null;
  invitationLink?: string | null;
  interviewResult?: boolean | null;
  rate?: number | null;
  startedAt?: dayjs.Dayjs | null;
  endedAt?: dayjs.Dayjs | null;
  comments?: string | null;
  attendee?: ICandidate | null;
  application?: IApplication | null;
}

export type NewInterview = Omit<IInterview, 'id'> & { id: null };
