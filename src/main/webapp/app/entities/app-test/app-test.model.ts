import { IAppTestType } from 'app/entities/app-test-type/app-test-type.model';

export interface IAppTest {
  id: number;
  name?: string | null;
  invitationLink?: string | null;
  totalScore?: number | null;
  status?: string | null;
  completed?: boolean | null;
  testID?: number | null;
  algorithm?: string | null;
  isCodeTest?: boolean | null;
  duration?: number | null;
  types?: IAppTestType[] | null;
}

export type NewAppTest = Omit<IAppTest, 'id'> & { id: null };
