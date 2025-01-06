import { IApplication } from 'app/entities/application/application.model';

export interface ICriteria {
  id: number;
  name?: string | null;
  applications?: IApplication[] | null;
}

export type NewCriteria = Omit<ICriteria, 'id'> & { id: null };
