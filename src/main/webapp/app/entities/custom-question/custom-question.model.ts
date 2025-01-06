import { IAppTest } from 'app/entities/app-test/app-test.model';

export interface ICustomQuestion {
  id: number;
  question?: string | null;
  answer?: string | null;
  correctAnswer?: string | null;
  appTest?: IAppTest | null;
}

export type NewCustomQuestion = Omit<ICustomQuestion, 'id'> & { id: null };
