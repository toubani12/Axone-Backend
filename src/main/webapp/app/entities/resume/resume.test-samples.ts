import { IResume, NewResume } from './resume.model';

export const sampleWithRequiredData: IResume = {
  id: 4478,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
};

export const sampleWithPartialData: IResume = {
  id: 31318,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
};

export const sampleWithFullData: IResume = {
  id: 21737,
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
};

export const sampleWithNewData: NewResume = {
  resume: '../fake-data/blob/hipster.png',
  resumeContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
