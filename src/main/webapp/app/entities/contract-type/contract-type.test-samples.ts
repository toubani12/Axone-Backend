import { IContractType, NewContractType } from './contract-type.model';

export const sampleWithRequiredData: IContractType = {
  id: 6209,
  name: 'generously which bitter',
};

export const sampleWithPartialData: IContractType = {
  id: 28027,
  name: 'ill-informed wonderfully scissors',
};

export const sampleWithFullData: IContractType = {
  id: 26961,
  name: 'brochure nice ritualise',
};

export const sampleWithNewData: NewContractType = {
  name: 'teem ill-fated wrongly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
