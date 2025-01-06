import { IAppTest } from 'app/entities/app-test/app-test.model';

export interface IAppTestType {
  id: number;
  type?: string | null;
  appTests?: IAppTest[] | null;
}

export type NewAppTestType = Omit<IAppTestType, 'id'> & { id: null };
