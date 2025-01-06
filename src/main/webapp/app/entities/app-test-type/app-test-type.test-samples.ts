import { IAppTestType, NewAppTestType } from './app-test-type.model';

export const sampleWithRequiredData: IAppTestType = {
  id: 4974,
  type: 'vanish',
};

export const sampleWithPartialData: IAppTestType = {
  id: 21859,
  type: 'cinema hm congregate',
};

export const sampleWithFullData: IAppTestType = {
  id: 22512,
  type: 'loudly',
};

export const sampleWithNewData: NewAppTestType = {
  type: 'opposite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
