import { IApplication } from 'app/entities/application/application.model';

export interface IContractType {
  id: number;
  name?: string | null;
  applications?: IApplication[] | null;
}

export type NewContractType = Omit<IContractType, 'id'> & { id: null };
