import { IAppAccountType, NewAppAccountType } from './app-account-type.model';

export const sampleWithRequiredData: IAppAccountType = {
  id: 32719,
  type: 'gosh',
};

export const sampleWithPartialData: IAppAccountType = {
  id: 232,
  type: 'linguistics vice',
};

export const sampleWithFullData: IAppAccountType = {
  id: 18800,
  type: 'oh hence',
};

export const sampleWithNewData: NewAppAccountType = {
  type: 'gosh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
