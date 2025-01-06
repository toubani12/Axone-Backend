import { ITechCVEducation, NewTechCVEducation } from './tech-cv-education.model';

export const sampleWithRequiredData: ITechCVEducation = {
  id: 12390,
  education: 'gadzooks astride',
};

export const sampleWithPartialData: ITechCVEducation = {
  id: 16092,
  education: 'notwithstanding bitmap',
};

export const sampleWithFullData: ITechCVEducation = {
  id: 23344,
  education: 'tremendously yippee ick',
};

export const sampleWithNewData: NewTechCVEducation = {
  education: 'ritual gentle',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
