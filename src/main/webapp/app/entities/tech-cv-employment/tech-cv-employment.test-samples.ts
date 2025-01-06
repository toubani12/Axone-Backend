import { ITechCVEmployment, NewTechCVEmployment } from './tech-cv-employment.model';

export const sampleWithRequiredData: ITechCVEmployment = {
  id: 20226,
  employment: 'remark hunch',
};

export const sampleWithPartialData: ITechCVEmployment = {
  id: 28341,
  employment: 'scarily aha',
};

export const sampleWithFullData: ITechCVEmployment = {
  id: 31042,
  employment: 'duh condominium',
};

export const sampleWithNewData: NewTechCVEmployment = {
  employment: 'aboard empty',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
