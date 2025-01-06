import { IAdmin, NewAdmin } from './admin.model';

export const sampleWithRequiredData: IAdmin = {
  id: 18471,
  systemName: 'brightly',
};

export const sampleWithPartialData: IAdmin = {
  id: 903,
  systemName: 'happy-go-lucky yuck',
};

export const sampleWithFullData: IAdmin = {
  id: 32630,
  systemName: 'decay',
};

export const sampleWithNewData: NewAdmin = {
  systemName: 'parched sarcastic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
