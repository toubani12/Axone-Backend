import { ICandidate } from 'app/entities/candidate/candidate.model';

export interface IResume {
  id: number;
  resume?: string | null;
  resumeContentType?: string | null;
  owner?: ICandidate | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
